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
    private LocalDate scheduledReturnDate;

    public BorrowRecord(String userId, int bookId, int copyId, LocalDate borrowDate, LocalDate ScheduledReturnDate) {
        this.userId = userId;
        this.bookId = bookId;
        this.copyId = copyId;
        this.borrowDate = borrowDate;
        this.returnDate = null; // 초기화 시 반납 날짜는 null
        this.scheduledReturnDate = ScheduledReturnDate;
    }


    public String getUserId() {
        return userId;
    }


    public int getCopyId() {
        return copyId;
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

    public LocalDate getScheduledReturnDate() {
        return scheduledReturnDate;
    }

    //todo 반납기한 넘었는지 검사하는 책임
    public boolean isOverdue(LocalDate currentDate) {
        return currentDate.isAfter(this.scheduledReturnDate);  // 반납 기한(dueDate)을 넘어섰는지 확인
        // ScheduledReturnDate 로 바껴야하는 거 아닌가?
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
