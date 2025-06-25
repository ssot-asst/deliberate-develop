package nmh.prac.api.user.response

import nmh.prac.domain.user.UserModel

data class UserResponse(
    override var id: Long,
    override val name: String,
    override val age: Int
) : UserModel