package org.library.management.system;

public class Book implements IItem{

	private int itemId;
	private String title;
	private String author;
	
	public String getTitle() {
		return title;
	}
	public String getAuthor() {
		return author;
	}

	public Book(int itemId,final String title,final String author) {
		this.author=author;
		this.title=title;
		this.itemId=itemId;
	}
	@Override
	public int getItemId() {
		return itemId;
	}
	@Override
	public String toString() {
		StringBuffer toString = new StringBuffer();
		toString.append("Item :: Type:")
			.append(this.getClass())
			.append(" ,Item ID:")
			.append(this.itemId)
			.append(" ,Title:")
			.append(this.title)
			.append(" ,Author:")
			.append(this.author);
		return toString().toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean returnValue=false;
		if(this == obj) {
			returnValue = true;
		}
		if(obj instanceof Book) {
			Book dvd = (Book)obj;
			if(this.itemId == dvd.getItemId() && this.title.equals(dvd.getTitle()) && this.author.equals(dvd.getAuthor())) {
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
