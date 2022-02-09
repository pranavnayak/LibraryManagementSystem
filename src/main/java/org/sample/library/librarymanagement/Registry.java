package org.sample.library.librarymanagement;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;


public class Registry {

	//defining late fee .. this could be a configuration property
	public static final int LATE_FEE=50;
	
	// Singleton pattern!. need to have just one instance of the Library registry!!
    private static Registry sSoleInstance;
    public static Registry getInstance(){
        if (sSoleInstance == null){ //if there is no instance available... create new one
            sSoleInstance = new Registry();
        }

        return sSoleInstance;
    }
    
    //Map Data Structure to hold User ID and User object key value pair.
    // UserID forms the unique UUID key
    //this will help us efficiently deal with users in the system
    private Map<String,User> uuidToUsers;
    

    //Map of Item ID and Items Object
    // this map holds all the Library item 
    private Map<Integer,Item> itemInventory;
    
    //map of Item id vs Item Inventory count
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

    
    
    //-- Register new user
    public String registerUser(String firstName, String lastName) {
    	Set<String> keySet = uuidToUsers.keySet();
    	String randomUUID="";
    	do{
    		// generating UUID and checking if it has been already consumed. Very very rare occurance !!
    		randomUUID = UUID.randomUUID().toString();
    	}while((keySet.contains(randomUUID)) );
    	
    	User user = new User(randomUUID, firstName, lastName);
    	
    	uuidToUsers.put(randomUUID, user);
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
    	
    	User user = uuidToUsers.get(customerID);
    	if(user !=null) {
    		user.displayRentedItems();
    	}else {
    		System.out.println("No User found with that user id");
    	}
    	
    }
    
    //-- Display list of overdue items for a customer and total amount due to be paid by the customer
    public void dislayCustomerOverDueItemsWithPenalty(String customerID) {
    	uuidToUsers.get(customerID).displayOverDueItems();
    }
    
    //supplementary method to populate the inventory with items
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
    	
    	//else  create new item with id one more than the current maxmimum itemId in the system
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
    
    //-- Check no. of items available in the library for a given item using item id. EX. Item ID 100 is Harry Potter and Chamber of Secrets book. Total count: 5. Available: 2
    public void displayItemInventoryStatus(int itemId) {
    	Item item = itemInventory.get(itemId);
    	if(item !=null) {
    		System.out.println("Item Title: "+item.getTitle());
    		System.out.println("Item Type: "+item.getItemType());
    		System.out.println("Total Count: "+item.getInventoryCount());
    		System.out.println("Available: "+itemCurrentInventory.get(itemId));
    	}else {
    		System.out.println("No Item in the Inventory for the entered ID");
    	}
    }
    
    // extra method
    public void displayInventory() {
    	itemInventory.forEach((key,value)->{
    		System.out.println(value.toStringWithInventoryCount());
    	});
    }
    
   // auxilary method
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
    
}
