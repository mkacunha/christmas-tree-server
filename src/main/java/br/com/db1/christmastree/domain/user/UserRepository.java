package br.com.db1.christmastree.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.jws.soap.SOAPBinding;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByNameContainingIgnoreCase(String name);

    Optional<User> findByMail(String mail);
}
