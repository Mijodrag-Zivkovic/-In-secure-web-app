package com.News.News.security.services;

import com.News.News.exceptions.AppException;
import com.News.News.exceptions.ErrorCode;
import com.News.News.models.UserAccount;
import com.News.News.repositories.AccountRepository;
import com.News.News.security.model.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserAccount> user = accountRepository.findByUsername(username);
        if (user.isPresent()) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.get().getRole())); // Role authority
            //authorities.add(new SimpleGrantedAuthority("USER_ID_" + user.get().getId())); // Custom authority
//            return User.builder()
//                    .username(user.get().getUsername())
//                    .password(user.get().getPassword())
//                    .authorities(authorities)
//                    .build();
            return new CustomUserDetails(user.get().getId(), user.get().getUsername(), user.get().getPassword(), authorities);
        }
        else throw new AppException("User not found", ErrorCode.NOT_FOUND);
    }
}