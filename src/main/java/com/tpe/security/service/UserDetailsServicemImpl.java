package com.tpe.security.service;

import com.tpe.domain.Role;
import com.tpe.domain.User;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//----11// bu classta amacimiz: security katmanina userlari verip bunlarin userDetailse donusturlmesi,roller-->GrantedAuthority
@Service//bu bir service classi
@RequiredArgsConstructor
public class UserDetailsServicemImpl implements UserDetailsService {
    //--12 user repositoryi buraya injection yapcaz findByUserNameyi kullanmak icin
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user= userRepository.findByUserName(username).orElseThrow(()-> new ResourceNotFoundException("user not found with username: "+ username));
   //useri userdetailse ceviricez
        if(user != null) {
            return new org.springframework.security.core.userdetails.User(user.getUserName(),
                    user.getPassword(),
                    buildGrantedAuthorities(user.getRoles()));
        } else {
            throw new UsernameNotFoundException("user not found username : " + username);
        }
    }
    //----13
    //useri userdetailse ceviricez
    private static List<SimpleGrantedAuthority> buildGrantedAuthorities(final Set<Role> roles) {//SimpleGrantedAuthority : bu class GrantedAuthorityi extend etmis rolleri GrantedAuthoritye cevirmek icin
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {//role turundeki rolleri tek tek gezip SimpleGrantedAuthority turune ceviriz
            authorities.add(new SimpleGrantedAuthority(role.getName().name()));
        }
        return  authorities;
    }
}
