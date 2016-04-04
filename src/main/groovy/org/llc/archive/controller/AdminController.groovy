package org.llc.archive.controller

import groovy.util.logging.Slf4j
import org.llc.archive.props.CongregationPropertyLoader
import org.llc.archive.rest.repository.CongregationRepository
import org.llc.archive.rest.repository.SyncExecutionRepository
import org.llc.archive.security.CongregationUserDetailsService
import org.llc.archive.service.ArchivedSermonsService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.propertyeditors.CustomDateEditor
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.stereotype.Controller
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

import java.text.SimpleDateFormat

@Controller
@Slf4j
@RequestMapping(value = "/admin")
class AdminController {

    private static final String DATE_PATTERN = 'MM-dd-yyyy'
    private static final Date START_OF_TIME = new Date().parse(DATE_PATTERN, '01-01-2000')

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    ArchivedSermonsService archivedSermonsService

    @Autowired
    CongregationPropertyLoader congregationPropertyLoader

    @Autowired
    CongregationUserDetailsService congregationUserDetailsService

    @Autowired
    CongregationRepository congregationRepository

    @Autowired
    SyncExecutionRepository syncExecutionRepository

    @Autowired
    AuthenticationManager authenticationManager

    @RequestMapping(value = '/refresh', method = RequestMethod.POST)
    public ResponseEntity refreshSermons(
            @RequestParam(value = 'fromDate', defaultValue = 'lastExecution', required = false) final Date fromDate,
            @RequestParam(value = 'toDate', defaultValue = 'today', required = false) final Date toDate,
            @RequestParam(value = 'congregation', defaultValue = 'all', required = false) final String congregation) {
        logger.info 'fromDate : {}', fromDate
        logger.info 'toDate : {}', toDate
        logger.info 'congregation : {}', congregation

        def congregations
        if ('all' == congregation) {
            congregations = congregationRepository.findAll().collect { it.name }
        } else {
            congregations = congregationRepository.findByName(congregation).collect { it.name }
        }

        archivedSermonsService.updateDatastore(fromDate, toDate, congregations)
        return ResponseEntity.ok().build()
    }


    @InitBinder
    public void initBinder(WebDataBinder binder) throws Exception {
        final CustomDateEditor dateEditor = new CustomDateEditor(new SimpleDateFormat(DATE_PATTERN), true) {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if ('today'.equals(text)) {
                    setValue(new Date())
                } else if ('startOfTime'.equals(text)) {
                    setValue(START_OF_TIME)
                } else if ('lastExecution'.equals(text)) {
                    def syncExecutionResult = syncExecutionRepository.findTop1ByOrderByDateDesc()
                    if (syncExecutionResult.size()) {
                        setValue(syncExecutionResult[0].date)
                    } else {
                        setValue(new Date())
                    }
                } else
                    super.setAsText(text)
            }
        }
        binder.registerCustomEditor(Date.class, dateEditor);
    }

}
