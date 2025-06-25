package nmh.prac.infrastructure.user

import nmh.prac.domain.user.UserModel

data class UserFileEntity(
    override var id: Long = 0L,
    override val name: String,
    override val age: Int
) : UserModel