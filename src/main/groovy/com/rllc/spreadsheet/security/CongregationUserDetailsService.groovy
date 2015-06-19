package com.rllc.spreadsheet.security

import com.rllc.spreadsheet.domain.Congregation
import com.rllc.spreadsheet.props.CongregationPropertyLoader
import groovy.util.logging.Log
import org.springframework.beans.factory.annotation.Autowired
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
        log.info("loadUserByUsername : {}", username)
        Congregation congregation = congregationPropertyLoader.congregations[username]
        log.info("congregation : {}", congregationd)
        if (congregation == null) {
            throw new UsernameNotFoundException("Congregation " + username + " not found ");
        }
        return new User(congregation.shortName, congregation.shortName, ['ROLE_ADMIN', 'ROLE_BASIC']);
    }
}