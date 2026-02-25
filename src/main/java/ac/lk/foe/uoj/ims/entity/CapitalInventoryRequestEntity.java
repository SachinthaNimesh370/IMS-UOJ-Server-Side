package ac.lk.foe.uoj.ims.entity;

import ac.lk.foe.uoj.ims.utill.RequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CapitalInventoryRequestEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserEntity sender;   // TO

    @ManyToOne
    @JoinColumn(name = "approver_id")
    private UserEntity approver; // HOD


}
