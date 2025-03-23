package edtech.afrilingo.language;

import edtech.afrilingo.course.Course;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "languages")
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String code;
    private String description;
    private String flagImage;
    // Relationships
    @OneToMany(mappedBy = "language", cascade = CascadeType.ALL)
    private List<Course> courses;
}