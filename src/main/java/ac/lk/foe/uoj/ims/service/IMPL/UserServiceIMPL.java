package ac.lk.foe.uoj.ims.service.IMPL;


import ac.lk.foe.uoj.ims.dto.UserLoginRequestDTO;
import ac.lk.foe.uoj.ims.dto.UserRegRequestDTO;
import ac.lk.foe.uoj.ims.entity.UserEntity;
import ac.lk.foe.uoj.ims.repo.UserRepository;
import ac.lk.foe.uoj.ims.service.UserService;
import ac.lk.foe.uoj.ims.utill.ServiceResponse;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceIMPL implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTServiceIMPL jwtService;


    public UserServiceIMPL(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTServiceIMPL jwtService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public ServiceResponse signUp(UserRegRequestDTO userRegRequestDTO) {
        if(isEnablePerson(userRegRequestDTO.getEmail())){
            return new ServiceResponse(false, "User already registered !",null);
        }

        try {
            UserEntity userEntity=modelMapper.map(userRegRequestDTO, UserEntity.class);
            userEntity.setPassword(passwordEncoder.encode(userRegRequestDTO.getPassword()));
            userEntity.setState(false);
            userRepository.save(userEntity);
            return new ServiceResponse(true, "User registered successfully",null);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ServiceResponse(false, "Registration Failed",null);
        }
    }

    @Override
    public ServiceResponse signIn(UserLoginRequestDTO userLoginRequestDTO) {
        System.out.println(userLoginRequestDTO.getEmail());
        if(isEnablePerson(userLoginRequestDTO.getEmail())){
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                userLoginRequestDTO.getEmail(),userLoginRequestDTO.getPassword()));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return new ServiceResponse(false,"Login failed. Please check your password.",null);
            }

            if(userRepository.findStateByEmail(userLoginRequestDTO.getEmail())){
                // After checking Valid user issue the key
                Map<String,String> map =clams(userLoginRequestDTO.getEmail());
                System.out.println(map);
                return new ServiceResponse(true,jwtService.jwtToken(userLoginRequestDTO.getEmail(),map),map);
            }else {
                return new ServiceResponse(false,"Login failed. Please Waiting For Approve By Admin",null);
            }
        }
        else{
            return new ServiceResponse(false,"Login failed. No registered user found with the provided information.",null);
        }
    }
    private Map<String,String> clams(String email){
        Map<String,String> map =new HashMap<>();
        map.put("role",userRepository.findRoleByEmail(email));
        return map;
    }

    @Override
    public boolean isEnablePerson(String email) {
        return userRepository.existsByEmail(email);
    }





}
