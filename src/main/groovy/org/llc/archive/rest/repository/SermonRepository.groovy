package org.llc.archive.rest.repository

import org.llc.archive.rest.domain.Sermon
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

/**
 * Created by Steven McAdams on 6/10/15.
 */
@RepositoryRestResource(collectionResourceRel = "sermons", path = "sermons")
interface SermonRepository extends PagingAndSortingRepository<Sermon, Long> {

    List<Sermon> findById(@Param("id") long id);

    Page<Sermon> findByCongregation_Name(@Param("name") String name, Pageable pageable)

    Sermon findFirstByOrderByDateDesc()

    @Query(value = """
        select s from Sermon s
        where
          s.congregation.name = :name and
          (
            LOWER(s.minister) like %:query% or
            LOWER(s.bibleText) like %:query% or
            LOWER(s.comments) like %:query%
          )
    """)
    Page<Sermon> findAnyFieldMatchingQueryByCongregation(
            @Param("name") String name, @Param("query") query, Pageable pageable)

    List<Sermon> findByFileUrlEndingWith(@Param("file") String file);

    @Query(value = """
        select s from Sermon s
        where
          LOWER(s.minister) like %:query% or
          LOWER(s.bibleText) like %:query% or
          LOWER(s.comments) like %:query%
    """)
    Page<Sermon> findAnyFieldMatchingQuery(@Param("query") String query, Pageable pageable);

}
