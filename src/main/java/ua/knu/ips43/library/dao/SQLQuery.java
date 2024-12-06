package ua.knu.ips43.library.dao;

import java.util.List;

public class SQLQuery {
    public String sql;
    public List<Object> params;

    public SQLQuery(String sql, List<Object> params) {
	this.sql = sql;
	this.params = params;
    }
}
