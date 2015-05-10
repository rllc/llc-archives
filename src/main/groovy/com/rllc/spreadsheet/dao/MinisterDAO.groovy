package com.rllc.spreadsheet.dao

import com.rllc.spreadsheet.domain.Minister

/**
 * Created by Robert on 5/9/2015.
 */
interface MinisterDAO {
    List<Minister> getMinisters()
    boolean ministerExists(Minister minister)
    void create(Minister minister)
    void delete(Minister minister)
}
