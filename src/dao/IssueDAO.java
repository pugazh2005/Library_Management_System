package dao;
import db.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class IssueDAO {
	public void borrowBook(int userId,int BookId) {
		try(Connection conn = DBConnection.getConnection()){
			String checkSql="SELECT available_quantity FROM books where book_id=?";
			PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, BookId);
            ResultSet rs = checkStmt.executeQuery();
            if(rs.next()) {
            	int available=rs.getInt("available_quantity");
            	if(available<=0) {
            		System.out.println("❌ Book not available.");
                    return;
            	}
            }
            else {
            	 System.out.println("❌ Book ID not found.");
                 return;
            }
            LocalDate issueDate=LocalDate.now();
            LocalDate dueDate=issueDate.plusDays(7);
            String issueSql = "INSERT INTO issued_books (user_id, book_id, issue_date, due_date) VALUES (?, ?, ?, ?)";
            PreparedStatement issueStmt = conn.prepareStatement(issueSql);
            issueStmt.setInt(1, userId);
            issueStmt.setInt(2, BookId);
            issueStmt.setDate(3, Date.valueOf(issueDate));
            issueStmt.setDate(4, Date.valueOf(dueDate));
            issueStmt.executeUpdate();
            
            String updateBook = "UPDATE books SET available_quantity = available_quantity - 1 WHERE book_id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateBook);
            updateStmt.setInt(1, BookId);
            updateStmt.executeUpdate();
            
            System.out.println("✅ Book borrowed. Due date: " + dueDate);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	public void returnBook(int userId,int BookId) {
		try(Connection conn = DBConnection.getConnection()){
			String sql="SELECT issue_id, due_date FROM issued_books WHERE user_id = ? AND book_id = ? AND return_date IS NULL";
			PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, BookId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
            	int issueId = rs.getInt("issue_id");
                LocalDate dueDate = rs.getDate("due_date").toLocalDate();
                LocalDate returnDate = LocalDate.now();

                long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
                double fine = daysLate > 0 ? daysLate * 5.0 : 0;
                
                String updateSql = "UPDATE issued_books SET return_date = ?, fine_amount = ? WHERE issue_id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setDate(1, Date.valueOf(returnDate));
                updateStmt.setDouble(2, fine);
                updateStmt.setInt(3, issueId);
                updateStmt.executeUpdate();
                
                String updateBook = "UPDATE books SET available_quantity = available_quantity + 1 WHERE book_id = ?";
                PreparedStatement updateBookStmt = conn.prepareStatement(updateBook);
                updateBookStmt.setInt(1, BookId);
                updateBookStmt.executeUpdate();

                System.out.println("✅ Book returned. Elapsed days: " + ChronoUnit.DAYS.between(dueDate.minusDays(7), returnDate));
                System.out.println("💰 Fine: ₹" + fine);
            }
            else {
                System.out.println("❌ No borrowed record found for this book.");
            }
		}
		catch(SQLException e) {
			e.printStackTrace();
		}	
	}
	public void viewuserHistory(int userId) {
		try(Connection conn=DBConnection.getConnection()){
			String sql="""
					 SELECT i.issue_id, b.title, b.author, i.issue_date, i.due_date, i.return_date, i.fine_amount
            FROM issued_books i
            JOIN books b ON i.book_id = b.book_id
            WHERE i.user_id = ?
            ORDER BY i.issue_date DESC
					""";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1,userId);
			ResultSet rs = stmt.executeQuery();

	        double totalFine = 0;
	        int totalBooks = 0;
	        System.out.println("\n📘 Borrowing History:");
	        System.out.printf("%-4s %-25s %-15s %-12s %-12s %-12s %-6s\n", "ID", "Title", "Author", "Issue", "Due", "Return", "Fine");
	        
	        while(rs.next()) {
	        	totalBooks++;
	        	double fine=rs.getDouble("fine_amount");
	        	totalFine+=fine;
	        	
	        	System.out.printf("%-4d %-25s %-15s %-12s %-12s %-12s ₹%-6.2f\n",
	                    rs.getInt("issue_id"),
	                    rs.getString("title"),
	                    rs.getString("author"),
	                    rs.getDate("issue_date"),
	                    rs.getDate("due_date"),
	                    rs.getDate("return_date") != null ? rs.getDate("return_date") : "Pending",
	                    fine
	                );
	        	System.out.println("\n📦 Total Books Borrowed: \" + totalBooks");
	        	System.out.println("💰 Total Fine Paid: ₹" + totalFine);
	        	
	        	if(totalBooks==0) {
	        		System.out.println("😶 No history found.");
	        	}
	        }
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public void viewUserHistoryByAdmin(String username) {
	    try (Connection conn = DBConnection.getConnection()) {
	        String getUserIdSql = "SELECT user_id FROM users WHERE username = ?";
	        PreparedStatement userStmt = conn.prepareStatement(getUserIdSql);
	        userStmt.setString(1, username);
	        ResultSet userRs = userStmt.executeQuery();

	        if (!userRs.next()) {
	            System.out.println("❌ User not found.");
	            return;
	        }

	        int userId = userRs.getInt("user_id");

	        String sql = """
	            SELECT i.issue_id, b.title, b.author, i.issue_date, i.due_date, i.return_date, i.fine_amount
	            FROM issued_books i
	            JOIN books b ON i.book_id = b.book_id
	            WHERE i.user_id = ?
	            ORDER BY i.issue_date DESC
	            """;

	        PreparedStatement stmt = conn.prepareStatement(sql);
	        stmt.setInt(1, userId);
	        ResultSet rs = stmt.executeQuery();

	        double totalFine = 0;
	        int totalBooks = 0;

	        System.out.println("\n📘 Borrowing History for: " + username);
	        System.out.printf("%-4s %-25s %-15s %-12s %-12s %-12s %-6s\n", "ID", "Title", "Author", "Issue", "Due", "Return", "Fine");

	        while (rs.next()) {
	            totalBooks++;
	            double fine = rs.getDouble("fine_amount");
	            totalFine += fine;

	            System.out.printf("%-4d %-25s %-15s %-12s %-12s %-12s ₹%-6.2f\n",
	                rs.getInt("issue_id"),
	                rs.getString("title"),
	                rs.getString("author"),
	                rs.getDate("issue_date"),
	                rs.getDate("due_date"),
	                rs.getDate("return_date") != null ? rs.getDate("return_date") : "Pending",
	                fine
	            );
	        }

	        if (totalBooks == 0) {
	            System.out.println("😶 No borrowing history found for this user.");
	        } else {
	            System.out.println("\n📦 Total Books Borrowed: " + totalBooks);
	            System.out.println("💰 Total Fine Paid: ₹" + totalFine);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

}
