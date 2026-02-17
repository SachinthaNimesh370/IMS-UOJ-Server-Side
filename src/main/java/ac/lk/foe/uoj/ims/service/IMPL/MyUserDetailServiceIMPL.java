package ac.lk.foe.uoj.ims.service.IMPL;


import ac.lk.foe.uoj.ims.entity.UserEntity;
import ac.lk.foe.uoj.ims.repo.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailServiceIMPL implements UserDetailsService {
    private final UserRepository userRepository;



    public MyUserDetailServiceIMPL(UserRepository userRepository) {

        this.userRepository = userRepository;

    }

    @Override
    public UserDetails loadUserByUsername(String reg_no) throws UsernameNotFoundException {
        UserEntity userEntity =userRepository.findByRegNo(reg_no).orElse(null);
        if(userEntity != null){
            UserDetails user = User.builder()
                    .username(userEntity.getRegNo())
                    .password(userEntity.getPassword())
                    .build();
            return user;
        }else {
            return null;
        }

    }




}
