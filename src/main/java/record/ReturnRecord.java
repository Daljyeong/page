package record;

import java.io.*;
import java.time.LocalDate;

public class ReturnRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private int bookId;
    private LocalDate returnDate;

    public ReturnRecord(String userId, int bookId, LocalDate returnDate) {
        this.userId = userId;
        this.bookId = bookId;
        this.returnDate = returnDate;
    }


    public String getUserId() {
        return userId;
    }

    public int getBookId() {
        return bookId;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    @Override
    public String toString() {
        return "ReturnRecord{" +
                ", userId='" + userId + '\'' +
                ", bookId='" + bookId + '\'' +
                ", returnDate=" + returnDate +
                '}';
    }
}
