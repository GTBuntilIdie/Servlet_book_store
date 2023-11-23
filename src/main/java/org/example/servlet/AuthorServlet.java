package org.example.servlet;

import org.example.dto.AuthorDto;
import org.example.service.AuthorService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/author-servlet"})
public class AuthorServlet extends HttpServlet {

    private final AuthorService authorService = AuthorService.getInstance();

    public AuthorServlet() {
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = Long.parseLong(req.getParameter("id"));

        AuthorDto dto = authorService.read(id);
        String name = dto.getName();
        String surname = dto.getSurname();

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println(name + " " + surname);
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");

        AuthorDto dto = new AuthorDto();
        dto.setName(name);
        dto.setSurname(surname);

        authorService.create(dto);

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("author created");
        out.println("</body></html>");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = Long.parseLong(req.getParameter("id"));

        String name = req.getParameter("new_name");
        String surname = req.getParameter("new_surname");

        AuthorDto dto = authorService.read(id);
        dto.setName(name);
        dto.setSurname(surname);

        authorService.update(id, dto);

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("author updated");
        out.println("</body></html>");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = Long.parseLong(req.getParameter("id"));

        authorService.delete(id);

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("author deleted");
        out.println("</body></html>");
    }
}
