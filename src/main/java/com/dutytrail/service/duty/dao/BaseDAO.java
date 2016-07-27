package com.dutytrail.service.duty.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;

@Component
class BaseDAO {

    @Value("${db.driver}") private String driver;
    @Value("${db.url}") private String url;
    @Value("${db.schema}") private String schema;
    @Value("${db.user}") private String user;
    @Value("${db.password}") private String password;

    Connection con = null;

    @PostConstruct
    public void initConnection() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class.forName(this.driver).newInstance();
        this.con = DriverManager.getConnection(url + schema, user, password);
    }

    void closeAll(PreparedStatement ps, ResultSet resultSet) throws SQLException {
        if(ps!=null) {
            ps.close();
        }
        if(resultSet!=null){
            resultSet.close();
        }
    }

}