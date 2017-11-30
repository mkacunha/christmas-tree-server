package br.com.db1.christmastree.domain.message;

import br.com.db1.christmastree.domain.user.UserDTO;

import java.io.Serializable;
import java.util.Date;

import static java.lang.Boolean.*;

public class MessageDTO implements Serializable {

	private static final long serialVersionUID = 120552849520519299L;

	private Long id;

	private UserDTO from;

	private UserDTO to;

	private String text;

	private Date date = new Date();

	private Boolean read = FALSE;

	private Boolean isRemote;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserDTO getFrom() {
		return from;
	}

	public void setFrom(UserDTO from) {
		this.from = from;
	}

	public UserDTO getTo() {
		return to;
	}

	public void setTo(UserDTO to) {
		this.to = to;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Boolean getRead() {
		return read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}

	public Boolean getRemote() {
		return isRemote;
	}

	public void setRemote(Boolean remote) {
		isRemote = remote;
	}
}
