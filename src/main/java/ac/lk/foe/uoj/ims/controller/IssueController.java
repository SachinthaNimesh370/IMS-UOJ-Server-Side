package ac.lk.foe.uoj.ims.controller;

import ac.lk.foe.uoj.ims.dto.IssueRequestDTO;
import ac.lk.foe.uoj.ims.dto.IssueResponseDTO;
import ac.lk.foe.uoj.ims.repo.UserRepository;
import ac.lk.foe.uoj.ims.service.IssueService;
import ac.lk.foe.uoj.ims.utill.StandardResponce;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/issue")
public class IssueController {

    private final IssueService issueService;
    private final UserRepository userRepository;

    public IssueController(IssueService issueService, UserRepository userRepository) {
        this.issueService = issueService;
        this.userRepository = userRepository;
    }

    private Long getCurrentUserId(Authentication auth) {
        return userRepository.findByEmail(auth.getName())
                .map(u -> u.getId()).orElse(null);
    }

    @PostMapping("/new")
    @PreAuthorize("hasAnyAuthority('LAB_TO','ADMIN')")
    public ResponseEntity<StandardResponce> issueItem(@RequestBody IssueRequestDTO dto,
                                                      Authentication auth) {
        try {
            Long userId = getCurrentUserId(auth);
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new StandardResponce(401, "Unauthorized", "User not found", null));
            }
            IssueResponseDTO response = issueService.issueItem(userId, dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new StandardResponce(201, "Item issued successfully", response, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new StandardResponce(400, "Failed to issue item", e.getMessage(), null));
        }
    }

    @PutMapping("/return/{id}")
    @PreAuthorize("hasAnyAuthority('LAB_TO','ADMIN')")
    public ResponseEntity<StandardResponce> returnItem(@PathVariable("id") Long id) {
        try {
            IssueResponseDTO response = issueService.returnItem(id);
            return ResponseEntity.ok(new StandardResponce(200, "Item returned successfully", response, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new StandardResponce(400, "Failed to return item", e.getMessage(), null));
        }
    }

    @GetMapping("/active/department/{deptId}")
    @PreAuthorize("hasAnyAuthority('LAB_TO','ADMIN','LAB_IN_CHARGE','HOD','IMO','MA')")
    public ResponseEntity<StandardResponce> getActiveByDepartment(@PathVariable("deptId") Long deptId) {
        List<IssueResponseDTO> issues = issueService.getActiveIssuesByDepartment(deptId);
        return ResponseEntity.ok(new StandardResponce(200, "Success", issues, null));
    }

    @GetMapping("/all/department/{deptId}")
    @PreAuthorize("hasAnyAuthority('LAB_TO','ADMIN','LAB_IN_CHARGE','HOD','IMO','MA')")
    public ResponseEntity<StandardResponce> getAllByDepartment(@PathVariable("deptId") Long deptId) {
        List<IssueResponseDTO> issues = issueService.getAllByDepartment(deptId);
        return ResponseEntity.ok(new StandardResponce(200, "Success", issues, null));
    }
}
