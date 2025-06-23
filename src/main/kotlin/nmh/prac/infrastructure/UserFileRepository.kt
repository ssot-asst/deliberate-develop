package nmh.prac.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import nmh.prac.application.UserRepository
import nmh.prac.domain.UserFileEntity
import nmh.prac.domain.UserModel
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
        val nextId = (users.maxOfOrNull { it.id } ?: 0) + 1
        user.id = nextId
        users.add(user)
        objectMapper.writeValue(files, users)
        return user
    }

    fun findAll(): List<UserModel> {
        return objectMapper.readValue(
            files, objectMapper.typeFactory.constructCollectionType(
                List::class.java,
                UserFileEntity::class.java
            )
        )
    }
}