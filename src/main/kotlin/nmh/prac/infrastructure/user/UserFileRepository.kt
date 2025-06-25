package nmh.prac.infrastructure.user

import com.fasterxml.jackson.databind.ObjectMapper
import nmh.prac.application.user.UserRepository
import nmh.prac.domain.user.UserModel
import org.springframework.stereotype.Component
import java.io.File

@Component
class UserFileRepository(
    val objectMapper: ObjectMapper,
) : UserRepository {

    val files: File = File("users.json")

    init {
        if (!files.exists()) {
            files.createNewFile()
            objectMapper.writeValue(files, emptyList<UserModel>())
        }
    }

    override fun save(user: UserModel): UserModel {
        val users = findAll().toMutableList()
        if (user.id != 0L) {
            val existingUserIndex = users.indexOfFirst { it.id == user.id }
            if (existingUserIndex != -1) {
                users[existingUserIndex] = user
                objectMapper.writeValue(files, users)
                return user
            }
        }
        val nextId = (users.maxOfOrNull { it.id } ?: 0) + 1
        user.id = nextId
        users.add(user)
        objectMapper.writeValue(files, users)
        return user
    }

    override fun findAll(): List<UserModel> {
        return objectMapper.readValue(
            files, objectMapper.typeFactory.constructCollectionType(
                List::class.java,
                UserFileEntity::class.java
            )
        )
    }

    override fun findById(id: Long): UserModel? {
        return findAll().find { it.id == id }
    }

    override fun deleteAll() {
        val users = findAll().toMutableList()
        users.clear()
        objectMapper.writeValue(files, users)
    }

    override fun deleteById(id: Long) {
        val users = findAll().toMutableList()
        val userToDelete = users.find { it.id == id }
        if (userToDelete != null) {
            users.remove(userToDelete)
            objectMapper.writeValue(files, users)
        }
    }
}