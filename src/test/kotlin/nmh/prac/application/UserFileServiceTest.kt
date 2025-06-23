package nmh.prac.application

import nmh.prac.domain.UserFileEntity
import nmh.prac.infrastructure.UserFileRepository
import org.assertj.core.api.BDDAssertions.then
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
            .isEqualTo(
                UserFileEntity(
                    name = "나민혁",
                    age = 26
                )
            )

    }
}