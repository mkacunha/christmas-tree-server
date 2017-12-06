package br.com.db1.christmastree.domain.message;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
		if (!StringUtils.isEmpty(dto.getTo().getRfid())) {
			Long rfid = Long.valueOf(dto.getTo().getRfid());
			message.setRfidTo(String.valueOf(rfid));
		}
		return message;
	}

}
