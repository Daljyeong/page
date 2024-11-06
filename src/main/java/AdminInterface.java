import manager.BookManager;
import manager.MemoryBookManager;
import models.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
            System.out.println("2. 도서 사본 추가");
            System.out.println("3. 도서 삭제");
            System.out.println("4. 도서 검색");
            System.out.println("5. 반납 기한 설정");  // 추가된 반납 기한 설정 옵션
            System.out.println("6. 로그아웃");
            System.out.println("--------------------------------------------------------------------------");
            System.out.print("원하는 작업의 번호를 입력하세요: ");
            int choice = getUserChoice(1, 6);
            switch (choice) {
                case 1:
                    handleAddBook();
                    System.out.println("도서 추가 화면으로 이동합니다.");
                    break;
                case 2:
                    handleAddCopies();
                    System.out.println("도서 사본 추가 화면으로 이동합니다.");
                    break;
                case 3:
                    handleDeleteBook();
                    System.out.println("도서 삭제 화면으로 이동합니다.");
                    break;
                case 4:
                    handleSearchBook();
                    System.out.println("도서 검색 화면으로 이동합니다.");
                    break;
                case 5:
                    handleSetReturnDeadline();
                    System.out.println("반납 기한 설정 화면으로 이동합니다.");
                    break;
                case 6:
                    System.out.println("로그아웃하고 초기화면으로 이동합니다.");
                    return;
                default:
                    break;
            }
        }
    }

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

    //todo C 전역 반납기한 설정
    private void handleSetReturnDeadline() {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println(" 반납 기한 설정");
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("날짜는 'YYYY-MM-DD' 형식이어야 합니다. 예: 2024-12-31");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate returnDate;
        while (true) {
            System.out.print("반납 기한을 입력하세요: ");
            String inputDate = scanner.nextLine().trim();
            try {
                returnDate = LocalDate.parse(inputDate, dateFormatter); // 전역 반납 기한 설정
                bookManager.setBookReturnDate(returnDate); // 대출 전 도서에 반영
                System.out.println("모든 대출 전 도서에 대해 반납 기한이 성공적으로 설정되었습니다.");
                break;
            } catch (DateTimeParseException e) {
                System.out.println("올바른 날짜 형식을 입력해주세요. 예: 2024-12-31");
            }
        }

        //todo 반납기한 설정
        System.out.print("반납기한을 설정 하시겠습니까? (y / 다른 키를 입력하면 관리자 메뉴 화면으로 이동합니다.): ");
        String confirm = scanner.nextLine();
        if ("y".equals(confirm)) {
            bookManager.setBookReturnDate(returnDate); // 대출 전 도서에 반영
            System.out.println("모든 대출 전 도서에 대해 반납 기한이 성공적으로 설정되었습니다.");
            bookManager.saveData();
        } else {
            System.out.println("관리자 메뉴 화면으로 이동합니다.");
        }
    }




    //todo 도서 추갸할때 메서드도 받음
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

        int quantity = 0;
        while (true) {
            System.out.print("도서 수량을 입력하세요: ");
            if (scanner.hasNextInt()) {
                quantity = scanner.nextInt();
                scanner.nextLine(); // 버퍼 클리어
                if (quantity > 0) break;
            } else {
                scanner.nextLine();
            }
            System.out.println("올바른 수량을 입력해주세요.");
        }

        System.out.print("도서를 추가하시겠습니까? (y / 다른 키를 입력하면 관리자 메뉴 화면으로 이동합니다.): ");
        String confirm = scanner.nextLine();
        if ("y".equals(confirm)) {
            Book newBook = bookManager.addBook(title, author, quantity);
            bookManager.saveData();
            System.out.println("도서가 성공적으로 추가되었습니다. 도서 ID는 [" + newBook.getId() + "]입니다.");
        } else {
            System.out.println("관리자 메뉴 화면으로 이동합니다.");
        }
    }

    //todo 도서 재고 추가 메서드
    private void handleAddCopies() {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println(" 도서 사본 추가 화면");
        System.out.println("--------------------------------------------------------------------------");

        int bookId;
        while (true) {
            System.out.print("사본을 추가할 도서의 ID를 입력하세요: ");
            try {
                bookId = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("잘못된 입력입니다. (정수 형태로 입력해주세요.)");
                System.out.print("다시 입력하시겠습니까? (y / 다른 키를 입력하면 관리자 메뉴 화면으로 이동합니다.): ");
                String choice = scanner.nextLine();
                if (!choice.equals("y")) {
                    System.out.println("관리자 메뉴 화면으로 이동합니다.");
                    return;
                }
            }
        }

        Book book = bookManager.getBookById(bookId);
        if (book == null) {
            System.out.println("입력하신 ID에 해당하는 도서가 존재하지 않습니다. 관리자 메뉴 화면으로 이동합니다.");
            return;
        }

        System.out.print("추가할 사본 수량을 입력하세요: ");
        int copiesToAdd = Integer.parseInt(scanner.nextLine());

        if (copiesToAdd > 0) {
            book.addCopies(copiesToAdd);
            bookManager.saveData();
            System.out.println("사본이 성공적으로 추가되었습니다. 관리자 메뉴 화면으로 이동합니다.");
        } else {
            System.out.println("올바른 수량을 입력해주세요.");
        }
    }



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

            if (book.hasBorrowedCopies()) {
                System.out.println("해당 도서에 대출 중인 사본이 있어 삭제할 수 없습니다.");
                System.out.println("--------------------------------------------------------------------------");
                continue;
            }

            System.out.println("--------------------------------------------------------------------------");
            System.out.println("삭제할 도서: " + book.getTitle() + ", " + book.getAuthor());
            System.out.print("도서를 삭제하시겠습니까? (y / 다른 키를 입력하면 취소하고 관리자 메뉴로 이동합니다.): ");
            String confirm = scanner.nextLine();
            if ("y".equals(confirm)) {
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

    //todo 사본 수량도 알려준다
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
                int availableCopies = book.getAvailableCopies();
                System.out.println(book.getId() + ": " + book.getTitle() + " by " + book.getAuthor() + " (사본 수량: " + availableCopies + ")");
            }
            System.out.println("--------------------------------------------------------------------------");
        }
    }

    private boolean isValidBookId(String id) {
        return id.matches("^\\d+$");
    }

    private boolean retryPrompt() {
        System.out.print("다시 입력하시겠습니까? (y / 다른 키를 입력하면 관리자 메뉴 화면으로 이동합니다.): ");
        String retry = scanner.nextLine();
        return "y".equals(retry);
    }
}
