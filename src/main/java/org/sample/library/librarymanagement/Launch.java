package org.sample.library.librarymanagement;

import java.util.Scanner;


public class Launch {

	public static void main(String[] args) {
		Registry instance = Registry.getInstance();
		greetings();
		populateInventory(instance);
		operate(instance);
		
	}

	private static void operate(Registry instance) {

		int option = 0;
		while(option !=9) {
			System.out.println("\n\n~~~~ LIBRARY OPERATIONS ~~~~");
			System.out.println("1 : Register New User");
			System.out.println("2 : Display User Info");
			System.out.println("3 : Display Items Rented By User");
			System.out.println("4 : Display Overdue Items and Overdue Amout of User");
			System.out.println("5 : Display Item Inventory Count");
			System.out.println("6 : Issue Item to Customer");
			System.out.println("7 : Return Customer Item");
			System.out.println("8 : Decresae the Issued week of an Item for an User");
			System.out.println("9 : Exit System");
			System.out.println("Please Enter the operation number ->\n\n");
			
			
			
			Scanner scanner = new Scanner(System.in);  // Create a Scanner object

		    try {
				option = Integer.parseInt(scanner.nextLine()); // Read user input
				if(option <1 || option >9) {
					System.err.println("Invalid Option, Plese re-enter valid operation number..\n");
					continue;
				}
				System.out.println("Option Entered is: " + option);  // Output user input
				
				processOption(instance,option,scanner);
			} catch (NumberFormatException e) {
				System.err.println("Invalid Option, Plese re-enter valid operation number..\n");
			}
		}
		
	}

	private static void processOption(Registry instance, int option, Scanner scanner) {

		String userId="";
		Integer itemId = 0;
		switch(option) {
		  case 1:
			  //Register New User
			  System.out.println("Enter First Name:");
			  String firstName = scanner.nextLine();
			  System.out.println("Enter Last Name:");
			  String lastName = scanner.nextLine();
			  userId = instance.registerUser(firstName, lastName);
			  System.out.println("User created. Generated Used ID:" +userId);
		    break;
		  case 2:
			  //Display User Info
			  System.out.println("Enter User ID :");
			  userId = scanner.nextLine();
			  instance.displayCustomerDetails(userId);
		    break;
		  case 3:
			  //Display Items Rented By User
			  System.out.println("Enter User ID :");
			  userId = scanner.nextLine();
			  instance.dislayCustomerRentedItems(userId);
			break;
		  case 4:
			  //Display Overdue Items and Overdue Amout of User
			  System.out.println("Enter User ID :");
			  userId = scanner.nextLine();
			  instance.dislayCustomerOverDueItemsWithPenalty(userId);
			break;
	      case 5:
				    // Display Item Inventory Count
	    	  try {
	    		  System.out.println("Enter Inventory Item ID number:");
				  itemId=Integer.parseInt(scanner.nextLine());
				  instance.displayItemInventoryStatus(itemId);
			} catch (NumberFormatException e) {
				System.err.println("Entered Item ID not a number \n");
			}
		    break;
		  case 6:
			  try {
				//Issue Item to Customer
				  System.out.println("Enter User ID :");
				  userId = scanner.nextLine();
				  System.out.println("Enter Inventory Item ID number:");
				  itemId=Integer.parseInt(scanner.nextLine());
				  instance.addNewItemToCustomerWithItemID(userId, itemId);
				  System.out.println("Item issued to User");
			} catch (NumberFormatException e) {
				System.err.println("Entered Item ID not a number \n");
			}
		    break;
		  case 7:
			  try {
			    // Return Customer Item
				  System.out.println("Enter User ID :");
				  userId = scanner.nextLine();
				  System.out.println("Enter Inventory Item ID number:");
				  itemId=Integer.parseInt(scanner.nextLine());
				  instance.returnItemFromCustomerWithItemID(userId, itemId);
				  System.out.println("Item Returned");
			  } catch (NumberFormatException e) {
					System.err.println("Entered Item ID not a number \n");
				}	  
	        break;
		  case 8:
			  try {
				  //Decresae the Issued week of an Item for an User
				  System.out.println("Enter User ID :");
				  userId = scanner.nextLine();
				  System.out.println("Enter Inventory Item ID number:");
				  itemId=Integer.parseInt(scanner.nextLine());
				  instance.decreaseCustomerRentedWeekForItem(userId, itemId);
			  } catch (NumberFormatException e) {
					System.err.println("Entered Item ID not a number \n");
				}	
			    return ;
		  case 9:
			  break;
		  default:
		    // code block
		}
		
	}

	static void greetings() {
		System.out.println("############# WELCOME TO LIBRARY MANAGEMENT SYSTEM #############");
		
	}
	
	
	
	static void populateInventory(Registry instance) {
		
		System.out.println("\n\n~~~~ PRE POPULATING THE LIBRARY INVENTORY ~~~~");
		instance.addItemToInventory("Harry Potter and Chamber of Secrets", Type.BOOK, 2);
		instance.addItemToInventory("Harry Potter and the Chamber of Secrets", Type.BOOK, 2);
		instance.addItemToInventory("Harry Potter and the Prisoner of Azkaban", Type.BOOK, 2);
		instance.addItemToInventory("Harry Potter and the Goblet of Fire ", Type.BOOK, 2);
		instance.addItemToInventory("Harry Potter and the Order of the Phoenix", Type.BOOK, 2);
		instance.addItemToInventory("Harry Potter and the Half-Blood Prince", Type.BOOK, 2);
		instance.addItemToInventory("Harry Potter and the Deathly Hallows", Type.BOOK, 2);
		
		instance.addItemToInventory("Game of Thrones: A Game of Thrones", Type.BOOK, 2);
		instance.addItemToInventory("Game of Thrones: A Clash of Kings", Type.BOOK, 2);
		instance.addItemToInventory("Game of Thrones: A Storm of Swords", Type.BOOK, 2);
		instance.addItemToInventory("Game of Thrones: A Feast for Crows", Type.BOOK, 2);
		instance.addItemToInventory("Game of Thrones: A Dance With Dragons", Type.BOOK, 2);
		instance.addItemToInventory("Game of Thrones: The Winds of Winter", Type.BOOK, 2);
		instance.addItemToInventory("Game of Thrones: A Dream of Spring", Type.BOOK, 2);
		
		
    	instance.addItemToInventory("Planet Earth (2006)", Type.DVD, 3);
    	instance.addItemToInventory("The Private Life of Plants (1995)", Type.DVD, 3);
    	instance.addItemToInventory("The Story of Maths (2008)", Type.DVD, 3);
    	instance.addItemToInventory("The Social Dilemma", Type.DVD, 3);
    	
    	instance.displayInventory();
    	System.out.println("~~~~ PRE POPULATING INVENTORY COMPLETED ~~~~\n\n");
	}
}
