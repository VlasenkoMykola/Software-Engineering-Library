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
import ua.knu.ips43.library.model.Copy;
import ua.knu.ips43.library.dao.CopyDAO;

@RestController
public class CopyController {

    private final CopyDAO copyDAO;
    //@Autowired
    public CopyController(CopyDAO copyDAO) {
	this.copyDAO = copyDAO;
    }

    @GetMapping(
	value = "/backend/get_copy_count",
	produces="application/json"
    )
    public Integer getCopyCount() {
        return copyDAO.getCount();
    }

    @GetMapping(
	value = "/backend/get_copies",
	produces="application/json"
    )

    public List<Copy> getCopies(
	@RequestParam(name = "id", required=false) Integer id,
	@RequestParam(name = "book_id", required=false) Integer book_id,
	@RequestParam(name = "reader_id", required=false) Integer reader_id,
	@RequestParam(name = "status", required=false) String status,
	@RequestParam(name = "book_or_lend_from", required=false) String book_or_lend_from,
	@RequestParam(name = "book_or_lend_to", required=false) String book_or_lend_to
	) {
	//System.err.println("CopyController: pattern="+ (pattern==null? "null":pattern));
	//book_or_lend_from
        return copyDAO.find(id, book_id, reader_id, status, null, null);
    }

    @GetMapping(
	value = "/backend/add_copy",
	produces="application/json"
    )
    public Integer addCopy(
	@RequestParam(name = "book_id", required=false) Integer book_id
	) {
	return copyDAO.add(book_id);
    }

    @GetMapping(
	value = "/backend/del_copy",
	produces="application/json"
    )
    public boolean delCopy(
	@RequestParam(name = "id", required=true) Integer id
	) {
        return copyDAO.del(id);
    }

}
