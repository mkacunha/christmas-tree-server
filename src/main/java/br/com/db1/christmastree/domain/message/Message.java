package br.com.db1.christmastree.domain.message;

import br.com.db1.christmastree.domain.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.DATE;

@Entity
@Table(name = "message")
public class Message implements Serializable {

	private static final long serialVersionUID = -385021783928927323L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_from_fk", nullable = false)
	private User from;

	@ManyToOne
	@JoinColumn(name = "user_to_fk", nullable = false)
	private User to;

	@Column(name = "message_text", nullable = false, length = 1000)
	private String text;

	@Temporal(DATE)
	@Column(nullable = false)
	private Date date = new Date();

	@Column(name = "message_read", nullable = false)
	private Boolean read = FALSE;

	@Column(name = "home_office", nullable = false)
	private Boolean homeHoffice;

	public Long getId() {
		return id;
	}

	public User getFrom() {
		return from;
	}

	public User getTo() {
		return to;
	}

	public String getText() {
		return text;
	}

	public Date getDate() {
		return date;
	}

	public Boolean getRead() {
		return read;
	}

	public void changeToRead() {
		read = TRUE;
	}

	public Boolean getHomeHoffice() {
		return homeHoffice;
	}
}
