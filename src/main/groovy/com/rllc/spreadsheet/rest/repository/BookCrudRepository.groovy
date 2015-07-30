package com.rllc.spreadsheet.rest.repository

import com.rllc.spreadsheet.rest.domain.Book
import org.springframework.data.repository.CrudRepository

/**
 * Created by Steven McAdams on 6/18/15.
 */
interface BookCrudRepository extends CrudRepository<Book, Long> {

}
