package edtech.afrilingo.question;

import edtech.afrilingo.quiz.Quiz;
import edtech.afrilingo.quiz.QuizRepository;
import edtech.afrilingo.quiz.option.Option;
import edtech.afrilingo.quiz.option.OptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;
    private final OptionRepository optionRepository;

    @Override
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public List<Question> getQuestionsByQuizId(Long quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    @Override
    public List<Question> getQuestionsByQuizIdAndType(Long quizId, QuestionType questionType) {
        return questionRepository.findByQuizIdAndQuestionType(quizId, questionType);
    }

    @Override
    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    @Override
    @Transactional
    public Question createQuestion(Question question) {
        // Validate question data
        if (question.getQuestionText() == null || question.getQuestionText().trim().isEmpty()) {
            throw new IllegalArgumentException("Question text is required");
        }

        if (question.getQuestionType() == null) {
            throw new IllegalArgumentException("Question type is required");
        }

        if (question.getQuiz() == null || question.getQuiz().getId() == null) {
            throw new IllegalArgumentException("Question must be associated with a quiz");
        }

        // Verify quiz exists
        if (!quizRepository.existsById(question.getQuiz().getId())) {
            throw new IllegalArgumentException("Quiz with id " + question.getQuiz().getId() + " not found");
        }

        // Set points if not specified
        if (question.getPoints() <= 0) {
            question.setPoints(1); // Default 1 point per question
        }

        // Validate options based on question type
        validateOptionsForQuestionType(question);

        return questionRepository.save(question);
    }

    @Override
    @Transactional
    public Question updateQuestion(Long id, Question questionDetails) {
        return questionRepository.findById(id)
                .map(existingQuestion -> {
                    // Update fields
                    if (questionDetails.getQuestionText() != null) {
                        existingQuestion.setQuestionText(questionDetails.getQuestionText());
                    }

                    if (questionDetails.getQuestionType() != null) {
                        existingQuestion.setQuestionType(questionDetails.getQuestionType());
                        // Re-validate options if question type changed
                        validateOptionsForQuestionType(existingQuestion);
                    }

                    if (questionDetails.getMediaUrl() != null) {
                        existingQuestion.setMediaUrl(questionDetails.getMediaUrl());
                    }

                    if (questionDetails.getPoints() > 0) {
                        existingQuestion.setPoints(questionDetails.getPoints());
                    }

                    if (questionDetails.getQuiz() != null && questionDetails.getQuiz().getId() != null) {
                        // Verify quiz exists
                        if (!quizRepository.existsById(questionDetails.getQuiz().getId())) {
                            throw new IllegalArgumentException("Quiz with id " +
                                    questionDetails.getQuiz().getId() + " not found");
                        }

                        // Get the quiz from the repository to ensure we have the full entity
                        Quiz quiz = quizRepository.findById(questionDetails.getQuiz().getId())
                                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));

                        existingQuestion.setQuiz(quiz);
                    }

                    // Update certification fields
                    if (questionDetails.getCertificationQuestion() != null) {
                        existingQuestion.setCertificationQuestion(questionDetails.getCertificationQuestion());
                    }

                    if (questionDetails.getCertificationLevel() != null) {
                        existingQuestion.setCertificationLevel(questionDetails.getCertificationLevel());
                    }

                    return questionRepository.save(existingQuestion);
                })
                .orElseThrow(() -> new RuntimeException("Question not found with id " + id));
    }

    @Override
    @Transactional
    public boolean deleteQuestion(Long id) {
        return questionRepository.findById(id)
                .map(question -> {
                    // First delete all options associated with this question
                    optionRepository.deleteByQuestionId(id);

                    // Then delete the question
                    questionRepository.delete(question);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public boolean existsById(Long id) {
        return questionRepository.existsById(id);
    }

    @Override
    public Page<Question> getQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Question addOptionToQuestion(Long questionId, Option option) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id " + questionId));

        // Set the question reference in the option
        option.setQuestion(question);

        // Save the option
        Option savedOption = optionRepository.save(option);

        // Add the option to the question's options list if it's not null
        if (question.getOptions() == null) {
            question.setOptions(new ArrayList<>());
        }
        question.getOptions().add(savedOption);

        // Validate options based on question type
        validateOptionsForQuestionType(question);

        return questionRepository.save(question);
    }

    @Override
    @Transactional
    public Question removeOptionFromQuestion(Long questionId, Long optionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id " + questionId));

        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option not found with id " + optionId));

        // Check if the option belongs to this question
        if (!option.getQuestion().getId().equals(questionId)) {
            throw new IllegalArgumentException("Option does not belong to this question");
        }

        // Remove the option from the question's options list
        if (question.getOptions() != null) {
            question.getOptions().removeIf(o -> o.getId().equals(optionId));
        }

        // Delete the option
        optionRepository.delete(option);

        // Validate remaining options based on question type
        validateOptionsForQuestionType(question);

        return questionRepository.save(question);
    }

    @Override
    public List<Option> getOptionsForQuestion(Long questionId) {
        if (!questionRepository.existsById(questionId)) {
            throw new RuntimeException("Question not found with id " + questionId);
        }

        return optionRepository.findByQuestionIdOrderById(questionId);
    }

    @Override
    public List<Option> getCorrectOptionsForQuestion(Long questionId) {
        if (!questionRepository.existsById(questionId)) {
            throw new RuntimeException("Question not found with id " + questionId);
        }

        return optionRepository.findByQuestionIdAndIsCorrectTrue(questionId);
    }

    @Override
    public int calculateTotalPoints(Long quizId) {
        if (!quizRepository.existsById(quizId)) {
            throw new RuntimeException("Quiz not found with id " + quizId);
        }

        Integer totalPoints = questionRepository.calculateTotalPoints(quizId);
        return totalPoints != null ? totalPoints : 0;
    }

    @Override
    public List<Question> searchQuestionsByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword cannot be empty");
        }

        return questionRepository.findByQuestionTextContaining(keyword);
    }

    // ==================== UPDATED CERTIFICATION METHODS ====================

    /**
     * Get certification questions with multiple fallback strategies
     * Updated to work with the corrected repository methods
     */
    public List<Question> getCertificationQuestions(String languageCode, String testLevel, int count) {
        try {
            log.info("Getting certification questions for language: {} level: {} count: {}",
                    languageCode, testLevel, count);

            List<Question> questions = new ArrayList<>();

            // Strategy 1: Try to get certification questions by quiz title containing language name
            if (languageCode != null) {
                try {
                    String languageName = getLanguageDisplayName(languageCode);
                    questions = questionRepository.findQuestionsByQuizTitleContainingAndLevel(
                            languageName, testLevel, PageRequest.of(0, count));
                    log.info("Found {} questions using quiz title strategy for {}", questions.size(), languageName);
                } catch (Exception e) {
                    log.warn("Quiz title-based query failed: {}", e.getMessage());
                }
            }

            // Strategy 2: If not enough questions, try by certification level only
            if (questions.size() < count) {
                try {
                    List<Question> levelQuestions = questionRepository.findCertificationQuestionsByLevel(
                            testLevel, PageRequest.of(0, count - questions.size()));
                    questions.addAll(levelQuestions);
                    log.info("Added {} questions using certification level strategy", levelQuestions.size());
                } catch (Exception e) {
                    log.warn("Certification level query failed: {}", e.getMessage());
                }
            }

            // Strategy 3: Get any certification questions regardless of level
            if (questions.size() < count) {
                try {
                    List<Question> certQuestions = questionRepository.findAllCertificationQuestions(
                            PageRequest.of(0, count - questions.size()));
                    questions.addAll(certQuestions);
                    log.info("Added {} questions using all certification questions strategy", certQuestions.size());
                } catch (Exception e) {
                    log.warn("All certification questions query failed: {}", e.getMessage());
                }
            }

            // Strategy 4: Fallback to random questions from any quiz
            if (questions.size() < count) {
                try {
                    List<Question> randomQuestions = questionRepository.findRandomQuestions(
                            PageRequest.of(0, count - questions.size()));
                    questions.addAll(randomQuestions);
                    log.info("Added {} questions using random strategy", randomQuestions.size());
                } catch (Exception e) {
                    log.warn("Random questions query failed: {}", e.getMessage());
                }
            }

            // Remove duplicates and limit to requested count
            List<Question> uniqueQuestions = questions.stream()
                    .distinct()
                    .limit(count)
                    .collect(Collectors.toList());

            log.info("Returning {} unique questions for certification", uniqueQuestions.size());
            return uniqueQuestions;

        } catch (Exception e) {
            log.error("Error getting certification questions: {}", e.getMessage(), e);

            // Ultimate fallback - get any questions from database
            try {
                List<Question> fallbackQuestions = questionRepository.findAll()
                        .stream()
                        .limit(count)
                        .collect(Collectors.toList());
                log.info("Using ultimate fallback, returning {} questions", fallbackQuestions.size());
                return fallbackQuestions;
            } catch (Exception fallbackError) {
                log.error("Even fallback failed: {}", fallbackError.getMessage());
                return new ArrayList<>();
            }
        }
    }

    /**
     * Check if answer is correct
     */
    public boolean isAnswerCorrect(Long questionId, Long selectedOptionId) {
        try {
            Optional<Question> questionOpt = questionRepository.findById(questionId);
            if (questionOpt.isEmpty()) {
                log.warn("Question not found: {}", questionId);
                return false;
            }

            Optional<Option> selectedOption = optionRepository.findById(selectedOptionId);
            if (selectedOption.isEmpty()) {
                log.warn("Option not found: {}", selectedOptionId);
                return false;
            }

            boolean isCorrect = selectedOption.get().isCorrect();
            log.debug("Question {} option {} is correct: {}", questionId, selectedOptionId, isCorrect);

            return isCorrect;

        } catch (Exception e) {
            log.error("Error checking answer correctness: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Get correct answer for a question
     */
    public Optional<Option> getCorrectAnswer(Long questionId) {
        try {
            return optionRepository.findByQuestionIdAndCorrectTrue(questionId);
        } catch (Exception e) {
            log.error("Error getting correct answer for question {}: {}", questionId, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Helper method to get language display name from language code
     */
    private String getLanguageDisplayName(String languageCode) {
        if (languageCode == null) return "";

        switch (languageCode.toLowerCase()) {
            case "rw": case "kin": return "Kinyarwanda";
            case "sw": case "swa": return "Swahili";
            case "am": case "amh": return "Amharic";
            case "ha": case "hau": return "Hausa";
            case "yo": case "yor": return "Yoruba";
            case "ig": case "ibo": return "Igbo";
            case "zu": case "zul": return "Zulu";
            case "af": case "afr": return "Afrikaans";
            default: return languageCode.toUpperCase();
        }
    }

    /**
     * Helper method to validate options based on question type
     */
    private void validateOptionsForQuestionType(Question question) {
        // Skip validation if options are not yet set
        if (question.getOptions() == null || question.getOptions().isEmpty()) {
            return;
        }

        int correctOptionsCount = 0;
        for (Option option : question.getOptions()) {
            if (option.isCorrect()) {
                correctOptionsCount++;
            }
        }

        switch (question.getQuestionType()) {
            case MULTIPLE_CHOICE:
                // Multiple choice should have at least 2 options and at least 1 correct option
                if (question.getOptions().size() < 2) {
                    throw new IllegalArgumentException("Multiple choice questions must have at least 2 options");
                }
                if (correctOptionsCount == 0) {
                    throw new IllegalArgumentException("Multiple choice questions must have at least 1 correct option");
                }
                break;

            case TRUE_FALSE:
                // True/False should have exactly 2 options and exactly 1 correct option
                if (question.getOptions().size() != 2) {
                    throw new IllegalArgumentException("True/False questions must have exactly 2 options");
                }
                if (correctOptionsCount != 1) {
                    throw new IllegalArgumentException("True/False questions must have exactly 1 correct option");
                }
                break;

            case FILL_BLANK:
                // Fill in the blank should have at least 1 correct option
                if (correctOptionsCount == 0) {
                    throw new IllegalArgumentException("Fill in the blank questions must have at least 1 correct option");
                }
                break;
        }
    }

    // ==================== NEW CERTIFICATION HELPER METHODS ====================

    /**
     * Get questions from specific quiz IDs (useful for language-specific quizzes)
     */
    public List<Question> getQuestionsFromSpecificQuizzes(List<Long> quizIds, String testLevel, int count) {
        try {
            return questionRepository.findCertificationQuestionsByQuizIdsAndLevel(
                    quizIds, testLevel, PageRequest.of(0, count));
        } catch (Exception e) {
            log.error("Error getting questions from specific quizzes: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Count available certification questions by level
     */
    public long countCertificationQuestionsByLevel(String testLevel) {
        try {
            return questionRepository.countCertificationQuestionsByLevel(testLevel);
        } catch (Exception e) {
            log.error("Error counting certification questions: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * Count all certification questions
     */
    public long countAllCertificationQuestions() {
        try {
            return questionRepository.countAllCertificationQuestions();
        } catch (Exception e) {
            log.error("Error counting all certification questions: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * Mark questions as certification questions (admin function)
     */
    @Transactional
    public void markQuestionsAsCertification(List<Long> questionIds, String certificationLevel) {
        try {
            for (Long questionId : questionIds) {
                Optional<Question> questionOpt = questionRepository.findById(questionId);
                if (questionOpt.isPresent()) {
                    Question question = questionOpt.get();
                    question.setCertificationQuestion(true);
                    question.setCertificationLevel(certificationLevel);
                    questionRepository.save(question);
                }
            }
            log.info("Marked {} questions as certification level {}", questionIds.size(), certificationLevel);
        } catch (Exception e) {
            log.error("Error marking questions as certification: {}", e.getMessage());
            throw new RuntimeException("Failed to mark questions as certification", e);
        }
    }
}