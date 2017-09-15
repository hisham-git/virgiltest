package nz.siteportal.utils.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.testng.annotations.Test;

/**
 * DB Utility class based on DBUnit
 * @author Ayman
 *
 */
public class DBUtil {
    /** 
     * Establish database connection with connection string from properties file
     * Returns the result set 
     */
	public ResultSet openConnection(String sqlString){
		ResultSet rs =null;
		Connection  conn = null;
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//			conn = DriverManager.getConnection("jdbc:sqlserver://10.21.64.110;databaseName=StrikeforceDB;", "sa", "#3Pencil");
			conn = DriverManager.getConnection("jdbc:sqlserver://192.168.1.86;databaseName=StrikeforceDB;", "sa", "Reli123");
			Statement st = conn.createStatement();					
			rs = st.executeQuery(sqlString);			
		}
		catch(Exception e){
			System.out.println(e);			
		}
		finally{
			if(conn!=null)
				conn=null;
		}
		return rs;
	}
	
/*	@Test(
			groups={"Query Panel", "Query Context"},
			description="Checks if Query Counts are contextual for a Filtered view"
		)
	public void test(){
		String sqlString = "Select TOP 10 FirstName,LastName from [StrikeForceDB].[dbo].[Person]";
		ResultSet rs = this.openConnection(sqlString);
		try {
			  while(rs.next()){
				System.out.println(rs.getString(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
*/
	
	/**
/****** Script for SelectTopNRows command from SSMS  *****
select DateDiff(DAY,CreationDate,GETDATE()) as Age,
ss.DefaultName as SubStatus,
Text,
TimeStamp,
CreatorFirstName,
CreatorLastName,
ReplyText,
ReplyTimestamp,
ReplierFirstName,
ReplierLastName,
SiteNumber,
s.SubjectNumber,
VisitNumber,
FormName,
FormItemDefaultName
from viewQuery
inner join Subject s
on s.SubjectID = viewQuery.SubjectID
inner join SubjectStatus ss
on s.SubjectStatusID = ss.SubjectStatusID
Where ss.DefaultName = 'NEW' 

	 */
}
