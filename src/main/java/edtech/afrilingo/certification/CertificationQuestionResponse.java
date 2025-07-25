package edtech.afrilingo.certification;

import edtech.afrilingo.question.Question;
import edtech.afrilingo.quiz.option.Option;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "certification_responses")
public class CertificationQuestionResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime answeredAt;
    private long timeSpentMs; // Time spent on this question
    private boolean correct;
    
    @ManyToOne
    @JoinColumn(name = "session_id")
    private CertificationSession session;
    
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    
    @ManyToOne
    @JoinColumn(name = "selected_option_id")
    private Option selectedOption;
}
