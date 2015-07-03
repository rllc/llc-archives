package com.rllc.spreadsheet.controller

import com.rllc.spreadsheet.props.CongregationPropertyLoader
import com.rllc.spreadsheet.security.CongregationUserDetailsService
import com.rllc.spreadsheet.service.ArchivedSermonsService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

import javax.servlet.http.HttpServletRequest

/**
 * Created by Steven McAdams on 6/28/15.
 */
@Controller
@RequestMapping(value = "/admin")
class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    ArchivedSermonsService archivedSermonsService

    @Autowired
    CongregationPropertyLoader congregationPropertyLoader

    @Autowired
    CongregationUserDetailsService congregationUserDetailsService

    @Autowired
    AuthenticationManager authenticationManager

    @RequestMapping(value = '/refresh', method = RequestMethod.POST)
    public ResponseEntity refreshSermons(HttpServletRequest request) {
        logger.info("principal : {}", SecurityContextHolder.context.authentication.principal)
        archivedSermonsService.updateDatastore()
        return ResponseEntity.ok().build()
    }
}
