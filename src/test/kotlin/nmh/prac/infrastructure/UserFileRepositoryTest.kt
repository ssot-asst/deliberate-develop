package nmh.prac.infrastructure

import nmh.prac.domain.UserFileEntity
import org.assertj.core.api.BDDAssertions.assertThat
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

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
}