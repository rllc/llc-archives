package com.rllc.spreadsheet.rest.repository

import com.rllc.spreadsheet.rest.domain.Congregation
import org.springframework.data.repository.CrudRepository
import org.springframework.security.access.prepost.PreAuthorize

/**
 * Created by Steven McAdams on 6/18/15.
 */
@PreAuthorize("hasRole('ROLE_ADMIN')")
interface CongregationCrudRepository extends CrudRepository<Congregation, Long> {

}
