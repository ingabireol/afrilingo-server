package edtech.afrilingo.certification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProctorEventRepository extends JpaRepository<ProctorEvent, Long> {
    
    List<ProctorEvent> findBySessionOrderByTimestampAsc(CertificationSession session);
    
    List<ProctorEvent> findBySessionAndFlaggedTrue(CertificationSession session);
    
    @Query("SELECT pe FROM ProctorEvent pe WHERE pe.session.id = :sessionId AND pe.eventType = :eventType")
    List<ProctorEvent> findBySessionAndEventType(Long sessionId, ProctorEventType eventType);
    
    @Query("SELECT pe FROM ProctorEvent pe WHERE pe.timestamp BETWEEN :start AND :end AND pe.flagged = true")
    List<ProctorEvent> findFlaggedEventsBetween(LocalDateTime start, LocalDateTime end);
}
