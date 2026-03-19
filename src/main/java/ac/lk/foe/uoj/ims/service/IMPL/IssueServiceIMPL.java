package ac.lk.foe.uoj.ims.service.IMPL;

import ac.lk.foe.uoj.ims.dto.IssueRequestDTO;
import ac.lk.foe.uoj.ims.dto.IssueResponseDTO;
import ac.lk.foe.uoj.ims.entity.InventoryItem;
import ac.lk.foe.uoj.ims.entity.IssueRecord;
import ac.lk.foe.uoj.ims.repo.InventoryItemRepository;
import ac.lk.foe.uoj.ims.repo.IssueRepository;
import ac.lk.foe.uoj.ims.repo.UserRepository;
import ac.lk.foe.uoj.ims.service.IssueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IssueServiceIMPL implements IssueService {

    private final IssueRepository issueRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final UserRepository userRepository;

    public IssueServiceIMPL(IssueRepository issueRepository, 
                            InventoryItemRepository inventoryItemRepository, 
                            UserRepository userRepository) {
        this.issueRepository = issueRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public IssueResponseDTO issueItem(Long userId, IssueRequestDTO dto) {
        InventoryItem item = inventoryItemRepository.findById(dto.getInventoryItemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (item.getQuantity() < dto.getQuantity()) {
            throw new RuntimeException("Insufficient quantity available in inventory");
        }

        // Update inventory: subtract the issued quantity
        item.setQuantity(item.getQuantity() - dto.getQuantity());
        inventoryItemRepository.save(item);

        // Create issue record
        IssueRecord record = new IssueRecord();
        record.setInventoryItemId(dto.getInventoryItemId());
        record.setQuantity(dto.getQuantity());
        record.setIssuedToRegNo(dto.getIssuedToRegNo());
        record.setDescription(dto.getDescription());
        record.setExpectedReturnDate(dto.getExpectedReturnDate());
        record.setIssuedById(userId);
        record.setStatus("ISSUED");

        IssueRecord saved = issueRepository.save(record);
        return mapToResponseDTO(saved);
    }

    @Override
    @Transactional
    public IssueResponseDTO returnItem(Long issueId) {
        IssueRecord record = issueRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Issue record not found"));

        if ("RETURNED".equals(record.getStatus())) {
            throw new RuntimeException("Item has already been returned");
        }

        // Update inventory: add the returned quantity back
        InventoryItem item = inventoryItemRepository.findById(record.getInventoryItemId())
                .orElseThrow(() -> new RuntimeException("Inventory item not found"));
        item.setQuantity(item.getQuantity() + record.getQuantity());
        inventoryItemRepository.save(item);

        // Update record
        record.setReturnDate(LocalDateTime.now());
        record.setStatus("RETURNED");

        IssueRecord saved = issueRepository.save(record);
        return mapToResponseDTO(saved);
    }

    @Override
    public List<IssueResponseDTO> getActiveIssuesByDepartment(Long deptId) {
        return issueRepository.findActiveByDepartmentId(deptId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<IssueResponseDTO> getAllByDepartment(Long deptId) {
        return issueRepository.findAllByDepartmentId(deptId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private IssueResponseDTO mapToResponseDTO(IssueRecord record) {
        IssueResponseDTO dto = new IssueResponseDTO();
        dto.setId(record.getId());
        dto.setInventoryItemId(record.getInventoryItemId());
        dto.setQuantity(record.getQuantity());
        dto.setIssuedToRegNo(record.getIssuedToRegNo());
        dto.setDescription(record.getDescription());
        dto.setIssueDate(record.getIssueDate());
        dto.setExpectedReturnDate(record.getExpectedReturnDate());
        dto.setReturnDate(record.getReturnDate());
        dto.setStatus(record.getStatus());

        // Resolve item name
        inventoryItemRepository.findById(record.getInventoryItemId())
                .ifPresent(item -> dto.setItemName(item.getName()));

        // Resolve user names
        userRepository.findById(record.getIssuedById())
                .ifPresent(user -> dto.setIssuedByNames(user.getF_Name() + " " + user.getL_Name()));

        return dto;
    }
}
