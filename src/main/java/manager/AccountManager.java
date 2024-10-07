package manager;

import java.io.*;
import java.util.HashMap;
import models.*;

public class AccountManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private static AccountManager instance = null;
    private HashMap<String, Account> accounts;
    private final String FILE_PATH = "accounts.post";

    private AccountManager() {
        accounts = new HashMap<>();
    }

    public static AccountManager getInstance() {
        if (instance == null) {
            synchronized (AccountManager.class) {
                if (instance == null) {
                    instance = new AccountManager();
                }
            }
        }
        return instance;
    }

    public void addAccount(Account account) {
        accounts.put(account.getId(), account);
        saveAccounts(); // 계정 추가 시 저장
    }

    public Account getAccountById(String id) {
        return accounts.get(id);
    }

    // 계정 데이터 저장
    public void saveAccounts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("accounts.post"))) {
            oos.writeObject(this);
        } catch (IOException e) {
            System.out.println("계정 데이터를 저장하는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 계정 데이터 로드
    public static void loadAccounts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("accounts.post"))) {
            instance = (AccountManager) ois.readObject();
        } catch (FileNotFoundException e) {
            // 파일이 없으면 새 인스턴스를 사용
            instance = new AccountManager();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("계정 데이터를 불러오는 중 오류가 발생했습니다: " + e.getMessage());
            instance = new AccountManager();
        }
    }

    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(accounts);
        } catch (IOException e) {
            System.out.println("계정 데이터를 저장하는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return; // 파일이 없으면 초기화된 상태 유지
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            accounts = (HashMap<String, Account>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("계정 데이터를 로드하는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
