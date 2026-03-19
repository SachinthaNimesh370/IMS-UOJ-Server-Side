package ac.lk.foe.uoj.ims.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueResponseDTO {
    private Long id;
    private Long inventoryItemId;
    private String itemName;
    private Integer quantity;
    private String issuedToRegNo;
    private String description;
    private LocalDateTime issueDate;
    private LocalDateTime expectedReturnDate;
    private LocalDateTime returnDate;
    private String status;
    private String issuedByNames;
}
