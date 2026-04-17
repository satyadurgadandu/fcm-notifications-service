package in.satya.fcmnotification.entity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	  @Column(nullable = false)
	    private String name;

	    @Column(nullable = false, unique = true)
	    private String email;

	    /*
	     * FCM Token — comes from the mobile app (Android/iOS/Flutter).
	     * Without this, we cannot send a notification to this user's device.
	     * Your mobile app must call POST /api/users/{id}/token to register it.
	     */
	    @Column(name = "fcm_token", columnDefinition = "TEXT")
	    private String fcmToken;

	    /*
	     * Notification flag:
	     *   true  → this user WILL receive notifications
	     *   false → this user will be SKIPPED
	     *
	     * This is how you control "5 users but only 3 get notified":
	     * Set notificationEnabled = false for the 2 users you want to skip.
	     */
	    @Column(name = "notification_enabled")
	    @Builder.Default
	    private boolean notificationEnabled = true;

	
	
}
