package org.llc.archive.rest.repository

import org.llc.archive.rest.domain.Book
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

/**
 * Created by Steven McAdams on 6/10/15.
 */
@RepositoryRestResource(collectionResourceRel = "books", path = "books")
interface BookRepository extends PagingAndSortingRepository<Book, Long> {

    List<Book> findById(@Param("id") long id);

    List<Book> findByName(@Param("name") String name);

    List<Book> findByAbbreviation(@Param("abbreviation") String abbreviation);
}
