package br.com.db1.christmastree.domain.user;

import java.io.Serializable;

public class UserDTO implements Serializable {

	private static final long serialVersionUID = -2454015343026041664L;

	private Long id;

	private String rfid;

	private String name;

	private String email;

	private boolean isRemote;

	public Long getId() {
		return id;
	}

	public String getRfid() {
		return rfid;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public boolean isRemote() {
		return isRemote;
	}
}
