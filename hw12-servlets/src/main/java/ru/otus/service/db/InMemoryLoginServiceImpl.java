package ru.otus.service.db;

import org.eclipse.jetty.security.AbstractLoginService;
import org.eclipse.jetty.security.RolePrincipal;
import org.eclipse.jetty.security.UserPrincipal;
import org.eclipse.jetty.util.security.Password;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.persistance.dao.UserDao;
import ru.otus.domain.User;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public class InMemoryLoginServiceImpl extends AbstractLoginService {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryLoginServiceImpl.class);

    private final UserDao userDao;

    public InMemoryLoginServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    protected List<RolePrincipal> loadRoleInfo(UserPrincipal userPrincipal) {
        return List.of(new RolePrincipal("user"));
    }

    @Override
    protected UserPrincipal loadUserInfo(String login) {
        logger.info(format("InMemoryLoginService#loadUserInfo(%s)", login));
        Optional<User> dbUser = userDao.findByLogin(login);
        return dbUser.map(u -> new UserPrincipal(u.getLogin(), new Password(u.getPassword()))).orElse(null);
    }
}
