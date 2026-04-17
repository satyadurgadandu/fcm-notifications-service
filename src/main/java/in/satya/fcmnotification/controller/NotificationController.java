package in.satya.fcmnotification.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import in.satya.fcmnotification.dto.NotificationDto;
import in.satya.fcmnotification.entity.NotificationLog;
import in.satya.fcmnotification.service.NotificationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService ;

    // ─────────────────────────────────────────────────────────
    //  POST /api/notifications/send/all
    //  Sends to all users with notificationEnabled = true
    //
    //  Body:
    //  {
    //    "title": "Hello!",
    //    "body": "This is a test notification"
    //  }
    // ─────────────────────────────────────────────────────────
    @PostMapping("/send/all")
    public ResponseEntity<NotificationDto.Result> sendToAll(
            @RequestBody NotificationDto.SendToAll request) {
        return ResponseEntity.ok(notificationService.sendToAll(request));
    }

    // ─────────────────────────────────────────────────────────
    //  POST /api/notifications/send/selected
    //  Sends ONLY to the userIds you list.
    //  Everyone else gets NOTHING.
    //
    //  Body:
    //  {
    //    "title": "Hello selected users!",
    //    "body": "Only you 3 can see this",
    //    "userIds": [1, 2, 3]
    //  }
    //
    //  → Users 4 and 5 will not receive anything.
    // ─────────────────────────────────────────────────────────
    @PostMapping("/send/selected")
    public ResponseEntity<NotificationDto.Result> sendToSelected(
            @RequestBody NotificationDto.SendToSelected request) {
        return ResponseEntity.ok(notificationService.sendToSelected(request));
    }

    // ─────────────────────────────────────────────────────────
    //  POST /api/notifications/send/user/1
    //  Sends to ONE specific user
    //
    //  Body:
    //  {
    //    "title": "Your order is ready!",
    //    "body": "Come pick it up"
    //  }
    // ─────────────────────────────────────────────────────────
    @PostMapping("/send/user/{userId}")
    public ResponseEntity<NotificationDto.Result> sendToOne(
            @PathVariable Long userId,
            @RequestBody NotificationDto.SendToOne request) {
        return ResponseEntity.ok(notificationService.sendToOne(userId, request));
    }

    // GET /api/notifications/logs
    // View all notification history
    @GetMapping("/logs")
    public ResponseEntity<List<NotificationLog>> getLogs() {
        return ResponseEntity.ok(notificationService.getLogs());
    }

    // GET /api/notifications/logs/user/1
    // View notification history for one user
    @GetMapping("/logs/user/{userId}")
    public ResponseEntity<List<NotificationLog>> getLogsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getLogsForUser(userId));
    }
}

