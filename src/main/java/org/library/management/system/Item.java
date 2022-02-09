package org.library.management.system;

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
	
	@Override
	public String toString() {
		StringBuilder returnSB = new StringBuilder();
		returnSB.append("Item :: Type:")
			.append(this.getItemType())
			.append(" ,Item ID:")
			.append(this.itemId)
			.append(" ,Title:")
			.append(this.title);
		/*.append(" ,inventoryCount:")
		.append(this.inventoryCount);*/
		return returnSB.toString();
	}
	
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
	
	public enum Type {
		  BOOK,
		  DVD
	}

}
