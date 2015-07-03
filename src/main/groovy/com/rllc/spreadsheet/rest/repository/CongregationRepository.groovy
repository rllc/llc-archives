package com.rllc.spreadsheet.rest.repository

import com.rllc.spreadsheet.rest.domain.Congregation
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

/**
 * Created by Steven McAdams on 6/10/15.
 */
@RepositoryRestResource(collectionResourceRel = "congregations", path = "congregations")
interface CongregationRepository extends PagingAndSortingRepository<Congregation, Long> {

    List<Congregation> findById(@Param("id") long id);

    List<Congregation> findByName(@Param("name") String name);

}
