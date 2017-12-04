package br.com.db1.christmastree.domain.message;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static java.lang.Boolean.*;
import static java.lang.Boolean.FALSE;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.DATE;

@Entity
@Table(name = "message")
public class Message implements Serializable {

	private static final long serialVersionUID = -385021783928927323L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String  nameFrom;

	@Column(nullable = false)
	private String  emailTo;

	@Column(nullable = false)
	private String  rfidTo;

	@Column(nullable = false)
	private String  nameTo;

	@Column(name = "message_text", nullable = false, length = 1000)
	private String text;

	@Temporal(DATE)
	@Column(nullable = false)
	private Date date = new Date();

	@Column(name = "message_read", nullable = false)
	private Boolean read = FALSE;

	@Column(nullable = false)
	private Boolean isRemote = FALSE;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNameFrom() {
		return nameFrom;
	}

	public void setNameFrom(String nameFrom) {
		this.nameFrom = nameFrom;
	}

	public String getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public String getRfidTo() {
		return rfidTo;
	}

	public void setRfidTo(String rfidTo) {
		this.rfidTo = rfidTo;
	}

	public String getNameTo() {
		return nameTo;
	}

	public void setNameTo(String nameTo) {
		this.nameTo = nameTo;
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

	public void changeToRead() {
		read = TRUE;
	}
}
