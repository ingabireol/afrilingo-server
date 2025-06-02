package edtech.afrilingo.userProgress;

import edtech.afrilingo.dto.ApiResponse;
import edtech.afrilingo.lesson.Lesson;
import edtech.afrilingo.lesson.LessonService;
import edtech.afrilingo.quiz.Quiz;
import edtech.afrilingo.quiz.QuizService;
import edtech.afrilingo.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/progress")
@RequiredArgsConstructor
@Tag(name = "User Progress", description = "Endpoints for tracking user progress")
public class UserProgressController {

    private final UserProgressRepository userProgressRepository;
    private final LessonService lessonService;
    private final QuizService quizService;
    private final UserQuizAttemptRepository userQuizAttemptRepository;

    @Operation(summary = "Get user streak", description = "Returns the current streak for the authenticated user")
    @GetMapping("/streak")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserStreak() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // Get all user progress records
        List<UserProgress> allUserProgress = userProgressRepository.findByUserId(currentUser.getId());
        
        // Get all quiz attempts
        List<UserQuizAttempt> allQuizAttempts = userQuizAttemptRepository.findByUserId(currentUser.getId());
        
        // Calculate streak
        int streak = calculateStreak(allUserProgress, allQuizAttempts);
        
        Map<String, Object> response = new HashMap<>();
        response.put("streak", streak);
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @Operation(summary = "Mark lesson as accessed", description = "Records that a user has accessed a lesson")
    @PostMapping("/lesson/access")
    public ResponseEntity<ApiResponse<UserProgress>> accessLesson(@RequestParam Long lessonId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // Get the lesson
        Optional<Lesson> lessonOpt = lessonService.getLessonById(lessonId);
        if (lessonOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, "Lesson not found"));
        }
        
        Lesson lesson = lessonOpt.get();
        
        // Check if progress record already exists
        Optional<UserProgress> existingProgress = userProgressRepository.findByUserIdAndLessonId(
                currentUser.getId(), lessonId);
        
        UserProgress progress;
        if (existingProgress.isPresent()) {
            progress = existingProgress.get();
        } else {
            // Create new progress record
            progress = UserProgress.builder()
                    .user(currentUser)
                    .lesson(lesson)
                    .completed(false)
                    .score(0)
                    .build();
        }
        
        userProgressRepository.save(progress);
        
        return ResponseEntity.ok(ApiResponse.success(progress));
    }
    
    @Operation(summary = "Mark lesson as completed", description = "Records that a user has completed a lesson")
    @PostMapping("/lesson/complete")
    public ResponseEntity<ApiResponse<UserProgress>> completeLesson(@RequestParam Long lessonId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // Get the lesson
        Optional<Lesson> lessonOpt = lessonService.getLessonById(lessonId);
        if (lessonOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, "Lesson not found"));
        }
        
        Lesson lesson = lessonOpt.get();
        
        // Check if progress record already exists
        Optional<UserProgress> existingProgress = userProgressRepository.findByUserIdAndLessonId(
                currentUser.getId(), lessonId);
        
        UserProgress progress;
        if (existingProgress.isPresent()) {
            progress = existingProgress.get();
        } else {
            // Create new progress record
            progress = UserProgress.builder()
                    .user(currentUser)
                    .lesson(lesson)
                    .build();
        }
        
        // Update progress
        progress.setCompleted(true);
        progress.setCompletedAt(LocalDateTime.now());
        
        userProgressRepository.save(progress);
        
        return ResponseEntity.ok(ApiResponse.success(progress));
    }
    
    @Operation(summary = "Record quiz progress", description = "Records that a user has attempted a quiz")
    @PostMapping("/quiz")
    public ResponseEntity<ApiResponse<UserQuizAttempt>> recordQuizProgress(
            @RequestParam Long quizId,
            @RequestParam int score,
            @RequestParam boolean passed) {
        
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // Get the quiz
        Optional<Quiz> quizOpt = quizService.getQuizById(quizId);
        if (quizOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, "Quiz not found"));
        }
        
        Quiz quiz = quizOpt.get();
        
        // Create quiz attempt record
        UserQuizAttempt attempt = UserQuizAttempt.builder()
                .user(currentUser)
                .quiz(quiz)
                .score(score)
                .passed(passed)
                .attemptedAt(LocalDateTime.now())
                .build();
        
        userQuizAttemptRepository.save(attempt);
        
        return ResponseEntity.ok(ApiResponse.success(attempt));
    }
    
    /**
     * Calculate the user's learning streak (consecutive days with activity)
     * @param allUserProgress All user progress records
     * @param allQuizAttempts All user quiz attempts
     * @return Streak count
     */
    private int calculateStreak(List<UserProgress> allUserProgress, List<UserQuizAttempt> allQuizAttempts) {
        // Implementation similar to UserDashboardService.calculateStreak
        // This is a simplified version for demonstration
        
        if (allUserProgress.isEmpty() && allQuizAttempts.isEmpty()) {
            return 0;
        }
        
        // Count distinct days with activity in the last 30 days
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thirtyDaysAgo = now.minusDays(30);
        
        long distinctDays = allUserProgress.stream()
                .filter(p -> p.getCompletedAt() != null && p.getCompletedAt().isAfter(thirtyDaysAgo))
                .map(p -> p.getCompletedAt().toLocalDate())
                .distinct()
                .count();
        
        distinctDays += allQuizAttempts.stream()
                .filter(a -> a.getAttemptedAt() != null && a.getAttemptedAt().isAfter(thirtyDaysAgo))
                .map(a -> a.getAttemptedAt().toLocalDate())
                .distinct()
                .count();
        
        return (int) Math.min(distinctDays, 30); // Cap at 30 days
    }
}
