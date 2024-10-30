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
    //todo copyIds로 필드 변경
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

    //todo 도서 대출 메서드
    public void borrowBook(int copyId) {
        borrowedCopyIds.add(copyId);
    }

    //todo 도서 반납 메서드
    public void returnBook(int copyId) {
        borrowedCopyIds.remove(Integer.valueOf(copyId));
    }

    //todo 특정 도서 복사본을 이미 대출했는지 확인
    public boolean hasBorrowedBook(int copyId) {
        return borrowedCopyIds.contains(copyId);
    }

    // 도서 대출 기록 추가
    public void addBorrowRecord(BorrowRecord newBorrowRecord) {
        this.borrowRecords.add(newBorrowRecord);
    }

    // 도서 반납 기록 추가
    public void addReturnRecord(ReturnRecord newReturnRecord) {
        this.returnRecords.add(newReturnRecord);
    }


//    // 대출 중인 도서 복사본 ID 반환
//    public int getBorrowedCopyId(int bookId) {
//        for (int copyId : borrowedCopyIds) {
//            if (copyId == bookId) {
//                return copyId;
//            }
//        }
//        return -1; // 대출 중인 복사본이 없을 경우
//    }

    //todo 반납안한 계정인지 검사하는 로직
    public boolean hasOverdueBooks() {
        LocalDate currentDate = LastAccessRecord.getInstance().getLastAccessDate();
        for (BorrowRecord record : borrowRecords) {
            if (record.isOverdue(currentDate) && !returnRecords.contains(record)) {
                return true;
            }
        }
        return false;
    }


}
