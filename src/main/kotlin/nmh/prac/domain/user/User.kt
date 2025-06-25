package nmh.prac.domain.user

interface UserModel {
    var id: Long
    val name: String
    val age: Int
}

data class User(
    override var id: Long = 0L,
    override val name: String,
    override val age: Int
) : UserModel