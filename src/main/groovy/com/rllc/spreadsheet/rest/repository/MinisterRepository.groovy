package com.rllc.spreadsheet.rest.repository

import com.rllc.spreadsheet.rest.domain.Minister
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

/**
 * Created by Steven McAdams on 6/10/15.
 */
@RepositoryRestResource(collectionResourceRel = "ministers", path = "ministers")
interface MinisterRepository extends PagingAndSortingRepository<Minister, Long> {

    List<Minister> findById(@Param("id") long id);

    List<Minister> findByFirstName(@Param("firstName") String firstName);

    List<Minister> findByLastName(@Param("lastName") String lastName);
}
