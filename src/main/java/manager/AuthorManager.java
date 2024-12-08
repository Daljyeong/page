package manager;

import models.Author;

import java.util.ArrayList;
import java.util.List;

public class AuthorManager {
    private List<Author> authors = new ArrayList<>();
    private int nextAuthorId = 1;

    // 저자 조회 또는 생성
    public Author getOrCreateAuthor(String name) {
        // 이름이 같아도 항상 새로운 고유번호를 생성
        Author newAuthor = new Author(name, nextAuthorId++);
        authors.add(newAuthor); // 새로운 저자를 리스트에 추가
        return newAuthor;
    }

    // 모든 저자를 출력 (디버깅용)
    public List<Author> getAllAuthors() {
        return authors;
    }
}
