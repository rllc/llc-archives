package com.rllc.spreadsheet.dao

import com.rllc.spreadsheet.domain.Minister
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

/**
 * Created by Steven McAdams on 6/10/15.
 */
@Component
class InMemoryMinisterDAO implements MinisterDAO {

    def ministers = []

    @PostConstruct
    public void init() {
        ministers << new Minister(
                name: 'Joukko Hapsaari'
        )
    }

    @Override
    List<Minister> getMinisters() {
        return ministers
    }

    @Override
    boolean ministerExists(String name) {
        return ministers.find { it.name == name }
    }

    @Override
    void create(Minister minister) {
        ministers << minister
    }

    @Override
    void delete(Minister minister) {
        ministers.remove(ministers.find { it.name == name })
    }
}
