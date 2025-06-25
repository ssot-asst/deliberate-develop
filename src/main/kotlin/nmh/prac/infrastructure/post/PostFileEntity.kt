package nmh.prac.infrastructure.post

import nmh.prac.domain.post.PostModel
import nmh.prac.infrastructure.user.UserFileEntity

data class PostFileEntity(
    override val id: Long = 0L,
    override val title: String,
    override val content: String,
    override val author: UserFileEntity
) : PostModel {
}