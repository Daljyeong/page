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
    private final LocalDate scheduledReturnDate;

    public BorrowRecord(String userId, int bookId, int copyId, LocalDate borrowDate, LocalDate ScheduledReturnDate) {
        this.userId = userId;
        this.bookId = bookId;
        this.copyId = copyId;
        this.borrowDate = borrowDate;
        this.returnDate = null; // 대출 기록 생성 시에는 반납한 날짜 null 로 설정
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

    public boolean isOverdue(LocalDate currentDate) {
        return currentDate.isAfter(this.scheduledReturnDate);  // 반납 기한(dueDate)을 넘어섰는지 확인
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
