package edtech.afrilingo.quiz.option;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long> {
    /**
     * Find options by question ID
     * @param questionId Question ID
     * @return List of options
     */
    List<Option> findByQuestionId(Long questionId);
    
    /**
     * Find correct options for a question
     * @param questionId Question ID
     * @return List of correct options
     */
    List<Option> findByQuestionIdAndIsCorrectTrue(Long questionId);
    
    /**
     * Find options by question ID ordered by ID
     * @param questionId Question ID
     * @return List of options ordered by ID
     */
    List<Option> findByQuestionIdOrderById(Long questionId);
    
    /**
     * Count options by question ID
     * @param questionId Question ID
     * @return Count of options
     */
    int countByQuestionId(Long questionId);
    
    /**
     * Count correct options by question ID
     * @param questionId Question ID
     * @return Count of correct options
     */
    int countByQuestionIdAndIsCorrectTrue(Long questionId);
    
    /**
     * Delete all options for a question
     * @param questionId Question ID
     */
    void deleteByQuestionId(Long questionId);
    
    /**
     * Find most selected option for a question based on user answers
     * @param questionId Question ID
     * @return Option ID and count
     */
    @Query("SELECT ua.option.id, COUNT(ua) FROM UserAnswer ua WHERE ua.question.id = :questionId GROUP BY ua.option.id ORDER BY COUNT(ua) DESC")
    List<Object[]> findMostSelectedOption(@Param("questionId") Long questionId);
}