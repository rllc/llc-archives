package com.rllc.spreadsheet.dao

import com.rllc.spreadsheet.domain.Sermon

/**
 * Created by z0019yw on 4/26/15.
 */
interface SermonDAO {

    Sermon get(String filename)
    void create(Sermon sermon)
    void update(Sermon sermon)
    void delete(Sermon sermon)

}
