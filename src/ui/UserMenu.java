package ui;

import dao.BookDAO;
import dao.IssueDAO;
import model.Book;
import model.User;

import java.util.List;
import java.util.Scanner;

public class UserMenu {
		
	public static void show(User user) {
		Scanner sc = new Scanner(System.in);
		
		IssueDAO issueDAO = new IssueDAO();
        BookDAO bookDAO = new BookDAO();
        
        while(true) {
        	System.out.println("\n=== User Menu (" + user.getUsername() + ") ===");
            System.out.println("1. View All Books");
            System.out.println("2. Borrow Book");
            System.out.println("3. Return Book");
            System.out.println("4. View Borrow History");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            
            switch(choice) {
            case 1:
            	 List<Book> books = bookDAO.getAllBooks();
                 System.out.println("\n--- Available Books ---");
                 for (Book b : books) {
                	 System.out.printf("ID: %d | Title: %s | Author: %s | Category: %s | Available: %d\n",
                			    b.getBookId(), b.getTitle(), b.getAuthor(), b.getCategory(), b.getAvailableQuantity());
                 }
                 break;
            case 2:
                System.out.print("Enter Book ID to borrow: ");
                int bookIdBorrow = sc.nextInt();
                issueDAO.borrowBook(user.getUserId(), bookIdBorrow);
                break;
            case 3:
                System.out.print("Enter Book ID to return: ");
                int bookIdReturn = sc.nextInt();
                issueDAO.returnBook(user.getUserId(), bookIdReturn);
                break;
            case 4:
                issueDAO.viewuserHistory(user.getUserId());
                break;
            case 5:
                System.out.println("👋 Logged out.");
                return;
            default:
                System.out.println("❌ Invalid choice!");
            }
        }
	}

}
