package record;

import java.io.*;
import java.time.LocalDate;

public class ReturnRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private int bookId;
    private int copyId;
    private LocalDate returnDate;

    public ReturnRecord(String userId, int bookId, int copyId, LocalDate returnDate) {
        this.userId = userId;
        this.bookId = bookId;
        this.copyId = copyId;
        this.returnDate = returnDate;
    }


    public String getUserId() {
        return userId;
    }

    public int getCopyId() {
        return copyId;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    @Override
    public String toString() {
        return "ReturnRecord{" +
                ", userId='" + userId + '\'' +
                ", bookId='" + bookId + '\'' +
                ", copyId='" + copyId + '\'' +
                ", returnDate=" + returnDate +
                '}';
    }
}
