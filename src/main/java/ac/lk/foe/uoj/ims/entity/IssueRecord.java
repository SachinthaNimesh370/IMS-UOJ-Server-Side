package ac.lk.foe.uoj.ims.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "issue_record")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inventory_item_id")
    private Long inventoryItemId;

    private Integer quantity;

    @Column(name = "issued_to_reg_no")
    private String issuedToRegNo;

    @Column(length = 1000)
    private String description;

    @Column(name = "issue_date")
    private LocalDateTime issueDate;

    @Column(name = "expected_return_date")
    private LocalDateTime expectedReturnDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    // ISSUED | RETURNED
    private String status;

    @Column(name = "issued_by_id")
    private Long issuedById;

    @PrePersist
    protected void onCreate() {
        this.issueDate = LocalDateTime.now();
        if (this.status == null) this.status = "ISSUED";
    }
}
