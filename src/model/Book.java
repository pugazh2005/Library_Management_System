package model;

public class Book {
	 	private int bookId;
	    private String title;
	    private String author;
	    private int totalQuantity;
	    private int availableQuantity;
	    private String category;
	    public Book(int bookId, String title, String author, int totalQuantity, int availableQuantity,String category) {
	        this.bookId = bookId;
	        this.title = title;
	        this.author = author;
	        this.totalQuantity = totalQuantity;
	        this.availableQuantity = availableQuantity;
	        this.category=category;
	    }

	    public int getBookId() { return bookId; }
	    public String getTitle() { return title; }
	    public String getAuthor() { return author; }
	    public int getTotalQuantity() { return totalQuantity; }
	    public int getAvailableQuantity() { return availableQuantity; }
	    public String getCategory() { return category; }
}
