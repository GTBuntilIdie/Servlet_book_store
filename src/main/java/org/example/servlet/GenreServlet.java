package org.example.servlet;

import org.example.dto.GenreDto;
import org.example.service.GenreService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/genre-servlet"})
public class GenreServlet extends HttpServlet {

    private final GenreService genreService = GenreService.getInstance();

    public GenreServlet() {
        super();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");

        // Создаем объект DTO и устанавливаем полученные значения
        GenreDto dto = new GenreDto();
        dto.setTitle(title);

        // Вызываем метод сервиса, передавая созданный объект DTO
        genreService.create(dto);

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("genre created");
        out.println("</body></html>");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = Long.parseLong(req.getParameter("id"));

        GenreDto dto = genreService.read(id);
        String text = dto.getTitle();

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println(text);
        out.println("</body></html>");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = Long.parseLong(req.getParameter("id"));

        String title = req.getParameter("new_title");

        GenreDto dto = genreService.read(id);
        dto.setTitle(title);

        genreService.update(id, dto);

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("genre updated");
        out.println("</body></html>");

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = Long.parseLong(req.getParameter("id"));
        genreService.delete(id);

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("жанр удален");
        out.println("</body></html>");
    }
}
