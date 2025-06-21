package dao;
import java.sql.*;

import db.DBConnection;
import model.User;

public class UserDAO {
	public void addUser(String username,String password,String role) {
		try (Connection conn = DBConnection.getConnection()) {
	        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
	        PreparedStatement stmt = conn.prepareStatement(sql);
	        stmt.setString(1, username);
	        stmt.setString(2, password);
	        stmt.setString(3, role);
	        stmt.executeUpdate();
	        System.out.println("✅ User added successfully.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	public void deleteUser(String username) {
		try(Connection conn = DBConnection.getConnection()){
			String sql = "DELETE FROM users WHERE username = ?";
	        PreparedStatement stmt = conn.prepareStatement(sql);
	        stmt.setString(1, username);
	        int rows = stmt.executeUpdate();
	        if (rows > 0)
	            System.out.println("✅ User deleted successfully.");
	        else
	            System.out.println("❌ Username not found.");
		}
		catch(SQLException e) {
			 e.printStackTrace();
		}
	}
	public User login(String username,String password) {
		try(Connection conn = DBConnection.getConnection()){
			String sql="SELECT * FROM users WHERE username=? AND password=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1,username);
			stmt.setString(2,password);
			ResultSet rs = stmt.executeQuery();
			 if (rs.next()) {
	                return new User(
	                    rs.getInt("user_id"),
	                    rs.getString("username"),
	                    rs.getString("password"),
	                    rs.getString("role")
	                );
	            }
		}
		 catch (SQLException e) {
	            e.printStackTrace();
	        }
		return null;
	}
}
