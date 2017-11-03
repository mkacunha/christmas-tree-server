package br.com.db1.christmastree.domain.message;

import br.com.db1.christmastree.domain.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static javax.persistence.TemporalType.DATE;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

	List<Message> findAll();

	long countByDate(@Temporal(DATE) Date date);

	@Query("SELECT m FROM Message m " +
			"WHERE m.to = :user AND m.read = false")
	List<Message> findAllMessageByUserAndNotRead(@Param("user") User user);

//	@Query("SELECT m FROM Message m " +
//			"INNER JOIN m.to u " +
//			"WHERE u.hash = :hash AND m.read = false")
//	List<Message> findAllMessageByUserAndNotRead(@Param("hash") User hash);
}
