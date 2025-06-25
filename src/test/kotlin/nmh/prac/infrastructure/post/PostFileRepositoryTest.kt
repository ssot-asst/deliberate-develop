package nmh.prac.infrastructure.post

import nmh.prac.domain.post.PostModel
import nmh.prac.infrastructure.user.UserFileEntity
import org.assertj.core.api.BDDAssertions.then
import org.assertj.core.api.BDDAssertions.tuple
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class PostFileRepositoryTest {

    @Autowired
    lateinit var postFileRepository: PostFileRepository

    @AfterEach
    fun tearDown() {
        postFileRepository.deleteAll()
    }

    @Test
    fun `전체 게시글 목록을 조회한다`() {
        // given
        // when
        postFileRepository.findAll()
        // then
        then(postFileRepository.findAll())
            .isNotNull
            .allSatisfy {
                then(it).isInstanceOf(PostModel::class.java)
            }
    }

    @Test
    fun `게시글을 작성 할 수 있다`() {
        // given
        val post = PostFileEntity(
            id = 1L,
            title = "테스트 게시글",
            content = "테스트 내용",
            author = UserFileEntity(id = 1L, name = "작성자", age = 30)
        )
        // when
        postFileRepository.save(post)
        // then
        then(postFileRepository.findAll())
            .isNotEmpty
            .anySatisfy {
                then(it.title).isEqualTo(post.title)
                then(it.content).isEqualTo(post.content)
                then(it.author.name).isEqualTo(post.author.name)
            }
    }

    @Test
    fun `authorId로 게시글을 조회 할 수 있다`() {
        // given
        val authorId = 1L
        val post1 = PostFileEntity(
            id = 1L,
            title = "연관 조회",
            content = "파일에서는 연관 조회를 어떻게 하는거에요?",
            author = UserFileEntity(id = authorId, name = "작성자", age = 30)
        )
        val post2 = PostFileEntity(
            id = 2L,
            title = "테스트 좋아",
            content = "테스트는 이렇게 짜야돼요",
            author = UserFileEntity(id = authorId, name = "작성자", age = 30)
        )
        postFileRepository.save(post1)
        postFileRepository.save(post2)
        // when
        val postsByAuthor = postFileRepository.findByAuthorId(authorId)
        // then
        then(postsByAuthor).hasSize(2)
            .extracting(PostModel::title, PostModel::content)
            .containsExactlyInAnyOrder(
                tuple(post1.title, post1.content),
                tuple(post2.title, post2.content)
            )
    }

    private fun PostFileRepository.deleteAll() {
        val files = this.files
        if (files.exists()) {
            files.delete()
        }
        files.createNewFile()
        this.objectMapper.writeValue(files, emptyList<PostModel>())
    }
}