package edtech.afrilingo.lesson;

import edtech.afrilingo.course.Course;
import edtech.afrilingo.lesson.content.LessonContent;
import edtech.afrilingo.quiz.Quiz;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private LessonType type; // AUDIO, READING, IMAGE_OBJECT

    private int orderIndex;
    private boolean isRequired;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonIgnoreProperties("lessons")
    private Course course;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("lesson")
    private List<LessonContent> contents;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("lesson")
    private List<Quiz> quizzes;
}
