package ru.otus.configuration.hibernate;

import ru.otus.service.db.DBServiceClient;

public interface HibernateConfiguration {
    DBServiceClient configure();
}
