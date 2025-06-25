package nmh.prac.docs.user

import com.nmh.commerce.controller.RestDocsTest
import com.nmh.commerce.controller.RestDocsUtils
import io.mockk.every
import io.mockk.mockk
import io.restassured.http.ContentType
import nmh.prac.api.user.UserController
import nmh.prac.api.user.response.UserResponse
import nmh.prac.application.user.UserService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.request.RequestDocumentation

class UserControllerTest : RestDocsTest() {
    private lateinit var controller: UserController
    private lateinit var service: UserService

    @BeforeEach
    fun setUp() {
        service = mockk()
        controller = UserController(service)
        mockMvc = mockController(controller)
    }

    @Test
    fun `사용자를 단일 조회한다`() {
        every { service.getById(any()) } returns UserResponse(1L, "나민혁", 26)

        given()
            .contentType(ContentType.JSON)
            .get("/api/users/{id}", 1L)
            .then()
            .status(HttpStatus.OK)
            .apply(
                MockMvcRestDocumentation.document(
                    "getUserById",
                    RestDocsUtils.requestPreprocessor(),
                    RestDocsUtils.responsePreprocessor(),
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("id").description("사용자 ID")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("id").type(JsonFieldType.NUMBER).description("사용자 ID"),
                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름"),
                        PayloadDocumentation.fieldWithPath("age").type(JsonFieldType.NUMBER).description("사용자 나이")
                    )
                )
            )
    }
}