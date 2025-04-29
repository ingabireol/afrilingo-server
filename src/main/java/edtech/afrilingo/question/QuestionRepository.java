package edtech.afrilingo.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    /**
     * Find questions by quiz ID
     * @param quizId Quiz ID
     * @return List of questions
     */
    List<Question> findByQuizId(Long quizId);
    
    /**
     * Find questions by quiz ID and question type
     * @param quizId Quiz ID
     * @param questionType Question type
     * @return List of questions
     */
    List<Question> findByQuizIdAndQuestionType(Long quizId, QuestionType questionType);
    
    /**
     * Find questions by media URL containing keyword
     * @param keyword Keyword to search for
     * @return List of questions
     */
    List<Question> findByMediaUrlContaining(String keyword);
    
    /**
     * Find questions by quiz ID ordered by points (descending)
     * @param quizId Quiz ID
     * @return List of questions ordered by points
     */
    List<Question> findByQuizIdOrderByPointsDesc(Long quizId);
    
    /**
     * Count questions by quiz ID
     * @param quizId Quiz ID
     * @return Count of questions
     */
    int countByQuizId(Long quizId);
    
    /**
     * Calculate total points for a quiz
     * @param quizId Quiz ID
     * @return Total points
     */
    @Query("SELECT SUM(q.points) FROM Question q WHERE q.quiz.id = :quizId")
    Integer calculateTotalPoints(@Param("quizId") Long quizId);
    
    /**
     * Find questions containing a keyword in questionText
     * @param keyword Keyword to search for
     * @return List of questions
     */
    @Query("SELECT q FROM Question q WHERE q.questionText LIKE %:keyword%")
    List<Question> findByQuestionTextContaining(@Param("keyword") String keyword);
}