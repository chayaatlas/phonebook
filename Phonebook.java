import java.util.Scanner;
import java.io.IOException;

public class Phonebook {
	public static void main(String[] args) throws 
		IOException, InterruptedException {
		Scanner keyboard = new Scanner(System.in);
		Directory phonebook = new Directory();
		// allows user to read data from file
		System.out.println
			("Would you like to read contacts from a file?");
		if (keyboard.nextLine().equalsIgnoreCase("yes")){
			phonebook.read();
		}
		// allows user to choose action from main menu
		for (;;) { // endless loop - user breaks out by entering "6"
			int choice = -1; 
			do {
				System.out.println();
				System.out.println("1 = Add New");
				System.out.println("2 = View All");
				System.out.println("3 = Search");
				System.out.println("4 = Edit");
				System.out.println("5 = Delete");
				System.out.println("6 = Exit Program");
				try {
					choice = keyboard.nextInt();
					keyboard.nextLine(); // moves Scanner past prev. input
				}
				catch(Exception e) {
					System.out.println("Invalid input");
					phonebook.close();
				}
				new ProcessBuilder("cmd" ,"/c" ,"cls").
					inheritIO().start().waitFor();
			} while (choice < 1 || choice > 6);
			if (choice == 1)
				phonebook.setNew();
			else if (choice == 2)
				phonebook.view();
			else if (choice == 3)
				phonebook.displaySearch();
			else if (choice == 4)
				phonebook.update();
			else if (choice == 5)
				phonebook.delete();
			else 
				phonebook.close();
		}
	}
}