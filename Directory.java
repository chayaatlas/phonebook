import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.lang.Math;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;

public class Directory {
	private static Scanner keyboard = new Scanner(System.in);
	ArrayList<Person> contacts = new ArrayList<Person>();
	
	// gets user input to new Person to contacts
	public void setNew() {
		String fname, lname, email, phone;
		boolean duplicate = false;
		fname = setVar("first name", "[a-zA-Z '-]+", "alphabetic letters");
		lname = setVar("last name", "[a-zA-Z '-]+", "alphabetic letters");
		email = setVar("email address", 
			"^(([\\w.!#$%&'*+-/=?^_`{}|]+)@([\\w-.]+)\\.([A-Za-z]{2,5}))$+", 
			"standard email format");
		phone = setVar("U.S. phone number", "^\\d{10}$", "10 numeric digits");
		// check for duplicate email address
		ArrayList<Integer> emailDuplicates = search(email);
		if (emailDuplicates.size() > 0) {
			System.out.println("Contacts with that email address:");
			for (int i = 0; i < emailDuplicates.size(); i++) {
				System.out.println
					(contacts.get(emailDuplicates.get(i)));
				System.out.println();
			}
			duplicate = true;
		}
		// check for duplicate phone number
		ArrayList<Integer> phoneDuplicates = search(phone);
		if (phoneDuplicates.size() > 0) {
			System.out.println("Contacts with that phone number:");
			for (int i = 0; i < phoneDuplicates.size(); i++) {
				System.out.println
					(contacts.get(phoneDuplicates.get(i)));
				System.out.println();
			}
			duplicate = true;
		}
		if (duplicate){
			System.out.println("Would you like to add this contact?");
			if (!keyboard.nextLine().equalsIgnoreCase("yes")) {
				return; // exit function - doesn't add contact
			}
		}
		addNew(fname, lname, email, phone); // add contact
		System.out.println("Contact added");
	}
	
	// adds new person to contacts
	private void addNew
		(String fname, String lname, String email, String phone) {
		Person temp = new Person(fname, lname, email, phone);
		if (!contacts.add(temp)) { // this should not happen
			System.out.println("Error");
			close();
		}
	}
	
	// prompts user to set a String for "seeking" with restrictions
	private static String setVar (String seeking, 
		String restriction, String restrictionDescript) {
		String newVar;
		try {
			System.out.println("Enter "+seeking+
				" (only "+ restrictionDescript +" allowed): ");
			newVar = keyboard.nextLine();
			if (!newVar.matches(restriction)) {
				throw new Exception();
			}
		}
		catch (Exception e) {
			System.out.println("That input was invalid. Try again.");
			return(setVar(seeking, restriction, restrictionDescript));
		}
		return newVar;
	}
	
	// displays all contacts
	public void view() {
		for (int i = 0; i < contacts.size(); i++) {
			System.out.println(contacts.get(i));
		}
		System.out.println("Displayed all contacts.");
	}
	
	// determines which contacts (if any) contains "quest"
	private ArrayList<Integer> search(String quest) {
		ArrayList<Integer> found = new ArrayList<Integer>(5);
		for (int i = 0; i < contacts.size(); i++) {
			if (contacts.get(i).email.equalsIgnoreCase(quest) ||
				contacts.get(i).fname.equalsIgnoreCase(quest) ||
				contacts.get(i).lname.equalsIgnoreCase(quest)||
				contacts.get(i).phone.equalsIgnoreCase(quest)) {
				// add index of contact w/ quest to beginning of found
				found.add(0,i);
			} else {
			// if similar (but not equal)
				if (contacts.get(i).email.matches
					("(.*)"+quest+"(.*)") ||
					contacts.get(i).fname.matches
					("(.*)"+quest+"(.*)") ||
					contacts.get(i).lname.matches
					("(.*)"+quest+"(.*)") ||
					contacts.get(i).phone.matches
					("(.*)"+quest+"(.*)")  ) {
					// add index of contact w/ quest to end of found
					found.add(i);
				}
			}
		}
		found.trimToSize();
		return found; // if not found, return -1
	}
	
	// prompts user for search input with message "message"
	// returns array of indexes where search input is found
	private ArrayList<Integer> promptSearch(String message) {
		String quest = "";
		System.out.println("Which contact would you like to " + 
			message +"?\n" +
			"Enter any information stored about that contact.\n" +
			"(Note: for a phone number, only use numeric digits.)");
		quest = keyboard.nextLine();
		ArrayList<Integer> result = search(quest);
		return result;
	}
	
	// displays search results
	public void displaySearch() {
		ArrayList<Integer> results = promptSearch("search");
		System.out.println("\nDisplaying Results:");
		if (results.size() > 0) { // if there is a matching contact
			for (int i = 0; i < results.size(); i++) {
				System.out.println(contacts.get(results.get(i)));
			}
		} else {
			System.out.println("Contact not found.");
			System.out.println("Do you want to add a new contact?");
			System.out.println("Enter \"yes\" to create");
			System.out.println("or any other key to continue.");
			if (keyboard.nextLine().equalsIgnoreCase("yes")) {
				setNew();
			}
		}	
	}
	
