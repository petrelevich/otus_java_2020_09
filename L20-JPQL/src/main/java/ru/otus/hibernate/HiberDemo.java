package ru.otus.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hibernate.model.Person;
import ru.otus.hibernate.model.Phone;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sergey
 * created on 08.10.18.
 */

public class HiberDemo {
    private static final Logger logger = LoggerFactory.getLogger(HiberDemo.class);

    private static final String URL = "jdbc:h2:mem:testDB;DB_CLOSE_DELAY=-1";
    private final SessionFactory sessionFactory;

    public static void main(String[] args) {
        var demo = new HiberDemo();
        //      demo.entityExample();
        //      demo.leakageExample();
        //      demo.fetchExample();
        //      demo.JPQLexample();
        //      demo.deleteFrom();
        //      demo.nativeExample();
    }

    private HiberDemo() {
        var configuration = new Configuration()
                .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect")
                .setProperty("hibernate.connection.url", URL)
                .setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/demoDB")
                .setProperty("hibernate.connection.username", "usr")
                .setProperty("hibernate.connection.password", "pwd")

                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.hbm2ddl.auto", "create")
                .setProperty("hibernate.generate_statistics", "true");

        var serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        var metadata = new MetadataSources(serviceRegistry)
                .addAnnotatedClass(Person.class)
                .addAnnotatedClass(Phone.class)
                .getMetadataBuilder()
                .build();

        sessionFactory = metadata.getSessionFactoryBuilder().build();
    }

    private void entityExample() {
        try (var session = sessionFactory.openSession()) {
            var person = new Person();
            person.setName("Ivan");
            person.setNickName("Durak");
            person.setAddress("derv str");
            session.persist(person);
            logger.info("persisted person:{}", person);

            var selected = session.load(Person.class, person.getId());
            logger.info("selected: {}", selected);
            logger.info(">>> updating >>>");

            Transaction transaction = session.getTransaction();
            transaction.begin();
            person.setAddress("moved street");
            transaction.commit();

            Person updated = session.load(Person.class, person.getId());
            logger.info("updated: {}", updated);

            session.detach(updated);

            logger.info(">>> updating detached>>>");

            Transaction transactionDetached = session.getTransaction();
            transactionDetached.begin();
            updated.setAddress("moved street NOT CHANGED");
            transactionDetached.commit();

            Person notUpdated = session.load(Person.class, person.getId());
            logger.info("notUpdated: {}", notUpdated);
        }
    }

    private void leakageExample() {
        try (var session = sessionFactory.openSession()) {
            var transaction = session.getTransaction();
            transaction.begin();

            var person = new Person();
            person.setName("Ivan");
            person.setNickName("Durak");
            person.setAddress("derv str");
            session.persist(person);
            logger.info("person:{}", person);

            transaction.commit();

            //session.detach(person);
            deepInIn(person);

            var selected = session.load(Person.class, person.getId());
            logger.info("selected: {}", selected);
        }
    }

    //Далекая часть программы
    private void deepInIn(Person person) {
        Person jon = person;
        jon.setName("jon");
        logger.info("jon: {}", jon);
    }

    private void fetchExample() {
        long personId;
        try (var session = sessionFactory.openSession()) {
            personId = createPerson(session);
        }
        Person selectedPerson;
        try (var session = sessionFactory.openSession()) {
            logger.info("before phone load");
            var selectedPhone = session.load(Phone.class, 3L);
            var selectedPhone2 = session.load(Phone.class, 3L);
            logger.info("after phone load");
            logger.info(">>>>>>>>>>>>>>>>>>>>>>>   selectedPhone: {}", selectedPhone);

            selectedPerson = session.load(Person.class, personId);
            //selectedPerson = session.get(Person.class, personId); //для НЕленивой загрузки
            logger.info(">>>>>>>>>>>>>>>>>>>>>>>   selectedPerson loaded");
            // } // сессия закрылась раньше, чеем мы воспользовались объектом.

            logger.info(">>>>>>>>>>>>>>>>>>>>>>>  selected person: {}", selectedPerson.getName());
            logger.info("phones:{}", selectedPerson.getPhones());
        }
    }


    private long createPerson(Session session) {
        var transaction = session.getTransaction();
        transaction.begin();

        var person = new Person();
        person.setName("Ivan");
        person.setNickName("Durak");
        person.setAddress("derv str");

        var listPhone = new ArrayList<Phone>();
        for (int idx = 0; idx < 5; idx++) {
            listPhone.add(new Phone("+" + idx, person));
        }
        person.setPhones(listPhone);
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>   persist...");
        session.save(person);
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>   commit...");

        // должны выполниться 1 insert для person и 5 для phone, update быть не должно
        transaction.commit();
        return person.getId();
    }

    private void JPQLexample() {
        try (var session = sessionFactory.openSession()) {
            createPerson(session);
        }

        var entityManager = sessionFactory.createEntityManager();

        logger.info("select phone list:");

        List<Phone> selectedPhones = entityManager.createQuery(
                "select p from Phone p where p.id > :paramId", Phone.class)
                .setParameter("paramId", 2L)
                .getResultList();

        logger.info("selectedPhones:{}", selectedPhones);


        Person person = entityManager
                .createNamedQuery("get_person_by_id", Person.class)
                .setParameter("id", 1L)
                .getSingleResult();

        logger.info("selected person:{}", person.getNickName());

        var builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> criteria = builder.createQuery(Person.class);
        Root<Person> root = criteria.from(Person.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get("id"), 1L));

        Person personCriteria = entityManager.createQuery(criteria).getSingleResult();
        logger.info("selected personCriteria:{}", personCriteria.getNickName());
        logger.info("selected personCriteria, Phones:{}", personCriteria.getPhones());
    }

    //https://www.baeldung.com/delete-with-hibernate
    //Deletion Using a JPQL Statement
    private void deleteFrom() {
        try (var session = sessionFactory.openSession()) {
            var transaction = session.getTransaction();
            transaction.begin();
            Person person = new Person();
            person.setName("Ivan");
            person.setNickName("Durak");
            person.setAddress("derv str");

            session.save(person);
            transaction.commit();
        }

        var personId = 1L;
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var loadedPerson = session.get(Person.class, personId); //загружаем в конекст, тут это важно
            logger.info("loadedPerson:{}", loadedPerson);

            var query = session.createQuery("delete from Person u where u.id = ?1");
            query.setParameter(1, personId);
            query.executeUpdate();

            Person deletedPerson = session.get(Person.class, personId);
            logger.info("deletedPerson:{}", deletedPerson);

            session.getTransaction().commit();

            Person reLoadedPerson = session.get(Person.class, personId);
            logger.info("reLoadedPerson:{}", reLoadedPerson);
        }
    }

    private void nativeExample() {
        try (var session = sessionFactory.openSession()) {
            createPerson(session);
        }

        try (var session = sessionFactory.openSession()) {
            String name = session.doReturningWork(connection -> {
                try (var ps = connection.prepareStatement("select name from tPerson where id = ?")) {
                    ps.setLong(1, 1L);
                    try (var rs = ps.executeQuery()) {
                        rs.next();
                        return rs.getString("name");
                    }
                }
            });
            logger.info("sqL name: {}", name);
        }
    }
}

