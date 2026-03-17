package ac.lk.foe.uoj.ims.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;

    // e.g. LOGIN, USER_APPROVED, ITEM_ADDED, REQUEST_SUBMITTED, MAINTENANCE_APPROVED
    private String action;

    // e.g. USER, INVENTORY_ITEM, REQUEST, MAINTENANCE
    private String entityType;

    private String entityId;

    @Column(length = 2000)
    private String description;

    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }
}
