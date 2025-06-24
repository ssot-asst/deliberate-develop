package nmh.prac.api.response

import nmh.prac.domain.UserModel

data class UserResponse(
    override var id: Long,
    override val name: String,
    override val age: Int
) : UserModel