package edtech.afrilingo.question;

import edtech.afrilingo.quiz.option.Option;
import edtech.afrilingo.quiz.Quiz;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionText;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType; // MULTIPLE_CHOICE, TRUE_FALSE, FILL_BLANK

    private String mediaUrl;
    private int points;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    @JsonIgnoreProperties("questions")
    private Quiz quiz;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("question")
    private List<Option> options;
}
