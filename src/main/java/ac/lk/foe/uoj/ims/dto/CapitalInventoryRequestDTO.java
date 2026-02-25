package ac.lk.foe.uoj.ims.dto;

import ac.lk.foe.uoj.ims.utill.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CapitalInventoryRequestDTO {
    private Long id;
    private String description;
    private RequestStatus status;
    private LocalDateTime createdAt;
    private String senderEmail;
    private String approverEmail;
}
