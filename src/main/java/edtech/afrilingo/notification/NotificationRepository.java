package edtech.afrilingo.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findByUserId(Long userId);
    
    Page<Notification> findByUserIdOrderByTimestampDesc(Long userId, Pageable pageable);
    
    List<Notification> findByUserIdAndReadOrderByTimestampDesc(Long userId, boolean read);
    
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user.id = ?1 AND n.read = false")
    long countUnreadNotifications(Long userId);
} 