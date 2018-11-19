package br.com.db1.christmastree.application.message;

import br.com.db1.christmastree.domain.user.UserAd;
import br.com.db1.christmastree.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("users")
public class UsersController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity allUsers() {
        userService.findAllUsersAdAndSave();
        List<UserAd> allUsersActive = userService.getAllUsersActive();
        System.out.println(allUsersActive.size());
        return ResponseEntity.ok(allUsersActive);
    }
}
