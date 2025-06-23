package nmh.prac.application

import nmh.prac.domain.UserModel

interface UserRepository {
    fun save(user: UserModel): UserModel

    fun findAll(): List<UserModel>

    fun findById(id: Long): UserModel?

    fun deleteAll()

    fun deleteById(id: Long)
}