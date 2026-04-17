package in.satya.fcmnotification.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.satya.fcmnotification.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByEmail(String email);


	 /*
     * Returns users who:
     *  - have notificationEnabled = true
     *  - have an FCM token saved (device registered)
     *
     * Used when you want to notify ALL eligible users.
     */
    @Query("SELECT u FROM User u WHERE u.notificationEnabled = true " +
           "AND u.fcmToken IS NOT NULL AND u.fcmToken <> ''")
    List<User> findAllNotifiableUsers();

    /*
     * Returns only specific users by ID who also have FCM tokens.
     * Used when you want to notify SELECTED users only.
     *
     * Example: userIds = [1, 2, 3]
     * → Users 4 and 5 are not fetched at all.
     */
    @Query("SELECT u FROM User u WHERE u.id IN :ids " +
           "AND u.fcmToken IS NOT NULL AND u.fcmToken <> ''")
    List<User> findByIdInWithToken(@Param("ids") List<Long> ids);
}
