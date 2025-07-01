package nmh.prac.infrastructure.user

import org.assertj.core.api.BDDAssertions.then
import org.assertj.core.api.BDDAssertions.thenThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Description
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

    @Description("해당 테스트는 무시가 된다. 셋업 데이터를 준비하면서 문제가 발생해서 해당 테스트가 무시되는것 같음")
    @Disabled("OOM이 발생하는 테스트로, 실제로는 실행되지 않습니다.")
    @Test
    fun `한번에 조회하는 데이터가 너무 많으면 OOM이 일어난다`() {
        // given
        val largeDataCount = 1_000_000_000
        val users = (1..largeDataCount).map {
            UserJpaEntity(name = "User$it", age = it % 100 + 1)
        }
        userJpaRepository.saveAll(users)

        // when
        // then
        thenThrownBy {
            userRepository.findAll()
        }.isInstanceOf(OutOfMemoryError::class.java)
    }
}