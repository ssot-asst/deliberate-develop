package nmh.prac.infrastructure.user

import nmh.prac.application.user.UserRepository
import nmh.prac.domain.user.User
import nmh.prac.domain.user.UserModel
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

interface UserJpaRepository : JpaRepository<UserJpaEntity, Long>

@Primary
@Component
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository
) : UserRepository {
    override fun save(user: UserModel): UserModel {
        val entity = UserJpaEntity(
            id = user.id,
            name = user.name,
            age = user.age
        )
        return userJpaRepository.save(entity).toModel()
    }

    override fun findAll(): List<UserModel> {
        return userJpaRepository.findAll().map { it.toModel() }
    }

    override fun findById(id: Long): UserModel? {
        return userJpaRepository.findByIdOrNull(id)?.toModel()
    }

    override fun deleteAll() {
        userJpaRepository.deleteAll()
    }

    override fun deleteById(id: Long) {
        userJpaRepository.deleteById(id)
    }

    private fun UserJpaEntity.toModel() = User(
        id = this.id,
        name = this.name,
        age = this.age,
    )
}