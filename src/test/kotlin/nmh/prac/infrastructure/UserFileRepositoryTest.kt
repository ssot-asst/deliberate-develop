package nmh.prac.infrastructure

import nmh.prac.domain.UserFileEntity
import org.assertj.core.api.BDDAssertions.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
class UserFileRepositoryTest {

    @Autowired
    lateinit var userFileRepository: UserFileRepository

    @AfterEach
    fun tearDown() {
        userFileRepository.deleteAll()
    }

    @Test
    fun `전체 데이터를 확인한다`() {
        // given
        // when
        val datas = userFileRepository.findAll()
        // then
        then(datas)
            .isNotNull
            .allSatisfy {
                assertThat(it).isInstanceOf(UserFileEntity::class.java)
            }
    }

    @Test
    fun `데이터를 삽입 할 수 있다`() {
        // given
        val user = UserFileEntity(
            name = "나파일",
            age = 26
        )
        // when
        userFileRepository.save(user)
        // then
        then(userFileRepository.findAll())
            .isNotEmpty
            .anySatisfy {
                assertThat(it.name).isEqualTo(user.name)
                assertThat(it.age).isEqualTo(user.age)
            }
    }

    @Test
    fun `데이터를 삽입 할 때 ID가 순차적으로 증가한다`() {
        // given
        val user1 = UserFileEntity(name = "나민혁", age = 26)
        val user2 = UserFileEntity(name = "나나나", age = 53)
        // when
        userFileRepository.save(user1)
        userFileRepository.save(user2)
        // then
        val users = userFileRepository.findAll()
        then(users)
            .isNotEmpty
            .hasSize(2)
        then(users[0].id).isLessThan(users[1].id)
    }

    @Test
    fun `deleteAll은 모든 데이터를 삭제한다`() {
        // given
        val user1 = UserFileEntity(name = "나민혁", age = 26)
        val user2 = UserFileEntity(name = "나나나", age = 53)
        userFileRepository.save(user1)
        userFileRepository.save(user2)

        // when
        userFileRepository.deleteAll()
        // then
        then(userFileRepository.findAll())
            .isEmpty()
    }

    @Test
    fun `findAll()은 모든 데이터를 조회한다`() {
        // given
        val user1 = UserFileEntity(name = "나민혁", age = 26)
        val user2 = UserFileEntity(name = "나나나", age = 53)
        userFileRepository.save(user1)
        userFileRepository.save(user2)
        // when
        val users = userFileRepository.findAll()
        // then
        then(users)
            .isNotEmpty
            .hasSize(2)
            .anySatisfy {
                assertThat(it.name).isEqualTo("나민혁")
                assertThat(it.age).isEqualTo(26)
            }
            .anySatisfy {
                assertThat(it.name).isEqualTo("나나나")
                assertThat(it.age).isEqualTo(53)
            }

    }

    @Test
    fun `findById()를 통해 id를 특정한 사용자를 찾을 수 있다`() {
        // given
        val user = UserFileEntity(name = "나민혁", age = 26)
        val savedUser = userFileRepository.save(user)
        // when
        val foundUser = userFileRepository.findById(savedUser.id)
        // then
        then(foundUser)
            .isNotNull
            .extracting("name", "age")
            .containsExactly("나민혁", 26)
    }

    @Disabled(
        "동시성을 코드레벨에서 지원하고 있지 않기 때문에 해당 테스트는 통과하지 않는다." +
                "별도로 통과를 위한다면 save에 `@Synchronized` 어노테이션을 추가해야 한다."
    )
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
                    userFileRepository.save(UserFileEntity(name = "나민혁$i", age = 26 + i))
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
        // then
        then(userFileRepository.findAll()).hasSize(20)
    }

    @Test
    fun `파일이 너무 크면 OOM이 일어난다`() {
        // given
        val largeData = ByteArray(50_000_000) // 50MB 배열
        val users = mutableListOf<UserFileEntity>()

        // when
        // then
        thenThrownBy {
            repeat(100) { // 5GB 정도면 대부분 환경에서 OOM
                users.add(
                    UserFileEntity(
                        name = String(largeData),
                        age = it
                    )
                )
            }
        }.isInstanceOf(OutOfMemoryError::class.java)
    }
}