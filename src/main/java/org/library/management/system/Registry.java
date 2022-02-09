package org.library.management.system;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.library.management.system.Item.Type;

public class Registry {

    private static Registry sSoleInstance;
    public static final int LATE_FEE=50;
    
    //private Map<String,List<Item>> uuidToItems;
    private Map<String,User> uuidToUsers;
    
    /*private Map<Item,Integer> itemInventory;
    private Map<Item,Integer> itemCurrentInventory;*/
    
    private Map<Integer,Item> itemInventory;
    private Map<Integer,Integer> itemCurrentInventory;
    

    //Singleton pattern requirement. Hence private constructor.
    private Registry(){
    	//uuidToItems= new HashMap<String, List<Item>>();
    	uuidToUsers = new HashMap<String, User>();
    	
    	/*
    	 * Requirement:
    	 * Please note, multiple users can transact with the library system simultaneously. 
    	 * For example, when User A is renting a DVD of movie M, user B can try to get another copy of the same movie.
    	 * 
    	 * Solution:
    	 * Both the maps, itemInventory and itemCurrentInventory have ConcurrentHashMap , which is thread safe.
    	 * reference: https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.html
    	 * 
    	 */
    	itemInventory = new ConcurrentHashMap<Integer,Item>();
    	itemCurrentInventory = new ConcurrentHashMap<Integer,Integer>();
    	
    }  

    public static Registry getInstance(){
        if (sSoleInstance == null){ //if there is no instance available... create new one
            sSoleInstance = new Registry();
        }

        return sSoleInstance;
    }
    
    //-- Register new user
    public String registerUser(String firstName, String lastName) {
    	Set<String> keySet = uuidToUsers.keySet();
    	String randomUUID="";
    	do{
    		randomUUID = UUID.randomUUID().toString();
    	}while((keySet.contains(randomUUID)) );
    	
    	User user = new User(randomUUID, firstName, lastName);
    	
    	uuidToUsers.put(randomUUID, user);
    	//uuidToItems.put(randomUUID, new LinkedList<Item>());
    	return randomUUID;
    	
    }
    
    //-- Track customer using customer id
    public void displayCustomerDetails(String customerID) {
    	
    	User user = uuidToUsers.get(customerID);
    	if(user != null) {
    		System.out.println(user.toString());
    		
    	}else {
    		System.out.println("No Customer found with ID: "+customerID);
    	}
    }
    
    //extra method
    public void displayAllCustomerDetails() {
    	
    	uuidToUsers.forEach((key,user) ->{
    		System.out.println("Customer ID::"+ key);
    		System.out.println(user.toString());
    	});
    }
    
    //-- Display list of items customer has rented so far.
    public void dislayCustomerRentedItems(String customerID) {
    	
    	uuidToUsers.get(customerID).displayRentedItems();
    	/*Map<Item, Integer> itemsToRentedWeeks = uuidToUsers.get(customerID).getItemsToRentedWeeks();
		if(itemsToRentedWeeks != null || itemsToRentedWeeks.size()>0) {
			itemsToRentedWeeks.forEach((item,week)->{
				System.out.println(item);
				System.out.println("Rented Duration in Weeks:"+week);
			});
		}else {
			System.out.println("No Items found with user");
		}*/
    }
    
    //-- Display list of overdue items for a customer and total amount due to be paid by the customer
    public void dislayCustomerOverDueItemsWithPenalty(String customerID) {
    	uuidToUsers.get(customerID).displayOverDueItems();
    }
    
    //supplementary method to populate the inventory with iteams
    public void addItemToInventory(final String title, Type type, int inventoryCount) {
    	// item ID is auto generated, incrementing for the max counter in the system
    	AtomicBoolean isPresent = new AtomicBoolean(false);
    	itemInventory.forEach((itemId,item)->{
    		
    		//if it is the item is already present in the system, then need to update the total number of items present in the system
    		if(item.getItemType().equals(type) && item.getTitle().equals(title)) {
    			item.setInventoryCount(item.getInventoryCount()+inventoryCount);
    			isPresent.set(true);
    			
    			// get the current inventory stock, and add to the value
    			Integer currentItemInventoryCount = itemCurrentInventory.get(item.getItemId());
    			itemCurrentInventory.put(item.getItemId(), currentItemInventoryCount+inventoryCount);
    		}
    	});
    	
    	//else  create new item with id one more than the curent maxminum itemId in the system
    	if(! isPresent.get()) {
    		Integer max = itemInventory.size() >0 ? Collections.max(itemInventory.keySet()) : 0;
    		itemInventory.put(max+1, new Item(max+1, title, inventoryCount, type));
    		
    		itemCurrentInventory.put(max+1, inventoryCount);
    	}
    }
    
