package nmh.prac.api

import nmh.prac.api.request.UserRegisterRequest
import nmh.prac.api.response.UserResponse
import nmh.prac.application.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun register(@RequestBody request: UserRegisterRequest) {
        userService.register(request.name, request.age)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<UserResponse> {
        return ResponseEntity.ok(
            userService.getById(id)
                .let {
                    UserResponse(
                        id = it.id,
                        name = it.name,
                        age = it.age
                    )
                })
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<UserResponse>> {
        return ResponseEntity.ok(
            userService.getAll()
                .map {
                    UserResponse(
                        id = it.id,
                        name = it.name,
                        age = it.age
                    )
                })
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long) {
        userService.deleteById(id)
    }
}