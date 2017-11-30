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
		message.setEmailFrom(dto.getFrom().getEmail());
		message.setEmailTo(dto.getTo().getEmail());
		message.setNameFrom(dto.getFrom().getEmail());
		message.setNameTo(dto.getTo().getName());
		message.setRfidFrom(dto.getFrom().getRfid());
		message.setRfidTo(dto.getTo().getRfid());
		return message;
	}

}
