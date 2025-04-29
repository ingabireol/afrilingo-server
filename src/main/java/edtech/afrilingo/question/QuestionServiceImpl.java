package edtech.afrilingo.question;

import edtech.afrilingo.quiz.Quiz;
import edtech.afrilingo.quiz.QuizRepository;
import edtech.afrilingo.quiz.option.Option;
import edtech.afrilingo.quiz.option.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
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

    /**
     * Helper method to validate options based on question type
     *
     * @param question Question to validate
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
}