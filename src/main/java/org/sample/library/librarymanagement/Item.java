package org.sample.library.librarymanagement;

/*
 * Item class denoting categories of items in the system, differentiated by the enum instance type
 * 
 */
public class Item {

	private int itemId;
	private String title;
	private Type itemType;
	private int inventoryCount;
	
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Type getItemType() {
		return itemType;
	}
	public void setItemType(Type itemType) {
		this.itemType = itemType;
	}
	public int getInventoryCount() {
		return inventoryCount;
	}
	public void setInventoryCount(int inventoryCount) {
		this.inventoryCount = inventoryCount;
	}
	public Item(int itemId,final String title, int inventoryCount,Type type) {
		this.title=title;
		this.itemId=itemId;
		this.itemType=type;
		this.inventoryCount = inventoryCount;
	}
	
	// Overrding to String to display Item content
	@Override
	public String toString() {
		//Using String Builder for efficiency
		StringBuilder returnSB = new StringBuilder();
		returnSB.append("Item :: Type:")
			.append(this.getItemType())
			.append(" ,Item ID:")
			.append(this.itemId)
			.append(" ,Title:")
			.append(this.title);
		return returnSB.toString();
	}
	
	public String toStringWithInventoryCount() {
		StringBuilder returnSB = new StringBuilder();
		returnSB
		.append("Title:")
		.append(this.title)
		.append(" -- Type:")
		.append(this.getItemType())
		.append(" -- Item ID:")
		.append(this.itemId)
		.append(" -- Inventory Count:")
		.append(this.inventoryCount);
		return returnSB.toString();
	}
	
	//Overriding hashcode and equals since this Item object is being used as a Key for Map in the Registry class
	@Override
	public boolean equals(Object obj) {
		boolean returnValue=false;
		if(this == obj) {
			returnValue = true;
		}
		if(obj instanceof Item) {
			Item item = (Item)obj;
			if(this.getItemType() == item.getItemType() && this.title.equals(item.getTitle())) {
				returnValue = true;
			}
		}
		
		return returnValue;
	}
	
	@Override
	public int hashCode() {
		return this.getTitle().hashCode();
	}
	
}
