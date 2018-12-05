package br.com.db1.christmastree.domain.user;

import org.springframework.util.StringUtils;

public class UserAd {

    private String name;

    private String mail;

    private boolean grupoAtivo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public boolean isGrupoAtivo() {
        return grupoAtivo;
    }

    public void setGrupoAtivo(boolean grupoAtivo) {
        this.grupoAtivo = grupoAtivo;
    }

    public boolean isValid() {
        return isGrupoAtivo() && StringUtils.hasText(mail);
    }

    @Override
    public String toString() {
        return "UserAd{" +
                "name='" + name + '\'' +
                ", mail='" + mail + '\'' +
                ", grupoAtivo=" + grupoAtivo +
                '}';
    }
}
