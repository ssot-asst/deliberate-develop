package nmh.prac.domain

interface UserModel {
    var id: Long
    val name: String
    val age: Int
}

data class UserFileEntity(
    override var id: Long = 0L,
    override val name: String,
    override val age: Int
) : UserModel