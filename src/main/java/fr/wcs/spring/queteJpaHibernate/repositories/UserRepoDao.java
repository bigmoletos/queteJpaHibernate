/**
 * 
 *
 */
package fr.wcs.spring.queteJpaHibernate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.wcs.spring.queteJpaHibernate.entities.User;

/**
 * @author franck Desmedt github/bigmoletos
 *
 */
@Repository
public interface UserRepoDao extends JpaRepository<User, Long> {

}
