package model;
import java.sql.Date;

public class IssuedBook {
	private int issueId;
    private int userId;
    private int bookId;
    private Date issueDate;
    private Date dueDate;
    private Date returnDate;
    private double fineAmount;
    public IssuedBook(int issueId, int userId, int bookId, Date issueDate, Date dueDate, Date returnDate, double fineAmount) {
        this.issueId = issueId;
        this.userId = userId;
        this.bookId = bookId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.fineAmount = fineAmount;
    }
    public int getIssueId() { return issueId; }
    public int getUserId() { return userId; }
    public int getBookId() { return bookId; }
    public Date getIssueDate() { return issueDate; }
    public Date getDueDate() { return dueDate; }
    public Date getReturnDate() { return returnDate; }
    public double getFineAmount() { return fineAmount; }
}
