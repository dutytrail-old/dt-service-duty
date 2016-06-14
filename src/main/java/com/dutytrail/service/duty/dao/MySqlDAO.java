package com.dutytrail.service.duty.dao;

import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlDAO extends BaseDAO {

    public void createDataBase(){
        String dropDuty = "drop table dt_duty cascade";
        String dropUser = "drop table dt_user cascade";
        String createDuty = "CREATE TABLE dt_duty (id MEDIUMINT NOT NULL AUTO_INCREMENT, name varchar(100) NOT NULL DEFAULT '', PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8";
        String createUser = "CREATE TABLE dt_user (id MEDIUMINT NOT NULL AUTO_INCREMENT, name varchar(100) NOT NULL DEFAULT '', PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8";
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            ps = con.prepareStatement(dropDuty);
            ps.executeUpdate();
            ps = con.prepareStatement(dropUser);
            ps.executeUpdate();
            ps = con.prepareStatement(createDuty);
            ps.executeUpdate();
            ps = con.prepareStatement(createUser);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            super.closeAll(ps, resultSet);
        }
    }

}
