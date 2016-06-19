package com.dutytrail.service.duty.dao;

import com.dutytrail.service.duty.entity.Duty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;

@Component
public class DutyDAO extends BaseDAO {

    @Value("${sql.select.duty}") private String duty;
    @Value("${sql.select.listDuty}") private String listDuty;
    @Value("${sql.insert.duty}") private String insertDuty;
    @Value("${sql.select.lastInsertId}") private String lastInsertId;
    @Value("${sql.delete.duty}") private String deleteDuty;

    public Duty getDuty(String dutyId) {
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            ps = con.prepareStatement(duty);
            ps.setString(1, dutyId);
            resultSet = ps.executeQuery();

            if (resultSet.next()) {
                return new Duty(resultSet.getLong("id"), resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            super.closeAll(ps, resultSet);
        }
        return null;
    }

    public ArrayList<Duty> getListDuty(String userId) {
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        ArrayList<Duty> dutyList = null;

        try {
            dutyList = new ArrayList<>();
            ps = con.prepareStatement(listDuty);
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                dutyList.add(new Duty(resultSet.getLong("id"), resultSet.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            super.closeAll(ps, resultSet);
        }
        return dutyList;
    }

    public Long postDuty(String name) {
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            //insert the task
            ps = con.prepareStatement(insertDuty);
            ps.setString(1, name);
            ps.executeUpdate();

            //getting the generated id
            ps = con.prepareStatement(lastInsertId);
            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("last_insert_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            super.closeAll(ps, resultSet);
        }
        return null;
    }

    public Long deleteDuty(Long dutyId) {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(deleteDuty);
            ps.setLong(1, dutyId);
            if(ps.executeUpdate()==1)
                return dutyId;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            super.closeAll(ps, null);
        }
        return -1L;
    }
}
