package org.library.management.system;

public class DVD implements IItem{

	private int itemId;
	private String title;
	
	public DVD(int itemId,final String title) {
		this.title=title;
		this.itemId=itemId;
	}
	@Override
	public int getItemId() {
		return itemId;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	@Override
	public String toString() {
		StringBuffer toString = new StringBuffer();
		toString.append("Item :: Type:")
			.append(this.getClass())
			.append(" ,Item ID:")
			.append(this.itemId)
			.append(" ,Title:")
			.append(this.title);
		return toString().toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean returnValue=false;
		if(this == obj) {
			returnValue = true;
		}
		if(obj instanceof DVD) {
			DVD dvd = (DVD)obj;
			if(this.itemId == dvd.getItemId() && this.title.equals(dvd.getTitle())) {
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
