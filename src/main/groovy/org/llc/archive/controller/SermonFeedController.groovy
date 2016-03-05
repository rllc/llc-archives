package org.llc.archive.controller

import groovy.util.logging.Slf4j
import org.llc.archive.rest.repository.SermonRepository
import org.llc.archive.rss.view.SermonRssFeedView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

/**
 * Created by Steven McAdams on 2/29/16.
 */
@Controller
@Slf4j
class SermonFeedController {

    @Autowired
    SermonRssFeedView sermonRssFeedView

    @Autowired
    SermonRepository sermonRepository

    @RequestMapping(value = "feed/{congregation}", produces = "application/*", method = RequestMethod.GET)
    public ModelAndView getFeed(
            @PathVariable String congregation,
            @RequestParam(required = false, defaultValue = '25') int max) {
        log.info "get feed : ${congregation}"

        ModelAndView mav = new ModelAndView()
        mav.setView(sermonRssFeedView)
        def sermons
        if ('all' == congregation) {
            sermons = sermonRepository.findAll(new PageRequest(0, max, Sort.Direction.DESC, 'date'))
        } else {
            sermons = sermonRepository.findByCongregation_Name(congregation, new PageRequest(0, max, Sort.Direction.DESC, 'date'))
        }
        mav.addObject('feedContent', sermons)

        return mav

    }

}
