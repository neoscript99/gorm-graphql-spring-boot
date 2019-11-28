package neo.script.gorm.general.controller

import neo.script.gorm.general.domain.sys.User
import neo.script.gorm.general.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
@RequestMapping("/api/users")
class UserController {
    @Autowired
    UserService userService

    @PostMapping("/withRoles")
    ResponseEntity<User> saveUserWithRoles(@RequestBody Map param) {
        return ResponseEntity.ok(userService.saveUserWithRoles(param.user, param.roleIds))
    }
}
