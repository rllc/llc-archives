package com.rllc.spreadsheet.service

import com.rllc.spreadsheet.rest.domain.Minister
import com.rllc.spreadsheet.rest.repository.MinisterCrudRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by Robert on 5/10/2015.
 */
@Component
class ArchivedMinistersServiceImpl implements ArchivedMinistersService {
    private static final Logger logger = LoggerFactory.getLogger(ArchivedMinistersServiceImpl.class);

    @Autowired
    MinisterCrudRepository ministerCrudRepository

    @Override
    void update(List<Minister> ministers) {
        for (Minister minister : ministers) {
            ministerCrudRepository.save(minister)
        }
    }

    @Override
    List<String> getMinisters() {
        return ministerCrudRepository.findAll().collect { it.fullName }
    }

    @Override
    void delete(Minister minister) {
        ministerCrudRepository.delete(minister)
    }
}
