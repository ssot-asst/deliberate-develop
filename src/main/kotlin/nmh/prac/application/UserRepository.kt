package nmh.prac.application

import nmh.prac.domain.UserModel

interface UserRepository {
    fun save(user: UserModel): UserModel
}