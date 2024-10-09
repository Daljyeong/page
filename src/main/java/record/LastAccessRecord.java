package record;// record.LastAccessRecord.java
import java.io.*;
import java.time.LocalDate;

public class LastAccessRecord {
    private static LastAccessRecord instance = null;
    private LocalDate lastAccessDate;
    private final String FILE_PATH = "lastAccess.post";

    private LastAccessRecord() {
        lastAccessDate = null;
    }

    public static LastAccessRecord getInstance() {
        if (instance == null) {
            synchronized (LastAccessRecord.class) {
                if (instance == null) {
                    instance = new LastAccessRecord();
                }
            }
        }
        return instance;
    }

    public LocalDate getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(LocalDate date) {
        this.lastAccessDate = date;
    }

    // 데이터 저장
    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(lastAccessDate);
        } catch (IOException e) {
            System.out.println("최근 접속 날짜를 저장하는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 데이터 로드
    public void loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return; // 파일이 없으면 초기화된 상태 유지
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            lastAccessDate = (LocalDate) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("최근 접속 날짜를 로드하는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
