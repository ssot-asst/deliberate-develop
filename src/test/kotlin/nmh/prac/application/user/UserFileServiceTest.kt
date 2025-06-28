package nmh.prac.application.user

import nmh.prac.domain.user.User
import org.assertj.core.api.BDDAssertions.then
import org.assertj.core.api.BDDAssertions.thenThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Description
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserFileServiceTest(
    private val userRepository: UserRepository
) {

    @Autowired
    private lateinit var userService: UserService

    @AfterEach
    fun tearDown() {
        userRepository.deleteAll()
    }

    @Test
    fun `사용자를 저장 할 수 있다`() {
        // given
        val user = User(
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
        val user = User(name = "나민혁", age = 26)
        val savedUser = userRepository.save(user)
        // when
        val foundUser = userService.getById(savedUser.id)
        // then
        then(foundUser)
            .extracting("name", "age")
            .containsExactly("나민혁", 26)

        then(userRepository.findById(savedUser.id))
            .extracting("name", "age")
            .containsExactly("나민혁", 26)

    }

    @Test
    fun `사용자 목록을 조회할 수 있다`() {
        // given
        val user1 = User(name = "나민혁", age = 26)
        val user2 = User(name = "나나나", age = 53)

        userRepository.save(user1)
        userRepository.save(user2)
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
        val user = User(name = "나민혁", age = 26)
        val savedUser = userRepository.save(user)
        // when
        userService.deleteById(savedUser.id)
        // then
        then(userRepository.findById(savedUser.id))
            .isNull()
    }

    @Test
    fun `사용자 정보를 변경 할 수 있다`() {
        // given
        val user = User(name = "나민혁", age = 26)
        val savedUser = userRepository.save(user)
        // when
        val updatedUser = userService.updateById(savedUser.id, "나나나", 53)
        // then
        then(updatedUser)
            .extracting("name", "age")
            .containsExactly("나나나", 53)

        then(userRepository.findById(savedUser.id))
            .extracting("name", "age")
            .containsExactly("나나나", 53)

    }

    @Description("파일을 저장소로 사용 할 경우 원자성이 지켜지지 않을 수 있다")
    @Disabled("파일이 아니기 때문에 테스트 제외")
    @Test
    fun `저장이 아토믹한지 확인한다`() {
        // given
        val user1 = User(name = "나민혁", age = 26)
        val user2 = User(name = "나나나", age = 53)
        val savedUser = userRepository.save(user1)
        userRepository.save(user2)
        // when
        // then
        thenThrownBy { userService.somethingWithException(savedUser.id, "김스트", 11) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("무언가 잘못되었기 때문에 예외를 발생시킵니다")

        // 아토믹한 파일 저장소를 사용하지 않는 경우, 예외 발생 후에도 이전 상태가 유지되지 않을 수 있다.
        then(userRepository.findById(savedUser.id))
            .extracting("name", "age")
            .containsExactly("김성공", 50)
    }

    @Description("파일을 저장소로 사용 할 경우 트랜잭션을 구현하지 않으면 원자성이 지켜지지 않는다")
    @Test
    fun `저장이 아토믹한 연산인지 확인한다`() {
        // given
        val user1 = User(name = "나민혁", age = 26)
        val user2 = User(name = "나나나", age = 53)
        val savedUser = userRepository.save(user1)
        userRepository.save(user2)
        // when
        thenThrownBy{ userService.somethingWithException(savedUser.id, "김스트", 11) }
            .isInstanceOf(IllegalArgumentException::class.java)
        // then
        then(userRepository.findById(savedUser.id))
            .extracting("name", "age")
            .containsExactly("나민혁", 26)
    }
}