package ac.lk.foe.uoj.ims.service.IMPL;

import ac.lk.foe.uoj.ims.dto.CapitalInventoryRequestDTO;
import ac.lk.foe.uoj.ims.entity.CapitalInventoryRequestEntity;
import ac.lk.foe.uoj.ims.entity.UserEntity;
import ac.lk.foe.uoj.ims.repo.CapitalInventoryRequestRepository;
import ac.lk.foe.uoj.ims.repo.UserRepository;
import ac.lk.foe.uoj.ims.service.CapitalInventoryRequestService;
import ac.lk.foe.uoj.ims.utill.RequestStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CapitalInventoryRequestServiceIMPL implements CapitalInventoryRequestService {

    private final CapitalInventoryRequestRepository requestRepository;
    private final UserRepository userRepository;

    public CapitalInventoryRequestServiceIMPL(CapitalInventoryRequestRepository requestRepository,
                                              UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    // CREATE
    @Override
    public CapitalInventoryRequestDTO createRequest(CapitalInventoryRequestDTO dto) {

        UserEntity sender = userRepository.findByEmail(dto.getSenderEmail())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        if (!sender.getRole().equals("TO")) {
            throw new RuntimeException("Only TO can create requests");
        }

        CapitalInventoryRequestEntity request = new CapitalInventoryRequestEntity();
        request.setDescription(dto.getDescription());
        request.setStatus(RequestStatus.PENDING);
        request.setCreatedAt(LocalDateTime.now());
        request.setSender(sender);
        request.setApprover(null);

        requestRepository.save(request);

        return mapToDTO(request);
    }

    // UPDATE STATUS (Sequential Approval)
    @Override
    public CapitalInventoryRequestDTO updateStatus(Long requestId, String email) {

        CapitalInventoryRequestEntity request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String role = user.getRole();

        // Prevent update if already finalized
        if(request.getStatus() == RequestStatus.COMPLETED || request.getStatus() == RequestStatus.REJECTED){
            throw new RuntimeException("Request already finalized");
        }

        switch (request.getStatus()) {

            case PENDING:
                if (!role.equals("INCHARGE"))
                    throw new RuntimeException("Only Incharge can approve");
                request.setStatus(RequestStatus.INCHARGE_APPROVED);
                request.setApprover(user);
                break;

            case INCHARGE_APPROVED:
                if (!role.equals("HOD"))
                    throw new RuntimeException("Only HOD can approve");
                request.setStatus(RequestStatus.HOD_APPROVED);
                request.setApprover(user);
                break;

            case HOD_APPROVED:
                if (!role.equals("IMO"))
                    throw new RuntimeException("Only IMO can approve");
                request.setStatus(RequestStatus.IMO_APPROVED);
                request.setApprover(user);
                break;

            case IMO_APPROVED:
                if (!role.equals("IMO"))
                    throw new RuntimeException("Only IMO can send to welfare");
                request.setStatus(RequestStatus.SENT_TO_WELFARE);
                request.setApprover(user);
                break;

            case SENT_TO_WELFARE:
                if (!role.equals("IMO"))
                    throw new RuntimeException("Only IMO can mark as received");
                request.setStatus(RequestStatus.RECEIVED);
                request.setApprover(user);
                break;

            case RECEIVED:
                if (!role.equals("IMO"))
                    throw new RuntimeException("Only IMO can complete");
                request.setStatus(RequestStatus.COMPLETED);
                request.setApprover(user);
                break;

            default:
                throw new RuntimeException("Invalid request state");
        }

        requestRepository.save(request);

        return mapToDTO(request);
    }

    // REJECT
    @Override
    public CapitalInventoryRequestDTO rejectRequest(Long requestId, String email) {

        CapitalInventoryRequestEntity request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String role = user.getRole();

        if(request.getStatus() == RequestStatus.COMPLETED){
            throw new RuntimeException("Cannot reject completed request");
        }

        if(!(role.equals("INCHARGE") || role.equals("HOD") || role.equals("IMO"))){
            throw new RuntimeException("Unauthorized to reject this request");
        }

        request.setStatus(RequestStatus.REJECTED);
        request.setApprover(user);

        requestRepository.save(request);

        return mapToDTO(request);
    }

    // MAP ENTITY TO DTO
    private CapitalInventoryRequestDTO mapToDTO(CapitalInventoryRequestEntity request) {

        CapitalInventoryRequestDTO dto = new CapitalInventoryRequestDTO();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setStatus(request.getStatus());
        dto.setCreatedAt(request.getCreatedAt());
        dto.setSenderEmail(request.getSender().getEmail());

        if(request.getApprover() != null){
            dto.setApproverEmail(request.getApprover().getEmail());
        } else {
            dto.setApproverEmail(null);
        }

        return dto;
    }
}