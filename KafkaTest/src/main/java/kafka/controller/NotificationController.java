package kafka.controller;

import kafka.service.KafkaConsumerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final KafkaConsumerService kafkaConsumerService;

    public NotificationController(KafkaConsumerService kafkaConsumerService) {
        this.kafkaConsumerService = kafkaConsumerService;
    }

    @GetMapping("/latest")
    public String getLatestNotification(@RequestParam("userId") int userId) {
        String notification = kafkaConsumerService.getNotificationForUser(userId);
        if (notification.equals("No notifications found")) {
            return "No notifications found for userId: " + userId;
        }
        return notification;
    }

}
