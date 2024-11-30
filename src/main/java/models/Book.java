package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Book 클래스
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String title;
    private List<String> authors;
    private List<BookCopy> copies;

    public Book(int id, String title, List<String> authors, int quantity, int num) {
        this.id = id;
        this.title = title + " (" + num + ")";
        this.authors = authors.isEmpty() ? List.of("no author") : new ArrayList<>(authors);
        this.copies = new ArrayList<>();
        addCopies(quantity);  // 초기 복사본 생성
    }

    // Getter 메서드
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return new ArrayList<>(authors);
    }

    public List<BookCopy> getCopies() {
        return copies;
    }

    // 새 복사본 추가 메서드
    public void addCopies(int quantity) {
        for (int i = 0; i < quantity; i++) {
            copies.add(new BookCopy(this.id));
        }
    }

    // 이용 가능한 BookCopy 반환
    public BookCopy borrowAvailableCopy() {
        for (BookCopy copy : copies) {
            if (!copy.isBorrowed()) {
                copy.borrow();
                return copy;
            }
        }
        return null;  // 대출 가능한 복사본이 없을 경우
    }

    public void returnCopy(int copyId) {
        for (BookCopy copy : copies) {
            if (copy.getCopyId() == copyId) {
                copy.returned();
                break;
            }
        }
    }

    // 대출 중인 복사본이 있는지 확인
    public boolean hasBorrowedCopies() {
        for (BookCopy copy : copies) {
            if (copy.isBorrowed()) {
                return true;
            }
        }
        return false;
    }

    // 이용 가능한 복사본 수 반환
    public int getAvailableCopies() {
        int availableCount = 0;
        for (BookCopy copy : copies) {
            if (!copy.isBorrowed()) {
                availableCount++;
            }
        }
        return availableCount;
    }
}
