package kafka.repository;

import kafka.entity.Refrifood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RefrifoodRepository extends JpaRepository<Refrifood, Integer> {
//    @Query("SELECT r FROM Refrifood r WHERE DATE(r.expiryDate) = :expiryDate")
//    List<Refrifood> findByExpiryDate(@Param("user_Id") Integer userId,@Param("expiryDate") LocalDate expiryDate);

    @Query("SELECT r FROM Refrifood r WHERE r.refriuserId = :userId AND DATE(r.expiryDate) = :expiryDate")
    List<Refrifood> findByRefriuserIdAndExpiryDate(Integer userId, LocalDate expiryDate);
}