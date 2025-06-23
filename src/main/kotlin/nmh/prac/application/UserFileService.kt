package nmh.prac.application

import nmh.prac.domain.UserFileEntity
import nmh.prac.domain.UserModel
import org.springframework.stereotype.Component

@Component
class UserFileService(
    private val userRepository: UserRepository
) : UserService {
    override fun register(name: String, age: Int): UserModel {
        return userRepository.save(UserFileEntity(name = name, age = age))
    }

}