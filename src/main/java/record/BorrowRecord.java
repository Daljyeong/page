package record;

import java.io.*;
import java.time.LocalDate;

public class BorrowRecord implements Serializable {
    private static final long serialVersionUID = 1L; // 직렬화 버전
    private String userId;
    private int bookId;
    private int copyId;
    private LocalDate borrowDate;
    private LocalDate returnDate;

    public BorrowRecord(String userId, int bookId, int copyId, LocalDate borrowDate) {
        this.userId = userId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.returnDate = null; // 초기화 시 반납 날짜는 null
    }


    public String getUserId() {
        return userId;
    }

    public int getBookId() {
        return bookId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }


    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    //todo 반납기한 넘었는지 검사하는 책임
    public boolean isOverdue(LocalDate currentDate) {
        return currentDate.isAfter(this.returnDate);  // 반납 기한(dueDate)을 넘어섰는지 확인
    }


    @Override
    public String toString() {
        return "BorrowRecord{" +
                ", userId='" + userId + '\'' +
                ", bookId='" + bookId + '\'' +
                ", borrowDate=" + borrowDate +
                ", returnDate=" + returnDate +
                '}';
    }
}
