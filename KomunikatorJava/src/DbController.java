
	import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
	
public class DbController 
{

		public static boolean createuser (Connection conn, String login, String password)
		{

			String query = " insert into komunikatorpsz.User (Login, Password, LoggedIn)"
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
			String url = "jdbc:mysql://db4free.net:3306/";
			String driver = "com.mysql.jdbc.Driver";
			String userName = "komunikator";
			String password = "pawel01";
			
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
					.executeQuery("select * from komunikatorpsz.User");
			while (resultSet.next()) {
				dbLogin = resultSet.getString("Login");

				dbPassword = resultSet.getString("Password");

				if (dbLogin.equals(login))
					if (dbPassword.equals(password)) {
						authentication = true;
						break;
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
}
