package nmh.prac.application

import nmh.prac.domain.UserFileEntity
import nmh.prac.infrastructure.UserFileRepository
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserFileServiceTest(
    private val userFileRepository: UserFileRepository
) {

    @Autowired
    private lateinit var userService: UserService

    @AfterEach
    fun tearDown() {
        userFileRepository.deleteAll()
    }

    @Test
    fun `사용자를 저장 할 수 있다`() {
        // given
        val user = UserFileEntity(
            name = "나민혁",
            age = 26
        )
        // when
        // then
        then(userService.register(user.name, user.age))
            .extracting("name", "age")
            .containsExactly("나민혁", 26)

    }

    @Test
    fun `사용자를 조회 할 수 있다`() {
        // given
        val user = UserFileEntity(name = "나민혁", age = 26)
        val savedUser = userFileRepository.save(user)
        // when
        val foundUser = userService.getById(savedUser.id)
        // then
        then(foundUser)
            .extracting("name", "age")
            .containsExactly("나민혁", 26)

        then(userFileRepository.findById(savedUser.id))
            .extracting("name", "age")
            .containsExactly("나민혁", 26)

    }

    @Test
    fun `사용자 목록을 조회할 수 있다`() {
        // given
        val user1 = UserFileEntity(name = "나민혁", age = 26)
        val user2 = UserFileEntity(name = "나나나", age = 53)

        userFileRepository.save(user1)
        userFileRepository.save(user2)
        // when
        val users = userService.getAll()
        // then
        then(users)
            .isNotEmpty
            .hasSize(2)
            .anySatisfy {
                then(it.name).isEqualTo("나민혁")
                then(it.age).isEqualTo(26)
            }
            .anySatisfy {
                then(it.name).isEqualTo("나나나")
                then(it.age).isEqualTo(53)
            }
    }

    @Test
    fun `사용자를 id를 통해 삭제 할 수 있다`() {
        // given
        val user = UserFileEntity(name = "나민혁", age = 26)
        val savedUser = userFileRepository.save(user)
        // when
        userService.deleteById(savedUser.id)
        // then
        then(userFileRepository.findById(savedUser.id))
            .isNull()
    }

    @Test
    fun `사용자 정보를 변경 할 수 있다`() {
        // given
        val user = UserFileEntity(name = "나민혁", age = 26)
        val savedUser = userFileRepository.save(user)
        // when
        val updatedUser = userService.updateById(savedUser.id, "나나나", 53)
        // then
        then(updatedUser)
            .extracting("name", "age")
            .containsExactly("나나나", 53)

        then(userFileRepository.findById(savedUser.id))
            .extracting("name", "age")
            .containsExactly("나나나", 53)

    }
}