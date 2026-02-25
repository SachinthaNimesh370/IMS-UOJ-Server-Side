package ac.lk.foe.uoj.ims.service;

import ac.lk.foe.uoj.ims.dto.CapitalInventoryRequestDTO;

public interface CapitalInventoryRequestService {

    // TO create request
    CapitalInventoryRequestDTO createRequest(CapitalInventoryRequestDTO dto);

    // Sequential approval / status update
    CapitalInventoryRequestDTO updateStatus(Long requestId, String userEmail);

    // Reject request
    CapitalInventoryRequestDTO rejectRequest(Long requestId, String userEmail);
}