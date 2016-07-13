package com.dutytrail.service.duty.dao;

import com.dutytrail.service.duty.entity.Duty;
import com.dutytrail.service.duty.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
    @Value("${sql.delete.subscribeDuty}") private String deleteSubscribeDuty;

    private PreparedStatement ps = null;
    private ResultSet resultSet = null;

    public Duty getDuty(String dutyId) throws SQLException {
        Duty resultDuty = null;

        try {
            ps = con.prepareStatement(duty);
            ps.setString(1, dutyId);
            resultSet = ps.executeQuery();

            if (resultSet.next()) {
                resultDuty = new Duty(resultSet.getLong("id"), resultSet.getString("name"), this.getUsers(this.getSubscriptions(resultSet.getLong("id"))));
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            super.closeAll(ps, resultSet);
        }
        return resultDuty;
    }

    public ArrayList<Duty> getListDuty(String userId) throws SQLException {
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
            throw e;
        } finally {
            super.closeAll(ps, resultSet);
        }
        return dutyList;
    }

    protected ArrayList<Long> getSubscriptions(Long dutyId) throws SQLException {
        ArrayList<Long> subcriptions = new ArrayList<>();

        try {
            ps = con.prepareStatement(subscriptions);
            ps.setLong(1, dutyId);
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                subcriptions.add(resultSet.getLong("user_id"));
            }
        } catch (SQLException e) {
            throw e;
        }
        return subcriptions;
    }

    protected ArrayList<User> getUsers(List<Long> userIds) throws SQLException {
        ArrayList<User> users = new ArrayList<>();

        try {
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
            throw e;
        }
        return users;
    }

    public Long postDuty(String name) throws SQLException {
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
            throw e;
        } finally {
            super.closeAll(ps, resultSet);
        }
        return null;
    }

    public Long postUserSubscribeDuty(Long userId, Long dutyId) throws SQLException {
        try {
            ps = con.prepareStatement(insertUserSubscribeDuty);
            ps.setLong(1, userId);
            ps.setLong(2, dutyId);
            return (long) ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            super.closeAll(ps, resultSet);
        }
    }

    public Long deleteUserSubscribeDuty(Long userId, Long dutyId) throws SQLException {
        try {
            if(userId!=null) {
                ps = con.prepareStatement(deleteUserSubscribeDuty);
                ps.setLong(1, userId);
                ps.setLong(2, dutyId);
            } else {
                ps = con.prepareStatement(deleteSubscribeDuty);
                ps.setLong(1, dutyId);
            }
            return (long) ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            super.closeAll(ps, resultSet);
        }
    }

    public Boolean isSubscribed(Long userId, Long dutyId) throws SQLException {
        boolean isSuscribed = false;
        try {
            ps = con.prepareStatement(isSubscribed);
            ps.setLong(1, userId);
            ps.setLong(2, dutyId);
            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                isSuscribed = true;
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            super.closeAll(ps, resultSet);
        }
        return isSuscribed;
    }

    public Long deleteDuty(Long dutyId) throws SQLException {
        try {
            ps = con.prepareStatement(deleteDuty);
            ps.setLong(1, dutyId);
            if(ps.executeUpdate()==1)
                return dutyId;
        } catch (SQLException e) {
            throw e;
        } finally {
            super.closeAll(ps, resultSet);
        }
        return -1L;
    }
}
