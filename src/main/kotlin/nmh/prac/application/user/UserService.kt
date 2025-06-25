package nmh.prac.application.user

import nmh.prac.domain.user.UserModel

interface UserService {

    fun register(name: String, age: Int): UserModel

    fun getAll(): List<UserModel>

    fun getById(id: Long): UserModel

    fun deleteById(id: Long)

    fun updateById(id: Long, name: String, age: Int): UserModel

    fun somethingWithException(id: Long, string: String, i: Int)
}