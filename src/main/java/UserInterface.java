import manager.BookManager;
import models.*;
import record.*;

import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private User user;
    private Scanner scanner;
    private BookManager bookManager = BookManager.getInstance();

    public UserInterface(User user) {
        this.user = user;
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        while (true) {
            System.out.println("--------------------------------------------------------------------------");
            System.out.println(" 사용자 메뉴");
            System.out.println("--------------------------------------------------------------------------");
            System.out.println("1. 도서 검색");
            System.out.println("2. 도서 대출");
            System.out.println("3. 도서 반납");
            System.out.println("4. 대출 현황 확인");
            System.out.println("5. 로그아웃");
            System.out.println("--------------------------------------------------------------------------");
            System.out.print("원하는 작업의 번호를 입력하세요: ");
            int choice = getUserChoice(1, 5);
            switch (choice) {
                case 1:
                    handleSearchBook();
                    break;
                case 2:
                    handleBorrowBook();
                    break;
                case 3:
                    handleReturnBook();
                    break;
                case 4:
                    handleViewBorrowedBooks();
                    break;
                case 5:
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

    // 도서 검색 처리 (사용자와 관리자 공용)
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

    // 도서 대출 처리
    private void handleBorrowBook() {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println(" 도서 대출 화면");
        System.out.println("--------------------------------------------------------------------------");
        while (true) {
            System.out.print("대출할 도서의 ID를 입력하세요: ");
            String inputId = scanner.nextLine();
            if (!isValidBookId(inputId)) {
                System.out.println("잘못된 입력입니다. (정수 형태로 입력해주세요.)");
                if (!retryPrompt()) {
                    System.out.println("사용자 메뉴 화면으로 이동합니다.");
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
                System.out.println("해당 도서는 이미 대출 중입니다.");
                return;
            }

            book.borrow();
            user.borrowBook(bookId);
            BorrowRecord newBorrowRecord = new BorrowRecord(user.getId(), bookId, LastAccessRecord.getInstance().getLastAccessDate());
            user.addBorrowRecord(newBorrowRecord);
            bookManager.saveData();
            System.out.println("도서 대출이 성공적으로 완료되었습니다. 사용자 메뉴 화면으로 이동합니다.");
            return;
        }
    }

    // 도서 반납 처리
    private void handleReturnBook() {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println(" 도서 반납 화면");
        System.out.println("--------------------------------------------------------------------------");
        while (true) {
            System.out.print("반납할 도서의 ID를 입력하세요: ");
            String inputId = scanner.nextLine();
            if (!isValidBookId(inputId)) {
                System.out.println("잘못된 입력입니다. (정수 형태로 입력해주세요.)");
                if (!retryPrompt()) {
                    System.out.println("사용자 메뉴 화면으로 이동합니다.");
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

            if (!user.hasBorrowedBook(bookId)) {
                System.out.println("해당 도서는 귀하가 대출한 도서가 아닙니다.");
                return;
            }

            book.returned();
            user.returnBook(bookId);
            bookManager.saveData();
            ReturnRecord newReturnRecord = new ReturnRecord(user.getId(), bookId, LastAccessRecord.getInstance().getLastAccessDate());
            user.addReturnRecord(newReturnRecord);
            System.out.println("도서 반납이 성공적으로 완료되었습니다. 사용자 메뉴 화면으로 이동합니다.");
            return;
        }
    }

    // 대출 현황 확인 처리
    private void handleViewBorrowedBooks() {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println(" 대출 현황 확인 화면");
        System.out.println("--------------------------------------------------------------------------");
        while (true) {
            System.out.print("대출 현황을 확인할 도서의 ID를 입력하세요: ");
            String inputId = scanner.nextLine();
            if (!isValidBookId(inputId)) {
                System.out.println("잘못된 입력입니다. (정수 형태로 입력해주세요.)");
                if (!retryPrompt()) {
                    System.out.println("사용자 메뉴 화면으로 이동합니다.");
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
                System.out.println("검색하신 도서는 이미 대출 중입니다. 사용자 메뉴 화면으로 이동합니다.");
            } else {
                System.out.println("검색하신 도서는 대출이 가능합니다. 사용자 메뉴 화면으로 이동합니다.");
            }
            return;
        }
    }

    // 유효한 도서 ID인지 검증
    private boolean isValidBookId(String id) {
        return id.matches("^\\d+$");
    }

    // 재입력 여부 확인
    private boolean retryPrompt() {
        System.out.print("다시 입력하시겠습니까? (y / 다른 키를 입력하면 사용자 메뉴 화면으로 이동합니다.): ");
        String retry = scanner.nextLine();
        return "y".equalsIgnoreCase(retry);
    }

    // 이름 유효성 검증 (static)
    public static boolean isValidName(String name) {
        // 알파벳 대소문자와 공백만, 연속된 공백 및 앞뒤 공백 없음
        return name.matches("^[A-Za-z]+(?: [A-Za-z]+)*$");
    }

    // 아이디 유효성 검증 (static)
    public static boolean isValidId(String id) {
        return id.matches("^[A-Za-z0-9]{3,20}$");
    }

    // 비밀번호 유효성 검증 (static)
    public static boolean isValidPassword(String password) {
        if (password.length() < 8 || password.length() > 20) return false;
        if (password.contains(" ")) return false;
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) hasUpper = true;
            else if (Character.isLowerCase(ch)) hasLower = true;
            else if (Character.isDigit(ch)) hasDigit = true;
            else if ("!@#$%^&*()".indexOf(ch) >= 0) hasSpecial = true;
        }
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
}
