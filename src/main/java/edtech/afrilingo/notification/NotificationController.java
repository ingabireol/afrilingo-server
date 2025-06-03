package edtech.afrilingo.notification;

import edtech.afrilingo.dto.ApiResponse;
import edtech.afrilingo.notification.dto.NotificationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import edtech.afrilingo.user.User;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Endpoints for notification management")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "Get current user's notifications", description = "Returns a list of all notifications for the current authenticated user")
    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationDTO>>> getCurrentUserNotifications() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<NotificationDTO> notifications = notificationService.getUserNotifications(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success(notifications));
    }

    @Operation(summary = "Get paginated notifications", description = "Returns a paginated list of notifications for the current user")
    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<Page<NotificationDTO>>> getPaginatedNotifications(Pageable pageable) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<NotificationDTO> notifications = notificationService.getPaginatedUserNotifications(currentUser.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success(notifications));
    }

    @Operation(summary = "Get unread notifications", description = "Returns a list of unread notifications for the current user")
    @GetMapping("/unread")
    public ResponseEntity<ApiResponse<List<NotificationDTO>>> getUnreadNotifications() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<NotificationDTO> notifications = notificationService.getUnreadNotifications(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success(notifications));
    }

    @Operation(summary = "Get unread notification count", description = "Returns the count of unread notifications for the current user")
    @GetMapping("/unread/count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long count = notificationService.getUnreadCount(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    @Operation(summary = "Mark a notification as read", description = "Marks a specific notification as read")
    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<NotificationDTO>> markAsRead(@PathVariable Long id) {
        NotificationDTO notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.success(notification, "Notification marked as read"));
    }

    @Operation(summary = "Mark all notifications as read", description = "Marks all notifications for the current user as read")
    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        notificationService.markAllAsRead(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .status(200)
                .message("All notifications marked as read")
                .build());
    }

    @Operation(summary = "Delete a notification", description = "Deletes a specific notification")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .status(200)
                .message("Notification deleted successfully")
                .build());
    }
} 