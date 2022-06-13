package ru.otus.server;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.core.repository.ClientDao;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.services.ClientAuthService;
import ru.otus.services.TemplateProcessor;
import ru.otus.servlet.*;

import java.util.Arrays;

public class ClientWebServerWithFilterBasedSecurity implements ClientWebServer {

    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";

    private final ClientDao clientDao;
    private final Gson gson;
    private final TemplateProcessor templateProcessor;
    private final Server server;
    private final ClientAuthService clientAuthService;

    public ClientWebServerWithFilterBasedSecurity(int port,
                                                  ClientDao clientDao,
                                                  Gson gson,
                                                  TemplateProcessor templateProcessor,
                                                  ClientAuthService clientAuthService) {
        this.clientDao = clientDao;
        this.gson = gson;
        this.templateProcessor = templateProcessor;
        this.clientAuthService = clientAuthService;
        server = new Server(port);
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
        ResourceHandler resourceHandler = initResourceHandler();
        ServletContextHandler servletContextHandler = initServletContextHandler();

        HandlerList handlerList = new HandlerList();
        handlerList.addHandler(resourceHandler);
        handlerList.addHandler(applySecurity(servletContextHandler, "/admin", "/api/clients", "/api/client"));

        server.setHandler(handlerList);
        return server;
    }

    private Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, clientAuthService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths).forEach(p -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), p, null));
        return servletContextHandler;
    }

    private ResourceHandler initResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler initServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new AllClientsShowApiServlet(clientDao, gson)), "/api/clients");
        servletContextHandler.addServlet(new ServletHolder(new ClientServlet(templateProcessor)), "/admin");
        servletContextHandler.addServlet(new ServletHolder(new ClientCreateApiServlet(clientDao, gson)), "/api/client");
        return servletContextHandler;
    }
}
