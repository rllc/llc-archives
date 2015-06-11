package com.rllc.spreadsheet.service

import com.rllc.spreadsheet.domain.Minister

/**
 * Created by Robert on 5/10/2015.
 */
interface ArchivedMinistersService {
    List<Minister> getMinisters()

    void update(List<Minister> ministers)

    void delete(Minister minister)
}
