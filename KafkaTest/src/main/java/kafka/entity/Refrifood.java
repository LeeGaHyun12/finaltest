package kafka.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "refrifood", schema = "final")
@Data
public class Refrifood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer refrifoodId;

    private Integer foodId;
    private String name;

    @Column(name="refriuser_id")
    private Integer refriuserId;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

}
