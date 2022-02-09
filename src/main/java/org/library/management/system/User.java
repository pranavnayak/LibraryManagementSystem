package org.library.management.system;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class User {

	private String userId;
	private String firstName;
	private String lastName;
	private Map<Item,Integer> itemsToRemainingWeeksToDue;
	
	public String getUserId() {
		return userId;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	
	public Map<Item, Integer> getItemsToRentedWeeks() {
		return itemsToRemainingWeeksToDue;
	}
	public User(String userId,String firstName,String lastName) {
		this.firstName=firstName;
		this.lastName=lastName;
		this.userId = userId;
		itemsToRemainingWeeksToDue = new HashMap<>();
	}
	
	@Override
	public String toString() {
		StringBuffer toString = new StringBuffer();
		toString.append("USER INFO :: First Name:")
			.append(this.firstName)
			.append(" ,Last Name:")
			.append(this.lastName);
		return toString.toString();
	}
	
	public boolean addItemWithWeek(Item item,Integer rentedWeek) {
		boolean isItemAdded = false;
		// check for overdue items for current user and do not add if items are overdue !!
		if(hasOverDueItems()) {
			System.out.println("User Has Overdue Items. Cannot add another");
			displayOverDueItems();
		}
		else if (itemsToRemainingWeeksToDue.containsKey(item)) {
			System.out.println("Item Already with customer. Cannot add another");
		}else {
			itemsToRemainingWeeksToDue.put(item, rentedWeek);
			isItemAdded =  true;
		}
		return isItemAdded; 
	}
	
	public boolean returnItem(Item item) {
		boolean isRetuned = true;
		if (itemsToRemainingWeeksToDue.containsKey(item)) {
			Integer weeksRented = itemsToRemainingWeeksToDue.get(item);
			if(weeksRented < 0) {
				int overDueDuration = Math.abs(weeksRented);
				System.out.println("Item was overdue by :"+overDueDuration);
				System.out.println("Penalty Changes: "+overDueDuration *Registry.LATE_FEE);
				System.out.println("Penalty Paid ...");
			}
			System.out.println("Item Returned : "+ item.toString());
			itemsToRemainingWeeksToDue.remove(item);
			
		}else {
			System.out.println("Item : "+ item.toString() +"NOT with the user: "+ this.toString());
			isRetuned = false;
		}
		
		return isRetuned;
	}
	public void decreaseRentedWeekForItem(Item item) {
		if (itemsToRemainingWeeksToDue.containsKey(item)) {
			Integer weeksRented = itemsToRemainingWeeksToDue.get(item);
			itemsToRemainingWeeksToDue.put(item, --weeksRented);
		}else {
			System.out.println("Couldnot decrease the rent week of Customer for the given Item.");
		}
	}
	
	public boolean hasOverDueItems() {
		boolean hasOverDueItems= false;
		AtomicInteger totalWeeksDue = new AtomicInteger(0);
		itemsToRemainingWeeksToDue.forEach((item,weeksRented)->{
			if(weeksRented < 0) {
				System.out.println(item);
				totalWeeksDue.set(totalWeeksDue.get()+Math.abs(weeksRented));
			}
		});
		if(totalWeeksDue.get()>0) {
			hasOverDueItems= true;
		}
		
		return hasOverDueItems;
	}
	
	
	public void displayOverDueItems() {
		System.out.println("Displaying Overdue Items:");
		AtomicInteger totalWeeksDue = new AtomicInteger(0);
		itemsToRemainingWeeksToDue.forEach((item,weeksRented)->{
			if(weeksRented < 0) {
				System.out.println(item);
				totalWeeksDue.set(totalWeeksDue.get()+Math.abs(weeksRented));
			}
		});
		System.out.println("Amout Due :" +(totalWeeksDue.get()*Registry.LATE_FEE));
	}
	
	public void displayRentedItems() {
		if(itemsToRemainingWeeksToDue != null || itemsToRemainingWeeksToDue.size()>0) {
			itemsToRemainingWeeksToDue.forEach((item,weeksRented)->{
				System.out.println(item);
				System.out.println("Rented Duration in Weeks:"+weeksRented);
			});
		}else {
			System.out.println("No Items found with user");
		}
	}

}
