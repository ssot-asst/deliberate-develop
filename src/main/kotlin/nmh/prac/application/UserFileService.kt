package nmh.prac.application

import nmh.prac.domain.UserModel
import org.springframework.stereotype.Component

@Component
class UserFileService : UserService {
    override fun register(name: String, age: Int): UserModel {
        TODO("파일 시스템으로 저장하는 로직 구현")
    }

}