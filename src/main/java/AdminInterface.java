import manager.BookManager;
import manager.MemoryBookManager;
import models.*;
import java.util.List;
import java.util.Scanner;

public class AdminInterface {
    private Admin admin;
    private Scanner scanner;
    private BookManager bookManager = MemoryBookManager.getInstance();

    public AdminInterface(Admin admin) {
        this.admin = admin;
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        while (true) {
            System.out.println("--------------------------------------------------------------------------");
            System.out.println(" 관리자 메뉴");
            System.out.println("--------------------------------------------------------------------------");
            System.out.println("1. 도서 추가");
            System.out.println("2. 도서 삭제");
            System.out.println("3. 도서 검색");
            System.out.println("4. 로그아웃");
            System.out.println("--------------------------------------------------------------------------");
            System.out.print("원하는 작업의 번호를 입력하세요: ");
            int choice = getUserChoice(1, 4);
            switch (choice) {
                case 1:
                    handleAddBook();
                    break;
                case 2:
                    handleDeleteBook();
                    break;
                case 3:
                    handleSearchBook();
                    break;
                case 4:
                    System.out.println("로그아웃하고 초기화면으로 이동합니다.");
                    return;
                default:
                    // 이 경우는 발생하지 않음
                    break;
            }
        }
    }

    // 사용자 입력을 받아 유효한 메뉴 선택인지 확인
    private int getUserChoice(int min, int max) {
        while (true) {
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); // 버퍼 클리어
                if (choice >= min && choice <= max) {
                    return choice;
                }
            } else {
                scanner.nextLine(); // 버퍼 클리어
            }
            System.out.print("잘못된 입력입니다. " + min + "~" + max + " 사이의 번호로 다시 입력해주세요: ");
        }
    }

    // 도서 추가 처리
    private void handleAddBook() {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println(" 도서 추가 화면");
        System.out.println("--------------------------------------------------------------------------");

        String title;
        while (true) {
            System.out.print("도서 제목: ");
            title = scanner.nextLine().trim();
            if (!title.isEmpty()) {
                break;
            } else {
                System.out.println("도서 제목을 입력해주세요.");
            }
        }

        String author;
        while (true) {
            System.out.print("도서 저자: ");
            author = scanner.nextLine().trim();
            if (!author.isEmpty()) {
                break;
            } else {
                System.out.println("도서 저자를 입력해주세요.");
            }
        }

        System.out.print("도서를 추가하시겠습니까? (y / 다른 키를 입력하면 관리자 메뉴 화면으로 이동합니다.): ");
        String confirm = scanner.nextLine();
        if ("y".equalsIgnoreCase(confirm)) {
            Book newBook = bookManager.addBook(title, author);
            bookManager.saveData();
            System.out.println("도서가 성공적으로 추가되었습니다. 도서 ID는 [" + newBook.getId() + "]입니다.");
        } else {
            System.out.println("관리자 메뉴 화면으로 이동합니다.");
        }
    }

    // 도서 삭제 처리
    private void handleDeleteBook() {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println(" 도서 삭제 화면");
        System.out.println("--------------------------------------------------------------------------");
        while (true) {
            System.out.print("삭제할 도서 ID를 입력하세요: ");
            String inputId = scanner.nextLine();
            if (!isValidBookId(inputId)) {
                System.out.println("잘못된 입력입니다. (정수 형태로 입력해주세요.)");
                if (!retryPrompt()) {
                    System.out.println("관리자 메뉴 화면으로 이동합니다.");
                    return;
                }
                continue;
            }

            int bookId = Integer.parseInt(inputId);
            Book book = bookManager.getBookById(bookId);
            if (book == null) {
                System.out.println("입력하신 ID에 해당하는 도서가 존재하지 않습니다.");
                return;
            }

            if (book.isBorrowed()) {
                System.out.println("해당 도서는 대출중입니다. 삭제할 수 없습니다.");
                System.out.println("--------------------------------------------------------------------------");
                continue;
            }

            System.out.println("--------------------------------------------------------------------------");
            System.out.println("삭제할 도서: " + book.getTitle() + ", " + book.getAuthor());
            System.out.print("도서를 삭제하시겠습니까? (y / 다른 키를 입력하면 취소하고 관리자 메뉴로 이동합니다.): ");
            String confirm = scanner.nextLine();
            if ("y".equalsIgnoreCase(confirm)) {
                bookManager.removeBook(bookId);
                bookManager.saveData();
                System.out.println("도서가 성공적으로 삭제되었습니다. 관리자 메뉴 화면으로 이동합니다.");
                return;
            } else {
                System.out.println("도서 삭제를 취소하였습니다. 관리자 메뉴 화면으로 이동합니다.");
                return;
            }
        }
    }

    // 도서 검색 처리 (관리자와 사용자 공용)
    private void handleSearchBook() {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println(" 도서 검색 화면");
        System.out.println("--------------------------------------------------------------------------");
        System.out.print("검색할 키워드를 입력하세요 (제목 또는 저자): ");
        String keyword = scanner.nextLine().trim();

        if (keyword.isEmpty()) {
            System.out.println("검색어를 입력해주세요.");
            return;
        }

        List<Book> results = bookManager.searchBooks(keyword);
        if (results.isEmpty()) {
            System.out.println("해당 키워드에 일치하는 도서가 존재하지 않습니다.");
        } else {
            System.out.println("--------------------------------------------------------------------------");
            System.out.println("검색 결과:");
            for (Book book : results) {
                System.out.println(book.getId() + ": " + book.getTitle() + " by " + book.getAuthor() + (book.isBorrowed() ? " (대출 중)" : ""));
            }
            System.out.println("--------------------------------------------------------------------------");
        }
    }

    // 유효한 도서 ID인지 검증
    private boolean isValidBookId(String id) {
        return id.matches("^\\d+$");
    }

    // 재입력 여부 확인
    private boolean retryPrompt() {
        System.out.print("다시 입력하시겠습니까? (y / 다른 키를 입력하면 관리자 메뉴 화면으로 이동합니다.): ");
        String retry = scanner.nextLine();
        return "y".equalsIgnoreCase(retry);
    }
}
