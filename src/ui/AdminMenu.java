package ui;
import dao.BookDAO;
import dao.UserDAO;
import model.Book;
import model.User;

import java.util.List;
import java.util.Scanner;
import dao.IssueDAO;
public class AdminMenu {
	public static void show(User admin) {
		Scanner sc = new Scanner(System.in);
        BookDAO bookDAO = new BookDAO();
        UserDAO userDAO = new UserDAO();
        
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. Add Book");
            System.out.println("2. Delete Book");
            System.out.println("3. Add User");
            System.out.println("4. Delete User");
            System.out.println("5. View All Books");
            System.out.println("6. View User Borrow History");
            System.out.println("7. Logout");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); 
            switch (choice) {
            case 1:
                System.out.print("Title: ");
                String title = sc.nextLine();
                System.out.print("Author: ");
                String author = sc.nextLine();
                System.out.print("Total Quantity: ");
                int total = sc.nextInt();
                sc.nextLine();
                System.out.print("Category: ");
                String category = sc.nextLine();
                Book book = new Book(0, title, author, total, total, category);
                bookDAO.addBook(book);
                break;
            case 2:
                System.out.print("Enter Book ID to delete: ");
                int bookId = sc.nextInt();
                sc.nextLine();
                bookDAO.deleteBook(bookId);
                break;
            case 3:
            	System.out.print("Username: ");
                String uname = sc.nextLine();
                System.out.print("Password: ");
                String pass = sc.nextLine();
                System.out.print("Role (student/teacher): ");
                String role = sc.nextLine();
                userDAO.addUser(uname, pass, role);
                break;
            case 4:
                System.out.print("Enter Username to delete: ");
                String delUser = sc.nextLine();
                userDAO.deleteUser(delUser);
                break;
            case 5:
            	List<Book> books = bookDAO.getAllBooks();
            	System.out.println("\n--- Library Books ---");
                for (Book b : books) {
                    System.out.printf("ID: %d | Title: %s | Author: %s | Total: %d | Available: %d\n",
                        b.getBookId(), b.getTitle(), b.getAuthor(),
                        b.getTotalQuantity(), b.getAvailableQuantity());
                }
                break;
            case 6:
                System.out.print("Enter Username to view history: ");
                String username = sc.nextLine();
                new IssueDAO().viewUserHistoryByAdmin(username);
                break;

            case 7:
                System.out.println("👋 Logged out.");
                return;
            default:
                System.out.println("❌ Invalid choice!");
            }
        }
	}
}

