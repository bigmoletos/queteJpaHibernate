Challenge
From Spring to MySQL
Spring : JPA et Hibernate
Dans cette quête, tu vas apprendre à créer une application Spring rudimentaire connectée à une base de données MySQL. Nous utiliserons Hibernate (et Spring Data JPA - pas de panique si le terme ne te dit rien pour l'instant, c'est normal) pour accéder à la base de données et effectuer les quatre opérations fondamentales de stockage de données : Create Read Update Delete (CRUD).

Objectifs

•
Comprendre le principe de Spring Data JPA et Hibernate

•
Savoir configurer un projet Spring Boot pour le lier à une base de données

•
Savoir créer les éléments nécessaires à la réalisation d'un CRUD dans une application Spring Boot

Challenge ️🕹️
Pour valider cette quête tu devras résoudre le challenge: From Spring to MySQL. Le principe du challenge est détaillé dans l’onglet Challenge.

voir le challenge
Un peu de théorie

Comme tu le sais déjà, Java est un langage orienté objet, c'est-à-dire que toutes les données sont stockées dans des objets. Une application Spring étant avant tout une application Java, la même principe s'applique pour elle.

Une base de données (BDD), elle, stocke les données dans des tables.

Pour que les données transitent sans encombres entre ton application Spring et ta BDD, il est donc nécessaire d'établir des correspondances entre les données de tes objets et les données de tes tables. En JDBC, tu as fait cela de manière relativement artisanale, en détaillant tes requêtes.

Mais ce serait quand même pratique si on pouvait automatiser tout ça, non ?

Exact ! C'est justement pour cela qu'a été créée la spécification Java Persistence API (JPA de son petit nom). En elle-même, JPA ne fournit aucune implémentation (aucun code concret), juste des normes à respecter, sous forme d'interfaces.

Ces interfaces sont concrètement implémentées par de nombreuses solutions. Dans cette quête, tu manipuleras deux d'entre elles : Spring Data JPA (que tu vas découvrir) et Hibernate (que tu connais déjà).

Pour rappel, Hibernate est une solution d'Object-Relational Mapping (ORM), c'est-à-dire un programme qui joue le rôle d'intermédiaire entre ton application et ta BDD.

Introduction to JPA using Spring Boot Data
Pour en savoir plus sur comment on est passé de *JDBC* à *JPA*, lis cet article jusqu'à la partie *Step-by-step Code example*.
https://dzone.com/articles/introduction-to-jpa-using-spring-boot-data-jpa
Configurer son projet
1. Créer un projet Spring Boot

À partir de Spring Initializr, crée un nouveau projet avec :

•
group : com.wildcodeschool.example

•
artifact : springHibernateExample

