package nmh.prac.infrastructure.post

import com.fasterxml.jackson.databind.ObjectMapper
import nmh.prac.application.post.PostRepository
import nmh.prac.domain.post.PostModel
import org.springframework.stereotype.Component
import java.io.File

@Component
class PostFileRepository(
    val objectMapper: ObjectMapper
) : PostRepository {

    val files: File = File("posts.json")

    init {
        if (!files.exists()) {
            files.createNewFile()
            objectMapper.writeValue(files, emptyList<PostModel>())
        }
    }

    override fun findAll(): List<PostModel> {
        return objectMapper.readValue(
            files, objectMapper.typeFactory.constructCollectionType(
                List::class.java,
                PostFileEntity::class.java
            )
        )
    }

    override fun save(post: PostModel): PostModel {
        val posts = findAll().toMutableList()
        posts.add(post)
        objectMapper.writeValue(files, posts)
        return post
    }

    override fun findByAuthorId(authorId: Long): List<PostModel> {
        return findAll().filter { it.author.id == authorId }
    }
}