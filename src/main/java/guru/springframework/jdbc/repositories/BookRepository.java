package guru.springframework.jdbc.repositories;

import guru.springframework.jdbc.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import java.util.Optional;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public interface BookRepository extends JpaRepository<Book, Long> {

    Book jpaNamed(@Param("title") String title);

    @Query(value = "select * from book where title = :title", nativeQuery = true)
    Book findBookByTitleNativeQuery(@Param("title") String title);

    @Query("select b from Book b where b.title = :title")
    Book findBookByTitleWithQueryNamed(@Param("title") String title);

    @Query("select b from Book b where b.title = ?1")
    Book findBookByTitleWithQuery(String title);

    Optional<Book> findBookByTitle(String title);

    Book readByTitle(String title);

    @Nullable
    Book getByTitle(@Nullable String title);

    Stream<Book> findAllByTitleNotNull();

    Future<Book> queryByTitle(String title);
}
