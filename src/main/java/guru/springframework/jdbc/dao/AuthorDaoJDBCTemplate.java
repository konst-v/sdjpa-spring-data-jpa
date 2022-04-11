package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorDaoJDBCTemplate implements  AuthorDao{

    private final JdbcTemplate jdbcTemplate;

    public AuthorDaoJDBCTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Author> findAuthorsByLastName(String lastName, Pageable pageable) {
        StringBuilder sb = new StringBuilder("select * from author where last_name = ? ");

        if(pageable.getSort().getOrderFor("first_name") != null) {
            sb.append("order by first_name ").append(pageable.getSort().getOrderFor("first_name").getDirection().name());
        }

        sb.append(" limit ? offset ?");
        return jdbcTemplate.query(sb.toString(), getAuthorMapper(), lastName, pageable.getPageSize(), pageable.getOffset());
    }

    @Override
    public Author getById(Long id) {
        return null;
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        return null;
    }

    @Override
    public Author saveNewAuthor(Author author) {
        return null;
    }

    @Override
    public Author updateAuthor(Author author) {
        return null;
    }

    @Override
    public void deleteAuthorById(Long id) {

    }

    private RowMapper<Author> getAuthorMapper() {
        return new AuthorMapper();
    }
}
