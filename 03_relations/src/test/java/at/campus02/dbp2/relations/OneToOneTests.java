package at.campus02.dbp2.relations;

import at.campus02.dbp2.relations.Animal;
import at.campus02.dbp2.relations.Student;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class OneToOneTests {
    EntityManagerFactory factory;
    EntityManager manager;

    @BeforeEach
    public  void setup(){
        factory = Persistence.createEntityManagerFactory("persistenceUnitName");
        manager = factory.createEntityManager();
    }

    @AfterEach
    public void teardown(){
        if(manager.isOpen()){
            manager.close();
        }
        if (factory.isOpen()) {
            factory.close();
        }
    }



    @Test
    public void persistAnimalAndStudentStoresRelationInDatabase() {
        // given

        Student student = new Student("Hansi");
        Animal animal = new Animal("Flipper");

        //im Speicher selber um die Referenzen kümmern
        student.setPet(animal);
        animal.setOwner(student);

        // when
        manager.getTransaction().begin();
        manager.persist(student);
        manager.persist(animal);

        manager.clear();

        Animal flipperFromDb = manager.find(Animal.class, animal.getId());
        assertThat(flipperFromDb.getOwner(),Matchers.is(student));
        Student ownerFromDb = manager.find(Student.class, student.getId());
        assertThat(ownerFromDb.getPet(), Matchers.is(animal));
    }
    @Test
    public void persistStudentWithCascadeAlsoPersistsAnimal() {

        Student hansi = new Student("Hansi");
        Animal bunny = new Animal("Bunny");

        bunny.setOwner(hansi);

        hansi.setPet(bunny);

        manager.getTransaction().begin();
        manager.persist(hansi);

        manager.getTransaction().commit();


        manager.clear();

        Animal bunnyFromDb = manager.find(Animal.class, bunny.getId());
        assertThat(bunnyFromDb.getOwner(),Matchers.is(hansi));

        Student hansiFromDb = manager.find(Student.class, hansi.getId());
        assertThat(hansiFromDb.getPet(), Matchers.is(bunny));

    }

    @Test
    public  void refreshClosesReferencesNotHandlesInMemory(){

        Student hansi = new Student("Hansi");
        Animal bunny = new Animal("Bunny");

        bunny.setOwner(hansi);

        manager.getTransaction().begin();
        manager.persist(bunny);
        manager.persist(hansi);
        manager.getTransaction().commit();

        manager.clear();
        Animal bunnyFromDB = manager.find(Animal.class, bunny.getId());
       assertThat(bunnyFromDB.getOwner(),Matchers.is(hansi));
        Student hansiFromDb = manager.find(Student.class, hansi.getId());
        assertThat(hansiFromDb.getPet(), Matchers.is(bunny));
    }


}
