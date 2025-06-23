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
}

fun UserFileRepository.deleteAll() {
    val users = findAll().toMutableList()
    users.clear()
    objectMapper.writeValue(files, users)
}