•
dependencies : Web (comporte toute la configuration de départ nécessaire pour un projet web), JPA (correspond à Spring Data JPA, que nous allons utiliser) et MySQL(c'est le driver MySQL JDBC).


Télécharge le projet généré, dézippe-le et importe-le dans Eclipse.

Avant de continuer, voici la structure finale du projet, à laquelle tu aboutiras une fois la quête terminée :


Nous te conseillons de l'ouvrir dans un onglet de ton navigateur afin de t'y référer au fur et à mesure, pour savoir dans quel package créer telle ou telle classe ou interface.

(Pour avoir la même vue du squelette de ton projet que sur la capture d'écran, sélectionne "hierarchical view" pour tes packages, cf. ressources.)

2. Créer la BDD

Pour créer la BDD que nous allons utiliser, tape dans ton terminal :

-- Crée une nouvelle BDD
mysql> CREATE DATABASE example_db;

-- Crée un nouvel utilisateur (tu peux bien sûr remplacer exampleuser)
mysql> CREATE USER 'exampleuser'@'localhost' IDENTIFIED BY 'exampleuser'; 

-- Attribue à l`utilisateur tous les privilèges pour la BDD créée
mysql> GRANT ALL ON example_db.* TO 'exampleuser'@'localhost'; 
Tu peux vérifier que la BDD a bien été créée via ton client SQL préféré (phpMyAdmin ou MySQL Workbench, par exemple).

3. Configurer la liaison entre Spring et la BDD

Dans src/main/resources, trouve le fichier application.properties. Celui-ci est vide pour l'instant. Remplissons-le !

# Informations sur la BDD à exploiter
# ajouter "?serverTimezone=GMT" à l'url est obligatoire pour MySQL 5
spring.datasource.url = jdbc:mysql://localhost:3306/example_db?serverTimezone=GMT
spring.datasource.username = exampleuser
spring.datasource.password = exampleuser

# Autoriser l'affichage des requêtes SQL faites par Hibernate
spring.jpa.show-sql=true

# Gérer Hibernate
spring.jpa.hibernate.ddl-auto = update

# Sélectionner un "dialecte" permet à Hibernate de générer du SQL adapté à la verison choisie
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5InnoDBDialect
Note que l'utilisation de spring.jpa.hibernate.ddl-auto aura pour conséquence le fait que les tables de ta BDD seront automatiquement créées à partir des données de ton application Spring. Cette propriété peut prendre différentes valeurs (create, create-drop, update ou none). Ici, le fait d'utiliser update permet d'indiquer que l'on souhaite que tout changement de données effectué dans notre application soit répercuté sur la BDD à chaque fois qu'on relance notre application.

Hierachical view for packages
https://stackoverflow.com/questions/3915961/how-to-view-hierarchical-package-structure-in-eclipse-package-explorer/3916062#3916062
Common application properties
Pour information, répertorie toutes les (nombreuses) propriéties qu'on peut définir dans `applications.properties`.
https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html
Définir une entité
Maintenant que la configuration est faite, passons au code !

Pour commencer, tu vas créer un objet Java tout simple pour représenter un utilisateur :

package com.wildcodeschool.example.springHibernateExample.entities;

public class User {

    private Long id;    /* Le type Long permet de prévoir 
                        le cas où l'id aurait une valeur importante */

    private String firstName;

    private String lastName;

    private int age;

    @Override
    public String toString() {
        return "User [id=" + id + ", firstName=" + firstName + 
                ", lastName=" + lastName + ", age=" + age + "]";
    }           
    /* Permet d'afficher les attributs de l'objet lors de l'invocation 
    de sa méthode toString() */                

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
Java
Pour transformer cet objet Java en entité (entity), c'est-à-dire en quelque chose de convertissable en table de BDD, on va l'annoter :

package com.wildcodeschool.example.springHibernateExample.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

    public User() {
    }

    public User(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    private String firstName;

    private String lastName;

    private int age;

    @Override
    public String toString() { 
        return "User [id=" + id + ", firstName=" + firstName + 
                ", lastName=" + lastName + ", age=" + age + "]";
    }                   

    public Long getId() {
        return id;
    }

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
Java
L'annotation @Entity indique que ton objet doit être géré par Spring Data et que ses attributs doivent être convertis en colonnes de tables de BDD.

@Id indique que l'attribut devra être utilisé en tant que clé primaire (id) dans la table de BDD.

@GeneratedValue(strategy = GenerationType.IDENTITY) indique que la valeur de l'id sera générée automatiquement, par incrémentation, lors de l'insertion dans la BDD. Cette annotation peut prendre diverses valeurs (cf. ressources), selon ce dont tu as besoin.

Utilisation de l’annotation @GeneratedValue
https://blog.axopen.com/2014/02/utilisation-de-lannotation-generatedvalue/
Définir un DAO
Tu as un objet et tu as une BDD ; il s'agit maintenant de créer un élément qui va pouvoir faire le pont entre les deux. Cet élément, c'est un Data Access Object (DAO). Dans une application, il est le seul à être en contact direct avec le système de stockage. Pour en savoir davantage, consulte la ressource Le modèle DAO.

Pour faire notre pont, on va créer une interface UserDao, qui hérite d'une interface JpaRepository déjà existante dans Spring Data. Si tu vas consulter la doc officielle de Spring (cf. ressources), tu verras que cette interface contient des signatures de méthodes correspondant à des opérations liées aux BDD (notamment save, delete, etc.). Puisque l'interface UserDao en est l'enfant, elle hérite de ses signatures de méthodes.

NB : JpaRepository hérite dePagingAndSortingRepository, lui-même enfant de CrudRepository. En pratique, cela signifie simplement qu'elle définit davantage de fonctionnalités que ses parents. Néanmoins, dans certaines situations, il pourra t'arriver d'avoir seulement besoin d'utiliser l'interface PagingAndSortingRepository ou CrudRepository. Consulte les ressources pour en savoir davantage.

NB bis : Nous avons utilisé le nom UserDao pour plus de clarté. Néanmoins, dans l'univers Spring, il est fréquent que ce type de classes s'intitule plutôt QuelqueChoseRepository (ce serait UserRepository ici).

package com.wildcodeschool.example.springHibernateExample.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.wildcodeschool.example.springHibernateExample.entities.User;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
}
Java
@Repository indique à Spring que cette classe est le lien entre notre application et la BDD (pour en savoir plus, va lire la ressource Spring Bean Annotations).

Les paramètre <User, Long> signalent que notre interface sera chargée de gérer notre entité User, et que l'identifiant unique de User (son id) est de type Long.

Les interfaces, c'est bien joli, mais où est le code ? Le vrai, le concret, celui qui implémente les méthodes des interfaces ?

C'est ici que la magie de Spring Data JPA opère. En effet, c'est le framework qui va générer automatiquement une implémentation pour notre interface.


(Oui, nous aussi ça nous a fait cet effet-là, la première fois qu'on en a entendu parler.)

Spring Bean Annotations
Un article bien sympathique pour voir ou revoir les principales annotations utilisées dans Spring
CrudRepository, JpaRepository, and PagingAndSortingRepository in Spring Data
JpaRepository - doc officielle Spring
https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html
Le modèle DAO
https://openclassrooms.com/fr/courses/626954-creez-votre-application-web-avec-java-ee/624784-le-modele-dao
Effectuer les opérations CRUD
Comme on est ici dans un exemple simple, tu ne vas pas créer de controller pour faire parler ton application Spring avec ta BDD. À la place, tu vas créer une classe avec une méthode permettant d'afficher un output dans ton terminal. Pour ce faire, cette classe doit implémenter l'interface CommandLineRunner, propre à Spring Boot et utilisée pour indiquer que le bean (c'est-à-dire l'objet créé à partir de ta classe ; pour que Spring reconnaisse ta classe en tant que classe pouvant produire un bean, n'oublie pas de l'annoter avec @Component) doit être exécuté dès que le contexte de ton application Spring Boot est initialisé. CommandLineRunner ne définit qu'une seule méthode, run, que nous allons overrider.

package com.wildcodeschool.example.springHibernateExample.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Outputter implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

    }
}
Java
Pour afficher l'output dans ton terminal, tu vas créer un objet de type Logger, en mettant en paramètre ton nom (ici, Wilder). Passons ici les détails de cette manipulation, qui sort du cadre de la quête. Note simplement que cet objet possède une méthode info(), qui va permettre d'afficher l'output souhaité dans ton terminal, ainsi que ton nom (ici, Wilder), ce qui est très utile pour distinguer les choses lorsqu'il y a beaucoup d'informations affichées.

package com.wildcodeschool.example.springHibernateExample.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Outputter implements CommandLineRunner {

    private Logger LOG = LoggerFactory.getLogger("Wilder");

    @Override
    public void run(String... args) throws Exception {
        LOG.info("******************");
    }   
}
Java
Attaquons les choses sérieuses.

Définis un attribut userDao. Grâce à @Autowired, Spring va créer tout seul le bean nécessaire en implémentant l'interface UserDao.

Invoque ensuite les différentes méthodes de l'objet userDao pour réaliser la fonctionnalité voulue. Toutes les méthodes définies dans l'interface JpaRepository te sont disponibles.

Commençons pour créer un nouvel utilisateur :

package com.wildcodeschool.example.springHibernateExample.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.wildcodeschool.example.springHibernateExample.entities.User;
import com.wildcodeschool.example.springHibernateExample.repositories.UserDao;

@Component
public class Outputter implements CommandLineRunner {

    private Logger LOG = LoggerFactory.getLogger("Wilder");

    @Autowired
    private UserDao userDao;

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
    }   
}
Java
Dans ton terminal, en te plaçant dans le dossier de ton projet et en faisant mvn spring-boot:run, tu peux exécuter ton application et voir que celle-ci fonctionne. Tu constateras également que les logs de ta classe Outputter sont entremêlés de logs correspondant aux requêtes gérées de manière sous-jacente par Hibernate :

Hibernate: select count(*) as col_0_0_ from user user0_
Wilder: Objects in DB : 0
Wilder: ******************
Wilder: User [id=null, firstName=Brenda, lastName=Wildeuse, age=19] has been created !
Hibernate: select next_val as id_val from hibernate_sequence for update
Hibernate: update hibernate_sequence set next_val= ? where next_val=?
Hibernate: insert into user (age, first_name, last_name, id) values (?, ?, ?, ?)
Wilder: User [id=1, firstName=Brenda, lastName=Wildeuse, age=19] has been saved !
Tu peux aussi vérifier que les données ont bien été enregistrées dans ta BDD via ton client MySQL (phpMyAdmin, par exemple).

Attention, à chaque fois que tu vas runner ton application, les données vont s'insérer dans ta BDD. Pour éviter d'avoir quatre-vingt Brenda dans ta BDD et/ou des erreurs en pagaille, vide ta table avant de relancer ton application (la commande TRUNCATE est ton ami).

Tu viens de tester la fonctionnalité Create de ton application.

Teste les fonctionnalités restantes (Read, Update, Delete) :

package com.wildcodeschool.example.springHibernateExample.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.wildcodeschool.example.springHibernateExample.entities.User;
import com.wildcodeschool.example.springHibernateExample.repositories.UserDao;

@Component
public class Outputter implements CommandLineRunner {

    private Logger LOG = LoggerFactory.getLogger("Wilder");

    @Autowired
    private UserDao userDao;

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
        User tempUser = userDao.findById(2L).get(); /* On écrit "2L" car 
                                                       le type de l'id est Long */
        LOG.info("******************");
        LOG.info("Second user's firstName is " + tempUser.getFirstName());
        LOG.info("Second user's lastName is " + tempUser.getLastName());
        LOG.info("Second user's age is " + tempUser.getAge());

        // Liste les utilisateurs enregistrés dans la BDD
        LOG.info("******************");
        LOG.info("Users in list are ");
        for(User myUser : userDao.findAll()) {
            LOG.info(myUser.toString());
        };

        // Supprime le second utilisateur de la BDD
        userDao.deleteById(2L); /* risque de provoquer une erreur si 
                                tu n'as pas vidé ta table avant de relancer 
                                ton application ! */

        /*  Liste les utilisateurs enregistrés dans la BDD
            (permet de vérifier que le second utilisateur
            a bien été supprimé de la BDD) */
        LOG.info("******************");
        LOG.info("Users in list are ");
        for(User myUser : userDao.findAll()) {
            LOG.info(myUser.toString());
        };
    }   
}