	// searches for one contact
	// if there are multiple results, the user chooses one
	private int searchOne(String goal) {
		ArrayList<Integer> results = promptSearch(goal);
		if (results.size() == 1) { // if there is one matching contact
			return results.get(0); // return that contact
		} else if (results.size() > 1) { // if there are multiple 
			int choice = -1;			 // matching contacts...
			System.out.println("There are multiple results.");
			for (int i = 0; i < results.size(); i++) { // display all
				System.out.println((i+1) + ". " + 
					contacts.get(results.get(i)));
			}
			// user chooses which result to update
			while (choice < 0 || choice > results.size()) {
				System.out.println
					("Which contact do you want to " + goal + "?");
				System.out.println
					("(Enter a number associated with a contact.)");
				// if input is not int, will reiterate
				try {choice = keyboard.nextInt()-1;}
				catch (Exception e) {
					System.out.println("Invalid input. Try again.");
					keyboard.nextLine();
				}
				keyboard.nextLine();
			}
			return results.get(choice);
		} else { // if contact isn't found
			System.out.println("Contact not found.");
			System.out.println("Try to modify your search.");
			return -1;
		}
	}
	
	// updates contact information
	public void update() {
		int updateItem = searchOne("update");
		// choose which aspect to update
		if (updateItem >= 0) {
			int updateSubItem = 0;
			System.out.println(contacts.get(updateItem) + "\n");
			while (updateSubItem < 1 || updateSubItem > 5) {
				System.out.println("What would you like to change?");
				System.out.println("1 = First Name \n2 = Last Name");
				System.out.println("3 = Email Address \n4 = Phone Number");
				System.out.println("5 = Return to Main Menu");
				try {
					updateSubItem = keyboard.nextInt();
					keyboard.nextLine(); // moves Scanner past prev. input
				}
				catch (Exception e) {
					System.out.println("Invalid input. Try again.");
					keyboard.nextLine();
				}
				// if input is not an int within range, will reiterate
				if (updateSubItem < 1 || updateSubItem > 5) {
					System.out.println("Number out of range. Try again.");
				}
			}
			System.out.println();
			switch (updateSubItem) {
				case 1:
					contacts.get(updateItem).fname = 
						setVar("first name", "[a-zA-Z '-]+", "alphabetic letters");
					break;
				case 2:
					contacts.get(updateItem).lname = 
						setVar("last name", "[a-zA-Z '-]+", "alphabetic letters");
					break;
				case 3:
					contacts.get(updateItem).email = setVar("email address",		
						"^(([\\w.!#$%&'*+-/=?^_`{}|]+)@([\\w-.]+)\\.([A-Za-z]{2,5}))$+",
						"standard email format");
					break;
				case 4:
					contacts.get(updateItem).phone = 
						setVar("U.S. phone number", "^\\d{10}$", "10 numeric digits");
					break;
				case 5:
					return;
				default: // should not happen
					System.out.println("Invalid Input.");
			}
			System.out.println("Contact now appears as:");
			System.out.println(contacts.get(updateItem));
		}
	}
	
	public void delete() {
		int removeItem = searchOne("delete");
		if (removeItem >= 0) {
			// confirm user wants to delete
			System.out.println("Are you sure you want to delete:");
			System.out.println(contacts.get(removeItem));
			System.out.println("Enter \"yes\" to delete");
			System.out.println("or any other key to continue.");
			if (keyboard.nextLine().equalsIgnoreCase("yes")) {
				contacts.remove(removeItem);
				System.out.println("Contact Deleted");
			}
		}
	}
	
	// opens file filename as a Scanner object
	private static Scanner newScanner(String fileName) {
		Scanner inputStream = null;
		try {
			inputStream = new Scanner(new File(fileName));
		}
		catch (FileNotFoundException e) {
			System.out.println("Cannot open file " + fileName);
			System.exit(0);
		}
		return inputStream;
	}
	
	// reads all contacts from file
	public void read() {
		System.out.println("Enter file name: ");
		String fileName = "";
		fileName = keyboard.nextLine();
		Scanner inputStream = newScanner(fileName);
		while (inputStream.hasNextLine()) {
			inputStream.nextLine(); // moves past empty \n
			String fname = inputStream.nextLine().substring(12);
			String lname = inputStream.nextLine().substring(11);
			String email = inputStream.nextLine().substring(15);
			String phone = inputStream.nextLine().substring(14);
			addNew(fname, lname, email, phone);
		}
		inputStream.close();
		System.out.println("File imported into contacts.");
	}
	
	// writes all contacts to file
	private void write() {
		System.out.println("Enter the file name to save your contacts.");
		System.out.println("Include the file extension.");
		System.out.println("Note: if this file already exists,");
		System.out.println("its contents will be overwritten.");
		String fileName = "";
		fileName = keyboard.nextLine();
		PrintWriter outputStream = null;
		try {
			outputStream = new PrintWriter(fileName);
		}
		catch (FileNotFoundException e) {
			System.out.println("Cannot open file " + fileName);
			System.exit(0);
		}
		for (int i = 0; i < contacts.size(); i++) {
			outputStream.println(contacts.get(i));
		}
		outputStream.close();
		System.out.println("Contacts written to file.");
	}
	
	// writes file, ends program
	public void close() {
		System.out.println("Exiting Program");
		System.out.println("Would you like to write contacts to a file?");
		if (keyboard.nextLine().equalsIgnoreCase("yes"))
			write();
		System.exit(0);
	}
	
	private class Person {
		
		String fname, lname, email, phone;
		
		// constructor
		Person (String firstName, String lastName, 
			String emailAddress, String phoneNumber) {
			fname = firstName;
			lname = lastName;
			email = emailAddress;
			phone = phoneNumber;
		}
		
		// to be used in println statement
		public String toString() {
			return ("\nFirst Name: " + fname +
				"\nLast Name: " + lname +
				"\nEmail Address: " + email +
				"\nPhone Number: " + phone);
		}
	}
}