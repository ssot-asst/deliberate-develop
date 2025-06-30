package nmh.prac.learning

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jdk.jfr.Description
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.stereotype.Component
import java.lang.reflect.Proxy

@SpringBootTest
class JpaInterfaceImplementationCreationTest {

    @Autowired
    private lateinit var repository: SampleRepository

    @Description("디버거를 통해서 SimpleJpaRepository의 메서드를 호출 하는지 확인 할 수 있다")
    @Disabled("해당 테스트는 단순히 JpaRepository 인터페이스의 구현체가 생성되는지 확인하기 위한 테스트입니다.")
    @Test
    fun name() {
        repository.save(SampleEntity(name = "실험용"))
    }
}

@Entity
class SampleEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
)

interface SampleRepository: JpaRepository<SampleEntity, Long>