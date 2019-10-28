/**
 * 
 *
 */
package fr.wcs.spring.queteJpaHibernate.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author franck Desmedt github/bigmoletos
 *
 */
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/*
	 * Le type Long permet de prévoir le cas où l'id aurait une valeur importante
	 */

	private String firstName;

	private String lastName;

	private int age;

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", age=" + age + "]";
	}
	/*
	 * Permet d'afficher les attributs de l'objet lors de l'invocation de sa méthode
	 * toString()
	 */

	public User() {

	}

	public User(String firstName, String lastName, int age) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
	}

	public Long getId() {
		return id;
	}

	/* On ne définit pas de setId() car l'id sera généré automatiquement */

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}
