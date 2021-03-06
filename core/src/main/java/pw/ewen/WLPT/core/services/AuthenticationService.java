package pw.ewen.WLPT.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pw.ewen.WLPT.core.repositories.UserRepository;

import java.util.Base64;
import java.util.HashMap;

/**
 * Created by wenliang on 17-7-14.
 * 认证服务
 */
@Service
public class AuthenticationService {

    private UserRepository userRepository;
    private UserDetailsService userDetailsService;

    @Autowired
    public AuthenticationService(UserRepository userRepository, UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    //用户认证接口
    public boolean checkAuthentication(HashMap<String,String> authInfo){
        UserDetails userDetails;
        try{
            userDetails = this.userDetailsService.loadUserByUsername(authInfo.get("userId"));
            String authString = userDetails.getUsername() + ":" + userDetails.getPassword();
            byte[] encodedBytes = Base64.getEncoder().encode(authString.getBytes());
            String encodedAuthString = new String(encodedBytes);
            return authInfo.get("authToken").equals(encodedAuthString);

        }catch (Exception exception){
            return false;
        }

    }
}
