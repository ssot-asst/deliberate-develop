package nmh.prac.docs

import com.nmh.commerce.controller.RestDocsTest
import com.nmh.commerce.controller.RestDocsUtils.requestPreprocessor
import com.nmh.commerce.controller.RestDocsUtils.responsePreprocessor
import io.mockk.every
import io.mockk.mockk
import io.restassured.http.ContentType
import nmh.prac.api.UserController
import nmh.prac.api.response.UserResponse
import nmh.prac.application.UserService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters

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
                document(
                    "getUserById",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    pathParameters(
                        parameterWithName("id").description("사용자 ID")
                    ),
                    responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("사용자 ID"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름"),
                        fieldWithPath("age").type(JsonFieldType.NUMBER).description("사용자 나이")
                    )
                )
            )
    }
}