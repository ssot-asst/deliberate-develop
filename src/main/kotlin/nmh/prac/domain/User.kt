package nmh.prac.domain

interface UserModel {
    val name: String
    val age: Int
}

data class UserFileEntity(
    override val name: String,
    override val age: Int
) : UserModel