package ru.otus.demo;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.proxy.HibernateProxy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.otus.base.AbstractHibernateTest;
import ru.otus.core.model.Client;

import javax.persistence.Query;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Демо работы с hibernate (без абстракций) должно ")
class WithoutAbstractionsTest extends AbstractHibernateTest {

    @DisplayName(" корректно сохранять и загружать клиента выполняя заданное кол-во запросов в нужное время")
    @ParameterizedTest(name = "клиент отключен от контекста (detached) перед загрузкой: {0}")
    @ValueSource(booleans = {false, true})
    void shouldCorrectSaveAndLoadClientWithExpectedQueriesCount(boolean clientDetachedBeforeGet) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Client savedClient = buildDefaultClient();

            session.save(savedClient);
            // Не должно быть выполнено ни одной вставки в БД не смотря на то что метод save был вызван
            assertThat(getUsageStatistics().getInsertCount()).isZero();

            session.getTransaction().commit();
            // Реальная вставка произошла в момент коммита транзакции (А что если GenerationType.IDENTITY?)
            assertThat(getUsageStatistics().getInsertCount()).isEqualTo(1);

            // Мы не ожидаем ни одного обращения к БД для загрузки клиента если он не отсоединен от контекста
            long expectedLoadCount = 0;
            if (clientDetachedBeforeGet) {
                session.detach(savedClient);
                // И ожидаем обращения к БД для загрузки если клиента в контексте нет
                expectedLoadCount = 1;
            }

            Client loadedClient = session.get(Client.class, savedClient.getId());

