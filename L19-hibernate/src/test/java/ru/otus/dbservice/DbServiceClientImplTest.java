package ru.otus.dbservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.core.dao.ClientDao;
import ru.otus.core.model.Client;
import ru.otus.core.service.DbServiceClientImpl;
import ru.otus.core.service.DbServiceException;
import ru.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;


@DisplayName("Сервис для работы с пользователями в рамках БД должен ")
@ExtendWith(MockitoExtension.class)
class DbServiceClientImplTest {

    private static final long CLIENT_ID = 1L;

    @Mock
    private SessionManager sessionManager;

    @Mock
    private ClientDao clientDao;

    private DbServiceClientImpl dbServiceClient;

    private InOrder inOrder;

    @BeforeEach
    void setUp() {
        given(clientDao.getSessionManager()).willReturn(sessionManager);
        inOrder = inOrder(clientDao, sessionManager);
        dbServiceClient = new DbServiceClientImpl(clientDao);
    }

    @Test
    @DisplayName(" корректно сохранять клиента")
    void shouldCorrectSaveClient() {
        var vasya = new Client();
        given(clientDao.insertOrUpdate(vasya)).willReturn(CLIENT_ID);
        long id = dbServiceClient.saveClient(vasya);
        assertThat(id).isEqualTo(CLIENT_ID);
    }

    @Test
    @DisplayName(" при сохранении клиента, открывать и коммитить транзакцию в нужном порядке")
    void shouldCorrectSaveClientAndOpenAndCommitTranInExpectedOrder() {
        dbServiceClient.saveClient(new Client());

        inOrder.verify(clientDao, times(1)).getSessionManager();
        inOrder.verify(sessionManager, times(1)).beginSession();
        inOrder.verify(sessionManager, times(1)).commitSession();
        inOrder.verify(sessionManager, never()).rollbackSession();
    }

    @Test
    @DisplayName(" при сохранении пользователя, открывать и откатывать транзакцию в нужном порядке")
    void shouldOpenAndRollbackTranWhenExceptionInExpectedOrder() {
        doThrow(IllegalArgumentException.class).when(clientDao).insertOrUpdate(any());

        assertThatThrownBy(() -> dbServiceClient.saveClient(null))
                .isInstanceOf(DbServiceException.class)
                .hasCauseInstanceOf(IllegalArgumentException.class);

        inOrder.verify(clientDao, times(1)).getSessionManager();
        inOrder.verify(sessionManager, times(1)).beginSession();
        inOrder.verify(sessionManager, times(1)).rollbackSession();
        inOrder.verify(sessionManager, never()).commitSession();
    }

    @Test
    @DisplayName(" корректно загружать пользователя по заданному id")
    void shouldLoadCorrectClientById() {
        Client expectedClient = new Client(CLIENT_ID, "Вася");
        given(clientDao.findById(CLIENT_ID)).willReturn(Optional.of(expectedClient));
        Optional<Client> mayBeClient = dbServiceClient.getClient(CLIENT_ID);
        assertThat(mayBeClient).isPresent().get().isEqualTo(expectedClient);
    }
}
