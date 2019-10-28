/**
 * 
 *
 */
package fr.wcs.spring.queteJpaHibernate.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import fr.wcs.spring.queteJpaHibernate.entities.User;
import fr.wcs.spring.queteJpaHibernate.repositories.UserRepoDao;

/**
 * @author franck Desmedt github/bigmoletos
 *
 */
@Component
public class OutPutter implements CommandLineRunner {

	private Logger LOG = LoggerFactory.getLogger("Wilder");

	@Autowired
	UserRepoDao userDao;

	@Override
	public void run(String... args) throws Exception {

		// Checke combien d'objets se trouvent dans la BDD
		LOG.info("******************");
		LOG.info("Objects in DB : " + userDao.count());

		// Crée un nouvel utilisateur et l'enregistre dans la BDD
		User user1 = new User("Brenda", "Wildeuse", 19);
		LOG.info("******************");
		LOG.info(user1 + " has been created !");
		userDao.save(user1);
		LOG.info(user1 + " has been saved !");

		// Crée un second utilisateur et l'enregistre dans la BDD
		User user2 = new User("Brandon", "Wilder", 33);
		LOG.info("******************");
		LOG.info(user2 + " has been created !");
		userDao.save(user2);
		LOG.info(user2 + " has been saved !");

		// Lit les informations correspondant au second utilisateur
		User tempUser = userDao.findById(2L).get(); /*
													 * On écrit "2L" car le type de l'id est Long
													 */
		LOG.info("******************");
		LOG.info("Second user's firstName is " + tempUser.getFirstName());
		LOG.info("Second user's lastName is " + tempUser.getLastName());
		LOG.info("Second user's age is " + tempUser.getAge());

		// Liste les utilisateurs enregistrés dans la BDD
		LOG.info("******************");
		LOG.info("Users in list are ");
		for (User myUser : userDao.findAll()) {
			LOG.info(myUser.toString());
		}

		// Supprime le second utilisateur de la BDD
		// userDao.deleteById(2L);
		/*
		 * risque de provoquer une erreur si tu n'as pas vidé ta table avant de relancer
		 * ton application !
		 */

		/*
		 * Liste les utilisateurs enregistrés dans la BDD (permet de vérifier que le
		 * second utilisateur a bien été supprimé de la BDD)
		 */
		LOG.info("******************");
		LOG.info("Users in list are ");
		for (User myUser : userDao.findAll()) {
			LOG.info(myUser.toString());
		}

	}
}
