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

import ua.knu.ips43.library.model.Author;

@Repository
public class AuthorDAO {
    private JdbcTemplate jdbcTemplate;
    //private ConcurrentHashMap<Integer, String> authorWebString;

    @Autowired
    public AuthorDAO(JdbcTemplate jdbcTemplate) {
	this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Author> get(int id) {
	List<Author> results = jdbcTemplate.query(
	    "select id, full_name,first_name,middle_name,last_name, description from Authors where id=?",
	    this::mapRowToAuthor,
	    id);
	return results.size() == 0 ?
	    Optional.empty() :
	    Optional.of(results.get(0));
    }

    public Integer add(String first_name, String middle_name, String last_name, String description) {
	if (first_name == null) {
	    first_name = "";
	}
	if (middle_name == null) {
	    middle_name = "";
	}
	if (last_name == null) {
	    last_name = "";
	}
	if (description == null) {
	    description = "";
	}
	String full_name = String.join(" ", last_name, first_name, middle_name);
	/*
	jdbcTemplate.update("INSERT INTO Authors (full_name,first_name,middle_name,last_name, description) VALUES (?, ?, ?, ?, ?)", full_name,first_name,middle_name,last_name, description);
	return 0;
	*/
	List<Integer> results = jdbcTemplate.query(
	    "INSERT INTO Authors (full_name,first_name,middle_name,last_name, description) VALUES (?, ?, ?, ?, ?) RETURNING id",
	    this::mapRowToId,
	    full_name,first_name,middle_name,last_name, description
	    );
	return results.get(0);
    }

    public boolean del(int id){
	List<Integer> results = jdbcTemplate.query(
	    "SELECT COUNT(*) FROM BookAuthors where author_id=?",
	    this::mapRowToCount, id);
	// TODO check if books still exist
      jdbcTemplate.update("delete from Authors where id=?", id);
      return true;
    }

    public List<Author> getAll() {
	List<Author> results = jdbcTemplate.query(
	    "SELECT id, full_name,first_name,middle_name,last_name, description FROM Authors ORDER BY id",
	    this::mapRowToAuthor);
	return results;
    }

    public List<Author> find(String letter, String last_name, String pattern) {
	ArrayList<Object> params = new ArrayList<Object>();
	StringBuilder sql = new StringBuilder("SELECT ");
	//ArrayList<String> columns = new ArrayList<String>();
	ArrayList<String> where_conditions = new ArrayList<String>();
	sql.append(" id, full_name,first_name,middle_name,last_name, description");
	//columns.add("description");
	//sql.append(String.join(", ", columns));
	sql.append(" FROM Authors");
	if (letter != null && letter!="") {
	    where_conditions.add("letter = ?");
	    params.add(letter);
	    //System.err.println("AuthorDAO: letter="+letter);
	}
	if (last_name != null && last_name!="") {
	    where_conditions.add("last_name = ?");
	    params.add(last_name);
	}
	if (pattern != null && pattern!="") {
	    where_conditions.add("full_name LIKE ?");
	    params.add("%" + pattern.trim() + "%");
	}
	if (where_conditions.size()>0) {
	    sql.append(" WHERE ");
	    sql.append(String.join(" AND ", where_conditions));
	}
	sql.append(" ORDER BY full_name");
	//System.err.println("AuthorDAO: sql="+sql.toString());
	List<Author> results = jdbcTemplate.query(
	    sql.toString(),
	    this::mapRowToAuthor,
	    params.toArray());
	return results;
    }


    public List<String> getLetters() {
	List<String> results = jdbcTemplate.query(
	    "SELECT DISTINCT letter FROM Authors ORDER BY letter",
	    this::mapRowToLetter);
	return results;
    }

    public Integer getCount() {
	List<Integer> results = jdbcTemplate.query(
	    "SELECT COUNT(*) FROM Authors",
	    this::mapRowToCount);
	return results.get(0);
    }


    private Author mapRowToAuthor(ResultSet row, int rowNum)
	throws SQLException{
	return new Author(
	    row.getInt("id"),
	    row.getString("full_name"),
	    row.getString("first_name"),
	    row.getString("middle_name"),
	    row.getString("last_name"),
	    row.getString("description")
	    );
    }

    private String mapRowToLetter(ResultSet row, int rowNum)
	throws SQLException{
	return row.getString("letter");
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
