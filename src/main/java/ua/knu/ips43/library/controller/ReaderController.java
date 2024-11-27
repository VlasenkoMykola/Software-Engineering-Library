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
import ua.knu.ips43.library.model.Reader;
import ua.knu.ips43.library.dao.ReaderDAO;

@RestController
public class ReaderController {

    private final ReaderDAO readerDAO;
    //@Autowired
    public ReaderController(ReaderDAO readerDAO) {
	this.readerDAO = readerDAO;
    }

    @GetMapping(
	value = "/backend/get_reader_count",
	produces="application/json"
    )
    public Integer getReaderCount() {
        return readerDAO.getCount();
    }

    @GetMapping(
	value = "/backend/get_readers",
	produces="application/json"
    )

    public List<Reader> getReaders(
	@RequestParam(name = "id", required=false) Integer id,
	@RequestParam(name = "first_name", required=false) String first_name,
	@RequestParam(name = "middle_name", required=false) String middle_name,
	@RequestParam(name = "last_name", required=false) String last_name,
	@RequestParam(name = "address", required=false) String address,
	@RequestParam(name = "phone", required=false) String phone,
	@RequestParam(name = "email", required=false) String email,
	@RequestParam(name = "addresspattern", required=false) String addresspattern
	) {
	//System.err.println("ReaderController: pattern="+ (pattern==null? "null":pattern));
        return readerDAO.find(id,first_name,middle_name,last_name,address,phone,email,addresspattern);
    }

    @GetMapping(
	value = "/backend/add_reader",
	produces="application/json"
    )
    public Integer addReader(
	@RequestParam(name = "first_name", required=true) String first_name,
	@RequestParam(name = "middle_name", required=false) String middle_name,
	@RequestParam(name = "last_name", required=true) String last_name,
	@RequestParam(name = "address", required=false) String address,
	@RequestParam(name = "phone", required=true) String phone,
	@RequestParam(name = "email", required=false) String email,
	@RequestParam(name = "valid_to", required=false) String valid_to
	) {
	return readerDAO.add(first_name,middle_name,last_name,address,phone,email, null);// valid_to
    }

    @GetMapping(
	value = "/backend/del_reader",
	produces="application/json"
    )
    public boolean delReader(
	@RequestParam(name = "id", required=true) Integer id
	) {
        return readerDAO.del(id);
    }

}
