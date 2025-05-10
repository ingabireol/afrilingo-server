package edtech.afrilingo.notification;

import edtech.afrilingo.notification.dto.NotificationDTO;
import edtech.afrilingo.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service for publishing notification events.
 * Other services can use this to create notifications for users.
 */
@Service
@RequiredArgsConstructor
public class NotificationEventPublisher {

    private final NotificationService notificationService;

    /**
     * Create a notification for a specific user
     * 
     * @param userId The ID of the user to notify
     * @param message The notification message
     * @param type The type of notification
     * @param relatedEntityId Optional ID of the related entity (e.g., course, lesson, quiz)
     * @return The created notification
     */
    public NotificationDTO notifyUser(Long userId, String message, NotificationType type, Long relatedEntityId) {
        return notificationService.createNotification(userId, message, type, relatedEntityId);
    }

    /**
     * Create a course completion notification
     * 
     * @param userId The ID of the user
     * @param courseId The ID of the completed course
     * @param courseName The name of the completed course
     * @return The created notification
     */
    public NotificationDTO notifyCourseCompleted(Long userId, Long courseId, String courseName) {
        String message = "Congratulations! You have completed the course: " + courseName;
        return notificationService.createNotification(userId, message, NotificationType.COURSE_COMPLETED, courseId);
    }

    /**
     * Create a lesson completion notification
     * 
     * @param userId The ID of the user
     * @param lessonId The ID of the completed lesson
     * @param lessonTitle The title of the completed lesson
     * @return The created notification
     */
    public NotificationDTO notifyLessonCompleted(Long userId, Long lessonId, String lessonTitle) {
        String message = "You have completed the lesson: " + lessonTitle;
        return notificationService.createNotification(userId, message, NotificationType.LESSON_COMPLETED, lessonId);
    }

    /**
     * Create a quiz completion notification
     * 
     * @param userId The ID of the user
     * @param quizId The ID of the completed quiz
     * @param quizTitle The title of the completed quiz
     * @param score The score achieved in the quiz
     * @return The created notification
     */
    public NotificationDTO notifyQuizCompleted(Long userId, Long quizId, String quizTitle, double score) {
        String message = "You scored " + score + "% on the quiz: " + quizTitle;
        return notificationService.createNotification(userId, message, NotificationType.QUIZ_COMPLETED, quizId);
    }

    /**
     * Create a new course availability notification
     * 
     * @param userId The ID of the user
     * @param courseId The ID of the new course
     * @param courseName The name of the new course
     * @return The created notification
     */
    public NotificationDTO notifyNewCourseAvailable(Long userId, Long courseId, String courseName) {
        String message = "A new course is now available: " + courseName;
        return notificationService.createNotification(userId, message, NotificationType.NEW_COURSE_AVAILABLE, courseId);
    }

    /**
     * Create an achievement notification
     * 
     * @param userId The ID of the user
     * @param achievementName The name of the achievement
     * @return The created notification
     */
    public NotificationDTO notifyAchievementUnlocked(Long userId, String achievementName) {
        String message = "Achievement unlocked: " + achievementName;
        return notificationService.createNotification(userId, message, NotificationType.ACHIEVEMENT_UNLOCKED, null);
    }

    /**
     * Create a system notification for a user
     * 
     * @param userId The ID of the user
     * @param message The notification message
     * @return The created notification
     */
    public NotificationDTO notifySystem(Long userId, String message) {
        return notificationService.createNotification(userId, message, NotificationType.SYSTEM_NOTIFICATION, null);
    }

    /**
     * Create a reminder notification for a user
     * 
     * @param userId The ID of the user
     * @param message The reminder message
     * @param relatedEntityId Optional ID of the related entity
     * @return The created notification
     */
    public NotificationDTO sendReminder(Long userId, String message, Long relatedEntityId) {
        return notificationService.createNotification(userId, message, NotificationType.REMINDER, relatedEntityId);
    }
} 