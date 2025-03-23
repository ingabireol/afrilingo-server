package edtech.afrilingo.userProgress;

import edtech.afrilingo.lesson.Lesson;
import edtech.afrilingo.question.Question;
import edtech.afrilingo.quiz.Option;
import edtech.afrilingo.quiz.Quiz;
import edtech.afrilingo.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_progress")
public class UserProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private boolean completed;
    private int score;
    private LocalDateTime completedAt;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
}

