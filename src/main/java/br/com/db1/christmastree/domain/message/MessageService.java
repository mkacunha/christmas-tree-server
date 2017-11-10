package br.com.db1.christmastree.domain.message;

import br.com.db1.christmastree.UserNotFoundException;
import br.com.db1.christmastree.domain.user.User;
import br.com.db1.christmastree.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static java.lang.String.format;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class MessageService {

	private static final String TEMPLATE_MESSAGE = "De: %s \n Mensagem: %s \n\n";

	private final MessageRepository messageRepository;

	private final UserRepository userRepository;

	private final JavaMailSender emailSender;

	@Autowired
	public MessageService(MessageRepository messageRepository, UserRepository userRepository,
			JavaMailSender emailSender) {
		this.messageRepository = messageRepository;
		this.userRepository = userRepository;
		this.emailSender = emailSender;
	}

	public List<Message> findAll() {
		return messageRepository.findAll();
	}

	public Long count() {
		return messageRepository.count();
	}

	public Long countToday() {
		return messageRepository.countByDate(new Date());
	}

	public List<Message> findMessagesByHash(String hash) {
		final User user = userRepository.findByHash(hash)
										.orElseThrow(
												new UserNotFoundException(format("Usuário %s não encontrado.", hash)));
		return messageRepository.findAllMessageByUserAndNotRead(user);
	}

	@Async
	public void sendMessages(List<Message> messages) {
		if (!isEmpty(messages)) {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setSubject("Banana");
			message.setTo(messages.get(0).getTo().getEmail());
			StringBuilder builder = new StringBuilder();
			messages.forEach(m -> builder.append(format(TEMPLATE_MESSAGE, m.getFrom().getName(), m.getText())));
			message.setText(builder.toString());
			emailSender.send(message);
			messages.forEach(Message::changeToRead);
			messageRepository.save(messages);
		}
	}

	@Scheduled(cron = "0 27 19 * * FRI")
	public void run() {
		System.out.println("Ok");
	}
}
