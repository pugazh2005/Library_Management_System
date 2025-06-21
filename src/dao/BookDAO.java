package dao;
import db.DBConnection;
import model.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Book;
public class BookDAO {
	public void addBook(Book book) {
		try(Connection conn=DBConnection.getConnection()){
			String sql="INSERT INTO books (title, author, total_quantity, available_quantity, category) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setInt(3, book.getTotalQuantity());
            stmt.setInt(4, book.getAvailableQuantity());
            stmt.setString(5, book.getCategory());
            stmt.executeUpdate();
            System.out.println("✅ Book added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
		}
	public void deleteBook(int bookId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM books WHERE book_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, bookId);
            int rows = stmt.executeUpdate();
            if (rows > 0)
                System.out.println("✅ Book deleted successfully.");
            else
                System.out.println("❌ Book ID not found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM books";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Book b = new Book(
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getInt("total_quantity"),
                    rs.getInt("available_quantity"),
                    rs.getString("category")
                );
                books.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
	
	}


