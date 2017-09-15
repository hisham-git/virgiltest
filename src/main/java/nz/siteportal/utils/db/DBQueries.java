package nz.siteportal.utils.db;

import java.sql.SQLException;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;

import mt.siteportal.utils.db.DbUtil;

public class DBQueries {

	/**
	 * 
	 * @param study
	 * @param site
	 * @return dataset as ITable object
	 */
	public ITable getQueriesByStudySite(String study, String site) {
		String sql = "select QueryID, st.Name as StudyName,DateDiff(DAY,CreationDate,GETDATE()) as Age," + "Text,"
				+ "TimeStamp," + "(CreatorFirstName+' '+CreatorLastName) as CreatorName,"
				// + "ReplyText,"
				// + "ReplyTimestamp,"
				// + "ReplierFirstName,"
				// + "ReplierLastName,"
				+ "SiteNumber," + "s.SubjectNumber," + "IsNULL(VisitDefaultName,'') as VisitName,"
				+ "IsNULL(FormName,'') as Assessment," + "IsNULL(FormItemDefaultName,'') as ScoreItem "
				+ "from viewQuery " + "inner join Subject s " + "on s.SubjectID = viewQuery.SubjectID "
				+ "inner join Study st " + "on st.StudyID = viewQuery.StudyID " + "Where st.Name = '" + study + "' "
				+ "AND viewQuery.SiteNumber = '" + site + "' " + "AND viewQuery.ReplyID IS NULL;";

		ITable table = null;
		try {
			table = DbUtil.getTempTable(sql);
		} catch (DataSetException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return table;
	}
	/**
	 * 
	 * @param study
	 * @param site
	 * @param filterVal
	 * @param type
	 * @return dataset
	 */
	public ITable getQueriesByStudySite(String study, String site, String filterVal, String type) {
		// By default the filter is for Subject
		String sqlFilter = "AND viewQuery.SubjectNumber = '" + filterVal + "' ;";
		switch (type) {
		case "Visit":
			sqlFilter = "AND (ISNULL(viewQuery.VisitDefaultName,'') + viewQuery.VisitNumber) = '" + filterVal + "';";
			// case "Responded": sqlFilter = "AND viewQuery.Addressed=1;";
			break;
		case "Assessment":
			sqlFilter = "AND FormName = '" + filterVal + "';";
			break;
		default:
			break;
		}

		String sql = "select QueryID, st.Name as StudyName,DateDiff(DAY,CreationDate,GETDATE()) as Age," + "Text,"
				+ "TimeStamp," + "(CreatorFirstName+' '+CreatorLastName) as CreatorName,"
				// + "ReplyText,"
				// + "ReplyTimestamp,"
				// + "ReplierFirstName,"
				// + "ReplierLastName,"
				+ "SiteNumber," + "s.SubjectNumber," + "IsNULL(VisitDefaultName,'') as VisitName,"
				+ "IsNULL(FormName,'') as Assessment," + "IsNULL(FormItemDefaultName,'') as ScoreItem "
				+ "from viewQuery " + "inner join Subject s " + "on s.SubjectID = viewQuery.SubjectID "
				+ "inner join Study st " + "on st.StudyID = viewQuery.StudyID " + "Where st.Name = '" + study + "' "
				+ "AND viewQuery.SiteNumber = '" + site + "' " + "AND viewQuery.ReplyID  IS NULL " + sqlFilter;

		ITable table = null;
		try {
			table = DbUtil.getTempTable(sql);
		} catch (DataSetException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return table;
	}


	/**
	 * 
	 * @param study
	 * @param site
	 * @param staus
	 * @return QueryCount
	 */
	public static int getQueryCountByStatus(String study, String site, String status) {
		// by default All Sites
		String sqlSiteFilter = "";
		// Set specific site filter
		if(!site.equals("All Sites"))
			sqlSiteFilter = "AND viewQuery.SiteNumber = '" + site+ "' ";
		// by default the status is Open
		String sqlFilter = "AND viewQuery.ReplyID IS NULL " + "AND viewQuery.CloseDate IS NULL;";
		switch (status) {
		case "Responded":
			sqlFilter = "AND viewQuery.ReplyID IS NOT NULL;";
			// case "Responded": sqlFilter = "AND viewQuery.Addressed=1;";
			break;
		case "Closed":
			sqlFilter = "AND viewQuery.CloseDate IS NOT NULL;";
			break;
		default:
			break;
		}
		String sql = "select count(QueryID) as Count " + "from viewQuery " + "inner join Subject s "
				+ "on s.SubjectID = viewQuery.SubjectID " + "inner join Study st "
				+ "on st.StudyID = viewQuery.StudyID " + "Where st.Name = '" + study + "' "
				+ sqlSiteFilter + sqlFilter;

		ITable table = null;
		int count = 0;
		try {
			table = DbUtil.getTempTable(sql);
			count = (int) table.getValue(0, "Count");
		} catch (DataSetException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 
	 * @param study
	 * @param site
	 * @param subject
	 * @return Query Count
	 */
	public int getQueryCountBySubject(String study, String site, String subject) {
		String sql = "select count(QueryID) as Count " + "from viewQuery " + "inner join Subject s "
				+ "on s.SubjectID = viewQuery.SubjectID " + "inner join Study st "
				+ "on st.StudyID = viewQuery.StudyID " + "Where st.Name = '" + study + "' "
				+ "AND viewQuery.SiteNumber = '" + site + "' " + "AND viewQuery.SubjectNumber = '" + subject + "';";

		ITable table = null;
		int count = 0;
		try {
			table = DbUtil.getTempTable(sql);
			count = (int) table.getValue(0, "Count");
		} catch (DataSetException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}

}
