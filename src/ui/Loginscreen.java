package ui;
import dao.UserDAO;
import model.User;
import java.util.Scanner;

public class Loginscreen {
	public static User login() {
		Scanner sc = new Scanner(System.in);
        System.out.println("=== Login ===");
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        UserDAO dao = new UserDAO();
        User user = dao.login(username, password);
        if (user != null) {
            System.out.println("✅ Login successful! Welcome " + user.getUsername() + " (" + user.getRole() + ")");
            return user;
        } else {
            System.out.println("❌ Invalid credentials. Try again.");
            return login();
        }
	}
}
