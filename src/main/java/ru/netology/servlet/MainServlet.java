package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.configuration.JavaConfig;
import ru.netology.controller.PostController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private PostController controller;
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";
    private static final String API_POSTS = "/api/posts";
    private static final String REG_ID = "/\\d+";

    @Override
    public void init() {
        final var context = new AnnotationConfigApplicationContext(JavaConfig.class);
        controller = context.getBean(PostController.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            // primitive routing
            if (method.equals(GET) && path.equals(API_POSTS)) {
                controller.all(resp);
                return;
            }
            if (method.equals(GET) && path.matches(API_POSTS + REG_ID)) {
                // easy way
                final var id = getId(path);
                controller.getById(id, resp);
                return;
            }
            if (method.equals(POST) && path.equals(API_POSTS)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals(DELETE) && path.matches(API_POSTS + REG_ID)) {
                // easy way
                final var id = getId(path);
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private long getId(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
    }
}

