package ac.lk.foe.uoj.ims.service;


import ac.lk.foe.uoj.ims.dto.UserLoginRequestDTO;
import ac.lk.foe.uoj.ims.dto.UserRegRequestDTO;
import ac.lk.foe.uoj.ims.utill.ServiceResponse;

public interface UserService {
    ServiceResponse signUp(UserRegRequestDTO userRegRequestDTO);
    ServiceResponse signIn(UserLoginRequestDTO userLoginRequestDTO);
    boolean isEnablePerson(String email);


}
