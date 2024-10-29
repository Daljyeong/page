package models;

import java.io.Serializable;

// BookCopy 클래스
public class BookCopy implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int nextCopyId = 1;  // 고유 ID 생성용
    private int copyId;
    private boolean isBorrowed;

    public BookCopy(int bookId) {
        this.copyId = nextCopyId++;
        this.isBorrowed = false;
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
}
