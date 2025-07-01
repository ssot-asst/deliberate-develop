package nmh.prac.infrastructure.user

import org.assertj.core.api.BDDAssertions.then
import org.assertj.core.api.BDDAssertions.thenThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Description
import org.springframework.test.context.jdbc.Sql
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
class UserRepositoryImplTest {

    @Autowired
    private lateinit var userRepository: UserRepositoryImpl

    @Autowired
    private lateinit var userJpaRepository: UserJpaRepository

    @AfterEach
    fun tearDown() {
        userJpaRepository.deleteAllInBatch()
    }

    @Test
    fun `사용자 요청이 동시에 일어날 경우를 확인한다`() {
        // given
        val threadCount = 20
        val executorService = Executors.newFixedThreadPool(10)
        val latch = CountDownLatch(threadCount)

        // when
        for (i in 0 until threadCount) {
            executorService.execute {
                try {
                    userRepository.save(UserJpaEntity(name = "나민혁$i", age = 26 + i))
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
        // then
        then(userJpaRepository.findAll()).hasSize(20)
    }

    @Description("SQL로 데이터를 삽입하고 조회 할 때 OOM이 발생하는지 확인하지만 실제로 데이터를 삽입할 때 부터 OOM이 발생 할 수 있다.")
    @Disabled("이 테스트는 OOM을 유발할 수 있으므로 주석 처리합니다.")
    @Sql("/datas/bulk-user.sql")
    @Test
    fun `한번에 조회하는 데이터가 너무 많으면 OOM이 일어난다`() {
        // given
        // when
        // then
        thenThrownBy {
            userRepository.findAll()
        }.isInstanceOf(OutOfMemoryError::class.java)
    }
}