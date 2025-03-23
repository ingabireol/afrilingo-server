package edtech.afrilingo.lesson;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourseId(Long courseId);
    List<Lesson> findByCourseIdOrderByOrderIndex(Long courseId);
    List<Lesson> findByType(LessonType type);
    List<Lesson> findByCourseIdAndOrderIndexGreaterThan(Long courseId, Integer orderIndex);
    int countByCourseId(Long courseId);
}