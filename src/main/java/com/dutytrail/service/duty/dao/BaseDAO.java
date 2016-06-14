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
    public void initConnection() {
        try {
            if(this.con == null) {
                System.out.println("Driver: "+driver);
                System.out.println("Url: "+url);
                System.out.println("Schema: "+schema);
                System.out.println("User: "+user);
                System.out.println("Password: "+password);

                Class.forName(this.driver).newInstance();
                this.con = DriverManager.getConnection(url + schema, user, password);
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

}
