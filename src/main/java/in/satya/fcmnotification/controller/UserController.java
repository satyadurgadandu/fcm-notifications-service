package in.satya.fcmnotification.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.satya.fcmnotification.dto.NotificationDto;
import in.satya.fcmnotification.entity.User;
import in.satya.fcmnotification.repo.UserRepository;
import in.satya.fcmnotification.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
	
	  private final UserService userService ;
	  private final UserRepository userRepository;

	    // POST /api/users
	    // Create a user
	    // Body: { "name": "Alice", "email": "alice@gmail.com" }
	    @PostMapping
	    public ResponseEntity<User> createUser(@RequestBody User user) {
	    	
	    	Optional<User> existing = userRepository.findByEmail(user.getEmail());

	        if (existing.isPresent()) {
	            throw new RuntimeException("Email already exists");
	        }

	        return ResponseEntity.ok(userService.createUser(user));
	    }

	    // GET /api/users
	    // Get all users
	    @GetMapping
	    public ResponseEntity<List<User>> getAllUsers() {
	        return ResponseEntity.ok(userService.getAllUsers());
	    }

	    // GET /api/users/1
	    // Get one user by ID
	    @GetMapping("/{id}")
	    public ResponseEntity<User> getUserById(@PathVariable Long id) {
	        return ResponseEntity.ok(userService.getUserById(id));
	    }

	    // GET /api/users/notifiable
	    // Get only users who have notifications enabled + FCM token
	    @GetMapping("/notifiable")
	    public ResponseEntity<List<User>> getNotifiable() {
	        return ResponseEntity.ok(userService.getNotifiableUsers());
	    }

	    // POST /api/users/1/token
	    // Register the device FCM token for a user
	    // Body: { "fcmToken": "device-token-from-firebase" }
	    @PostMapping("/{id}/token")
	    public ResponseEntity<String> registerToken(@PathVariable Long id,
	                                                @RequestBody NotificationDto.RegisterToken req) {
	        return ResponseEntity.ok(userService.saveFcmToken(id, req.getFcmToken()));
	    }

	    // PUT /api/users/1/toggle?enabled=false
	    // Disable notifications for user 1 (they will be skipped)
	    // PUT /api/users/1/toggle?enabled=true
	    // Re-enable notifications for user 1
	    @PutMapping("/{id}/toggle")
	    public ResponseEntity<String> toggle(@PathVariable Long id,
	                                         @RequestParam boolean enabled) {
	        return ResponseEntity.ok(userService.toggleNotifications(id, enabled));
	    }

}
