package mt.siteportal.utils.db;

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mssql.MsSqlConnection;
import org.dbunit.operation.DatabaseOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;

import mt.siteportal.utils.data.AppContext;
import mt.siteportal.utils.tools.Log;
import nz.siteportal.objects.Score;

/**
 * DB utility based on DBUnit.
 *
 */
public class DbUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbUtil.class);

    public static final String DATASETS_DIRECTORY = "/datasets";
/*
    @Value( "${db.schema}" )
    private static String schema;*/

    private static IDatabaseConnection connection;
    //private static Connection dbConnection;
/*    public static DbUtil instance;

    public static DbUtil getInstance(){
        if (null == instance){
            instance = new DbUtil();
        }

        return instance;
    }*/

    /**
     * Deletes all data in the tables specified by the special data-set.xml.
     *
     * @throws Exception
     */
    public void cleanAll() {
        execute(DatabaseOperation.DELETE_ALL, getDataSet("clean_all.xml"));
    }

    /**
     * Inserts data in the User table. See users.xml data-set for data details.
     *
     * @throws Exception
     */
    public void insertStandardUserEntries() {
        execute(DatabaseOperation.CLEAN_INSERT, getDataSet("users.xml"));
    }
    /**
     * Gets count of rows in result set after execution of query
     *
     * @param sql - String sql query to select.
     */
    public int getResultCount(String sql) throws DataSetException, SQLException{
    	IDatabaseConnection conn = getConnection();
	    ITable resultTable = conn.createQueryTable("temp", sql);
	    conn.close();
    	return resultTable.getRowCount();
    }
    private static Connection getDBConnection()  {
    	
    	DataSource dataSource = (DataSource) AppContext.getContext().getBean("dataSource");

        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
        } catch (SQLException e) {
        	Log.logException("Connection to DB instance was not successfull !!!", e);
            e.printStackTrace();
        }
        Log.logInfo("Connected to DB instance...");
        return dbConnection;
	}
    public static void callProcedure(String proc, List<String> values) {
        Connection con = getDBConnection();
        CallableStatement cs = null;
        int size = values.size();
        try {
        	StringBuilder params = new StringBuilder();
    		for (int i = 0;i<size;i++){
    			if (i==0){
    				params.append(" (");
    			}
    			if (i==(size-1)){
    				params.append("?)");
    			}
    			else{
    				params.append("?,");	
    			}
    		}
            cs = con.prepareCall("{call "+proc+params+"}");
            for (int i=0;i<values.size();i++){
            	cs.setString(i+1, values.get(i));
            }
            cs.executeUpdate();
            int result = cs.getUpdateCount();
			System.out.println("The number of deleted records is: " + (result < 0 ? ++result : result));           
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        finally {
            if (cs != null) {
                try {
                    cs.close();
                } catch (SQLException e) {
                    System.err.println("SQLException: " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.err.println("SQLException: " + e.getMessage());
                }
            }
        }
    }
    public static void deleteRecordFromTable(String sql, String whereValue ) throws SQLException {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(sql);
			preparedStatement.setString(1, whereValue);
			
			// execute delete SQL stetement
			preparedStatement.executeUpdate();

			Log.logInfo("Record is deleted!");
			
		} catch (SQLException e) {

			LOGGER.error(e.getMessage());

		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}

	}

    public static ITable getTempTable(String sql) throws DataSetException, SQLException{
    	IDatabaseConnection conn = getConnection();
	    ITable resultTable = conn.createQueryTable("temp", sql);
	    conn.close();
    	return resultTable;
    }
    
    private synchronized static IDatabaseConnection getConnection(){
//        if (null == connection){
            DataSource dataSource = (DataSource) AppContext.getContext().getBean("dataSource");
            try {
                Connection sqlConnection = dataSource.getConnection();
                connection = new MsSqlConnection(sqlConnection);
                connection.getConfig().setProperty("http://www.dbunit.org/features/batchedStatements", true);
            } catch (DatabaseUnitException e) {
                LOGGER.error("Error getting connection to MySql.", e);
            }catch (SQLException sqlEx) {
                LOGGER.error("Error getting connection using data-source.", sqlEx);
            }
 //       }
        return connection;
    }

    private IDataSet getDataSet(String datasetFileName){
        InputStream inpt = getClass().getResourceAsStream(DATASETS_DIRECTORY + "/" + datasetFileName);

        if (null == inpt) {
            String msg = "Was not able to find the data-set file " + datasetFileName;
            Log.logError(msg);
            throw new RuntimeException(msg);
        }

        //NOTE: //http://dbunit.996259.n3.nabble.com/skipping-missing-columns-during-insert-instead-of-sending-null-td723.html issue.
        ReplacementDataSet dataSet = null;
        try {
            dataSet = new ReplacementDataSet(new FlatXmlDataSetBuilder().build(inpt));
        } catch (DataSetException dse) {
            Log.logException("DataSet error.", dse);
            throw new RuntimeException(dse);
        }
        dataSet.addReplacementObject("[NULL]", null);

        return dataSet;
    }

    /**
     * Executes the specified DBUnit operation against the dataset.<br>
     *
     * @param operation - DB operation to perform.
     *
     * @param dataSet - data-set to use.
     *
     * @return True in case of success. False otherwise.
     */
    private boolean execute(DatabaseOperation operation, IDataSet dataSet) {
        boolean result = false;
        try{
            operation.execute(getConnection(), dataSet);
            result = true;
        }catch (SQLException se) {
            LOGGER.error("SQL error." + se.getMessage());
            SQLException exNext = se.getNextException();

            if (exNext != null) {
                LOGGER.error("SQL error." + exNext.getMessage());
                throw new RuntimeException(exNext);
            }

        }catch (Exception e) {
            LOGGER.error("SQL error." + e.getMessage());
            throw new RuntimeException(e);
        }

        return result;
    }
    
///--------------------------------------NZ implementation--------------------------------------//
    
    private final static String ASSESSMENT_DATA_QUERY_STATEMENT = 
			"SELECT sts.SiteNumber, s.SubjectNumber, s.Initials, s.RandomizationNumber, "
			+ "s.ScreeningNumber, sv.SVID, stv.EnglishName AS VisitName,"
			+ "ft.Abbreviation AS FormName," + "fr.Revision,"
			+ "fti.AnalyticalName, fti.ItemName, fti.ShortName as DisplayText,"
			+ "dbo.ufn_GetDecode(fti.FormTemplateVersionID, fti.ScoreType, fri.Value, 'en-us') as DisplayValue, "
			+ "c.EnglishName "
			+ "FROM Study st " + "INNER JOIN StudySite sts ON st.StudyID = sts.StudyID "
			+ "INNER JOIN Subject s ON sts.StudySiteID = s.StudySiteID "
			+ "INNER JOIN SubjectVisit sv ON s.SubjectID = sv.SubjectID "
			+ "INNER JOIN SubjectVisitFormInstance svfi ON sv.SubjectVisitID = svfi.SubjectVisitID "
			+ "INNER JOIN FormInstance fi ON svfi.FormID = fi.FormID "
			+ "INNER JOIN FormInstanceItem fii ON fi.FormID = fii.FormID "
			+ "INNER JOIN FormTemplateItem fti ON fii.FormTemplateItemID = fti.FormTemplateItemID "
			+ "INNER JOIN FormRevision fr ON fr.[FormID] = fi.[FormID] "
			+ "INNER JOIN FormRevisionItem fri on fr.FormRevisionID = fri.FormRevisionID and fri.FormTemplateItemID = fti.FormTemplateItemID "
			+ "INNER JOIN CultureFormTemplateVersion cftv ON fi.CultureFormTemplateVersionID = cftv.CultureFormTemplateVersionID "
			+ "INNER JOIN StudyVisit stv ON st.StudyID = stv.StudyID AND sv.StudyVisitID = stv.StudyVisitID "
			+ "INNER JOIN CultureFormTemplate cft on cftv.[CultureFormTemplateID] = cft.[CultureFormTemplateID] "
			+ "INNER JOIN FormTemplateVersion ftv on cft.[FormTemplateVersionID] = ftv.[FormTemplateVersionID] "
			+ "INNER JOIN FormTemplate ft on ftv.[FormTemplateID] = ft.[FormTemplateID] "
			+ "LEFT OUTER JOIN Code c on c.[Key] = fri.OverrideValue and c.CodeType = 'OverrideReason' "
			+ "Where ft.Abbreviation = ? AND sv.SVID = ? and fti.ForDisplay =1 "
					+ "ORDER BY fti.ItemName";

	private static List<Score> convertScoresToList(ResultSet results) throws SQLException {
		List<Score> scores = new ArrayList<Score>();
		while (results.next()) {
			String question = results.getString("DisplayText"); 
			String answer = results.getString("DisplayValue");
			String question_id = results.getString("ItemName");
			Score temp = new Score();
			temp.setScore_id(question_id);
			temp.setScore_text(question);
			temp.setAnswer(answer);
			scores.add(temp);
		}
		return scores;
	}
    
    private static List<Score> getCompleteAssessmentDataFromDB(String formName, int svid) throws SQLException{
    	Connection conn = getDBConnection();
    	PreparedStatement preparedStatement = null;
    	List<Score> results = null;
    	try {
			preparedStatement = conn.prepareStatement(ASSESSMENT_DATA_QUERY_STATEMENT);
			preparedStatement.setString(1, formName);
			preparedStatement.setInt(2, svid);
			results = convertScoresToList(preparedStatement.executeQuery());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(preparedStatement != null )
				preparedStatement.close();
			if(conn != null)
				conn.close();
		}
    	return results;
    }
    
    public static List<Score> getCompletedScoresFromDBFor(String formName, int svid){
    	try {
			return getCompleteAssessmentDataFromDB(formName, svid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return null;
    }
}
