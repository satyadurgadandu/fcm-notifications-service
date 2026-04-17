package in.satya.fcmnotification.service;

import java.util.List;

import org.springframework.stereotype.Service;

import in.satya.fcmnotification.entity.User;
import in.satya.fcmnotification.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	 private final UserRepository userRepository;

	    // Create a new user
	    public User createUser(User user) {
	        return userRepository.save(user);
	    }

	    // Get all users
	    public List<User> getAllUsers() {
	        return userRepository.findAll();
	    }

	    // Get one user by ID
	    public User getUserById(Long id) {
	        return userRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
	    }

	    // Get only users who can receive notifications
	    public List<User> getNotifiableUsers() {
	        return userRepository.findAllNotifiableUsers();
	    }

	    // Save or update the FCM token for a user
	    // Call this when your mobile app sends the device token after login
	    public String saveFcmToken(Long userId, String fcmToken) {
	        User user = getUserById(userId);
	        user.setFcmToken(fcmToken);
	        userRepository.save(user);
	      //  log.info("FCM token saved for user: {}", user.getName());
	        return "FCM token saved for " + user.getName();
	    }

	    // Enable or disable notifications for a user
	    // enabled = true  → user will receive notifications
	    // enabled = false → user will be skipped when sending
	    public String toggleNotifications(Long userId, boolean enabled) {
	        User user = getUserById(userId);
	        user.setNotificationEnabled(enabled);
	        userRepository.save(user);
	        String msg = "Notifications " + (enabled ? "ENABLED" : "DISABLED")
	                     + " for " + user.getName();
	        //log.info(msg);
	       
	        return msg;
	    }

	    // Clear an invalid/expired FCM token
	    public void clearFcmToken(User user) {
	        user.setFcmToken(null);
	        userRepository.save(user);
	       // log.warn("Cleared expired FCM token for user: {}", user.getName());
	    }
}
