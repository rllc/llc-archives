package com.rllc.spreadsheet.rest.repository

import com.rllc.spreadsheet.rest.domain.Sermon
import org.springframework.data.repository.CrudRepository

/**
 * Created by Steven McAdams on 6/18/15.
 */
interface SermonCrudRepository extends CrudRepository<Sermon, Long> {

}
