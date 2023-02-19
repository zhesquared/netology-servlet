package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private PostController controller;

    private final String get = "GET";

    private final String delete = "DELETE";

    private final String post = "POST";

    private final String mainPath = "/api/posts";

    @Override
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            // primitive routing
            if (method.equals(get) && path.equals(mainPath)) {
                controller.all(resp);
                return;
            }
            if (method.equals(get) && path.matches(mainPath + "\\d+")) {
                // easy way
                final var id = getId(path);
                controller.getById(id, resp);
                return;
            }
            if (method.equals(post) && path.equals(mainPath)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals(delete) && path.matches(mainPath + "\\d+")) {
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

    public static long getId(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/")));
    }
}

