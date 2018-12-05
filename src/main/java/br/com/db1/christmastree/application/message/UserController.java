package br.com.db1.christmastree.application.message;

import br.com.db1.christmastree.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("by-name")
    public ResponseEntity getByName(@RequestParam("name") String name) {
        return ResponseEntity.ok(userService.findByName(name));
    }
}
