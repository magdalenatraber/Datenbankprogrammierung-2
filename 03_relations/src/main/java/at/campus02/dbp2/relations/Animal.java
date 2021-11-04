package at.campus02.dbp2.relations;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Animal {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    @ManyToOne
    private Species species;

    public List<Country> getCountries() {
        return countries;
    }

    @ManyToMany
    private final List<Country> countries = new ArrayList<>();

    @OneToOne
    private Student owner;

    public Animal() {

    }

    public Student getOwner() {
        return owner;
    }

    public void setOwner(Student owner) {
        this.owner = owner;
    }


    // default constructor - im Fall von JPA definieren


    public Animal(String name) {
        setName(name);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return Objects.equals(id, animal.id) &&
                Objects.equals(name, animal.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
