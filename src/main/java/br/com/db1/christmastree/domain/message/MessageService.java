package br.com.db1.christmastree.domain.message;

import br.com.db1.christmastree.domain.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;

@Service
public class MessageService {

	private static final String FIELDS_REQUIRED = "Para enviar seu feedback é necessário informar remetente, destinatário e uma mensagem de feedback.";

	private static final String TEMPLATE_MESSAGE = "De: %s \nMensagem: %s \n\n";

	private static final String URL = "http://192.168.208.164:32568/api/collaborator/getbyname/?collaboratorName=";

	private static Set<String> rfids = new HashSet<>();

	private final MessageRepository messageRepository;

	private final JavaMailSender emailSender;

	private final MessageTranslator messageTranslator;

	@Autowired
	public MessageService(MessageRepository messageRepository,
			JavaMailSender emailSender, MessageTranslator messageTranslator) {
		this.messageRepository = messageRepository;
		this.emailSender = emailSender;
		this.messageTranslator = messageTranslator;
	}

	public Message save(MessageDTO message) {
		checkArgument(nonNull(message.getTo()) && nonNull(message.getFrom()) && hasText(message.getText()),
				FIELDS_REQUIRED);
		return messageRepository.save(messageTranslator.translatorDTOToMessage(message));
	}

	public List<Message> findAll() {
		return messageRepository.findAll();
	}

	public List<UserDTO> findAllByName(String name) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<UserDTO>> user = restTemplate
				.exchange(URL + name, HttpMethod.GET, null,  new ParameterizedTypeReference<List<UserDTO>>() {
				});
		return user.getBody();
	}

	public Long count() {
		return messageRepository.count();
	}

	public Long countToday() {
		return messageRepository.countByDate(new Date());
	}

	public List<Message> findMessagesByRfid(String rfid) {
		return messageRepository.findAllMessageByMailAndNotRead(rfid);
	}

	private void findMessagesHomeOffice() {
		List<Message> dataBaseMessages = messageRepository.findAllMessageByHomeOfficeAndNotRead();
		Map<String, List<Message>> messagesGroupByRfid = dataBaseMessages.stream()
				.collect(Collectors.groupingBy(Message::getRfidTo));
		messagesGroupByRfid.forEach((hash, messages) ->
				this.sendMessages(messages, hash));
	}

	@Async
	public void sendMessages(List<Message> messages, String hash) {
		if (!isEmpty(messages) && !rfids.contains(hash)) {
			try {
				rfids.add(hash);
				SimpleMailMessage message = new SimpleMailMessage();
				message.setSubject("Feedbacks de Natal :):)");
				message.setTo(messages.get(0).getEmailTo());
				StringBuilder builder = new StringBuilder();
				messages.forEach(m -> builder.append(format(TEMPLATE_MESSAGE, m.getNameFrom(), m.getText())));
				message.setText(builder.toString());
				emailSender.send(message);
				messages.forEach(Message::changeToRead);
				messageRepository.save(messages);
			} finally {
				rfids.remove(hash);
			}
		}
	}

	@Scheduled(cron = "0 00 17 * * FRI")
	public void enviarEmailHomeOffice() {
		findMessagesHomeOffice();
	}
}
