package br.com.db1.christmastree.application.user;

import br.com.db1.christmastree.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "/users")
@CrossOrigin(origins = "*")
public class UserController {

	private final UserRepository repository;

	@Autowired
	public UserController(UserRepository repository) {
		this.repository = repository;
	}

	@RequestMapping(method = GET)
	public ResponseEntity findAll() {
		return ResponseEntity.ok(repository.findAll());
	}

	@RequestMapping(path = "/contains/{name}", method = GET)
	public ResponseEntity findByContainsName(@PathVariable("name") String name) {
		return ResponseEntity.ok(repository.findByNameContaining(name));
	}

}
