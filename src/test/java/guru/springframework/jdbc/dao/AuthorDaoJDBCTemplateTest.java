package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackages = {"guru.springframework.jdbc.dao"})
public class AuthorDaoJDBCTemplateTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    AuthorDao authorDao;

    @BeforeEach
    void setup() {
        authorDao = new AuthorDaoJDBCTemplate(jdbcTemplate);
    }

    @Test
    void testFindAuthorsByLastName_SortByFirstName_page1() {
        List<Author> authors = authorDao.findAuthorsByLastName("Smith", PageRequest.of(0, 10,
                Sort.by(Sort.Order.asc("first_name"))));
        assertThat(authors).isNotNull();
        assertEquals(authors.get(0).getFirstName(), "Ahmed");
    }

    @Test
    void testFindAuthorsByLastName_SortByFirstName_page2() {
        List<Author> authors = authorDao.findAuthorsByLastName("Smith", PageRequest.of(1, 1,
                Sort.by(Sort.Order.asc("first_name"))));
        assertThat(authors).isNotNull();
        assertEquals(authors.get(0).getFirstName(), "Ahmed");
    }
}
