package models;

import record.BorrowRecord;
import record.ReturnRecord;

import java.util.ArrayList;
import java.util.List;

// models.User 클래스
public class User extends Account {
    private static final long serialVersionUID = 1L;

    private String name;
    private List<Integer> borrowedBookIds;
    private List<BorrowRecord> borrowRecords;
    private List<ReturnRecord> returnRecords;

    public User(String name, String id, String password) {
        super(id, password);
        this.name = name;
        this.borrowedBookIds = new ArrayList<>();
        this.borrowRecords = new ArrayList<>();
        this.returnRecords = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Integer> getBorrowedBookIds() {
        return borrowedBookIds;
    }

    public void borrowBook(int bookId) {
        borrowedBookIds.add(bookId);
    }

    public void returnBook(int bookId) {
        borrowedBookIds.remove(Integer.valueOf(bookId));
    }


    public boolean hasBorrowedBook(int bookId) {
        return borrowedBookIds.contains(bookId);
    }

    public void addBorrowRecord(BorrowRecord newBorrowRecord){
        borrowRecords.add(newBorrowRecord);

    }
    public void addReturnRecord(ReturnRecord newReturnRecord){
        returnRecords.add(newReturnRecord);
    }

}
