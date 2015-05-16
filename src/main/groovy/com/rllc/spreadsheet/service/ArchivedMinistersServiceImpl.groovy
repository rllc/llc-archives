package com.rllc.spreadsheet.service

import com.rllc.spreadsheet.dao.MinisterDAO
import com.rllc.spreadsheet.domain.Minister
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by Robert on 5/10/2015.
 */
class ArchivedMinistersServiceImpl implements ArchivedMinistersService {
    private static final Logger logger = LoggerFactory.getLogger(ArchivedMinistersServiceImpl.class);

    @Autowired
    MinisterDAO ministerDAO

    @Override
    void updateSpreadsheet(List<Minister> ministers) {
        logger.info("========= UPDATING SPREADSHEET =========");
        for (Minister minister : ministers) {
            if(!ministerDAO.ministerExists(minister.name)) {
                ministerDAO.create(minister)
            }
        }
    }

    @Override
    List<String> getMinisters() {
        List<String> ministers = new ArrayList<>()
        for (Minister minister : ministerDAO.getMinisters()) {
            ministers.add(minister.name)
        }
        return ministers
    }

    @Override
    void deleteMinister(Minister minister) {
        ministerDAO.delete(minister)
    }
}
