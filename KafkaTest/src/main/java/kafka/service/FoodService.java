package kafka.service;

import kafka.entity.Refrifood;
import kafka.repository.FoodRepository;
import kafka.repository.RefrifoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private RefrifoodRepository refrifoodRepository;

//    public List<Refrifood> checkExpiryDates(LocalDate dateToCheck) {
//        return refrifoodRepository.findByExpiryDate(dateToCheck);
//    }

    public List<Refrifood> findExpiringFoodsForUser(Integer userId, LocalDate dateToCheck) {
        // 유통기한이 dateToCheck와 일치하는 음식 찾기
        return refrifoodRepository.findByRefriuserIdAndExpiryDate(userId, dateToCheck);
    }
}
