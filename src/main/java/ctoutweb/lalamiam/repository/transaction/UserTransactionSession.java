package ctoutweb.lalamiam.repository.transaction;

import ctoutweb.lalamiam.repository.UserRepository;
import ctoutweb.lalamiam.repository.entity.UserEntity;
import jakarta.persistence.EntityManagerFactory;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class UserTransactionSession {
  private final EntityManagerFactory entityManagerFactory;
  private final UserRepository userRepository;

  public UserTransactionSession(EntityManagerFactory entityManagerFactory, UserRepository userRepository) {
    this.entityManagerFactory = entityManagerFactory;
    this.userRepository = userRepository;
  }

  public UserEntity getUserInformationById(Long userId) {
    UserEntity user = null;
    Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
    Transaction transaction = session.beginTransaction();
    try {
      user = session.get(UserEntity.class, userId);

      // Si pas de commande
      if(user == null) return null;

      Hibernate.initialize(user.getRoles());
      Hibernate.initialize(user.getJwt());
      transaction.commit();
    } catch (Exception ex) {

    } finally {
      session.close();
      return user;
    }
  }

  public UserEntity getUserInformationByEmail(String email) {
    UserEntity user = null;
    Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
    Transaction transaction = session.beginTransaction();
    try {
      Query<UserEntity> query = session.createQuery("FROM UserEntity u WHERE u.email = :email", UserEntity.class);
      query.setParameter("email", email);
      user = query.uniqueResult();

      // Si pas de commande
      if(user == null) return null;

      Hibernate.initialize(user.getRoles());
      transaction.commit();
    } catch (Exception ex) {

    } finally {
      session.close();
      return user;
    }
  }
}
