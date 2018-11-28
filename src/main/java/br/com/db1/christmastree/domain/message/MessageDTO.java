package br.com.db1.christmastree.domain.message;

import java.io.Serializable;
import java.util.Date;

import static java.lang.Boolean.FALSE;

public class MessageDTO implements Serializable {

    private static final long serialVersionUID = 120552849520519299L;

    private Long id;

    private String nameFrom;

    private String emailFrom;

    private String nameTo;

    private String emailTo;

    private String text;

    private Date date = new Date();

    private Boolean read = FALSE;


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

    public String getNameTo() {
        return nameTo;
    }

    public void setNameTo(String nameTo) {
        this.nameTo = nameTo;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
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
}
