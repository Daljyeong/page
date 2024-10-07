package models;

import record.BorrowRecord;
import record.ReturnRecord;

import java.util.ArrayList;
import java.util.List;

// models.User 클래스
public class User extends Account {
    private static final long serialVersionUID = 1L;

    private String name;
    private List<Integer> borrowedBookIds = new ArrayList<Integer>();
    private List<BorrowRecord> borrowRecords = new ArrayList<BorrowRecord>();
    private List<ReturnRecord> returnRecords = new ArrayList<ReturnRecord>();

    public User(String name, String id, String password) {
        super(id, password);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void borrowBook(int bookId) {
        this.borrowedBookIds.add(bookId);
    }

    public void returnBook(int bookId) {
        borrowedBookIds.remove(Integer.valueOf(bookId));
    }


    public boolean hasBorrowedBook(int bookId) {
        return borrowedBookIds.contains(bookId);
    }

    public void addBorrowRecord(BorrowRecord newBorrowRecord){
        this.borrowRecords.add(newBorrowRecord);

    }
    public void addReturnRecord(ReturnRecord newReturnRecord){
        this.returnRecords.add(newReturnRecord);
    }

}
