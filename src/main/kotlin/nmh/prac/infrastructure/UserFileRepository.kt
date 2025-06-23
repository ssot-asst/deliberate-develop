package nmh.prac.infrastructure

import nmh.prac.application.UserRepository
import nmh.prac.domain.UserModel

class UserFileRepository : UserRepository {
    override fun save(user: UserModel): UserModel {
        TODO("Not yet implemented")
    }
}