package no.noroff.hvz.repositories;

import no.noroff.hvz.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
