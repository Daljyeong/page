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
                    System.out.println("도서 추가 화면으로 이동합니다.");
                    handleAddBook();
                    break;
                case 2:
                    System.out.println("도서 사본 추가 화면으로 이동합니다.");
                    handleAddCopies();
                    break;
                case 3:
                    System.out.println("도서 삭제 화면으로 이동합니다.");
                    handleDeleteBook();
                    break;
                case 4:
                    System.out.println("도서 검색 화면으로 이동합니다.");
                    handleSearchBook();
                    break;
                case 5:
                    System.out.println("반납 기한 설정 화면으로 이동합니다.");
                    handleSetReturnDeadline();
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
        System.out.println(" 반납 기한 설정 화면");
        System.out.println("--------------------------------------------------------------------------");

        int returnPeriod = 0;

        // 반납 기한 입력 처리
        while (true) {
            System.out.print("반납 기한(일수)를 정수 형태로 입력하세요. : ");
            try {
                returnPeriod = scanner.nextInt(); // 정수 입력
                scanner.nextLine(); // 버퍼 비우기
                break;
            } catch (Exception e) {
                System.out.println("잘못된 입력입니다. (정수 형태로 입력해주세요.)");
                scanner.nextLine(); // 버퍼 비우기
                if (!retryPrompt()) return;
            }
        }

        // 확인 입력 처리
        System.out.print("반납기한을 설정 하시겠습니까? (y / 다른 키를 입력하면 관리자 메뉴 화면으로 이동합니다.): ");
        String confirm = scanner.nextLine(); // 입력받기
        if ("y".equalsIgnoreCase(confirm)) { // 대소문자 무시 비교
            bookManager.setReturnPeriod(returnPeriod);
            System.out.println("반납 기한이 설정되었습니다. (" + returnPeriod + "일)");
        } else {
            System.out.println("관리자 메뉴 화면으로 이동합니다.");
        }
    }

    //todo 싹 다 개조 함
    private void handleAddBook() {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println(" 도서 추가 화면");
        System.out.println("--------------------------------------------------------------------------");

        String title;
        while (true) {
            System.out.print("도서 제목: ");
            title = scanner.nextLine().trim();
            if (!title.isEmpty() && title.matches("^[a-zA-Z0-9 ]+$")) { // 영어와 숫자만 허용
                break;
            } else {
                System.out.println("잘못된 입력입니다. (영어 형태로 입력해주세요.)");
                if (!retryPrompt()) return;
            }
        }

        String author;
        while (true) {
            System.out.print("도서 저자: ");
            author = scanner.nextLine().trim();
            if (!author.isEmpty() && author.matches("^[a-zA-Z ]+$")) { // 영어만 허용
                break;
            } else {
                System.out.println("잘못된 입력입니다. (영어 형태로 입력해주세요.)");
                if (!retryPrompt()) return;
            }
        }

        int quantity = 0;
        while (true) {
            System.out.print("도서 수량을 입력하세요: ");
            if (scanner.hasNextInt()) {
                quantity = scanner.nextInt();
                scanner.nextLine(); // 버퍼 클리어
                if (quantity > 0) break;
                else System.out.println("수량은 1 이상의 정수여야 합니다.");
            } else {
                scanner.nextLine(); // 버퍼 클리어
                System.out.println("잘못된 입력입니다. (정수 형태로 입력해주세요.)");
            }
            if (!retryPrompt()) return;
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


    private void handleAddCopies() {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println(" 도서 사본 추가 화면");
        System.out.println("--------------------------------------------------------------------------");

        int bookId;
        //todo 보강 사항
        while (true) {
            System.out.print("사본을 추가할 도서의 ID를 입력하세요: ");
            String input = scanner.nextLine().trim();
            if (input.matches("^\\d+$")) { // 정수 형태인지 확인
                bookId = Integer.parseInt(input);
                break;
            } else {
                System.out.println("잘못된 입력입니다. (정수 형태로 입력해주세요.)");
                if (!retryPrompt()) return;
            }
        }

        Book book = bookManager.getBookById(bookId);
        if (book == null) {
            System.out.println("입력하신 ID에 해당하는 도서가 존재하지 않습니다. 관리자 메뉴 화면으로 이동합니다.");
            return;
        }

        int copiesToAdd;
        while (true) {
            System.out.print("추가할 사본 수량을 입력하세요: ");
            String quantityInput = scanner.nextLine().trim();
            if (quantityInput.matches("^\\d+$")) { // 정수 형태인지 확인
                copiesToAdd = Integer.parseInt(quantityInput);
                if (copiesToAdd > 0) break;
                else System.out.println("수량은 1 이상의 정수여야 합니다.");
            } else {
                System.out.println("잘못된 입력입니다. (정수 형태로 입력해주세요.)");
                if (!retryPrompt()) return;
            }
        }

        book.addCopies(copiesToAdd);
        bookManager.saveData();
        System.out.println("사본이 성공적으로 추가되었습니다. 관리자 메뉴 화면으로 이동합니다.");
    }




    private void handleDeleteBook() {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println(" 도서 삭제 화면");
        System.out.println("--------------------------------------------------------------------------");
        while (true) {
            System.out.print("삭제할 도서 사본 ID를 입력하세요: ");
            String inputId = scanner.nextLine();
            if (!isValidBookId(inputId)) {
                System.out.println("잘못된 입력입니다. (정수 형태로 입력해주세요.)");
                // todo 수정
                if (!retryPrompt()) return;
                continue;
            }

            int bookCopyId = Integer.parseInt(inputId);
            BookCopy bookCopy = bookManager.getBookCopyById(bookCopyId);
            if (bookCopy == null) {
                System.out.println("입력하신 ID에 해당하는 도서가 존재하지 않습니다.");
                System.out.println("관리자 메뉴 화면으로 이동합니다.");
                return;
            }
            System.out.println("--------------------------------------------------------------------------");
            System.out.println("삭제할 도서 사본: " + bookCopy.getBookId() + ", " + bookCopy.getCopyId());
            if (bookCopy.isBorrowed()) {
                System.out.println("해당 도서 사본은 대출중입니다. 삭제할 수 없습니다.");
                System.out.println("관리자 메뉴 화면으로 이동합니다.");
                return;
            }

            System.out.print("도서 사본을 삭제하시겠습니까? (y / 다른 키를 입력하면 취소하고 관리자 메뉴로 이동합니다.): ");
            String confirm = scanner.nextLine();
            if ("y".equals(confirm)) {
                bookManager.removeBookCopy(bookCopyId);
                bookManager.saveData();
                System.out.println("도서 사본이 성공적으로 삭제되었습니다. 관리자 메뉴 화면으로 이동합니다.");
                return;
            } else {
                System.out.println("도서 삭제를 취소하였습니다. 관리자 메뉴 화면으로 이동합니다.");
                return;
            }
        }
    }

    //todo 다 뜯어고침
    private void handleSearchBook() {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println(" 도서 검색 화면");
        System.out.println("--------------------------------------------------------------------------");

        String keyword;
        while (true) {
            System.out.print("검색할 키워드를 입력하세요 (제목 또는 저자): ");
            keyword = scanner.nextLine().trim();

            // 영어가 아닌 문자 포함 여부 검사
            if (keyword.isEmpty()) {
                System.out.println("검색어를 입력해주세요.");
            } else if (!keyword.matches("[a-zA-Z\\s\\d]+")) {
                System.out.println("잘못된 입력입니다. (영어 형태로 입력해주세요.)");
            } else {
                break;
            }

            if (!retryPrompt()) return;
        }
        List<Book> results = bookManager.searchBooks(keyword);
        if (results.isEmpty()) {
            System.out.println("해당 키워드에 일치하는 도서가 존재하지 않습니다. 관리자 메뉴 화면으로 이동합니다.");
        } else {
            System.out.println("--------------------------------------------------------------------------");
            System.out.println("검색 결과:");
            for (Book book : results) {
                List<BookCopy> copies = book.getCopies();
                for (BookCopy copy : copies) {
                    System.out.println("사본ID: " + copy.getCopyId() + " - 도서ID: " + book.getId() + " - " + book.getTitle() + " - " + book.getAuthor());
                }
            }
            System.out.println("--------------------------------------------------------------------------");
        }
    }


    private boolean isValidBookId(String id) {
        return id.matches("^(0|[1-9]\\d*)$");
    }

    private boolean retryPrompt() {
        System.out.print("다시 입력하시겠습니까? (y / 다른 키를 입력하면 관리자 메뉴 화면으로 이동합니다.): ");
        String retry = scanner.nextLine();
        return "y".equals(retry);
    }
}
