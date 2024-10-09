import manager.*;
import models.*;
import record.LastAccessRecord;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static AccountManager accountManager = AccountManager.getInstance();
    private static BookManager bookManager = BookManager.getInstance();
    private static LastAccessRecord lastAccessRecord = LastAccessRecord.getInstance();

    public static void main(String[] args) {
        // 데이터 로드 post 파일 로드하함
        accountManager.loadData();
        bookManager.loadData();
        lastAccessRecord.loadData();

        // 관리자 계정 미리 추가 맨처음에만
        if (accountManager.getAccountById("secret") == null) {
            accountManager.addAccount(new Admin("secret", "1234"));
            accountManager.saveData();
        }
        // 초기화면 임
        while (true) {
            showMainMenu();
            int choice = getUserChoice(1, 3);
            switch (choice) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    handleRegistration();
                    break;
                case 3:
                    System.out.println("프로그램을 종료합니다.");
                    // 데이터 저장
                    accountManager.saveData();
                    bookManager.saveData();
                    lastAccessRecord.saveData();
                    System.exit(0);
                    break;
                default:
                    // 이 경우는 발생하지 않음
                    break;
            }
        }
    }

    // 초기 메인 메뉴 표시
    private static void showMainMenu() {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println(" 도서관 관리 프로그램에 오신 것을 환영합니다");
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("1. 로그인");
        System.out.println("2. 회원가입");
        System.out.println("3. 프로그램 종료");
        System.out.println("--------------------------------------------------------------------------");
        System.out.print("원하는 작업의 번호를 입력하세요: ");
    }

    // 사용자 입력을 받아 유효한 메뉴 선택인지 화긴
    // todo 사실 유저 초이스를 따로 래퍼 객체를 만들어서 하면 더 객체지향적으로 좋긴한데... 귀찮다 아쉬운대로!
    private static int getUserChoice(int min, int max) {
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

    // 로그인 처리
    private static void handleLogin() {
        if (!validateAccessDate()) {
            return;
        }

        System.out.print("아이디: ");
        String id = scanner.nextLine();
        System.out.print("비밀번호: ");
        String password = scanner.nextLine();

        Account account = accountManager.getAccountById(id);
        if (account != null && account.authenticate(password)) {
            System.out.print("로그인 하시겠습니까? (y / 다른 키를 입력하면 초기화면으로 이동합니다.): ");
            String confirm = scanner.nextLine();
            if ("y".equalsIgnoreCase(confirm)) {
                LocalDate currentDate = LocalDate.now();
                lastAccessRecord.setLastAccessDate(currentDate);
                lastAccessRecord.saveData();

                if (account instanceof Admin) {
                    System.out.println("관리자 권한으로 접속합니다. 관리자 메뉴화면으로 이동합니다.");
                    AdminInterface adminInterface = new AdminInterface((Admin) account);
                    adminInterface.showMenu();
                } else if (account instanceof User) {
                    System.out.println("사용자 권한으로 접속합니다. 사용자 메뉴화면으로 이동합니다.");
                    UserInterface userInterface = new UserInterface((User) account);
                    userInterface.showMenu();
                }
            } else {
                System.out.println("초기화면으로 돌아갑니다.");
            }
        } else {
            System.out.println("아이디나 비밀번호가 올바르지 않습니다. 다시 시도해주세요.");
        }
    }

    // 회원가입 처리
    private static void handleRegistration() {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println(" 회원가입 화면");
        System.out.println("--------------------------------------------------------------------------");

        String name;
        while (true) {
            System.out.print("이름: ");
            name = scanner.nextLine();
            if (isValidName(name)) {
                break;
            } else {
                System.out.println("이름은 알파벳 대소문자, 공백만 허용되며, 특수문자를 포함할 수 없습니다.");
            }
        }

        String id;
        while (true) {
            System.out.print("아이디: ");
            id = scanner.nextLine();
            if (isValidId(id)) {
                if (accountManager.getAccountById(id) == null) {
                    break;
                } else {
                    System.out.println("이미 존재하는 아이디입니다. 다른 아이디로 시도해주세요.");
                }
            } else {
                System.out.println("아이디는 3자 이상 20자 이하의 문자로 알파벳 대소문자와 숫자만 허용됩니다.");
            }
        }

        String password;
        while (true) {
            System.out.print("비밀번호: ");
            password = scanner.nextLine();
            if (isValidPassword(password)) {
                break;
            } else {
                System.out.println("비밀번호는 8자 이상 20자 이하로, 알파벳 대소문자, 숫자 및 특수문자를 최소 하나 이상 포함해야 합니다.");
            }
        }

        String confirmPassword;
        while (true) {
            System.out.print("비밀번호 확인: ");
            confirmPassword = scanner.nextLine();
            if (password.equals(confirmPassword)) {
                break;
            } else {
                System.out.println("비밀번호와 비밀번호 확인이 일치하지 않습니다. 다시 입력해주세요.");
            }
        }

        System.out.print("회원가입 하시겠습니까? (y / 다른 키를 입력하면 초기화면으로 이동합니다.): ");
        String confirm = scanner.nextLine();
        if ("y".equalsIgnoreCase(confirm)) {
            accountManager.addAccount(new User(name, id, password));
            accountManager.saveData();
            System.out.println("회원가입이 완료되었습니다. 초기화면으로 돌아갑니다.");
        } else {
            System.out.println("초기화면으로 돌아갑니다.");
        }
    }

    // 이름 유효성 검증
    private static boolean isValidName(String name) {
        // 알파벳 대소문자와 공백만, 연속된 공백 및 앞뒤 공백 없음
        return name.matches("^[A-Za-z]+(?: [A-Za-z]+)*$");
    }

    // 아이디 유효성 검증
    private static boolean isValidId(String id) {
        return id.matches("^[A-Za-z0-9]{3,20}$");
    }

    // 비밀번호 유효성 검증
    private static boolean isValidPassword(String password) {
        if (password.length() < 8 || password.length() > 20) return false;
        if (password.contains(" ")) return false; // 공백 포함 검증

        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char ch : password.toCharArray()) {
            if (Character.isLetter(ch)) hasLetter = true;
            else if (Character.isDigit(ch)) hasDigit = true;
            else if ("!@#$%^&*()".indexOf(ch) >= 0) hasSpecial = true;
        }

        return hasLetter && hasDigit && hasSpecial;
    }


    private static boolean validateAccessDate() {
        while (true) {
            System.out.println("--------------------------------------------------------------------------");
            System.out.println(" 날짜 입력 화면");
            System.out.println("--------------------------------------------------------------------------");
            System.out.println("날짜는 'YYYY-MM-DD' 형식이어야 합니다. 예: 2024-09-25");
            System.out.print("프로그램에서 사용할 날짜를 입력해주세요: ");
            String inputDate = scanner.nextLine();

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            try {
                LocalDate input = LocalDate.parse(inputDate, dateFormatter);
                lastAccessRecord.loadData();
                if (lastAccessRecord.getLastAccessDate() != null && input.isBefore(lastAccessRecord.getLastAccessDate())) {
                    System.out.println("최근에 입력한 날짜보다 과거입니다. 다시 입력해주세요.");
                    continue;
                }

                lastAccessRecord.setLastAccessDate(input);
                lastAccessRecord.saveData();
                System.out.println("날짜 입력이 완료되었습니다. 로그인 화면으로 이동합니다.");
                return true;

            } catch (DateTimeParseException e) {
                System.out.println("날짜 형식이 올바르지 않습니다. 다시 입력해주세요.");
            }
        }
    }
}
