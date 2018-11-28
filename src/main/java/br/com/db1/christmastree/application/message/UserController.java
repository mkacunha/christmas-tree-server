package br.com.db1.christmastree.application.message;

import br.com.db1.christmastree.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("by-name")
    public ResponseEntity getByName(@RequestParam("name") String name) {
        return ResponseEntity.ok(userService.findByName(name));
    }

    @PutMapping("message")
    public ResponseEntity save(HttpServletRequest request, Principal principal) {
//		try {
//			return ResponseEntity.ok(service.save(message, request.getRemoteAddr()));
//		} catch (IllegalArgumentException e) {
//			return ResponseEntity.badRequest().body(e.getMessage());
//		} catch (Exception e) {
//			return ResponseEntity.badRequest().body(ERROR_SAVE_FEEDBACK);
//		}
        return ResponseEntity.ok("ok");
    }
}
