/**
 * 
 *
 */
package fr.wcs.spring.queteJpaHibernate.utils;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import fr.wcs.spring.queteJpaHibernate.entities.Exploitant;
import fr.wcs.spring.queteJpaHibernate.repositories.ExploitantRepoDao;

/**
 * @author franck Desmedt github/bigmoletos
 *
 */
@Component
public class OutPutter implements CommandLineRunner {
//	Notice the typical CRUD
//	 *         functionality:
//	 * 
//	 *         save(…) – save an Iterable of entities. Here, we can pass multiple
//	 *         objects to save them in a batch 
//	 *         findOne(…) – get a single entity
//	 *         based on passed primary key value 
//	 *         findAll() – get an Iterable of all
//	 *         available entities in database 
//	 *         count() – return the count of total entities in a table 
//	 *         delete(…) – delete an entity based on the passed
//	 *         object exists(…) – verify if an entity exists based on the passed
//	 *         primary key value
	private Logger LOG = LoggerFactory.getLogger("Wilder");

	@Autowired
	ExploitantRepoDao exploitantDao;

	@Override
	public void run(String... args) throws Exception {

		// Checke combien d'objets se trouvent dans la BDD
		LOG.info("******************");
		LOG.info("Objects in DB : " + exploitantDao.count());
		// vide la table avant de commencer
		for (Exploitant myexploitant : exploitantDao.findAll()) {
			exploitantDao.delete(myexploitant);
			LOG.info(myexploitant.toString());
		}

		// Crée un nouvel utilisateur et l'enregistre dans la BDD
		Exploitant exploitant1 = new Exploitant("Francois", "Pignon", LocalDateTime.now(), "b.wildeuse@tit.com", 42);
		LOG.info("******************");
		LOG.info(exploitant1 + " has been created !");
		exploitantDao.save(exploitant1);
		LOG.info(exploitant1 + " has been saved !");

		// Crée un second utilisateur et l'enregistre dans la BDD
		Exploitant exploitant2 = new Exploitant("Juste", "Blanc", LocalDateTime.now(), "jblanc@juste.blanc", 46);
		LOG.info("******************");
		LOG.info(exploitant2 + " has been created !");
		exploitantDao.save(exploitant2);
		LOG.info(exploitant2 + " has been saved !");

		// Lit les informations correspondant au second utilisateur
		Exploitant tempexploitant = exploitantDao.findById(2L).get();
		/*
		 * On écrit "2L" car le type de l'id est Long
		 */
		LOG.info("******************");
		LOG.info("Second exploitant's firstName is " + tempexploitant.getFirstName());
		LOG.info("Second exploitant's lastName is " + tempexploitant.getLastName());
		LOG.info("Second exploitant's age is " + tempexploitant.getDateInscription());
		LOG.info("Second exploitant's age is " + tempexploitant.getEmail());
		LOG.info("Second exploitant's age is " + tempexploitant.getAge());

		// Liste les utilisateurs enregistrés dans la BDD
		LOG.info("******************");
		LOG.info("exploitants in list are ");
		for (Exploitant myexploitant : exploitantDao.findAll()) {
			LOG.info(myexploitant.toString());
		}

		// Supprime le second utilisateur de la BDD
		exploitantDao.deleteById(2L);
		// exploitantDao.deleteById(4L);
		/*
		 * risque de provoquer une erreur si tu n'as pas vidé ta table avant de relancer
		 * ton application !
		 */

		/*
		 * Liste les utilisateurs enregistrés dans la BDD (permet de vérifier que le
		 * second utilisateur a bien été supprimé de la BDD)
		 */
		LOG.info("******************");
		LOG.info("exploitants in list are ");
		for (Exploitant myexploitant : exploitantDao.findAll()) {
			LOG.info(myexploitant.toString());
		}

	}
}
