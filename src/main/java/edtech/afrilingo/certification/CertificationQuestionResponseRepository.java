package edtech.afrilingo.certification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificationQuestionResponseRepository extends JpaRepository<CertificationQuestionResponse, Long> {
    
    List<CertificationQuestionResponse> findBySessionOrderByAnsweredAtAsc(CertificationSession session);
    
    @Query("SELECT AVG(r.timeSpentMs) FROM CertificationQuestionResponse r WHERE r.session.id = :sessionId")
    Double getAverageTimeSpentBySession(Long sessionId);
    
    @Query("SELECT COUNT(r) FROM CertificationQuestionResponse r WHERE r.session.id = :sessionId AND r.correct = true")
    Long getCorrectAnswersCount(Long sessionId);
}
