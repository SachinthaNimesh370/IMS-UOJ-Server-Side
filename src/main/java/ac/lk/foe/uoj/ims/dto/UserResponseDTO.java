package ac.lk.foe.uoj.ims.dto;

import ac.lk.foe.uoj.ims.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String f_Name;
    private String l_Name;
    private String email;
    private String contactNo;
    private String role;
    private String gender;
    private boolean state;
    private Long departmentId;
    private String departmentName; // Resolved from Department entity

    public UserResponseDTO(UserEntity entity) {
        this.id = entity.getId();
        this.f_Name = entity.getF_Name();
        this.l_Name = entity.getL_Name();
        this.email = entity.getEmail();
        this.contactNo = entity.getContactNo();
        this.role = entity.getRole();
        this.gender = entity.getGender();
        this.state = entity.isState();
        this.departmentId = entity.getDepartmentId();
        if (entity.getDepartment() != null) {
            this.departmentName = entity.getDepartment().getName();
        }
    }
}
