package com.rllc.spreadsheet.service

import com.rllc.spreadsheet.domain.Minister

/**
 * Created by Robert on 5/10/2015.
 */
interface ArchivedMinistersService {
    void updateSpreadsheet(List<Minister> ministers)
    List<String> getMinisters()
    void deleteMinister(Minister minister)
}