            // Проверка, что количество загрузок из БД соответствует ожиданиям
            assertThat(getUsageStatistics().getLoadCount()).isEqualTo(expectedLoadCount);
            // И что мы достали того же клиента, что сохраняли
            assertThat(loadedClient).isNotNull().usingRecursiveComparison().isEqualTo(savedClient);
        }
    }

    @DisplayName(" корректно сохранять клиента в одной сессии и загружать в другой выполнив один запрос к БД для загрузки")
    @Test
    void shouldCorrectSaveAndLoadClientWithExpectedQueriesCountInTwoDifferentSessions() {
        Client savedClient = buildDefaultClient();
        // Сохранили клиента в отдельной сессии
        saveClient(savedClient);

        try (Session session = sessionFactory.openSession()) {
            // Загрузка клиента в отдельной сессии
            Client loadedClient = session.get(Client.class, savedClient.getId());

            // Проверка, что для получения клиента было сделано обращение к БД
            // (т.е. клиент не сохранился в контексте при смене сессии)
            assertThat(getUsageStatistics().getLoadCount()).isEqualTo(1);

            // И что мы достали того же клиента, что сохраняли
            assertThat(loadedClient).isNotNull().usingRecursiveComparison().isEqualTo(savedClient);
        }
    }

    @DisplayName(" показывать в каких случаях загруженный с помощью load объект является прокси для сценария: ")
    @ParameterizedTest(name = "{2}")
    @CsvSource({"true, false, клиент не существует ",
            "false, false, клиент существует и он persistent",
            "false, true, клиент существует и он detached"})
        void shouldLoadProxyObjectWithLoadMethod(boolean loadedNotExistingClient, boolean clientDetachedBeforeLoad, String scenarioDescription) {

        Client savedClient = buildDefaultClient();
        try (Session session = sessionFactory.openSession()) {
            // Сохранили клиента в рамках текущей сессии
            saveClient(session, savedClient);

            if (clientDetachedBeforeLoad) {
                // Отсоединили клиента от контекста если это нужно по текущему сценарию
                session.detach(savedClient);
            }

            // Если по сценарию нужно загружать клиента не существующего в БД, выставляем id=-1
            long id = loadedNotExistingClient ? -1L : savedClient.getId();
            Client loadedClient = session.load(Client.class, id);

            // Метод load должен вернуть клиента не зависимо от того, существует ли он в БД или нет
            assertThat(loadedClient).isNotNull();

            if (loadedNotExistingClient || clientDetachedBeforeLoad) {
                // Если загружен не существующий в БД клиент или он был отсоединен от контекста, то загруженный объект д.б. Proxy
                assertThat(loadedClient).isInstanceOf(HibernateProxy.class);

                // Если загружен не существующий в БД клиент обращение к полю должно привести к ObjectNotFoundException
                if (loadedNotExistingClient) {
                    assertThatThrownBy(loadedClient::getName).isInstanceOf(ObjectNotFoundException.class);
                } else {
                    assertThatCode(loadedClient::getName).doesNotThrowAnyException();
                }
            } else {
                assertThat(loadedClient).isNotInstanceOf(HibernateProxy.class).isInstanceOf(Client.class);
            }
        }
    }

    @DisplayName(" показывать что если загрузить, с помощью load, не существующий объект, то с ним можно нормально работать после того, как он был добавлен в БД")
    @Test
    void shouldLoadNotExistingObjectAndWorkWithHimAfterItSaved() {
        Client savedClient = buildDefaultClient();
        Client loadedClient;
        try (Session session = sessionFactory.openSession()) {
            // На момент загрузки такого юзера в БД нет
            loadedClient = session.load(Client.class, 1L);
            // Проверяем, что вернулся прокси
            assertThat(loadedClient).isInstanceOf(HibernateProxy.class);
            // И не произошло обращения к БД
            assertThat(getUsageStatistics().getLoadCount()).isZero();

            // Сохраняем клиента в другой сессии
            saveClient(savedClient);
            // Теперь объект есть в БД. Проверяем что с объектом можно нормально работать
            assertThat(loadedClient.getName()).isEqualTo(TEST_CLIENT_NAME);
            // И в момент обращения к свойству произошла загрузка из БД
            assertThat(getUsageStatistics().getLoadCount()).isEqualTo(1);
        }
    }

    @DisplayName(" показывать, что загруженный с помощью get объект не является прокси")
    @Test
    void shouldLoadNotAProxyObjectWithGetMethod() {
        try (Session session = sessionFactory.openSession()) {
            Client savedClient = buildDefaultClient();
            saveClient(session, savedClient);

            // Загрузка с помощью метода get не существующего в БД клиента должна приводить к возврату null
            assertThat(session.get(Client.class, -1L)).isNull();

            // Метод get для существующего в БД клиента должен вернуть объект клиента не являющегося прокси
            assertThat(session.get(Client.class, savedClient.getId())).isNotNull()
                    .usingRecursiveComparison().isEqualTo(savedClient)
                    .isNotInstanceOf(HibernateProxy.class);
        }
    }

    @DisplayName(" показывать, что несколько обновлений в одной транзакции станут одним запросом к БД")
    @Test
    void shouldExecuteOneUpdateQueryForMultipleUpdateInOneTransaction() {
        try (Session session = sessionFactory.openSession()) {
            Client savedClient = buildDefaultClient();
            // Сохранили клиента в рамках текущей сессии
            saveClient(session, savedClient);

            session.beginTransaction();

            // Изменили имя клиента
            savedClient.setName(TEST_CLIENT_NEW_NAME);
            session.update(savedClient);

            // Еще раз изменили имя клиента
            savedClient.setName(TEST_CLIENT_NAME);
            session.update(savedClient);

            // И еще Еще раз изменили имя клиента
            savedClient.setName(TEST_CLIENT_NEW_NAME2);
            session.update(savedClient);

            session.getTransaction().commit();

            // Проверка, что в итоге был только один запрос к БД на обновление
            assertThat(getUsageStatistics().getUpdateCount()).isEqualTo(1);
        }
    }

    @DisplayName(" показывать, что вызов метода save на detached объекте приводит к генерации нового id")
    @Test
    void shouldGenerateNewIdWhenExecuteSaveMethodOnSameEntity() {
        try (Session session = sessionFactory.openSession()) {
            Client savedClient = buildDefaultClient();
            // Сохранили клиента в рамках текущей сессии
            saveClient(session, savedClient);
            // Запомнили его id
            long id = savedClient.getId();

            // Отсоединили клиента от контекста
            session.detach(savedClient);

            // Еще раз сохранили
            saveClient(session, savedClient);

            // Проверка, что второй раз сохраненный клиент имеет новый id
            assertThat(id).isNotEqualTo(savedClient.getId());
        }
    }

    @DisplayName(" показывать, что вызов метода saveOrUpdate на detached объекте не приводит к генерации нового id")
    @Test
    void shouldGenerateNewIdWhenExecuteSaveOrUpdateMethodOnSameEntity() {
        try (Session session = sessionFactory.openSession()) {
            Client savedClient = buildDefaultClient();
            // Сохранили клиента в рамках текущей сессии
            saveClient(session, savedClient);
            // Запомнили его id
            long id = savedClient.getId();

            // Отсоединили клиента от контекста
            session.detach(savedClient);

            // Еще раз сохранили с помощью saveOrUpdate
            session.beginTransaction();
            savedClient.setName(TEST_CLIENT_NEW_NAME);
            session.saveOrUpdate(savedClient);
            session.getTransaction().commit();

            Client loadedClient = loadClient(id);

            // Проверка, что второй раз сохраненный клиент имеет тот же id
            assertThat(loadedClient).usingRecursiveComparison().isEqualTo(savedClient);
        }
    }

    @DisplayName(" показывать, что вызов метода update на transient объекте приводит к исключению")
    @Test
    void shouldThrowExceptionWhenCommitTransactionAfterUpdateTransientEntity() {
        try (Session session = sessionFactory.openSession()) {
            // Создали нового клиента, но не сохранили его
            Client savedClient = buildDefaultClient();

            // Вызвали для данного клиента update
            session.beginTransaction();
            session.update(savedClient);
            // Проверка, что id у него не появился
            assertThat(savedClient.getId()).isZero();
            // Проверка, что коммит транзакции приведет к исключению
            assertThatThrownBy(session.getTransaction()::commit).isInstanceOf(Exception.class);
        }
    }

    @DisplayName(" показывать, что изменение persistent объекта внутри транзакции приводит к его изменению в БД")
    @Test
    void shouldUpdatePersistentEntityInDBWhenChangedFieldsInTransaction() {
        Client savedClient = buildDefaultClient();
        try (Session session = sessionFactory.openSession()) {
            // Открыли транзакцию
            session.beginTransaction();

            // Сохранили клиента
            session.save(savedClient);

            // Убедились, что его имя соответствует ожидаемому
            assertThat(savedClient.getName()).isEqualTo(TEST_CLIENT_NAME);
            // Сменили имя на новое
            savedClient.setName(TEST_CLIENT_NEW_NAME);

            // Завершили транзакцию
            session.getTransaction().commit();
            // И сессию
        }
        // Загрузили клиента в новой сессии
        Client loadedClient = loadClient(savedClient.getId());
        // Проверка, что имя загруженного клиента соответствует тому, что дали после сохранения
        assertThat(loadedClient.getName()).isEqualTo(TEST_CLIENT_NEW_NAME);
    }

    @DisplayName(" показывать, что удаленный через HQL persistent объект остается в сессии, но удаляется в БД")
    @Test
    void shouldNotDetachPersistentEntityWhenRemoveWithHQLQuery() {
        Client savedClient = buildDefaultClient();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            // Сохранили клиента в рамках текущей сессии. Теперь у него state = persistent
            session.save(savedClient);

            // Удалили клиента с помощью запроса
            Query query = session.createQuery("delete from Client u where u.id = ?1");
            query.setParameter(1, savedClient.getId());
            query.executeUpdate();

            // Загрузили клиента
            Client loadedClient = session.get(Client.class, savedClient.getId());
            // Проверка, что загруженный клиент не null и равен сохраненному ранее
            assertThat(loadedClient).isNotNull().usingRecursiveComparison().isEqualTo(savedClient);

            // Отсоединили клиента от контекста
            session.detach(savedClient);

            // Загрузили клиента еще раз
            loadedClient = session.get(Client.class, savedClient.getId());

            // Проверка, что загруженный клиент null
            assertThat(loadedClient).isNull();

            session.getTransaction().commit();
        }
    }
}
