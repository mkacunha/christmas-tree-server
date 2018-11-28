package br.com.db1.christmastree.domain.message;

import br.com.db1.christmastree.domain.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
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

	private static final String TEMPLATE_MESSAGE = "<div><p><strong>De: </strong>%s</p><p style=\"text-align: justify\"><strong>Feedback: </strong>%s</p></div><hr style=\"margin-top: 30px;margin-bottom: 30px\" />";

	private static final String URL = "http://192.168.208.164:32568/api/collaborator/getbyname/?collaboratorName=";

	private static final String TEMPLATE_BASE = "<body><div style=\"width: 600px; margin: auto;\"><p><strong>Você recebeu feedbacks de natal :)</strong></p><p style=\"padding-bottom: 30px;\"><strong>Veja o que seus amigos tem a dizer para você:</strong></p> %s <p><strong>Envie seu feedback <a href=\"http://natal.db1.com.br\" target=\"_blank\">aqui</a> e faça o natal de alguém mais feliz.</strong></p><p><strong>Feliz Natal!!!</strong></p></div></body>";

	private static final String TEMPLATE_FINAL = "<body><div style=\"width: 600px; margin: auto;\"><p><strong>Você está recebendo uma retrospectiva de seus feedbacks :)</strong></p><p><strong>Agradecemos a todos pela participação!!!</strong></p><p><strong>Feliz 2018!!!</strong></p></div></body>";

	private static final String MAXIMO_CARACTERES = "Seu feedback deve ter no máximo 5000 caracteres.";

	private static Set<String> keys = new HashSet<>();

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

	public Message save(MessageDTO message, String remoteAddr) {
		checkArgument(nonNull(message.getEmailTo()) && nonNull(message.getNameFrom()) && hasText(message.getText()),
				FIELDS_REQUIRED);
		checkArgument(message.getText().length() <= 5000, MAXIMO_CARACTERES);
		return messageRepository.save(messageTranslator.translatorDTOToMessage(message, remoteAddr));
	}


	@Transactional
	public List<Message> findUnReadMessageLoggedUser(String emailUserLogged) {
		List<Message> messages = messageRepository.findAllByEmailToAndReadFalse(emailUserLogged);
		messages.forEach(message -> {
			message.changeToRead();
			messageRepository.save(message);
		});
		return messages;
	}

	public Page<Message> findAllMessageLoggedUser(Pageable pageable, String emailUserLogged) {
		return messageRepository.findAllByEmailTo(emailUserLogged, pageable);
	}

	public List<Message> findAll() {
		return messageRepository.findAll();
	}

	public List<UserDTO> findAllByName(String name) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<UserDTO>> user = restTemplate
				.exchange(URL + name, HttpMethod.GET, null, new ParameterizedTypeReference<List<UserDTO>>() {
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
		String convertedRfid = String.valueOf(Long.valueOf(rfid));
		System.out.println("------ Convertendo Hash -------- " + convertedRfid);
		return messageRepository.findAllMessageByMailAndNotRead(convertedRfid);
	}

	private void findMessagesHomeOffice() {
		List<Message> dataBaseMessages = messageRepository.findAllMessageByHomeOfficeAndNotRead();
		Map<String, List<Message>> messagesGroupByRfid = dataBaseMessages.stream()
				.collect(Collectors
						.groupingBy(Message::getEmailTo));
		messagesGroupByRfid.forEach((email, messages) ->
				this.sendMessages(messages, email));
	}

	private void retrospectiveMessage() {
		List<Message> dataBaseMessages = messageRepository.findAll();
		Map<String, List<Message>> messagesGroupByRfid = dataBaseMessages.stream()
				.collect(Collectors
						.groupingBy(Message::getEmailTo));
		messagesGroupByRfid.forEach((email, messages) ->
				this.sendRetrospectiveMessages(messages, email));
	}

	@Async
	public void sendMessages(List<Message> messages, String email) {
		if (!isEmpty(messages) && !keys.contains(email)) {
			try {
				keys.add(email);
				MimeMessage mimeMessage = emailSender.createMimeMessage();
				MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
				mimeMessageHelper.setSubject("Feedbacks de Natal :):)");
				mimeMessageHelper.setTo(messages.get(0).getEmailTo());
				StringBuilder builder = new StringBuilder();
				messages.forEach(m -> builder.append(format(TEMPLATE_MESSAGE, m.getNameFrom(), m.getText())));
				mimeMessageHelper.setText(String.format(TEMPLATE_BASE, builder.toString()), true);
				emailSender.send(mimeMessage);
				messages.forEach(Message::changeToRead);
				messageRepository.saveAll(messages);
			}
			catch (MessagingException e) {
				System.out.println(e.getMessage());
			}
			finally {
				keys.remove(email);
			}
		}
	}

	@Async
	public void sendRetrospectiveMessages(List<Message> messages, String email) {
		if (!isEmpty(messages) && !keys.contains(email)) {
			try {
				keys.add(email);
				MimeMessage mimeMessage = emailSender.createMimeMessage();
				MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
				mimeMessageHelper.setSubject("Retrospectiva de feedbacks");
				mimeMessageHelper.setTo(messages.get(0).getEmailTo());
				StringBuilder builder = new StringBuilder();
				messages.forEach(m -> builder.append(format(TEMPLATE_MESSAGE, m.getNameFrom(), m.getText())));
				mimeMessageHelper.setText(String.format(TEMPLATE_FINAL, builder.toString()), true);
				emailSender.send(mimeMessage);
				messages.forEach(Message::changeToRead);
				messageRepository.saveAll(messages);
			}
			catch (MessagingException e) {
				System.out.println(e.getMessage());
			}
			finally {
				keys.remove(email);
			}
		}
	}

	@Scheduled(cron = "0 00 17 * * *")
	public void enviarEmailHomeOffice() {
		findMessagesHomeOffice();
	}

	//FIXME - Metodo que enviara mensagem de retrospectiva
	@Scheduled(cron = "0 00 17 * * *")
	public void enviarEmailRetrospectiva() {
		retrospectiveMessage();
	}

	public long countMessageLoggedUser() {
		//FIXME - Usuario Logado
		UserDTO dto = new UserDTO();
		return messageRepository.countByEmailToAndReadFalse(dto.getEmail());
	}

	public Message updateMessage(long id) {
		Message message = messageRepository.findById(id).orElse(null);
		message.changeToRead();
		return messageRepository.save(message);
	}

	public Message sendEmail(long id) {
		Message message = messageRepository.findById(id).orElse(null);
		sendMessage(message);
		return  message;
	}

	@Async
	public void sendMessage(Message message) {
		try {
			//FIXME - Usuario Logado
			UserDTO dto = new UserDTO();
			MimeMessage mimeMessage = emailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
			mimeMessageHelper.setSubject("Feedbacks de Natal :):)");
			mimeMessageHelper.setTo(dto.getEmail());
			String formatMessage = format(TEMPLATE_MESSAGE, message.getNameFrom(), message.getText());
			mimeMessageHelper.setText(String.format(TEMPLATE_BASE, formatMessage), true);
			emailSender.send(mimeMessage);
		}
		catch (MessagingException e) {
			System.out.println(e.getMessage());
		}
	}
}
