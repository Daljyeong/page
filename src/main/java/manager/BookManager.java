package manager;

import models.Book;

import java.time.LocalDate;
import java.util.List;

public interface BookManager {
    // 도서 추가 메서드
    public Book addBook(String title, String author, int quantity);

    // 도서 삭제 메서드
    public void removeBook(int id);

    // 도서 ID로 검색
    public Book getBookById(int id);

    // 제목 또는 저자로 도서 검색
    public List<Book> searchBooks(String keyword);
    // 모든 도서 로드


    public void loadData();

    // 모든 도서 저장
    public void saveData();

    //todo 당연히 인터페이스에도 동작 명시해야겠지? -> 구현체에 기능이 추가됐으니까
    void setBookReturnDate(LocalDate returnDate);
}
