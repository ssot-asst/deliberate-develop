package nmh.prac.application.post

import nmh.prac.domain.post.PostModel

interface PostRepository {
    fun findAll(): List<PostModel>
    fun save(post: PostModel): PostModel
    fun findByAuthorId(authorId: Long): List<PostModel>
}