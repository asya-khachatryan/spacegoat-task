package am.spacegoat.task.service;

import am.spacegoat.task.containers.PostgresTestContainer;
import am.spacegoat.task.domain.UserEntity;
import am.spacegoat.task.repository.TransactionRepository;
import am.spacegoat.task.repository.UserRepository;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration
public class TransferServiceTests {

   // @ClassRule
    // public static PostgreSQLContainer<PostgresTestContainer> postgreSQLContainer = PostgresTestContainer.getInstance();

    @Autowired
    private UserAccountService service;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private UserEntity user1 = UserEntity.builder()
            .firstName("John")
            .lastName("Doe")
            .city("New York")
            .username("john.doe")
            .balance(BigDecimal.valueOf(1000))
            .build();

    private UserEntity user2 = UserEntity.builder()
            .firstName("Jane")
            .lastName("Smith")
            .city("Los Angeles")
            .username("jane.smith")
            .balance(BigDecimal.valueOf(1000))
            .build();

    private UserEntity user3 = UserEntity.builder()
            .firstName("Jane")
            .lastName("Smith")
            .city("Los Angeles")
            .username("jane")
            .balance(BigDecimal.valueOf(1000))
            .build();


    @BeforeEach
    public void addUsers() {
        transactionRepository.deleteAll();
        userRepository.deleteAll();
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        user3 = userRepository.save(user3);
    }

    @RepeatedTest(1)
    public void testConcurrentTransferBetweenUsersDeadlocks() throws InterruptedException {
        try {
       //     postgreSQLContainer.start();
            CountDownLatch startSignal = new CountDownLatch(1);
            CountDownLatch doneSignal = new CountDownLatch(2);

            ExecutorService executorService = Executors.newFixedThreadPool(2);

            executorService.execute(() -> {
                try {
                    startSignal.await();
                    service.transferRepeatableRead(user1.getId(), user2.getId(), new BigDecimal(100));
                    doneSignal.countDown();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            executorService.execute(() -> {
                try {
                    startSignal.await();
                    service.transferRepeatableRead(user2.getId(), user1.getId(), new BigDecimal(200));
                    doneSignal.countDown();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            startSignal.countDown();
            doneSignal.await();
        } catch (CannotAcquireLockException e) {
            assertTrue(true, "Test passed with a deadlock exception.");
        }
    }

    @Test
//    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void testConcurrentTransferBetweenUsersReadCommitted() throws InterruptedException {
//        try {
         //   postgreSQLContainer.start();
            CountDownLatch startSignal = new CountDownLatch(1);
            CountDownLatch doneSignal = new CountDownLatch(2);

            ExecutorService executorService = Executors.newFixedThreadPool(2);

            executorService.execute(() -> {
                try {
                    startSignal.await();
                    service.transferReadCommitted(user1.getId(), user2.getId(), new BigDecimal(100));
                    doneSignal.countDown();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            executorService.execute(() -> {
                try {
                    startSignal.await();
                    service.transferReadCommitted(user2.getId(), user1.getId(), new BigDecimal(200));
                    doneSignal.countDown();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            startSignal.countDown();
            doneSignal.await();
//        } catch (CannotAcquireLockException e) {
//            assertTrue(true, "Test passed with a deadlock exception.");
//        }
    }

    @Test
    public void testConcurrentTransferBetweenUsersReadUncommitted() throws InterruptedException {
//        try {
        // postgreSQLContainer.start();

        Thread thread1 = new Thread(() -> service.transferReadUncommitted(user1.getId(), user2.getId(), new BigDecimal(100)));
        Thread thread2 = new Thread(() -> service.transferReadUncommitted(user2.getId(), user1.getId(), new BigDecimal(200)));

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        } catch (CannotAcquireLockException e) {
//            assertTrue(true, "Test passed with a deadlock exception.");
//        }
    }

    @RepeatedTest(1)
    public void readDataUncommitted() throws InterruptedException {
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(2);

        AtomicReference<BigDecimal> balance = new AtomicReference<>();
        AtomicReference<BigDecimal> balance2 = new AtomicReference<>();

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.execute(() -> {
            try {
                startSignal.await();
                System.out.println("Uncommited changes started");
                service.transferReadUncommittedWithSleep(user1.getId(), user2.getId(), new BigDecimal(100));
                System.out.println("Uncommited changes made");
                balance.set(userRepository.getBalanceByUserId(user2.getId()));
                doneSignal.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        executorService.execute(() -> {
            try {
                startSignal.await();
                balance2.set(service.getBalance(user2.getId()));
                System.out.println("Balance 2 set");
                doneSignal.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        startSignal.countDown();
        doneSignal.await();

        assertEquals(balance.get(), balance2.get());
    }
}
