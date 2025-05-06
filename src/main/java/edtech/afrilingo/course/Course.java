package edtech.afrilingo.course;

import edtech.afrilingo.language.Language;
import edtech.afrilingo.lesson.Lesson;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version = 0L;

    @PrePersist
    public void prePersist() {
        if (version == null) {
            version = 0L;
        }
    }

    @PreUpdate
    public void preUpdate() {
        if (version == null) {
            version = 0L;
        }
    }

    private String title;
    private String description;
    private String level;
    private String image;
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "language_id")
    @JsonBackReference
    private Language language;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Lesson> lessons;
}