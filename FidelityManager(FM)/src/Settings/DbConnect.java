package Settings;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;


public class DbConnect {
	
	
	
	public boolean dbConnect(String db_connect_string, String db_userid, String db_password){
		Logger log = Logger.getLogger("myLog");
	     try {
	         Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	         Connection conn = DriverManager.getConnection(db_connect_string,
	                  db_userid, db_password);
	         statement = conn.createStatement();
	         return true;
	         
	      } catch (Exception e) {
	    	  log.fatal("Errore connessione db : "+e);
	    	  return false;
	      }
	   }
	
	public ResultSet query(String q){
		ResultSet rs;
		try {
			rs = statement.executeQuery(q);
			
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	Statement statement;
	
	
}
