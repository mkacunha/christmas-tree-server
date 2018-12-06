package br.com.db1.christmastree.domain.message;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

@Service
public class MessageService {

    private static final String FIELDS_REQUIRED = "Para enviar seu feedback é necessário informar remetente, destinatário e uma mensagem de feedback.";

    private static final String MAXIMO_CARACTERES = "Seu feedback deve ter no máximo 5000 caracteres.";

    private final MessageRepository messageRepository;

    private final MessageTranslator messageTranslator;

    @Autowired
    public MessageService(MessageRepository messageRepository, MessageTranslator messageTranslator) {
        this.messageRepository = messageRepository;
        this.messageTranslator = messageTranslator;
    }

    public Message save(MessageDTO message, String remoteAddr) {
        checkArgument(nonNull(message.getEmailTo()) && nonNull(message.getNameFrom()) && hasText(message.getText()),
                FIELDS_REQUIRED);
        checkArgument(message.getText().length() <= 5000, MAXIMO_CARACTERES);
        return messageRepository.save(messageTranslator.translatorDTOToMessage(message, remoteAddr));
    }


    @Transactional
    public long countMessageLoggedUser(String emailUserLogged) {
        List<Message> messages = messageRepository.findAllByEmailToAndStatusIn(emailUserLogged, Lists.newArrayList(MessageStatus.RECEIVED));
        messages.forEach(message -> {
            message.changeToUnRead();
            messageRepository.save(message);
        });
        return messages.size();
    }

    public Page<Message> findAllMessageLoggedUser(Pageable pageable, String emailUserLogged) {
        return messageRepository
                .findAllByEmailToAndStatusInOrderByIdDesc(emailUserLogged, Lists.newArrayList(MessageStatus.READ, MessageStatus.UNREAD), pageable);
    }

    @Transactional
    public void changeToRead(Long id) {
        messageRepository.findById(id).ifPresent(message -> {
            message.changeToRead();
            messageRepository.save(message);
        });
    }

    public Long count() {
        return messageRepository.count();
    }

    public Long countToday() {
        return messageRepository.countByDate(new Date());
    }
}
