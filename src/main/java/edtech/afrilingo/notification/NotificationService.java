package edtech.afrilingo.notification;

import edtech.afrilingo.exception.ResourceNotFoundException;
import edtech.afrilingo.notification.dto.NotificationDTO;
import edtech.afrilingo.user.User;
import edtech.afrilingo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationDTO createNotification(Long userId, String message, NotificationType type, Long relatedEntityId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Notification notification = Notification.builder()
                .user(user)
                .message(message)
                .timestamp(LocalDateTime.now())
                .read(false)
                .type(type)
                .relatedEntityId(relatedEntityId)
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        NotificationDTO notificationDTO = mapToDTO(savedNotification);
        
        // Send real-time notification via WebSocket
        messagingTemplate.convertAndSendToUser(
                user.getUsername(),
                "/queue/notifications",
                notificationDTO
        );
        
        return notificationDTO;
    }

    public List<NotificationDTO> getUserNotifications(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        return notificationRepository.findByUserIdOrderByTimestampDesc(userId, Pageable.unpaged())
                .getContent()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Page<NotificationDTO> getPaginatedUserNotifications(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        return notificationRepository.findByUserIdOrderByTimestampDesc(userId, pageable)
                .map(this::mapToDTO);
    }

    public List<NotificationDTO> getUnreadNotifications(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        return notificationRepository.findByUserIdAndReadOrderByTimestampDesc(userId, false)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public long getUnreadCount(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        return notificationRepository.countUnreadNotifications(userId);
    }

    @Transactional
    public NotificationDTO markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", notificationId));
        
        notification.setRead(true);
        return mapToDTO(notificationRepository.save(notification));
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndReadOrderByTimestampDesc(userId, false);
        unreadNotifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(unreadNotifications);
    }

    @Transactional
    public void deleteNotification(Long notificationId) {
        if (!notificationRepository.existsById(notificationId)) {
            throw new ResourceNotFoundException("Notification", "id", notificationId);
        }
        notificationRepository.deleteById(notificationId);
    }

    private NotificationDTO mapToDTO(Notification notification) {
        return NotificationDTO.builder()
                .id(notification.getId())
                .userId(notification.getUser().getId())
                .message(notification.getMessage())
                .timestamp(notification.getTimestamp())
                .read(notification.isRead())
                .type(notification.getType())
                .relatedEntityId(notification.getRelatedEntityId())
                .build();
    }
} 