package kafka.controller;

import kafka.entity.Refrifood;
import kafka.service.FoodService;
import kafka.service.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/kafka")
@CrossOrigin(origins = "http://localhost:5173")
public class KafkaController {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private FoodService foodService;

    @GetMapping("/publish")
    public String publish(@RequestParam("message") String message) {
        kafkaProducer.sendMessage(message);
        return "Message published successfully";
    }


    @GetMapping("/check-expiry")
    public List<Refrifood> checkExpiry(@RequestParam("userId") Integer userId) {

        //LocalDate dateToCheck = LocalDate.parse(date).minusDays(3);
        LocalDate dateToCheck = LocalDate.now().plusDays(3); // 현재 날짜 기준으로 3일 후
        System.out.println("dateTocheck: "+dateToCheck);
        //List<Refrifood> expiringFoods = foodService.checkExpiryDates(dateToCheck);

        List<Refrifood> expiringFoods = foodService.findExpiringFoodsForUser(userId, dateToCheck);

        for (Refrifood food : expiringFoods) {
            String message = "당신의 냉장고 안 '" + food.getName() + "'의 유통기한이 3일 남았어요 !";
            kafkaProducer.sendMessage(message);

        }

        return expiringFoods;
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/group")
    public Message broadcastGroupMessage(@Payload Message message){
        return message;
    }


}
