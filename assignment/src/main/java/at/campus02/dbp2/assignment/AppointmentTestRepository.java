package at.campus02.dbp2.assignment;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppointmentTestRepository implements AppointmentRepository {

    private EntityManager manager;

    public AppointmentTestRepository(EntityManagerFactory factory) {

        this.manager = factory.createEntityManager();
    }


    @Override
    public boolean create(Customer customer) {
        if (customer == null) {
            return false;
        }
        if(customer.getEmail() == null) {
            return false;
        }

        if (customer.getEmail().equals(read(customer.getEmail()).getEmail())) {
            return false;
        }

        manager.getTransaction().begin();
        manager.persist(customer);
        manager.getTransaction().commit();
        return true;
    }

    @Override
    public Customer read(String email) {
        if(email == null)
        return null;

        return manager.find(Customer.class, email);
    }

    @Override
    public Customer update(Customer customer) {
        if(customer == null)
        return null;
        if (read(customer.getEmail()) == null)
            throw new IllegalArgumentException("Customer does not exist, cannot update!");

        manager.getTransaction().begin();
        Customer managed = manager.merge(customer);
        manager.getTransaction().commit();
        return managed;

    }

    @Override
    public boolean delete(Customer customer) {
        if (customer == null)
        return false;

        if (read(customer.getEmail()) == null)
            throw new IllegalArgumentException("Cannot find Customer");
        manager.getTransaction().begin();
        manager.remove(manager.merge(customer));
        manager.getTransaction().commit();
        return true;
    }

    @Override
    public boolean create(Provider provider) {
        if (provider == null)
            return false;

        if(provider.getId() == null)
            return false;
        if (provider.getId() == read(provider.getId()).getId())
            return false;


        manager.getTransaction().begin();
        manager.persist(provider);
        manager.getTransaction().commit();
        return true;
    }

    @Override
    public Provider read(Integer id) {
        if(id == null)
            return null;

        return manager.find(Provider.class, id);
    }

    @Override
    public Provider update(Provider provider) {
        if(provider == null)
            return null;
        if (read(provider.getId()) == null)
            throw new IllegalArgumentException("Customer does not exist, cannot update!");

        manager.getTransaction().begin();
        Provider managed = manager.merge(provider);
        manager.getTransaction().commit();
        return managed;
    }

    @Override
    public boolean delete(Provider provider) {
        if (provider == null)
            return false;

        if (read(provider.getId()) == null)
            throw new IllegalArgumentException("Cannot find Customer");
        manager.getTransaction().begin();
        manager.remove(manager.merge(provider));
        manager.getTransaction().commit();
        return true;
    }

    @Override
    public List<Customer> findCustomersBy(String lastname, String firstname) {

        if(lastname == null) {
            throw new IllegalArgumentException("no lastname specified");
        }
        if(firstname == null) {
            TypedQuery<Customer> query = manager.createQuery(
                    "select c from Customer c " +
                            "where c.lastname = :lastname",
                    Customer.class
            );
            query.setParameter("lastname", lastname);
            return query.getResultList();
        }
        TypedQuery<Customer> query = manager.createQuery(
                "select c from Customer c " +
                        "where c.lastname = :lastname" +
                        " and c.firstname = :firstname",
                Customer.class
        );
        query.setParameter("lastname",lastname);
        query.setParameter("firstname", firstname);
        return query.getResultList();


    }

    @Override
    public List<Provider> findProvidersBy(ProviderType type, String addressPart) {
        if(type == null || addressPart == null)
            return Collections.EMPTY_LIST;


        TypedQuery<Provider> query = manager.createQuery(
                "select p from Provider p " +
                        "where p.type = :type" +
                        " and p.address = :addressPart",
                Provider.class
        );
        query.setParameter("type", type);
        query.setParameter("addressPart", type);
        return query.getResultList();
    }

    @Override
    public List<Appointment> findAppointmentsAt(String addressPart) {
        return null;
    }

    @Override
    public List<Appointment> findAppointments(LocalDateTime from, LocalDateTime to) {
        return null;
    }

    @Override
    public List<Appointment> getAppointmentsFor(Customer customer) {
        return null;
    }

    @Override
    public boolean reserve(Appointment appointment, Customer customer) {
        return false;
    }

    @Override
    public boolean cancel(Appointment appointment, Customer customer) {
        return false;
    }

    @Override
    public void close() {
    manager.close();
    }
}
