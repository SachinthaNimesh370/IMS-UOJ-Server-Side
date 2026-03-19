package ac.lk.foe.uoj.ims.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueRequestDTO {
    private Long inventoryItemId;
    private Integer quantity;
    private String issuedToRegNo;
    private String description;
    private LocalDateTime expectedReturnDate;
}
