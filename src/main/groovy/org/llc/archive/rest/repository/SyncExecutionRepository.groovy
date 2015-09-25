package org.llc.archive.rest.repository

import org.llc.archive.rest.domain.SyncExecution
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

/**
 * Created by Steven McAdams on 6/10/15.
 */
@RepositoryRestResource(collectionResourceRel = "sync", path = "sync")
interface SyncExecutionRepository extends PagingAndSortingRepository<SyncExecution, Long> {

    List<SyncExecution> findTop1ByOrderByDateDesc();

    List<SyncExecution> findById(@Param("id") long id);

    List<SyncExecution> findByUsername(@Param("username") String username);

}
