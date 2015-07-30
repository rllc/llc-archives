package com.rllc.spreadsheet.rest.repository

import com.rllc.spreadsheet.rest.domain.Minister
import org.springframework.data.repository.CrudRepository

/**
 * Created by Steven McAdams on 6/18/15.
 */
interface MinisterCrudRepository extends CrudRepository<Minister, Long> {

}
