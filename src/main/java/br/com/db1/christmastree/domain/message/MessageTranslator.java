package br.com.db1.christmastree.domain.message;

import org.springframework.stereotype.Component;

@Component
public class MessageTranslator {

    public Message translatorDTOToMessage(MessageDTO dto, String remoteAddr) {
        Message message = new Message();
        message.setDate(dto.getDate());
        message.setRead(dto.getRead());
        message.setText(dto.getText());
        message.setEmailTo(dto.getEmailTo());
        message.setNameTo(dto.getNameTo());
        message.setEmailFrom(dto.getEmailFrom());
        message.setNameFrom(dto.getNameFrom());
        message.setIpFrom(remoteAddr);
        return message;
    }

}
