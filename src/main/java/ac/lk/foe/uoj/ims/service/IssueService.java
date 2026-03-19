package ac.lk.foe.uoj.ims.service;

import ac.lk.foe.uoj.ims.dto.IssueRequestDTO;
import ac.lk.foe.uoj.ims.dto.IssueResponseDTO;

import java.util.List;

public interface IssueService {
    IssueResponseDTO issueItem(Long userId, IssueRequestDTO issueRequestDTO);
    IssueResponseDTO returnItem(Long issueId);
    List<IssueResponseDTO> getActiveIssuesByDepartment(Long deptId);
    List<IssueResponseDTO> getAllByDepartment(Long deptId);
}
