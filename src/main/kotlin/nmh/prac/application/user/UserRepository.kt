package nmh.prac.application.user

import nmh.prac.domain.user.UserModel

interface UserRepository {
    fun save(user: UserModel): UserModel

    fun findAll(): List<UserModel>

    fun findById(id: Long): UserModel?

    fun deleteAll()

    fun deleteById(id: Long)
}