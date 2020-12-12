package ru.otus.jdbc.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.base.AbstractHibernateTest;
import ru.otus.core.model.Client;
import ru.otus.hibernate.dao.ClientDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Dao для работы с пользователями должно ")
class ClientDaoHibernateTest extends AbstractHibernateTest {

    private SessionManagerHibernate sessionManagerHibernate;
    private ClientDaoHibernate clientDaoHibernate;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        sessionManagerHibernate = new SessionManagerHibernate(sessionFactory);
        clientDaoHibernate = new ClientDaoHibernate(sessionManagerHibernate);
    }

    @Test
    @DisplayName(" корректно загружать пользователя по заданному id")
    void shouldFindCorrectUserById() {
        Client expectedClient = new Client(0, "Вася");
        saveClient(expectedClient);

        assertThat(expectedClient.getId()).isPositive();

        sessionManagerHibernate.beginSession();
        Optional<Client> mayBeClient = clientDaoHibernate.findById(expectedClient.getId());
        sessionManagerHibernate.commitSession();

        assertThat(mayBeClient).isPresent().get().isEqualToComparingFieldByField(expectedClient);
    }

    @DisplayName(" корректно сохранять пользователя")
    @Test
    void shouldCorrectSaveUser() {
        Client expectedClient = new Client(0, "Вася");
        sessionManagerHibernate.beginSession();
        clientDaoHibernate.insertOrUpdate(expectedClient);
        long id = expectedClient.getId();
        sessionManagerHibernate.commitSession();

        assertThat(id).isGreaterThan(0);

        Client actualClient = loadClient(id);
        assertThat(actualClient).isNotNull().hasFieldOrPropertyWithValue("name", expectedClient.getName());

        expectedClient = new Client(id, "Не Вася");
        sessionManagerHibernate.beginSession();
        clientDaoHibernate.insertOrUpdate(expectedClient);
        long newId = expectedClient.getId();
        sessionManagerHibernate.commitSession();

        assertThat(newId).isGreaterThan(0).isEqualTo(id);
        actualClient = loadClient(newId);
        assertThat(actualClient).isNotNull().hasFieldOrPropertyWithValue("name", expectedClient.getName());

    }

    @DisplayName(" возвращать менеджер сессий")
    @Test
    void getSessionManager() {
        assertThat(clientDaoHibernate.getSessionManager()).isNotNull().isEqualTo(sessionManagerHibernate);
    }
}
