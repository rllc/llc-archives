package com.rllc.spreadsheet.service

import com.rllc.spreadsheet.dao.MinisterDAO
import com.rllc.spreadsheet.domain.Minister
import spock.lang.Specification

/**
 * Created by Robert on 5/10/2015.
 */
class ArchivedMinistersServiceImplSpec extends Specification {
    def ministerDAO = Mock(MinisterDAO)
    ArchivedMinistersService archivedMinistersService

    def setup() {
        archivedMinistersService = new ArchivedMinistersServiceImpl()
        archivedMinistersService.ministerDAO = ministerDAO
    }

    def "UpdateSpreadsheet"() {
        given:
        def storedMinisters = []
        def newMinisters = []

        storedMinisters << new Minister(
                name: 'Craig Kumpula'
        )
        newMinisters << new Minister(
                name: 'Craig Kumpula'
        )
        newMinisters << new Minister(
                name: 'Joe Lehtola'
        )
        ministerDAO.ministerExists('Craig Kumpula') >> {v -> return true}
        ministerDAO.ministerExists('Joe Lehtola') >> {v -> return false}

        when: "update spreadsheet is called with updated items"
        archivedMinistersService.updateSpreadsheet(newMinisters)

        then: "only new ministers are added"
        1 * ministerDAO.create(_ as Minister) >> { Minister minister ->
            assert minister.name == 'Joe Lehtola'
        }
    }

    def "getMinisters"() {
        given:
        def storedMinisters = []

        storedMinisters << new Minister(
                name: 'Craig Kumpula'
        )
        storedMinisters << new Minister(
                name: 'Joe Lehtola'
        )
        ministerDAO.getMinisters() >> {v -> return storedMinisters}

        when: "get ministers is called"
        List<String> ministers = archivedMinistersService.getMinisters()

        then: "list of minister names is returned"
        ministers.contains('Craig Kumpula')
        ministers.contains('Joe Lehtola')
    }

}
