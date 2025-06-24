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

    override fun somethingWithException(id: Long, name: String, age: Int) {
        userRepository.findById(id) ?: throw NoSuchElementException("ID가 ${id}인 유저가 없습니다.")
        userRepository.save(UserFileEntity(id = id, name = "김성공", age = 50))
        throw IllegalArgumentException("무언가 잘못되었기 때문에 예외를 발생시킵니다")
        userRepository.save(UserFileEntity(id = id, name = name, age = age))
    }

}