package br.com.db1.christmastree.application.message;

import br.com.db1.christmastree.domain.message.Message;
import br.com.db1.christmastree.domain.message.MessageDTO;
import br.com.db1.christmastree.domain.message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "/messages")
@CrossOrigin(origins = "*")
public class MessageController {

	public static final String ERROR_SAVE_FEEDBACK = "Não foi possível enviar seu Feedback, entre em contato com CBPGP.";

	private final MessageService service;

	@Autowired
	public MessageController(MessageService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity save(@RequestBody MessageDTO message) {
		try {
			return ResponseEntity.ok(service.save(message));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(ERROR_SAVE_FEEDBACK);
		}
	}

	@RequestMapping(value = "/by-name", method = GET)
	public ResponseEntity findAllByname(@RequestParam("name") String name) {
		return ResponseEntity.ok(service.findAllByName(name));
	}

	@RequestMapping(method = GET)
	public ResponseEntity findAll() {
		return ResponseEntity.ok(service.findAll());
	}


	@RequestMapping(value = "/count", method = GET)
	public ResponseEntity count() {
		return ResponseEntity.ok(service.count());
	}

	@RequestMapping(value = "/count-today", method = GET)
	public ResponseEntity countToday() {
		return ResponseEntity.ok(service.countToday());
	}

	@RequestMapping(value = "/read/{hash}", method = GET)
	public ResponseEntity readMessages(@PathVariable("hash") String hash) {
		try {
			final List<Message> messages = service.findMessagesByRfid(hash);
			service.sendMessages(messages, hash);
			return ResponseEntity.ok(messages.size());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
