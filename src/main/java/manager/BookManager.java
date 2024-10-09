package manager;

import models.Book;
import java.util.List;

public interface BookManager {
    // 도서 추가 메서드
    public Book addBook(String title, String author);

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
}
