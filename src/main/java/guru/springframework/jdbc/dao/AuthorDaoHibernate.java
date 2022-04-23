package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class AuthorDaoHibernate implements AuthorDao{

    private final EntityManagerFactory emf;

    public AuthorDaoHibernate(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public List<Author> findAllAuthorsByLastName(String lastName, Pageable pageable) {
        EntityManager em = getEntityManager();

        try {
            StringBuilder hql = new StringBuilder("select a from Author a where last_name = :last_name");
            if(pageable.getSort().getOrderFor("first_name") != null) {
                hql.append(" order by first_name ").append(pageable.getSort().getOrderFor("first_name").getDirection().name());
            }
            TypedQuery<Author> query = em.createQuery(hql.toString(), Author.class);
            query.setParameter("last_name", lastName);
            query.setFirstResult(Math.toIntExact(pageable.getPageSize()));
            query.setMaxResults(Math.toIntExact(pageable.getPageSize()));
            return query.getResultList();
        } finally {
            em.close();
        }
    }


    @Override
    public Author getById(Long id) {
        EntityManager em = getEntityManager();
        Author author;
        try {
            author = getEntityManager().find(Author.class, id);
        }  finally {
            em.close();
        }
        return author;
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        EntityManager em = getEntityManager();
        Author author;
        try {
            TypedQuery<Author> query = em.createNamedQuery("find_by_name", Author.class);

            query.setParameter("first_name", firstName);
            query.setParameter("last_name", lastName);
            author = query.getSingleResult();
        } finally {
            em.close();
        }
        return author;
    }

    @Override
    public Author saveNewAuthor(Author author) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(author);
            em.flush();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return author;
    }

    @Override
    public Author updateAuthor(Author author) {
        EntityManager em = getEntityManager();
        Author savedAuthor;
        try {
            em.getTransaction().begin();
            em.merge(author);
            em.flush();
            em.getTransaction().commit();
            em.clear();
            savedAuthor = em.find(Author.class, author.getId());
        } finally {
            em.close();
        }
        return savedAuthor;
    }

    @Override
    public void deleteAuthorById(Long id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Author author = em.find(Author.class, id);
            em.remove(author);
            em.flush();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

}
