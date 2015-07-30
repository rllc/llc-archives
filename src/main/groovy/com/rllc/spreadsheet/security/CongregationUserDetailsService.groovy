package com.rllc.spreadsheet.security

import com.rllc.spreadsheet.domain.CongregationCredentials
import com.rllc.spreadsheet.props.CongregationPropertyLoader
import groovy.util.logging.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * Created by Steven McAdams on 6/11/15.
 */
@Service
@Log
public class CongregationUserDetailsService implements UserDetailsService {
    @Autowired
    private CongregationPropertyLoader congregationPropertyLoader

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info "loadUserByUsername : $username"
        CongregationCredentials congregation = congregationPropertyLoader.credentials[username]
        if (congregation == null) {
            throw new UsernameNotFoundException("CongregationCredentials " + username + " not found ");
        }
        return new User(username, congregation.password, [
                new SimpleGrantedAuthority('ROLE_ADMIN'),
                new SimpleGrantedAuthority('ROLE_BASIC'),
        ])
    }
}