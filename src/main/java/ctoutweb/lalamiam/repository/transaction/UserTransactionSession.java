package ctoutweb.lalamiam.repository.transaction;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.repository.UserRepository;
import ctoutweb.lalamiam.repository.entity.UserEntity;
import jakarta.persistence.EntityManagerFactory;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
  private static final Logger LOGGER = LogManager.getLogger();
  private final EntityManagerFactory entityManagerFactory;
  private final UserRepository userRepository;
  private final RoleUserRepository roleUserRepository;

  public UserTransactionSession(
          EntityManagerFactory entityManagerFactory,
          UserRepository userRepository,
          RoleUserRepository roleUserRepository
  ) {
    this.entityManagerFactory = entityManagerFactory;
    this.userRepository = userRepository;
    this.roleUserRepository = roleUserRepository;
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
      Query<UserEntity> query = session.createQuery("FROM UserEntity u WHERE LOWER(u.email) = :email", UserEntity.class);
      query.setParameter("email", email);
      user = query.uniqueResult();

      // Si pas de commande
      if(user == null) return null;

      Hibernate.initialize(user.getRoles());
      transaction.commit();
    } catch (Exception ex) {
      LOGGER.error(ex.getMessage());
    } finally {
      session.close();
      return user;
    }
  }

  @Transactional
  public UserEntity registerUser(String email, String password) {
    // Role USer
    final int USER_ROLE = 1;

    // Données a sauvgarder
    UserEntity userToSave = Factory.getUSerWithEmailPassword(email, password);

    // Sauvegarde utilisateur
    UserEntity saveUser = userRepository.save(userToSave);

    // Sauvegarde du ROLE
    roleUserRepository.save(Factory.createRoleUser(USER_ROLE, saveUser.getId()));

    // Renvoie les données utilisateurs
    return getUserInformationById(saveUser.getId());
  }
}
