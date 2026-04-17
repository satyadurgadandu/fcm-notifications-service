package in.satya.fcmnotification.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.satya.fcmnotification.entity.NotificationLog;

@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long>{
	
	List<NotificationLog> findByUserId(Long userId);

}
