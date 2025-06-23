package nmh.prac.api

import nmh.prac.api.request.UserRegisterRequest
import nmh.prac.application.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/api/users")
class UserController(
    private val userService: UserService
) {

    @PostMapping
    fun register(request: UserRegisterRequest) {
        userService.register(request.name, request.age)
    }
}