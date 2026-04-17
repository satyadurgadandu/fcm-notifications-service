package in.satya.fcmnotification.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;

public class NotificationDto {
	
	

    /* ── Request: Send to all enabled users ── */
    @Data
    public static class SendToAll {
        private String title;
        private String body;
        private Map<String, String> data; // optional extra info
        
        
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getBody() {
			return body;
		}
		public void setBody(String body) {
			this.body = body;
		}
		public Map<String, String> getData() {
			return data;
		}
		public void setData(Map<String, String> data) {
			this.data = data;
		}
        
        
    }

    /* ── Request: Send to SPECIFIC users only ── */
    @Data
    public static class SendToSelected {
        private String title;
        private String body;
        private List<Long> userIds;       // only THESE users get notified
        private Map<String, String> data;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getBody() {
			return body;
		}
		public void setBody(String body) {
			this.body = body;
		}
		public List<Long> getUserIds() {
			return userIds;
		}
		public void setUserIds(List<Long> userIds) {
			this.userIds = userIds;
		}
		public Map<String, String> getData() {
			return data;
		}
		public void setData(Map<String, String> data) {
			this.data = data;
		}
        
        
        
    }

    /* ── Request: Send to ONE user ── */
    @Data
    public static class SendToOne {
        private String title;
        private String body;
        private Map<String, String> data;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getBody() {
			return body;
		}
		public void setBody(String body) {
			this.body = body;
		}
		public Map<String, String> getData() {
			return data;
		}
		public void setData(Map<String, String> data) {
			this.data = data;
		}
        
        
    }

    /* ── Request: Register device FCM token ── */
    @Data
    public static class RegisterToken {
        private String fcmToken;

		public String getFcmToken() {
			return fcmToken;
		}

		public void setFcmToken(String fcmToken) {
			this.fcmToken = fcmToken;
		}
        
        
    }

    /* ── Response: Result after sending ── */
    @Data
    public static class Result {
        private int totalRequested;
        private int successCount;
        private int failedCount;
        private int skippedCount;
        private List<String> successUsers;
        private List<String> failedUsers;
        private String message;

        public Result(int totalRequested, int successCount, int failedCount,
                      int skippedCount, List<String> successUsers,
                      List<String> failedUsers, String message) {
            this.totalRequested = totalRequested;
            this.successCount = successCount;
            this.failedCount = failedCount;
            this.skippedCount = skippedCount;
            this.successUsers = successUsers;
            this.failedUsers = failedUsers;
            this.message = message;
        }

		public int getTotalRequested() {
			return totalRequested;
		}

		public void setTotalRequested(int totalRequested) {
			this.totalRequested = totalRequested;
		}

		public int getSuccessCount() {
			return successCount;
		}

		public void setSuccessCount(int successCount) {
			this.successCount = successCount;
		}

		public int getFailedCount() {
			return failedCount;
		}

		public void setFailedCount(int failedCount) {
			this.failedCount = failedCount;
		}

		public int getSkippedCount() {
			return skippedCount;
		}

		public void setSkippedCount(int skippedCount) {
			this.skippedCount = skippedCount;
		}

		public List<String> getSuccessUsers() {
			return successUsers;
		}

		public void setSuccessUsers(List<String> successUsers) {
			this.successUsers = successUsers;
		}

		public List<String> getFailedUsers() {
			return failedUsers;
		}

		public void setFailedUsers(List<String> failedUsers) {
			this.failedUsers = failedUsers;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
        
        
        
    }

    

}
