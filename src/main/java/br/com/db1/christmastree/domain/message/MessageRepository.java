package br.com.db1.christmastree.domain.message;

import br.com.db1.christmastree.domain.user.UserDTO;
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
			"WHERE m.rfidTo = :rfid AND m.read = false")
	List<Message> findAllMessageByMailAndNotRead(@Param("rfid") String rfid);

	@Query("SELECT m FROM Message m " +
			"WHERE m.read = false AND m.isRemote = true")
	List<Message> findAllMessageByHomeOfficeAndNotRead();
}
