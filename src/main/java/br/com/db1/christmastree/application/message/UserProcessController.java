package br.com.db1.christmastree.application.message;

import br.com.db1.christmastree.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserProcessController {

    @Autowired
    private UserService userService;

    @GetMapping("users-process")
    public String process() {
        userService.findAllUsersAdAndSave();
        return "OK";
    }

}