   // -- Option for taking a new item from the library by the customer. Check availability and grant/deny
    public void addNewItemToCustomerWithItemID(String customerID, int itemID)
    {
    	Integer count = itemCurrentInventory.get(itemID);
    	if(count>0) {
    		Item item = itemInventory.get(itemID);
    		User user = uuidToUsers.get(customerID);
    		boolean isItemAddedToUser = user.addItemWithWeek(item, 1);
    		/*List<Item> listItems = uuidToItems.get(customerID);
    		listItems.add(item);*/
    		itemCurrentInventory.put(itemID, --count);
    	}else {
    		System.out.println("Couldnot add the Item as no current Inventory available");
    	}
    	
    }
    
    public void addNewItemToCustomerWithTitleAndType(String customerID, String title, Type type)
    {
    	
    }
    
    //-- Check no. of items available in the library for a given item using item id. EX. Item ID 100 is Harry Potter and Chamber of Secrets book. Total count: 5. Available: 2
    public void displayItemInventoryStatus(int itemId) {
    	System.out.println("Total Count: "+itemInventory.get(itemId).getInventoryCount());
    	System.out.println("Available: "+itemCurrentInventory.get(itemId));
    }
    
    // extra method
    public void displayInventory() {
    	itemInventory.forEach((key,value)->{
    		System.out.println(value);
    	});
    }
    
    public void decreaseCustomerRentedWeekForItem(String customerID, int itemID) {
    	
    		Item item = itemInventory.get(itemID);
    		User user = uuidToUsers.get(customerID);
    		user.decreaseRentedWeekForItem(item);
    	
    	
    }
    
    //-- Option for returning an item back to the library.
    public void returnItemFromCustomerWithItemID(String customerID, int itemID) {
    	Item item = itemInventory.get(itemID);
    	System.out.println("Inveroty count of Item: "+item +"before returning : "+itemCurrentInventory.get(itemID));
		User user = uuidToUsers.get(customerID);
		boolean returnItem = user.returnItem(item);
		if(returnItem) {
			Integer currentItemCount = itemCurrentInventory.get(itemID);
			itemCurrentInventory.put(itemID, ++currentItemCount);
		}
		System.out.println("Inveroty count of Item: "+item +"after returning : "+itemCurrentInventory.get(itemID));
    }
    
    public static void main(String[] args) {
    	Registry instance = Registry.getInstance();
    	instance.addItemToInventory("a", Type.BOOK, 1);
    	instance.addItemToInventory("c", Type.DVD, 1);
    	instance.addItemToInventory("b", Type.BOOK, 1);
    	instance.addItemToInventory("c", Type.DVD, 1);
    	instance.addItemToInventory("b", Type.BOOK, 1);
    	instance.displayInventory();
    	
    	String registerUser = instance.registerUser("pop", "pop");
    	String registerUser2 = instance.registerUser("pip", "pip");
    	String registerUser3 = instance.registerUser("pup", "pup");
    	//instance.displayAllCustomerDetails();
    	
    	instance.displayCustomerDetails(registerUser);
    	instance.addNewItemToCustomerWithItemID(registerUser, 1);
    	instance.addNewItemToCustomerWithItemID(registerUser2, 2);
    	instance.dislayCustomerRentedItems(registerUser);
    	
    	instance.displayCustomerDetails(registerUser2);
    	instance.dislayCustomerRentedItems(registerUser2);
    	
    	instance.dislayCustomerRentedItems(registerUser);
    	instance.dislayCustomerRentedItems(registerUser2);
    	
    	
    	instance.displayCustomerDetails(registerUser3);
    	instance.dislayCustomerRentedItems(registerUser3);
    	
    	System.out.println("#####################");
    	instance.decreaseCustomerRentedWeekForItem(registerUser, 1);
    	instance.decreaseCustomerRentedWeekForItem(registerUser, 1);
    	instance.decreaseCustomerRentedWeekForItem(registerUser, 1);
    	//instance.decreaseCustomerRentedWeekForItem(registerUser, 1);
    	instance.dislayCustomerOverDueItemsWithPenalty(registerUser);
    	System.out.println("#####################");
    	instance.dislayCustomerRentedItems(registerUser);
    	
    	instance.displayItemInventoryStatus(1);
    	instance.displayItemInventoryStatus(2);
    	instance.displayItemInventoryStatus(3);
	}
}
