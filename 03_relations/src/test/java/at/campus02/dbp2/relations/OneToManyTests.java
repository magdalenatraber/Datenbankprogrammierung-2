package at.campus02.dbp2.relations;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OneToManyTests {
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
    public void persistSpeciesWithCascade() {
        Animal bunny = new Animal("Bunny");
        Animal flipper = new Animal("Flipper");
        Species mammals = new Species("Mammals");

        bunny.setSpecies(mammals);
        flipper.setSpecies(mammals);
        mammals.getAnimals().add(bunny);
        mammals.getAnimals().add(flipper);

        manager.getTransaction().begin();
        manager.persist(mammals);
        manager.getTransaction().commit();

        manager.clear();

        Species mammalsFromDb = manager.find(Species.class, mammals.getId());
        assertThat(mammalsFromDb.getAnimals().size(), is(2));
        assertThat(mammalsFromDb.getAnimals(), containsInAnyOrder(bunny,flipper));

    }

    @Test
    @Disabled("Only works without orphanRemoval - enable after setting orphanremoval to false")
    public void updateExampleWithCorrectingReferences(){
        Animal clownfish = new Animal("Nemo");
        Animal squirrel = new Animal("Squirrel");
        Species fish = new Species("Fish");

        clownfish.setSpecies(fish);
        squirrel.setSpecies(fish);

        fish.getAnimals().add(clownfish);
        fish.getAnimals().add(squirrel);

        manager.getTransaction().begin();
        manager.persist(fish);
        manager.getTransaction().commit();

        manager.clear();
        //fail
        manager.getTransaction().begin();
        squirrel.setSpecies(null);
        manager.merge(squirrel);
        manager.getTransaction().commit();
        manager.clear();

        Animal squirrelFromDb = manager.find(Animal.class,squirrel.getId());
        assertThat(squirrelFromDb, is(notNullValue()));

        assertThat(squirrelFromDb.getSpecies(), is(nullValue()));

        Species mergeFish   = manager.merge(fish);
        manager.refresh(mergeFish);
        assertThat(mergeFish.getAnimals().size(), is(1));
    }

@Test
    public void orphansRemovalDeletesOrphansFromDatabase(){
    Animal clownfish = new Animal("Nemo");
    Animal squirrel = new Animal("Squirrel");
    Species fish = new Species("Fish");

    clownfish.setSpecies(fish);
    squirrel.setSpecies(fish);

    fish.getAnimals().add(clownfish);
    fish.getAnimals().add(squirrel);

    manager.getTransaction().begin();
    manager.persist(fish);
    manager.getTransaction().commit();

    manager.clear();
    //fail
    manager.getTransaction().begin();
    fish.getAnimals().remove(squirrel);
    manager.merge(fish);
    manager.getTransaction().commit();
    manager.clear();

    Animal squirrelFromDb = manager.find(Animal.class,squirrel.getId());
    assertThat(squirrelFromDb,is(nullValue()));

    Species refreshedFish = manager.merge(fish);
    manager.refresh(refreshedFish);

    assertThat(refreshedFish.getAnimals().size(),is(1));

}



}
