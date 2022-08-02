package ru.otus.service.web.server;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import ru.otus.service.web.helper.ClientMappingHelper;
import ru.otus.service.web.helper.FileSystemHelper;
import ru.otus.service.web.servlet.ClientServlet;
import ru.otus.service.db.DBServiceClient;
import ru.otus.service.db.TemplateProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientWebServerWithBasicSecurity implements ClientWebServer {
    private static final String ROLE_NAME_ADMIN = "admin";
    private static final String ROLE_NAME_USER = "user";
    private static final String CONSTRAINT_NAME = "auth";
    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";
    private static final String PATH_CLIENTS = "/clients";

    private final LoginService loginService;
    protected final TemplateProcessor templateProcessor;
    private final Server server;
    private final DBServiceClient dbServiceClient;

    public ClientWebServerWithBasicSecurity(LoginService loginService, TemplateProcessor templateProcessor, Server server, DBServiceClient dbServiceClient) {
        this.loginService = loginService;
        this.templateProcessor = templateProcessor;
        this.server = server;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private Server initContext() {
        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();
        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(applySecurity(servletContextHandler, PATH_CLIENTS));
        server.setHandler(handlers);
        return server;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new ClientServlet(templateProcessor, dbServiceClient, new ClientMappingHelper())), PATH_CLIENTS);
        return servletContextHandler;
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        Constraint constraint = new Constraint();
        constraint.setName(CONSTRAINT_NAME);
        constraint.setAuthenticate(true);
        constraint.setRoles(new String[]{ROLE_NAME_ADMIN, ROLE_NAME_USER});
        List<ConstraintMapping> constraintMappings = new ArrayList<>();
        Arrays.stream(paths).forEachOrdered(path -> {
            ConstraintMapping mapping = new ConstraintMapping();
            mapping.setPathSpec(path);
            mapping.setConstraint(constraint);
            constraintMappings.add(mapping);
        });
        ConstraintSecurityHandler security = new ConstraintSecurityHandler();
        security.setAuthenticator(new BasicAuthenticator());
        security.setLoginService(loginService);
        security.setConstraintMappings(constraintMappings);
        security.setHandler(new HandlerList(servletContextHandler));
        return security;
    }
}
