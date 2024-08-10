package kafka.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class KafkaConsumer {


    @KafkaListener(topics = "expiry_topic", groupId = "my-group")
    public String consume(String message) {
        System.out.println("Consumed message: " + message);
        return "Consumed message"+message;
    }


}
