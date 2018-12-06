package br.com.db1.christmastree.domain.message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static javax.persistence.TemporalType.DATE;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAll();

    long countByDate(@Temporal(DATE) Date date);

    List<Message> findAllByEmailToAndStatusIn(String emailTo, List<MessageStatus> status);

    Page<Message> findAllByEmailToAndStatusInOrderByIdDesc(String emailTo, List<MessageStatus> status, Pageable pageable);
}
