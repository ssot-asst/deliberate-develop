package nmh.prac.infrastructure.user

import jakarta.persistence.*
import nmh.prac.domain.user.UserModel

@Entity
@Table(name = "users")
class UserJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long = 0,
    override val name: String,
    override val age: Int
) : UserModel