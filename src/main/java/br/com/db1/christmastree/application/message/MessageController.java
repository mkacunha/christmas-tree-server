package br.com.db1.christmastree.application.message;

import br.com.db1.christmastree.domain.message.MessageDTO;
import br.com.db1.christmastree.domain.message.MessageService;
import br.com.db1.christmastree.domain.user.User;
import br.com.db1.christmastree.domain.user.UserService;
import br.com.db1.christmastree.infra.UserLogged;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class MessageController {

    public static final String ERROR_SAVE_FEEDBACK = "Não foi possível enviar seu Feedback, entre em contato com CBPGP.";

    private final MessageService service;

    private final UserService userService;

    @Autowired
    public MessageController(MessageService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @PostMapping("/api/messages")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity save(@RequestBody MessageDTO message, HttpServletRequest request, PreAuthenticatedAuthenticationToken authToken) {
        try {
            UserLogged userLogged = UserLogged.of(authToken);
            User userByMail = userService.findByMail(message.getEmailTo());
            message.setNameFrom(userLogged.getName());
            message.setEmailFrom(userLogged.getEmail());
            message.setNameTo(userByMail.getName());
            return ResponseEntity.ok(service.save(message, request.getRemoteAddr()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_SAVE_FEEDBACK);
        }

    }

    @PostMapping("/api/messages/read")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity changeToRead(@RequestBody MessageDTO message) {
        if (message.getId() == null) {
            return ResponseEntity.badRequest().body("ID da mensagem deve ser informado");
        }
        service.changeToRead(message.getId());
        return ResponseEntity.ok("success");
    }

    @RequestMapping(value = "/api/messages/unread/count/me", method = GET)
    public ResponseEntity countUnreadMessage(PreAuthenticatedAuthenticationToken authToken) {
        UserLogged userLogged = UserLogged.of(authToken);
        return ResponseEntity.ok(service.countMessageLoggedUser(userLogged.getEmail()));
    }

    @RequestMapping(value = "/api/messages/all/me", method = GET)
    public ResponseEntity allMe(Pageable pageable, PreAuthenticatedAuthenticationToken authToken) {
        UserLogged userLogged = UserLogged.of(authToken);
        return ResponseEntity.ok(service.findAllMessageLoggedUser(pageable, userLogged.getEmail()));
    }

    @RequestMapping(value = "/count", method = GET)
    public ResponseEntity count() {
        return ResponseEntity.ok(service.count());
    }

    @RequestMapping(value = "/count-today", method = GET)
    public ResponseEntity countToday() {
        return ResponseEntity.ok(service.countToday());
    }
}
