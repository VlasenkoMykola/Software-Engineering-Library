package ua.knu.ips43.library.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.sql.Date;
import java.time.LocalDate;

import ua.knu.ips43.library.model.Author;
import ua.knu.ips43.library.dao.AuthorDAO;

@RestController
public class AuthorController {

    private final AuthorDAO authorDAO;
    //@Autowired
    public AuthorController(AuthorDAO authorDAO) {
	this.authorDAO = authorDAO;
    }

    @GetMapping(
	value = "/backend/get_author_count",
	produces="application/json"
    )
    public Integer getAuthorCount() {
        return authorDAO.getCount();
    }

    @GetMapping(
	value = "/backend/get_author_book_count",
	produces="application/json"
    )
    public Integer getAuthorBookCount(
	@RequestParam(name = "id", required=true) Integer id
	) {
        return authorDAO.getCountByAuthor(id);
    }

    @GetMapping(
	value = "/backend/get_letters",
	produces="application/json"
    )
    public List<String> getLetters() {
        return authorDAO.getLetters();
    }

    @GetMapping(
	value = "/backend/get_author_map_id_name",
	produces="application/json"
    )
    public Map<Integer, String> getAuthorMapId2Name() {
        return authorDAO.getAuthorMapId2Name();
    }


    @GetMapping(
	value = "/backend/get_authors",
	produces="application/json"
    )
    public List<Author> getAuthors(
	@RequestParam(name = "id", required=false) Integer id,
	@RequestParam(name = "letter", required=false) String letter,
	@RequestParam(name = "first_name", required=false) String first_name,
	@RequestParam(name = "middle_name", required=false) String middle_name,
	@RequestParam(name = "last_name", required=false) String last_name,
	@RequestParam(name = "full_name", required=false) String full_name,
	@RequestParam(name = "pattern", required=false) String pattern
	) {
	//System.err.println("AuthorController: letter="+ (letter==null? "null":letter));
        return authorDAO.find(first_name, middle_name, last_name, full_name, id, letter, pattern);
    }

    @GetMapping(
	value = "/backend/add_author",
	produces="application/json"
    )
    public Integer addAuthor(
	@RequestParam(name = "first_name", required=false) String first_name,
	@RequestParam(name = "middle_name", required=false) String middle_name,
	@RequestParam(name = "last_name", required=true) String last_name,
	@RequestParam(name = "description", required=false) String description
	) {
        return authorDAO.add(first_name,middle_name,last_name,description);
    }

    @GetMapping(
	value = "/backend/del_author",
	produces="application/json"
    )
    public boolean delAuthor(
	@RequestParam(name = "id", required=true) Integer id
	) {
        return authorDAO.del(id);
    }

}
