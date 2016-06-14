package com.dutytrail.service.duty.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;

@Slf4j
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
                log.info("Driver: "+driver);
                log.info("Url: "+url);
                log.info("Schema: "+schema);
                log.info("User: "+user);
                log.info("Password: "+password);

                Class.forName(this.driver).newInstance();
                this.con = DriverManager.getConnection(url + schema, user, password);
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

}
