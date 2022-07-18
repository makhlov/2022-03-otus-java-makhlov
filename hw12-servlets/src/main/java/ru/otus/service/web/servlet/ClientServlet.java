package ru.otus.service.web.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.service.db.DBServiceClient;
import ru.otus.service.web.helper.ClientMappingHelper;
import ru.otus.service.db.TemplateProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class ClientServlet extends HttpServlet {
    private final TemplateProcessor templateProcessor;
    private final DBServiceClient dbServiceClient;
    private final ClientMappingHelper clientMappingHelper;

    public ClientServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient, ClientMappingHelper clientMappingHelper) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
        this.clientMappingHelper = clientMappingHelper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("clients", dbServiceClient.findAll()
                .stream()
                .map(clientMappingHelper::mapToTemplateData)
                .collect(toList()));
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage("clients.html", paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException {
        dbServiceClient.saveClient(clientMappingHelper.mapToInstance(req));
        response.sendRedirect(req.getServletPath());
    }
}
