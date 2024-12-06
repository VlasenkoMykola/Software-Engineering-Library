package ua.knu.ips43.library.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.springframework.jdbc.support.rowset.SqlRowSet;

//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Statement;
//import java.sql.PreparedStatement;
//import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
//import java.sql.Date;
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

import ua.knu.ips43.library.model.Book;
import ua.knu.ips43.library.model.Book;

@Repository
public class BookDAO {
    private JdbcTemplate jdbcTemplate;
    //private ConcurrentHashMap<Integer, String> authorWebString;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
	this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Book> get(int id) {
	List<Book> results = jdbcTemplate.query(
	    "select id, title, description, lang from Books where id=?",
	    this::mapRowToBook,
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

    public List<Book> getAll() {
	List<Book> results = jdbcTemplate.query(
	    "SELECT id, title, description, lang FROM Books ORDER BY id",
	    this::mapRowToBook);
	return results;
    }

    public Integer getCount() {
	List<Integer> results = jdbcTemplate.query(
	    "SELECT COUNT(*) FROM Books",
	    this::mapRowToCount);
	return results.get(0);
    }

    public List<Book> find(String title, String pattern, String id, String authorId) {
	SQLQuery query = queryArgs(title, pattern, id, authorId, null, Select.ALL);
	return findQuery(query);
    }

    public List<Book> findQuery(SQLQuery query) {
	return jdbcTemplate.query(
	    query.sql,
	    this::mapRowToBook,
	    query.params.toArray());
    }

    public SQLQuery queryArgs(String title, String pattern, String id, String authorId, SQLQuery idRestriction, Select select)
	throws NumberFormatException
	{
	ArrayList<Object> params = new ArrayList<Object>();
	StringBuilder sql = new StringBuilder("SELECT ");
	//ArrayList<String> columns = new ArrayList<String>();
	ArrayList<String> where_conditions = new ArrayList<String>();
	sql.append(
	    switch (select) {
	    case BOOK2TITLE -> "id, title";
	    default -> "id, title, genres, description, lang";
	    }
	);
	//sql.append("id, title, genres, description, lang");
	//columns.add("description");
	//sql.append(String.join(", ", columns));
	sql.append(" FROM Books");
	if (title != null && !title.isEmpty()) {
	    where_conditions.add("title = ?");
	    params.add(title);
	}
	if (pattern != null && !pattern.isEmpty()) {
	    where_conditions.add("title LIKE ?");
	    params.add("%" + pattern.trim() + "%");
	}
	if (id != null && !id.isEmpty()) {
	    where_conditions.add("id=?");
	    params.add(Integer.valueOf(id));
	}
	//System.err.println("BookDAO: authorId0:" + (authorId==null ? "NuLL": authorId));
	if (authorId != null && !authorId.isEmpty()) {
	    where_conditions.add("id IN (SELECT DISTINCT book_id FROM BookAuthors WHERE author_id=?)");
	    params.add(Integer.valueOf(authorId));
	}
	if (idRestriction != null) {
	    where_conditions.add("id IN (" + idRestriction.sql + ")");
	    params.addAll(idRestriction.params);
	}
	if (where_conditions.size()>0) {
	    sql.append(" WHERE ");
	    sql.append(String.join(" AND ", where_conditions));
	}
	//sql.append(" ORDER BY title");
	return new SQLQuery(sql.toString(), params);
    }

    public Integer getAuthorIdsFor(Integer id) {
	List<Integer> results = jdbcTemplate.query(
	    "SELECT author_id AS COUNT FROM BookAuthors WHERE book_id = ?",
	    this::mapRowToCount, id);
	return results.get(0);
    }

    public Map<Integer, List<Integer>> getMapBookAuthors() {
	//ResultSet resultSet
	SqlRowSet resultSet = jdbcTemplate.queryForRowSet(
	    "SELECT book_id, author_id FROM BookAuthors");
	Map<Integer, List<Integer>> valueMap = new HashMap<>();
	while (resultSet.next()) {
	    Integer book_id = resultSet.getInt("book_id");
	    Integer author_id = resultSet.getInt("author_id");
	    valueMap.computeIfAbsent(book_id, data -> new ArrayList<>()).add(author_id);
	}
	return valueMap;
    }

    public Map<Integer, String> getBookMapId2Title(SQLQuery idRestriction) {
	SQLQuery query = queryArgs(null, null, null, null, idRestriction, BookDAO.Select.BOOK2TITLE);
	//System.err.println("BookDAO: getBookMapId2Title: param sql=" + idRestriction.sql);
	//System.err.println("BookDAO: getBookMapId2Title: result sql=" + query.sql);
	SqlRowSet resultSet = jdbcTemplate.queryForRowSet(
	    query.sql, query.params.toArray());
	Map<Integer, String> valueMap = new HashMap<>();
	while (resultSet.next()) {
	    Integer id = resultSet.getInt("id");
	    String name = resultSet.getString("title");
	    valueMap.put(id, name);
	}
	return valueMap;
    }

    private Book mapRowToBook(ResultSet row, int rowNum)
	throws SQLException{
	return new Book(
	    row.getInt("id"),
	    row.getString("title"),
	    row.getString("genres"),
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

    public enum Select {
	ALL,
	BOOK2TITLE
    }

}
