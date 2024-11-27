package ua.knu.ips43.library.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.time.LocalDate;

import ua.knu.ips43.library.model.Author;
import ua.knu.ips43.library.model.Book;
import ua.knu.ips43.library.model.BookRaw;
import ua.knu.ips43.library.dao.BookDAO;

@RestController
public class BookController {

    private final BookDAO bookDAO;
    //@Autowired
    public BookController(BookDAO bookDAO) {
	this.bookDAO = bookDAO;
    }

    @GetMapping(
	value = "/backend/get_book_count",
	produces="application/json"
    )
    public Integer getBookCount() {
        return bookDAO.getCount();
    }

    @GetMapping(
	value = "/backend/get_books",
	produces="application/json"
    )
    public List<BookRaw> getBooks(
	@RequestParam(name = "title", required=false) String title,
	@RequestParam(name = "pattern", required=false) String pattern
	) {
	//System.err.println("BookController: pattern="+ (pattern==null? "null":pattern));
        return bookDAO.find(title, pattern);
    }

    @GetMapping(
	value = "/backend/add_book",
	produces="application/json"
    )
    public Integer addBook(
	@RequestParam(name = "title", required=true) String title,
	@RequestParam(name = "description", required=false) String description,
	@RequestParam(name = "lang", required=false) String lang
	) {
	return bookDAO.add(title, description, lang);
    }

    @GetMapping(
	value = "/backend/del_book",
	produces="application/json"
    )
    public boolean delBook(
	@RequestParam(name = "id", required=true) Integer id
	) {
        return bookDAO.del(id);
    }

}
