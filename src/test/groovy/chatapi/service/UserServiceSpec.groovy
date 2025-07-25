package chatapi.service

import com.marto.chatapi.dto.CreateUserRequest
import com.marto.chatapi.model.User
import com.marto.chatapi.repository.MessageRepository
import com.marto.chatapi.repository.UserRepository
import com.marto.chatapi.service.UserService
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class UserServiceSpec extends Specification {

    def userRepository = Mock(UserRepository)
    def messageRepository = Mock(MessageRepository)

    @Subject
    UserService userService = new UserService(messageRepository, userRepository)

    def "createUser should throw exception when username already exists"() {
        given: "a create user request with existing username"
        def request = new CreateUserRequest("existinguser", "password123", "USER")

        and: "username already exists"
        userRepository.existsByUsername("existinguser") >> true

        when: "creating the user"
        userService.createUser(request)

        then: "exception is thrown"
        def exception = thrown(IllegalArgumentException)
        exception.message == "User with username 'existinguser' already exists"

        and: "save is never called"
        0 * userRepository.save(_)
    }

    def "deleteUser should delete user and anonymize messages"() {
        given: "an existing user"
        def userId = 1L
        def user = new User("testuser", "password123", User.UserRole.USER)
        user.id = userId

        and: "user exists in repository"
        userRepository.findById(userId) >> Optional.of(user)

        when: "deleting the user"
        userService.deleteUser(userId)

        then: "user is deleted and messages are anonymized"
        1 * messageRepository.updateMessagesToAnonymous(userId)
        1 * userRepository.delete(user)
    }

    def "deleteUser should throw exception when user not found"() {
        given: "a non-existing user id"
        def userId = 999L

        and: "user doesn't exist in repository"
        userRepository.findById(userId) >> Optional.empty()

        when: "deleting the user"
        userService.deleteUser(userId)

        then: "exception is thrown"
        def exception = thrown(IllegalArgumentException)
        exception.message == "User with id '999' not found"

        and: "no repository operations are performed"
        0 * messageRepository.updateMessagesToAnonymous(_)
        0 * userRepository.delete(_)
    }
}