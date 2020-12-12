package ru.otus.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.base.AbstractHibernateTest;
import ru.otus.core.dao.ClientDao;
import ru.otus.core.model.Client;
import ru.otus.core.service.DBServiceClient;
import ru.otus.core.service.DbServiceClientImpl;
import ru.otus.hibernate.dao.ClientDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Демо работы с hibernate (с абстракциями) должно ")
class WithAbstractionsTest extends AbstractHibernateTest {

    private DBServiceClient dbServiceClient;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        ClientDao clientDao = new ClientDaoHibernate(sessionManager);
        dbServiceClient = new DbServiceClientImpl(clientDao);
    }

    @Test
    @DisplayName(" корректно сохранять пользователя")
    void shouldCorrectSaveClient() {
        Client savedClient = buildDefaultClient();
        long id = dbServiceClient.saveClient(savedClient);
        Client loadedClient = loadClient(id);

        assertThat(loadedClient).isNotNull().usingRecursiveComparison().isEqualTo(savedClient);

        System.out.println(savedClient);
        System.out.println(loadedClient);
    }

    @Test
    @DisplayName(" корректно загружать пользователя")
    void shouldLoadCorrectClient() {
        Client savedClient = buildDefaultClient();
        saveClient(savedClient);

        Optional<Client> mayBeUser = dbServiceClient.getClient(savedClient.getId());

        assertThat(mayBeUser).isPresent().get().usingRecursiveComparison().isEqualTo(savedClient);

        System.out.println(savedClient);
        mayBeUser.ifPresent(System.out::println);
    }

    @Test
    @DisplayName(" корректно изменять ранее сохраненного пользователя")
    void shouldCorrectUpdateSavedClient() {
        Client savedClient = buildDefaultClient();
        saveClient(savedClient);

        Client savedClient2 = new Client(savedClient.getId(), TEST_CLIENT_NEW_NAME);
        long id = dbServiceClient.saveClient(savedClient2);
        Client loadedClient = loadClient(id);

        assertThat(loadedClient).isNotNull().usingRecursiveComparison().isEqualTo(savedClient2);

        System.out.println(savedClient);
        System.out.println(savedClient2);
        System.out.println(loadedClient);
    }

}
