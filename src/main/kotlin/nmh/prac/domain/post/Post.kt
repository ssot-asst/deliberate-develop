package nmh.prac.domain.post

import nmh.prac.domain.user.User
import nmh.prac.domain.user.UserModel

interface PostModel {
    val id: Long
    val title: String
    val content: String
    val author: UserModel
}

data class Post(
    override val id: Long = 0L,
    override val title: String,
    override val content: String,
    override val author: User
) : PostModel