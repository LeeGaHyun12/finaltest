package kafka.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class KafkaConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
    private final Map<Integer, String> notifications = new HashMap<>();

    @KafkaListener(topics = "expiry-topic", groupId = "your-group-id")
    public void listen(ConsumerRecord<Integer, String> record) {
        int userId = record.key(); // 메시지의 키로 userId를 가정
        String message = record.value();
        logger.info("Received message: {} for userId: {}", message, userId);
        notifications.put(userId, message);
    }

    public String getNotificationForUser(int userId) {
        return notifications.getOrDefault(userId, "No notifications found");
    }
}
