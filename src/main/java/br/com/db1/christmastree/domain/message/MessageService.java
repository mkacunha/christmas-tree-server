package br.com.db1.christmastree.domain.message;

import br.com.db1.christmastree.UserNotFoundException;
import br.com.db1.christmastree.domain.user.User;
import br.com.db1.christmastree.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;

@Service
public class MessageService {

	public static final String FIELDS_REQUIRED = "Para enviar seu feedback é necessário informar remetente, destinatário e uma mensagem de feedback.";

	private static final String TEMPLATE_MESSAGE = "De: %s \nMensagem: %s \n\n";

	private static Set<String> hashs = new HashSet<>();

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

	public Message save(Message message) {
		checkArgument(nonNull(message.getFrom()) && nonNull(message.getTo()) && hasText(message.getText()),
				FIELDS_REQUIRED);
		return messageRepository.save(message);
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
	public void sendMessages(List<Message> messages, String hash) {
		if (!isEmpty(messages) && !hashs.contains(hash)) {
			try {
				hashs.add(hash);
				SimpleMailMessage message = new SimpleMailMessage();
				message.setSubject("Feedbacks de Natal :):)");
				message.setTo(messages.get(0).getTo().getEmail());
				StringBuilder builder = new StringBuilder();
				messages.forEach(m -> builder.append(format(TEMPLATE_MESSAGE, m.getFrom().getName(), m.getText())));
				message.setText(builder.toString());
				emailSender.send(message);
				messages.forEach(Message::changeToRead);
				messageRepository.save(messages);
			} finally {
				hashs.remove(hash);
			}
		}
	}
}
