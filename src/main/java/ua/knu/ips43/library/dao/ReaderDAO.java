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
import java.sql.Timestamp;
//import java.time.LocalDateTime;

import ua.knu.ips43.library.model.Reader;

@Repository
public class ReaderDAO {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ReaderDAO(JdbcTemplate jdbcTemplate) {
	this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Reader> get(int id) {
	List<Reader> results = jdbcTemplate.query(
	    "select id, first_name,middle_name,last_name, address, phone, email, registration_date, valid_to from Readers where id=?",
	    this::mapRowToReader,
	    id);
	return results.size() == 0 ?
	    Optional.empty() :
	    Optional.of(results.get(0));
    }

    // , Timestamp registration_date
    public Integer add(String first_name, String middle_name, String last_name, String address, String phone, String email, Timestamp valid_to) {
	jdbcTemplate.update("INSERT INTO Readers (first_name,middle_name,last_name, address, phone, email, registration_date, valid_to) VALUES (?, ?, ?, ?, ?, ?, NOW(), ?)", first_name,middle_name,last_name, address, phone, email, valid_to);
	return 0;
    }

    public boolean del(int id){
      jdbcTemplate.update("delete from Readers where id=?", id);
      return true;
    }

    public List<Reader> findAll() {
	List<Reader> results = jdbcTemplate.query(
	    "SELECT id, first_name,middle_name,last_name, address, phone, email, registration_date, valid_to FROM Readers",
	    this::mapRowToReader);
	return results;
    }

    public List<Reader> find(Integer id, String first_name, String middle_name, String last_name, String address, String phone, String email, String addresspattern) {
	ArrayList<Object> params = new ArrayList<Object>();
	StringBuilder sql = new StringBuilder("SELECT ");
	//ArrayList<String> columns = new ArrayList<String>();
	ArrayList<String> where_conditions = new ArrayList<String>();
	sql.append(" id, first_name,middle_name,last_name, address, phone, email, registration_date, valid_to");
	//columns.add("first_name");
	//columns.add("");
	//sql.append(String.join(", ", columns));
	sql.append(" FROM Readers");
	if (id != null && id!=0) {
	    where_conditions.add("id = ?");
	    params.add(id);
	}
	if (first_name != null && first_name!="") {
	    where_conditions.add("first_name = ?");
	    params.add(first_name);
	}
	if (middle_name != null && middle_name!="") {
	    where_conditions.add("middle_name = ?");
	    params.add(middle_name);
	}
	if (last_name != null && last_name!="") {
	    where_conditions.add("last_name = ?");
	    params.add(last_name);
	}
	if (address != null && address!="") {
	    where_conditions.add("address = ?");
	    params.add(address);
	}
	if (phone != null && phone!="") {
	    where_conditions.add("phone = ?");
	    params.add(phone);
	}
	if (email != null && email!="") {
	    where_conditions.add("email = ?");
	    params.add(email);
	}
	if (addresspattern != null && addresspattern!="") {
	    where_conditions.add("address LIKE ?");
	    params.add("%" + addresspattern.trim() + "%");
	}
	if (where_conditions.size()>0) {
	    sql.append(" WHERE ");
	    sql.append(String.join(" AND ", where_conditions));
	}
	sql.append(" ORDER BY id");
	//System.err.println("ReaderDAO: sql="+sql.toString());
	List<Reader> results = jdbcTemplate.query(
	    sql.toString(),
	    this::mapRowToReader,
	    params.toArray());
	return results;
    }

    public Integer getCount() {
	List<Integer> results = jdbcTemplate.query(
	    "SELECT COUNT(*) FROM Readers",
	    this::mapRowToCount);
	return results.get(0);
    }

    private Reader mapRowToReader(ResultSet row, int rowNum)
	throws SQLException{
	return new Reader(
	    row.getInt("id"),
	    row.getString("first_name"),
	    row.getString("middle_name"),
	    row.getString("last_name"),
	    row.getString("address"),
	    row.getString("phone"),
	    row.getString("email"),
	    row.getTimestamp("registration_date"),
	    row.getTimestamp("valid_to")
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
