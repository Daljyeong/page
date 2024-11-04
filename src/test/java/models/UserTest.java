package models;

import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import record.BorrowRecord;
import record.ReturnRecord;
import record.LastAccessRecord;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private User user;
    private BorrowRecord borrowRecord;
    private ReturnRecord returnRecord;

    @BeforeEach
    void setUp() {
        user = new User("John Doe", "user123", "password");
        borrowRecord = new BorrowRecord("user123", 101, LocalDate.now().minusDays(5));
        returnRecord = new ReturnRecord("user123", 101, LocalDate.now().minusDays(2));
        LastAccessRecord.getInstance().setLastAccessDate(LocalDate.now());
    }

    @Test
    void testAuthenticate() {
        assertTrue(user.authenticate("password"));
        assertFalse(user.authenticate("wrongpassword"));
    }

    @Test
    void testBorrowBook() {
        user.borrowBook(101);
        assertTrue(user.hasBorrowedBook(101));
    }

    @Test
    void testReturnBook() {
        user.borrowBook(101);
        user.returnBook(101);
        assertFalse(user.hasBorrowedBook(101));
    }

    @Test
    void testAddBorrowRecord() {
        user.addBorrowRecord(borrowRecord);
        assertTrue(user.hasOverdueBooks());
    }

    @Test
    void testAddReturnRecord() {
        user.addBorrowRecord(borrowRecord);
        user.addReturnRecord(returnRecord);
        assertFalse(user.hasOverdueBooks());
    }

    @Test
    void testHasOverdueBooks() {
        user.addBorrowRecord(borrowRecord);
        assertTrue(user.hasOverdueBooks());

        user.addReturnRecord(returnRecord);
        assertFalse(user.hasOverdueBooks());
    }


    @InjectMocks
    private User user;

    @Mock
    private LastAccessRecord mockLastAccessRecord;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("John Doe", "user123", "password");
    }

    @Test
    public void testHasOverdueBooks() {
        // 모의 LastAccessRecord에서 현재 날짜 반환 설정
        LocalDate overdueDate = LocalDate.now().minusDays(10);
        LocalDate currentDate = LocalDate.now();

        when(mockLastAccessRecord.getLastAccessDate()).thenReturn(currentDate);

        // 대출 기록 추가 (연체된 날짜로 설정)
        BorrowRecord overdueBorrowRecord = new BorrowRecord(user.getId(), 1, overdueDate);
        user.addBorrowRecord(overdueBorrowRecord);

        // 연체된 책이 있는지 확인
        assertTrue(user.hasOverdueBooks());

        // 모의 LastAccessRecord 호출 확인
        verify(mockLastAccessRecord, times(1)).getLastAccessDate();
    }

    @Test
    public void testHasNoOverdueBooks() {
        LocalDate nonOverdueDate = LocalDate.now().minusDays(1);
        LocalDate currentDate = LocalDate.now();

        when(mockLastAccessRecord.getLastAccessDate()).thenReturn(currentDate);

        // 비연체 대출 기록 추가
        BorrowRecord nonOverdueBorrowRecord = new BorrowRecord(user.getId(), 1, nonOverdueDate);
        user.addBorrowRecord(nonOverdueBorrowRecord);

        // 연체된 책이 없는지 확인
        assertFalse(user.hasOverdueBooks());

        // 모의 LastAccessRecord 호출 확인
        verify(mockLastAccessRecord, times(1)).getLastAccessDate();
    }
}
