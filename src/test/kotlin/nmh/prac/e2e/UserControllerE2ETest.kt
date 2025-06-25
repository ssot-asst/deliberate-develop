package nmh.prac.e2e

import nmh.prac.api.user.request.UserRegisterRequest
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserControllerE2ETest(
    private val webTestClient: WebTestClient
) {

    @Test
    fun `새로운 사용자를 등록한다`() {
        val request = UserRegisterRequest(
            name = "나민혁",
            age = 26
        )

        webTestClient.post()
            .uri("/api/users")
            .bodyValue(request)
            .exchange()
            .expectStatus().isCreated
            .expectBody().isEmpty
    }
}