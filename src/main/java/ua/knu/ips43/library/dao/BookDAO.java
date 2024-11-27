package ua.knu.ips43.library.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Statement;
//import java.sql.PreparedStatement;
//import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
//import java.sql.Date;
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

import ua.knu.ips43.library.model.Book;
import ua.knu.ips43.library.model.BookRaw;

@Repository
public class BookDAO {
    private JdbcTemplate jdbcTemplate;
    //private ConcurrentHashMap<Integer, String> authorWebString;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
	this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<BookRaw> get(int id) {
	List<BookRaw> results = jdbcTemplate.query(
	    "select id, title, description, lang from Books where id=?",
	    this::mapRowToBookRaw,
	    id);
	return results.size() == 0 ?
	    Optional.empty() :
	    Optional.of(results.get(0));
    }

    public Integer add(String title, String description, String lang) {
	if (title == null) {
	    title = "";
	}
	if (description == null) {
	    description = "";
	}
	if (lang == null) {
	    lang = "uk";
	}
/*
INSERT INTO Books (title, description, lang) VALUES (?, ?, ?)
jdbcTemplate.update("INSERT INTO Books (title, description, lang) VALUES (?, ?, ?)", title, description, lang);

WITH newbook AS ( INSERT INTO Books (title, description, lang) VALUES (?, ?, ?) ) SELECT id FROM newbook
 */
	List<Integer> results = jdbcTemplate.query(
	    "INSERT INTO Books (title, description, lang) VALUES (?, ?, ?) RETURNING id",
	    this::mapRowToId,
	    title, description, lang
	    );
	return results.get(0);
    }

    public boolean del(int id){
      jdbcTemplate.update("delete from Books where id=?", id);
      jdbcTemplate.update("delete from BookAuthors where book_id=?", id);
      return true;
    }

    public List<BookRaw> getAll() {
	List<BookRaw> results = jdbcTemplate.query(
	    "SELECT id, title, description, lang FROM Books ORDER BY id",
	    this::mapRowToBookRaw);
	return results;
    }

    public Integer getCount() {
	List<Integer> results = jdbcTemplate.query(
	    "SELECT COUNT(*) FROM Books",
	    this::mapRowToCount);
	return results.get(0);
    }

    public List<BookRaw> find(String title, String pattern) {
	ArrayList<Object> params = new ArrayList<Object>();
	StringBuilder sql = new StringBuilder("SELECT ");
	//ArrayList<String> columns = new ArrayList<String>();
	ArrayList<String> where_conditions = new ArrayList<String>();
	sql.append(" id, title, description, lang");
	//columns.add("description");
	//sql.append(String.join(", ", columns));
	sql.append(" FROM Books");
	if (title != null && title!="") {
	    where_conditions.add("title = ?");
	    params.add(title);
	}
	if (pattern != null && pattern!="") {
	    where_conditions.add("title LIKE ?");
	    params.add("%" + pattern.trim() + "%");
	}
	if (where_conditions.size()>0) {
	    sql.append(" WHERE ");
	    sql.append(String.join(" AND ", where_conditions));
	}
	sql.append(" ORDER BY title");
	//System.err.println("BookRawDAO: sql="+sql.toString());
	List<BookRaw> results = jdbcTemplate.query(
	    sql.toString(),
	    this::mapRowToBookRaw,
	    params.toArray());
	return results;
    }

    private BookRaw mapRowToBookRaw(ResultSet row, int rowNum)
	throws SQLException{
	return new BookRaw(
	    row.getInt("id"),
	    row.getString("title"),
	    row.getString("description"),
	    row.getString("lang")
	    );
    }

    private Integer mapRowToCount(ResultSet row, int rowNum)
	throws SQLException{
	return row.getInt("count");
    }

    private Integer mapRowToId(ResultSet row, int rowNum)
	throws SQLException{
	return row.getInt("id");
    }

}
