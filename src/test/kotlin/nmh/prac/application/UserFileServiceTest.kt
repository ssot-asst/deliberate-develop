package nmh.prac.application

import nmh.prac.domain.UserFileEntity
import nmh.prac.infrastructure.UserFileRepository
import org.assertj.core.api.BDDAssertions.then
import org.assertj.core.api.BDDAssertions.thenThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Disabled
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

    @Test
    fun `파일을 저장소로 사용 할 경우 원자성이 지켜지지 않을 수 있다`() {
        // given
        val user1 = UserFileEntity(name = "나민혁", age = 26)
        val user2 = UserFileEntity(name = "나나나", age = 53)
        val savedUser = userFileRepository.save(user1)
        userFileRepository.save(user2)
        // when
        // then
        thenThrownBy { userService.somethingWithException(savedUser.id, "김스트", 11) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("무언가 잘못되었기 때문에 예외를 발생시킵니다")

        // 아토믹한 파일 저장소를 사용하지 않는 경우, 예외 발생 후에도 이전 상태가 유지되지 않을 수 있다.
        then(userFileRepository.findById(savedUser.id))
            .extracting("name", "age")
            .containsExactly("김성공", 50)
    }

    @Disabled("파일을 저장소로 사용 할 경우 원자성이 지켜지지 않을 수 있다 원자성이 보장되는 경우에만 아래 테스트가 통과한다")
    @Test
    fun `파일을 저장소로 사용 할 경우 트랜잭션을 구현하지 않으면 원자성이 지켜지지 않는다`() {
        // given
        val user1 = UserFileEntity(name = "나민혁", age = 26)
        val user2 = UserFileEntity(name = "나나나", age = 53)
        val savedUser = userFileRepository.save(user1)
        userFileRepository.save(user2)
        // when
        userService.somethingWithException(savedUser.id, "김스트", 11)
        // then
        then(userFileRepository.findById(savedUser.id))
            .extracting("name", "age")
            .containsExactly("나민혁", 26)
    }
}