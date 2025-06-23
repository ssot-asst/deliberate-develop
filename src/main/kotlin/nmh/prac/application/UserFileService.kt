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

    override fun getAll(): List<UserModel> {
        return userRepository.findAll()
    }

    override fun getById(id: Long): UserModel {
        return userRepository.findById(id) ?: throw NoSuchElementException("ID가 ${id}인 유저가 없습니다.")
    }

    override fun deleteById(id: Long) {
        userRepository.deleteById(id)
    }

    override fun updateById(id: Long, name: String, age: Int): UserModel {
        val user = userRepository.findById(id) ?: throw NoSuchElementException("ID가 ${id}인 유저가 없습니다.")
        val updatedUser = UserFileEntity(id = id, name = name, age = age)
        return userRepository.save(updatedUser)
    }

}