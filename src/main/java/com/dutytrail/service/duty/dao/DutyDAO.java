package com.dutytrail.service.duty.dao;

import com.dutytrail.service.duty.entity.Duty;
import com.dutytrail.service.duty.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class DutyDAO extends BaseDAO {

    @Value("${sql.select.duty}") private String duty;
    @Value("${sql.select.listDuty}") private String listDuty;
    @Value("${sql.select.listDutyByUser}") private String listDutyByUser;
    @Value("${sql.select.isSubscribed}") private String isSubscribed;
    @Value("${sql.insert.duty}") private String insertDuty;
    @Value("${sql.select.lastInsertId}") private String lastInsertId;
    @Value("${sql.select.subscriptions}") private String subscriptions;
    @Value("${sql.select.users}") private String usersQuery;
    @Value("${sql.delete.duty}") private String deleteDuty;
    @Value("${sql.insert.userSubscribeDuty}") private String insertUserSubscribeDuty;
    @Value("${sql.delete.userSubscribeDuty}") private String deleteUserSubscribeDuty;

    public Duty getDuty(String dutyId) {
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            ps = con.prepareStatement(duty);
            ps.setString(1, dutyId);
            resultSet = ps.executeQuery();

            if (resultSet.next()) {
                return new Duty(resultSet.getLong("id"), resultSet.getString("name"), this.getUsers(this.getSubscriptions(resultSet.getLong("id"))));
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
            if(userId.equals("{userId}")){
                ps = con.prepareStatement(listDuty);
            }else {
                ps = con.prepareStatement(listDutyByUser);
                ps.setString(1, userId);
            }
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                dutyList.add(new Duty(resultSet.getLong("id"), resultSet.getString("name"), this.getUsers(this.getSubscriptions(resultSet.getLong("id")))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            super.closeAll(ps, resultSet);
        }
        return dutyList;
    }

    private ArrayList<Long> getSubscriptions(Long dutyId) {
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        ArrayList<Long> subcriptions = null;

        try {
            subcriptions = new ArrayList<>();
            ps = con.prepareStatement(subscriptions);
            ps.setLong(1, dutyId);
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                subcriptions.add(resultSet.getLong("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            super.closeAll(ps, resultSet);
        }
        return subcriptions;
    }

    private ArrayList<User> getUsers(List<Long> userIds) {
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        ArrayList<User> users = null;

        try {
            users = new ArrayList<>();
            ps = con.prepareStatement(usersQuery);
            for(Long userId : userIds) {
                ps.setLong(1, userId);
                resultSet = ps.executeQuery();

                while (resultSet.next()) {
                    users.add(new User(resultSet.getLong("id"), resultSet.getString("first_name"), resultSet.getString("last_name"), resultSet.getString("email"), resultSet.getString("passphrase"), resultSet.getString("gender")));
                }
                ps.clearParameters();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            super.closeAll(ps, resultSet);
        }
        return users;
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

    public Long postUserSubscribeDuty(Long userId, Long dutyId) {
        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(insertUserSubscribeDuty);
            ps.setLong(1, userId);
            ps.setLong(2, dutyId);
            return (long) ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            super.closeAll(ps, null);
        }
        return null;
    }

    public Long deleteUserSubscribeDuty(Long userId, Long dutyId) {
        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(deleteUserSubscribeDuty);
            ps.setLong(1, userId);
            ps.setLong(2, dutyId);
            return (long) ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            super.closeAll(ps, null);
        }
        return null;
    }

    public Boolean isSubscribed(Long userId, Long dutyId) {
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            ps = con.prepareStatement(isSubscribed);
            ps.setLong(1, userId);
            ps.setLong(2, dutyId);
            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            super.closeAll(ps, resultSet);
        }
        return false;
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
