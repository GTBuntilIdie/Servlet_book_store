package org.example.servlet;


import org.example.dto.AuthorDto;
import org.example.dto.BookDto;
import org.example.entity.Author;
import org.example.service.BookService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@WebServlet(urlPatterns = {"/book-servlet"})
public class BookServlet extends HttpServlet {

    BookService bookService = BookService.getInstance();
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
        LocalDate publicationDate = LocalDate.parse(req.getParameter("publicationDate"));

        Long authorId = Long.valueOf(req.getParameter("authorId"));
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(authorId);

        String genresIds = req.getParameter("genres");

        BookDto bookDto = new BookDto();
        bookDto.setTitle(title);
        bookDto.setPublicationDate(publicationDate);
        bookDto.setAuthor(authorDto);
        /*bookDto.setGenreIds(getLongs(genresIds));*/

        bookService.create(bookDto);

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("book created");
        out.println("</body></html>");
    }

    private Set<Long> getLongs(String genresIds) {
        Set<Long> genreSet = new HashSet<>();
        String[] genresIdsList = genresIds.split("_");
        for (String genre_id : genresIdsList) {
            genreSet.add(Long.parseLong(genre_id));
        }
        return genreSet;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = Long.parseLong(req.getParameter("id"));

        BookDto dto = bookService.read(id);
        String title = dto.getTitle();

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println(title);
        out.println("</body></html>");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = Long.parseLong(req.getParameter("id"));
        String title = req.getParameter("new_title");
        LocalDate publicationDate = LocalDate.parse(req.getParameter("new_publicationDate"));

        Long authorId = Long.valueOf(req.getParameter("new_authorId"));
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(authorId);

        String genresIds = req.getParameter("new_genres");

        BookDto bookDto = new BookDto();
        bookDto.setTitle(title);
        bookDto.setPublicationDate(publicationDate);
        bookDto.setAuthor(authorDto);
        /*bookDto.setGenreIds(getLongs(genresIds));
*/
        bookService.update(id, bookDto);

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("book updated");
        out.println("</body></html>");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = Long.parseLong(req.getParameter("id"));

        bookService.delete(id);

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("book deleted");
        out.println("</body></html>");
    }

}
