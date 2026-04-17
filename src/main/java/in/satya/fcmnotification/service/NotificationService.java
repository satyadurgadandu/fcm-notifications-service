package in.satya.fcmnotification.service;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;

import in.satya.fcmnotification.dto.NotificationDto;
import in.satya.fcmnotification.entity.NotificationLog;
import in.satya.fcmnotification.entity.User;
import in.satya.fcmnotification.repo.NotificationLogRepository;
import in.satya.fcmnotification.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserRepository userRepository;
    private final NotificationLogRepository logRepository;
    private final UserService userService;

    // ─────────────────────────────────────────────────────────
    //  OPTION 1: Send to ALL users with notificationEnabled=true
    //
    //  If you have 5 users and set 2 of them to
    //  notificationEnabled = false, only 3 will get the message.
    // ─────────────────────────────────────────────────────────
    public NotificationDto.Result sendToAll(NotificationDto.SendToAll req) {
        log.info("Sending to ALL enabled users → title: {}", req.getTitle());

        List<User> users = userRepository.findAllNotifiableUsers();

        if (users.isEmpty()) {
            return result(0, 0, 0, 0, List.of(), List.of(), "No notifiable users found");
        }

        return sendFirebaseMessages(users, req.getTitle(), req.getBody(), req.getData(), users.size());
    }

    // ─────────────────────────────────────────────────────────
    //  OPTION 2: Send to SELECTED users only  ← YOUR REQUIREMENT
    //
    //  You pass: userIds = [1, 2, 3]
    //  Users 4 and 5 are completely ignored.
    // ─────────────────────────────────────────────────────────
    public NotificationDto.Result sendToSelected(NotificationDto.SendToSelected req) {
        log.info("Sending to selected users: {} → title: {}", req.getUserIds(), req.getTitle());

        List<User> users = userRepository.findByIdInWithToken(req.getUserIds());
        int requested = req.getUserIds().size();

        return sendFirebaseMessages(users, req.getTitle(), req.getBody(), req.getData(), requested);
    }

    // ─────────────────────────────────────────────────────────
    //  OPTION 3: Send to ONE user by their ID
    // ─────────────────────────────────────────────────────────
    public NotificationDto.Result sendToOne(Long userId, NotificationDto.SendToOne req) {
        log.info("Sending to user id={} → title: {}", userId, req.getTitle());

        User user = userService.getUserById(userId);

        if (!user.isNotificationEnabled()) {
            return result(1, 0, 0, 1, List.of(), List.of(), "User has notifications disabled");
        }
        if (user.getFcmToken() == null || user.getFcmToken().isBlank()) {
            return result(1, 0, 1, 0, List.of(), List.of(user.getName()), "User has no FCM token");
        }

        return sendFirebaseMessages(List.of(user), req.getTitle(), req.getBody(), req.getData(), 1);
    }

    // ─────────────────────────────────────────────────────────
    //  INTERNAL: Loops through users and sends each notification
    // ─────────────────────────────────────────────────────────
    private NotificationDto.Result sendFirebaseMessages(List<User> users,
            String title, String body, Map<String, String> data, int totalRequested) {

        List<String> successUsers = new ArrayList<>();
        List<String> failedUsers  = new ArrayList<>();

        for (User user : users) {
            try {
                // Build the Firebase message for this user's device token
                Message.Builder builder = Message.builder()
                        .setToken(user.getFcmToken())
                        .setNotification(Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build())
                        .setAndroidConfig(AndroidConfig.builder()
                                .setPriority(AndroidConfig.Priority.HIGH)
                                .setNotification(AndroidNotification.builder()
                                        .setSound("default")
                                        .build())
                                .build())
                        .setApnsConfig(ApnsConfig.builder()
                                .setAps(Aps.builder()
                                        .setSound("default")
                                        .setBadge(1)
                                        .build())
                                .build());

                // Add optional extra data payload
                if (data != null && !data.isEmpty()) {
                    builder.putAllData(data);
                }

                // 🚀 Send via Firebase
                String messageId = FirebaseMessaging.getInstance().send(builder.build());

                log.info("✅ Sent to {} | messageId: {}", user.getName(), messageId);
                successUsers.add(user.getName());
                saveLog(user, title, body, "SUCCESS", messageId, null);

            } catch (FirebaseMessagingException e) {
                log.error("❌ Failed for {}: {}", user.getName(), e.getMessage());
                failedUsers.add(user.getName());
                saveLog(user, title, body, "FAILED", null, e.getMessage());

             // Auto-remove expired token
                if (e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
                    userService.clearFcmToken(user);
                }

            }
        }

        int skipped = totalRequested - successUsers.size() - failedUsers.size();
        String msg = "Success: " + successUsers.size()
                   + " | Failed: " + failedUsers.size()
                   + " | Skipped: " + skipped
                   + " | Total: " + totalRequested;

        log.info(msg);
        return result(totalRequested, successUsers.size(), failedUsers.size(),
                skipped, successUsers, failedUsers, msg);
    }

    // Save a record of every notification attempt
    private void saveLog(User user, String title, String body,
                         String status, String msgId, String error) {
        logRepository.save(NotificationLog.builder()
                .user(user)
                .title(title)
                .body(body)
                .status(status)
                .firebaseMessageId(msgId)
                .errorMessage(error)
                .sentAt(LocalDateTime.now())
                .build());
    }

    // Convenience builder for the result DTO
    private NotificationDto.Result result(int total, int success, int failed,
            int skipped, List<String> ok, List<String> ko, String msg) {
        return new NotificationDto.Result(total, success, failed, skipped, ok, ko, msg);
    }

    // Get logs from DB
    public List<NotificationLog> getLogs() {
        return logRepository.findAll();
    }

    public List<NotificationLog> getLogsForUser(Long userId) {
        return logRepository.findByUserId(userId);
    }
}
