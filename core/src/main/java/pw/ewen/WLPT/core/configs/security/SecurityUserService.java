package pw.ewen.WLPT.core.configs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pw.ewen.WLPT.core.domains.entities.Role;
import pw.ewen.WLPT.core.domains.entities.User;
import pw.ewen.WLPT.core.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wenliang on 17-2-9.
 * 用户服务（提供用户查找等服务）
 */
@Component
public class SecurityUserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public SecurityUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByuserId(userId);
        Role role;
        if(user != null){
            role = user.getRole();
            List<GrantedAuthority> authorities = new ArrayList<>();
            if(role != null){
                authorities.add(new SimpleGrantedAuthority(role.getRoleId()));
            }

            return new org.springframework.security.core.userdetails.User(user.getUserId(), user.getPassword(), authorities);
        }

        throw new UsernameNotFoundException("User id: '" + userId + "' not found");
    }
}
