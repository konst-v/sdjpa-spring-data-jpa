package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookDaoJDBCTemplate implements BookDao {

    private final JdbcTemplate jdbcTemplate;

    public BookDaoJDBCTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> findAllBooksSortByTitle(Pageable pageable) {
        String sql = "select * from book order by title " + pageable
                .getSort().getOrderFor("title").getDirection().name()
                + " limit ? offset ?";

        return jdbcTemplate.query(sql, getBookMapper(), pageable.getPageSize(), pageable.getOffset());
    }

    @Override
    public List<Book> findAllBooks(Pageable pageable) {
        return jdbcTemplate.query("select * from book limit ? offset ?", getBookMapper(),
                pageable.getPageSize(), pageable.getOffset());
    }

    @Override
    public List<Book> findAllBooks(int pageSize, int offset) {
        return jdbcTemplate.query("select * from book limit ? offset ?", getBookMapper(), pageSize, offset);
    }

    @Override
    public List<Book> findAllBooks() {
        return jdbcTemplate.query("select * from book", getBookMapper());
    }

    @Override
    public Book getById(Long id) {
        return jdbcTemplate.queryForObject("select * from book where id = ?", getBookMapper(), id);
    }

    @Override
    public Book findBookByTitle(String title) {
        return jdbcTemplate.queryForObject("select * from book where title = ?", getBookMapper(), title);
    }

    @Override
    public Book saveNewBook(Book book) {
        jdbcTemplate.update("insert into book(title, isbn, publisher, author_id) values(?, ?, ? ,?)",
                book.getTitle(), book.getIsbn(), book.getPublisher(), book.getAuthorId());
        Long createdId = jdbcTemplate.queryForObject("select last_insert_id()", Long.class);
        return this.getById(createdId);
    }

    @Override
    public Book updateBook(Book book) {
        jdbcTemplate.update("update book set title = ?, isbn = ?, publisher = ?, author_id = ? where id = ?",
                book.getTitle(), book.getIsbn(), book.getPublisher(), book.getAuthorId(), book.getId());
        return this.getById(book.getId());
    }

    @Override
    public void deleteBookById(Long id) {
        jdbcTemplate.update("delete from book where id = ?", id);
    }

    private RowMapper<Book> getBookMapper() {
        return new BookMapper();
    }
}
