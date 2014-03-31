
	import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
	
public class DbController 
{

		public static boolean createuser (Connection conn, String login, String password)
		{

			String query = " insert into 8306_traffii1.User (Login, Password, LoggedIn)"
					+ " values (?, ?, ?)";
			PreparedStatement preparedStmt;
			try
			{
				preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1, login);
				preparedStmt.setString(2, password);
				preparedStmt.setInt(3, 0);

				preparedStmt.execute();
				return true;
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				return false;
			}
			
		}
		public static Connection dbconnect ()
		{
			String url = "jdbc:mysql://mysql3.webio.pl:3306/";
			String driver = "com.mysql.jdbc.Driver";
			String userName = "8306_traffii";
			String password = "terra2002!";
			
			try
			{
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(url, userName, password);
			return conn;
			}
			catch (Exception ex)
			{
				return null;
			}
		}
		public static boolean login (Connection conn, String login, String password)
		{

			ResultSet resultSet = null;
			Statement statement = null;
			String dbLogin;
			String dbPassword;
			boolean authentication = false;
			
			try {
				statement = conn.createStatement();
				resultSet = statement
					.executeQuery("select * from 8306_traffii1.User");
			while (resultSet.next()) {
				dbLogin = resultSet.getString("Login");

				dbPassword = resultSet.getString("Password");

				if (dbLogin.equals(login))
					if (dbPassword.equals(password)) {
						authentication = true;
						
						String query = "UPDATE 8306_traffii1.User " +
				                   "SET LoggedIn = 1 WHERE Login in (?)";
						PreparedStatement preparedStmt;
						try
						{
							preparedStmt = conn.prepareStatement(query);
							preparedStmt.setString(1, login);

							preparedStmt.execute();
							return true;
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
							return false;
						}
					} else
						authentication = false;
				
			}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return authentication;

		}
		public static boolean logout(String login)
		{
			Connection conn = dbconnect();

			String query = "UPDATE 8306_traffii1.User " +
	                   "SET LoggedIn = 0 WHERE Login in (?)";
			PreparedStatement preparedStmt;
			try
			{
				preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1, login);

				preparedStmt.execute();
				conn.close();
				return true;
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				return false;
			}
		}
		public static List<String> getfriends(Connection conn, String login)
		{
			List<String> result = new ArrayList<String>();
			int userid=0;
			int id;
			String query1 = "select `ID` from 8306_traffii1.user Where `Login` = (?)";
			String query2 = "select `friendid` from 8306_traffii1.friends Where `userid` = (?)";
			String query3 = "select `Login` from 8306_traffii1.user Where `ID` = (?)";
			PreparedStatement preparedStmt1;
			PreparedStatement preparedStmt2;
			PreparedStatement preparedStmt3;
			
			ResultSet resultSet1 = null;
			ResultSet resultSet2 = null;
			ResultSet resultSet3 = null;
			
			try {
				preparedStmt1 = conn.prepareStatement(query1);
				preparedStmt1.setString(1, login);
				resultSet1=preparedStmt1.executeQuery();
				while(resultSet1.next())
				{
				userid = resultSet1.getInt("ID");
				System.out.println(userid);
				}
				preparedStmt2 = conn.prepareStatement(query2);
				preparedStmt2.setInt(1, userid);
				resultSet2=preparedStmt2.executeQuery();
				while(resultSet2.next())
				{
					id = resultSet2.getInt("friendid");
					System.out.println(id);
					preparedStmt3 = conn.prepareStatement(query3);
					preparedStmt3.setInt(1, id);
					resultSet3=preparedStmt3.executeQuery();
					while(resultSet3.next())
					{
					System.out.println(resultSet3.getString("Login"));
					result.add(resultSet3.getString("Login"));
					}
				}
				return result;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
			
		}
		public static boolean addfriend(Connection conn, String loggedas, String friendlogin)
		{
			ResultSet resultSet = null;
			Statement statement = null;
			String query1 = "select `ID` from 8306_traffii1.user Where `Login` = (?)";
			PreparedStatement preparedStmt1;
			PreparedStatement preparedStmt2;
			PreparedStatement preparedStmt3;
			String query2 = "INSERT INTO `8306_traffii1`.`friends` (`userid`, `friendid`) VALUES ((?), (?)), ((?), (?))";
			ResultSet resultSet1 = null;
			ResultSet resultSet2 = null;
			int userid=0;
			int friendid=0;
			String dbLogin;
		
			boolean authentication = false;
			
			
			
				try {
					
					statement = conn.createStatement();
				resultSet = statement
					.executeQuery("select * from 8306_traffii1.User");
				
			while (resultSet.next()) 
			{
				dbLogin = resultSet.getString("Login");

				if (dbLogin.equals(friendlogin))
				{
					authentication = true;
					
				}
			} 
				} 
				catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			if(authentication)
			{
				try {
					preparedStmt1 = conn.prepareStatement(query1);
					preparedStmt1.setString(1, loggedas);
					resultSet1=preparedStmt1.executeQuery();
					while(resultSet1.next())
					{
					userid = resultSet1.getInt("ID");
					System.out.println(userid);
					}
					
					preparedStmt2 = conn.prepareStatement(query1);
					preparedStmt2.setString(1, friendlogin);
					resultSet2=preparedStmt2.executeQuery();
					while(resultSet2.next())
					{
					friendid = resultSet2.getInt("ID");
					System.out.println(friendid);
					}

					preparedStmt3 = conn.prepareStatement(query2);
					preparedStmt3.setInt(1, userid);
					preparedStmt3.setInt(2, friendid);
					preparedStmt3.setInt(3, friendid);
					preparedStmt3.setInt(4, userid);
					preparedStmt3.execute();
					
					
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return authentication;
			}
			else
				return authentication;
			
		}
		
		public static List<Integer> getactivity(Connection conn, String loggedas)
		{
			List<Integer> result = new ArrayList<Integer>();
			PreparedStatement preparedStmt1;
			PreparedStatement preparedStmt2;
			PreparedStatement preparedStmt3;
			ResultSet resultSet1 = null;
			ResultSet resultSet2 = null;
			ResultSet resultSet3 = null;
			int friendid, userid = 0;
			String query1 = "select `ID` from 8306_traffii1.user Where `Login` = (?)";
			String query2 = "select `friendid` from 8306_traffii1.friends Where `userid` = (?)";
			String query3 = "select `LoggedIn` from 8306_traffii1.user Where `ID` = (?)";
			
			try {
				preparedStmt1 = conn.prepareStatement(query1);
				preparedStmt1.setString(1, loggedas);
				resultSet1=preparedStmt1.executeQuery();
				while(resultSet1.next())
				{
				userid = resultSet1.getInt("ID");
				System.out.println(userid);
				}	

				preparedStmt2 = conn.prepareStatement(query2);
				preparedStmt2.setInt(1, userid);
				resultSet2=preparedStmt2.executeQuery();
				while(resultSet2.next())
				{
					friendid = resultSet2.getInt("friendid");
					System.out.println(friendid);
					preparedStmt3 = conn.prepareStatement(query3);
					preparedStmt3.setInt(1, friendid);
					resultSet3=preparedStmt3.executeQuery();
					while(resultSet3.next())
					{
					System.out.println(resultSet3.getInt("LoggedIn"));
					result.add(resultSet3.getInt("LoggedIn"));
					}
				}

				return result;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			}
		}
		
}
