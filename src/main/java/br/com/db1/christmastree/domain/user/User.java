package br.com.db1.christmastree.domain.user;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "user")
public class User implements Serializable {

	private static final long serialVersionUID = -2454015343026041664L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String hash;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(unique = true, nullable = false)
	private String email;

	public Long getId() {
		return id;
	}

	public String getHash() {
		return hash;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}
}
