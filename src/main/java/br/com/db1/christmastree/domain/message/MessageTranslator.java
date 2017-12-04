package br.com.db1.christmastree.domain.message;

import org.springframework.stereotype.Component;

@Component
public class MessageTranslator {

	public Message translatorDTOToMessage(MessageDTO dto) {
		Message message = new Message();
		message.setDate(dto.getDate());
		message.setRead(dto.getRead());
		message.setText(dto.getText());
		message.setRemote(dto.getTo().isRemote());
		message.setEmailTo(dto.getTo().getEmail());
		message.setNameFrom(dto.getNameFrom());
		message.setNameTo(dto.getTo().getName());
		message.setRfidTo(dto.getTo().getRfid());
		return message;
	}

}
