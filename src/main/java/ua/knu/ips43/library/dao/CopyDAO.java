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
import java.sql.Date;
import java.sql.Timestamp;
//import java.time.LocalDateTime;

import ua.knu.ips43.library.model.Book;
import ua.knu.ips43.library.model.Copy;
import ua.knu.ips43.library.model.CopyStatus;

@Repository
public class CopyDAO {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CopyDAO(JdbcTemplate jdbcTemplate) {
	this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Copy> get(int id) {
	List<Copy> results = jdbcTemplate.query(
	    "select id, book_id, reader_id, status, status_change, book_or_lend_from, book_or_lend_to from Copies where id=?",
	    this::mapRowToCopy,
	    id);
	return results.size() == 0 ?
	    Optional.empty() :
	    Optional.of(results.get(0));
    }

    public Integer add(int book_id) {
	/*
	jdbcTemplate.update("INSERT INTO Copies (book_id, status) VALUES (?, 'free')", book_id);
	return 0;
	*/
	List<Integer> results = jdbcTemplate.query(
	    "INSERT INTO Copies (book_id, status) VALUES (?, 'free') RETURNING id",
	    this::mapRowToId,
	    book_id
	    );
	return results.get(0);

    }

    public void book(int id, int reader_id, Date book_or_lend_from, Date book_or_lend_to) {
	jdbcTemplate.update("UPDATE Copies SET reader_id=?, book_or_lend_from=?, book_or_lend_to=?, status='booked', status_change=NOW() WHERE id=?", reader_id, book_or_lend_from, book_or_lend_to, id);
    }

    public void give(int id, int reader_id, Date book_or_lend_from, Date book_or_lend_to) {
	jdbcTemplate.update("UPDATE Copies SET reader_id=?, book_or_lend_from=?, book_or_lend_to=?, status='given', status_change=NOW() WHERE id=?", reader_id, book_or_lend_from, book_or_lend_to, id);
    }

    public void free(int id) {
	jdbcTemplate.update("UPDATE Copies SET reader_id=NULL, book_or_lend_from=NULL, book_or_lend_to=NULL, status='free', status_change=NOW() WHERE id=?", id);
    }

    public boolean del(int id){
      jdbcTemplate.update("delete from Copies where id=?", id);
      return true;
    }

    public List<Copy> getAll() {
	List<Copy> results = jdbcTemplate.query(
	    "SELECT id, book_id, reader_id, status, status_change, book_or_lend_from, book_or_lend_to FROM Copies",
	    this::mapRowToCopy);
	return results;
    }

    public Integer getCount(Integer book_id, Integer reader_id, String status, Date book_or_lend_from, Date book_or_lend_to) {
	SQLQuery query = queryArgs(null, book_id, reader_id, status, book_or_lend_from, book_or_lend_to, Select.COUNT);
	List<Integer> results = jdbcTemplate.query(
	    query.sql,
	    this::mapRowToCount,
	    query.params.toArray());
	return results.get(0);
    }

    public List<Copy> find(Integer id, Integer book_id, Integer reader_id, String status, Date book_or_lend_from, Date book_or_lend_to) {
	SQLQuery query = queryArgs(id, book_id, reader_id, status, book_or_lend_from, book_or_lend_to, Select.ALL);
	return findQuery(query);
    }

    public List<Copy> findQuery(SQLQuery query) {
	return jdbcTemplate.query(
	    query.sql,
	    this::mapRowToCopy,
	    query.params.toArray());
    }

    public SQLQuery queryArgs(Integer id, Integer book_id, Integer reader_id, String status, Date book_or_lend_from, Date book_or_lend_to, Select select) {
	ArrayList<Object> params = new ArrayList<Object>();
	StringBuilder sql = new StringBuilder("SELECT ");
	//ArrayList<String> columns = new ArrayList<String>();
	ArrayList<String> where_conditions = new ArrayList<String>();

	sql.append(
	    switch (select) {
	    case BOOK_ID -> " book_id";
	    case READER_ID -> " reader_id";
	    case COUNT -> " COUNT(*)";
	    default -> " id, book_id, reader_id, status, status_change, book_or_lend_from, book_or_lend_to";
	    }
	);
	//sql.append(" id, book_id, reader_id, status, status_change, book_or_lend_from, book_or_lend_to");
	//columns.add("");
	//sql.append(String.join(", ", columns));
	sql.append(" FROM Copies");
	if (id != null && id!=0) {
	    where_conditions.add("id = ?");
	    params.add(id);
	}
	if (book_id != null && book_id!=0) {
	    where_conditions.add("book_id = ?");
	    params.add(book_id);
	}
	if (reader_id != null && reader_id!=0) {
	    where_conditions.add("reader_id = ?");
	    params.add(reader_id);
	}
	if (status != null && status!="") {
	    where_conditions.add("status::text = ?");
	    params.add(status);
	}
	if (book_or_lend_from != null) {
	    where_conditions.add("book_or_lend_from = ?");
	    params.add(book_or_lend_from);
	}
	if (book_or_lend_to != null) {
	    where_conditions.add("book_or_lend_to = ?");
	    params.add(book_or_lend_to);
	}
	if (where_conditions.size()>0) {
	    sql.append(" WHERE ");
	    sql.append(String.join(" AND ", where_conditions));
	}
	//sql.append(" ORDER BY id");
	//System.err.println("CopyDAO: sql="+sql.toString());
	return new SQLQuery(sql.toString(), params);
    }

    private Copy mapRowToCopy(ResultSet row, int rowNum)
	throws SQLException{
	return new Copy(
	    row.getInt("id"),
	    row.getInt("book_id"),
	    row.getInt("reader_id"),
	    CopyStatus.valueOfLabel(row.getString("status")),
	    row.getTimestamp("status_change"),
	    row.getDate("book_or_lend_from"),
	    row.getDate("book_or_lend_to")
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
	BOOK_ID,
	READER_ID,
	COUNT
    }
}
