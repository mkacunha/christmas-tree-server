package br.com.db1.christmastree.domain.message;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.DATE;

@Entity
@Table(name = "message")
public class Message implements Serializable {

    private static final long serialVersionUID = -385021783928927323L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, name = "name_from")
    private String nameFrom;

    @Column(nullable = false, name = "email_from")
    private String emailFrom;

    @Column(nullable = false, name = "email_to")
    private String emailTo;

    @Column(nullable = false, name = "name_to")
    private String nameTo;

    @Column(name = "message_text", nullable = false, length = 1000)
    private String text;

    @Temporal(DATE)
    @Column(nullable = false)
    private Date date = new Date();

    @Column(name = "ip_from", length = 30)
    private String ipFrom;

    @Enumerated(EnumType.STRING)
    @Column(name = "tp_status", length = 30, nullable = false)
    private MessageStatus status = MessageStatus.RECEIVED;

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

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
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

    public String getIpFrom() {
        return ipFrom;
    }

    public void setIpFrom(String ipFrom) {
        this.ipFrom = ipFrom;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public void changeToRead() {
        this.status = MessageStatus.READ;
    }

    public void changeToUnRead() {
        this.status = MessageStatus.UNREAD;
    }
}
