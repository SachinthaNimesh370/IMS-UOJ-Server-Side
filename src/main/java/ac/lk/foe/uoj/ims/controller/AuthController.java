package ac.lk.foe.uoj.ims.controller;

import ac.lk.foe.uoj.ims.dto.UserLoginRequestDTO;
import ac.lk.foe.uoj.ims.dto.UserLoginResponceDTO;
import ac.lk.foe.uoj.ims.dto.UserRegRequestDTO;
import ac.lk.foe.uoj.ims.service.UserService;
import ac.lk.foe.uoj.ims.utill.ServiceResponse;
import ac.lk.foe.uoj.ims.utill.StandardResponce;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;


    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/signup")
    public ResponseEntity<StandardResponce> signUp(@RequestBody UserRegRequestDTO userRegRequestDTO){
        ServiceResponse message = userService.signUp(userRegRequestDTO);
        if(message.isSuccess()){
            return new ResponseEntity<StandardResponce>(
                    new StandardResponce(
                            201, "Created", message.getObject(),null),
                    HttpStatus.CREATED);
        }else{
            System.out.println(message.getObject());
            return new ResponseEntity<StandardResponce>(
                    new StandardResponce(
                            400, "Bad", message.getObject(),null),
                    HttpStatus.BAD_REQUEST);
        }

    }
    @PostMapping("/signin")
    public ResponseEntity<StandardResponce> signIn(@RequestBody UserLoginRequestDTO userLoginRequestDTO){
        System.out.println(userLoginRequestDTO.getEmail());

        ServiceResponse message = userService.signIn(userLoginRequestDTO);
        System.out.println(message.getObject());
        if(message.isSuccess()){
            return  new ResponseEntity<StandardResponce>(
                    new StandardResponce(
                            200,"Ok",new UserLoginResponceDTO(
                            message.getObject(), LocalDateTime.now()),message.getRole()),
                    HttpStatus.OK);
        }else {
            return  new ResponseEntity<StandardResponce>(
                    new StandardResponce(
                            400,"Bad Request", new UserLoginResponceDTO(
                            message.getObject(),null),null),
                    HttpStatus.BAD_REQUEST);
        }

    }
}
