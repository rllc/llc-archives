package com.rllc.spreadsheet.service

import com.rllc.spreadsheet.dao.MinisterDAO
import spock.lang.Specification

/**
 * Created by Robert on 5/10/2015.
 */
class ArchivedMinistersServiceImplTest extends Specification {
    def ministerDAO = Mock(MinisterDAO)
    ArchivedMinistersService archivedMinistersService

    def setup() {
        archivedMinistersService = new ArchivedMinistersServiceImpl()
        archivedMinistersService.ministerDAO = ministerDAO
    }

    def "UpdateSpreadsheet"() {
        //TODO
    }

}
