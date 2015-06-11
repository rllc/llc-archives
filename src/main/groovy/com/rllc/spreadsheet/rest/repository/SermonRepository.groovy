package com.rllc.spreadsheet.rest.repository

import com.rllc.spreadsheet.rest.domain.Sermon
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

/**
 * Created by Steven McAdams on 6/10/15.
 */
@RepositoryRestResource(collectionResourceRel = "sermons", path = "sermons")
interface SermonRepository extends PagingAndSortingRepository<Sermon, Long> {

    List<Sermon> findById(@Param("id") long id);
}
