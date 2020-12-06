package ru.otus.core.dao;

import ru.otus.core.model.Client;
import ru.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface ClientDao {
    Optional<Client> findById(long id);

    long insert(Client client);

    //void update(Client user);
    //void insertOrUpdate(Client user);

    SessionManager getSessionManager();
}
