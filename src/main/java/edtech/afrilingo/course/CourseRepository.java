package edtech.afrilingo.course;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByLanguageId(Long languageId);
    List<Course> findByLanguageIdAndIsActiveTrue(Long languageId);
}