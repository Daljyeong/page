package models;

import java.io.Serializable;
import java.time.LocalDate;

// BookCopy 클래스
public class BookCopy implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int nextCopyId = 1;  // 고유 ID 생성용
    private int copyId;
    private boolean isBorrowed;
    private LocalDate returnDate;
    private int bookId;
    private LocalDate addedDate;

    public BookCopy(int bookId) {
        this.copyId = nextCopyId++;
        this.isBorrowed = false;
        this.bookId = bookId;
        this.addedDate = LocalDate.now();
    }

    public int getCopyId() {
        return copyId;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void borrow() {
        this.isBorrowed = true;
    }

    public void returned() {
        this.isBorrowed = false;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    public LocalDate getReturnDate() {
        return returnDate;
    }

    public int getBookId() {
        return bookId;
    }

    public LocalDate getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(LocalDate addedDate) {
        this.addedDate = addedDate;
    }
}
