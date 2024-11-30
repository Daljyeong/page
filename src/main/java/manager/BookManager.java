package manager;

import models.Book;
import models.BookCopy;

import java.util.List;

public interface BookManager {
    // 도서 추가 메서드
    public Book addBook(String title, String author, int quantity);

    // 도서 삭제 메서드
    public void removeBookCopy(int copyId);

    // 도서 ID로 검색
    public Book getBookById(int id);

    // 제목 또는 저자로 도서 검색
    public List<Book> searchBooks(String keyword);
    // 모든 도서 로드

    public BookCopy getBookCopyById(int bookCopyId);
    
    public void loadData();

    // 모든 도서 저장
    public void saveData();

    // 반납 기한 설정
    void setBorrowPeriod(int borrowPeriod);

    int getBorrowPeriod();


}
