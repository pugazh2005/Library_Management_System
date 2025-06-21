package main;
import model.User;
import ui.Loginscreen;
import ui.AdminMenu;
import ui.UserMenu;

public class LibraryApp {

	public static void main(String[] args) {
		User user=Loginscreen.login();
		switch (user.getRole()) {
        case "admin":
        	AdminMenu.show(user);
            break;

        default:
            UserMenu.show(user);
    }

	}

}
