package nmh.prac.application

import nmh.prac.domain.UserModel

interface UserService {

    fun register(name: String, age: Int): UserModel

}