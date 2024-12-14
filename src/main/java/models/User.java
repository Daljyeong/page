package models;

import record.BorrowRecord;
import record.LastAccessRecord;
import record.ReturnRecord;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// models.User 클래스
public class User extends Account implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private List<Integer> borrowedCopyIds = new ArrayList<>();  // 대출 중인 복사본 ID 목록
    private List<BorrowRecord> borrowRecords = new ArrayList<>();
    private List<ReturnRecord> returnRecords = new ArrayList<>();

    public User(String name, String id, String password) {
        super(id, password);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void borrowBook(int copyId) {
        if (!borrowedCopyIds.contains(copyId)) {
            borrowedCopyIds.add(copyId);
        }
    }

    public void returnBook(int copyId) {
        borrowedCopyIds.remove(Integer.valueOf(copyId));
    }

    public boolean hasBorrowedBook(BookCopy bookCopy) {
        return borrowedCopyIds.contains(Integer.valueOf(bookCopy.getCopyId()));
    }

    // 도서 대출 기록 추가
    public void addBorrowRecord(BorrowRecord newBorrowRecord) {
        borrowBook(newBorrowRecord.getCopyId());
        this.borrowRecords.add(newBorrowRecord);
    }

    // 도서 반납 기록 추가
    public void addReturnRecord(ReturnRecord newReturnRecord) {
        returnBook(newReturnRecord.getCopyId());
        this.returnRecords.add(newReturnRecord);
    }

    public boolean hasOverdueBooks() {
        boolean hasOverdue = false;
        LocalDate currentDate = LastAccessRecord.getInstance().getLastAccessDate();

        for (BorrowRecord record : this.borrowRecords) {
            boolean isReturned = returnRecords.stream()
                    .anyMatch(returnRecord -> returnRecord.getCopyId() == record.getCopyId());

            // 반납되지 않았고 연체된 경우만 처리
            if (!isReturned && record.isOverdue(currentDate)) {
                hasOverdue = true;
//                System.out.println("연체한 도서 사본 ID: " + record.getCopyId() + " (디버깅용)");
            }
        }
        return hasOverdue;
    }
}
