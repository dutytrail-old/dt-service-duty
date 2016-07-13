package com.dutytrail.service.duty.dao;

import com.dutytrail.service.duty.dao.DutyDAO;
import com.dutytrail.service.duty.entity.User;
import com.dutytrail.service.duty.starter.Duty;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = Duty.class, loader = SpringApplicationContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest({"eureka.client.enabled=false","spring.cloud.config.enabled=false"})
public class DutyDAOTest {

    @Mock Connection mockedCon;
    @Mock PreparedStatement mockedPreparedStatement;
    @Mock ResultSet mockedResultSet;

    private @Autowired
    DutyDAO dutyDAO;

    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);
        doNothing().when(this.mockedCon).commit();
        when(this.mockedCon.prepareStatement(Mockito.anyString())).thenReturn(this.mockedPreparedStatement);
        doNothing().when(this.mockedPreparedStatement).setString(Mockito.anyInt(), Mockito.anyString());
        doNothing().when(this.mockedPreparedStatement).setLong(Mockito.anyInt(), Mockito.anyLong());
        when(this.mockedPreparedStatement.executeUpdate()).thenReturn(1);
        when(this.mockedPreparedStatement.executeQuery()).thenReturn(this.mockedResultSet);
        when(this.mockedResultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        when(this.mockedResultSet.getLong(Mockito.anyString())).thenReturn(1L);
        when(this.mockedResultSet.getString(Mockito.anyString())).thenReturn("some string");
        when(this.mockedResultSet.getTimestamp(Mockito.anyString())).thenReturn(new Timestamp(Calendar.getInstance().getTime().getTime()));

        ReflectionTestUtils.setField(dutyDAO, "con", this.mockedCon);
        ReflectionTestUtils.setField(dutyDAO, "ps", this.mockedPreparedStatement);
        ReflectionTestUtils.setField(dutyDAO, "resultSet", this.mockedResultSet);
    }

    @Test
    public void getDutyTest() throws SQLException {
        com.dutytrail.service.duty.entity.Duty duty = this.dutyDAO.getDuty("1");
        Assert.assertNotNull(duty);
        Assert.assertTrue(1L == duty.getId());
        Assert.assertEquals("some string", duty.getName());
        List<User> subscriptions = duty.getSubscriptions();
        Assert.assertNotNull(subscriptions);
    }

    @Test(expected = SQLException.class)
    public void getTrailExceptionTest() throws SQLException {
        when(this.mockedResultSet.next()).thenThrow(new SQLException());
        this.dutyDAO.getDuty("1");
    }

    @Test
    public void getTrailNoResultsTest() throws SQLException {
        when(this.mockedResultSet.next()).thenReturn(Boolean.FALSE);
        Assert.assertNull(this.dutyDAO.getDuty("1"));
    }

    @Test
    public void getListDutyTest() throws SQLException {
        List<com.dutytrail.service.duty.entity.Duty> duties = this.dutyDAO.getListDuty("1");
        Assert.assertNotNull(duties);
        Assert.assertTrue(1L == duties.get(0).getId());
        Assert.assertEquals("some string", duties.get(0).getName());
        List<User> subscriptions = duties.get(0).getSubscriptions();
        Assert.assertNotNull(subscriptions);
    }

    @Test
    public void getListDutyNoUserTest() throws SQLException {
        List<com.dutytrail.service.duty.entity.Duty> duties = this.dutyDAO.getListDuty("{userId}");
        Assert.assertNotNull(duties);
        Assert.assertTrue(1L == duties.get(0).getId());
        Assert.assertEquals("some string", duties.get(0).getName());
        List<User> subscriptions = duties.get(0).getSubscriptions();
        Assert.assertNotNull(subscriptions);
    }

    @Test(expected = SQLException.class)
    public void getListDutyExceptionTest() throws SQLException {
        when(this.mockedResultSet.next()).thenThrow(new SQLException());
        this.dutyDAO.getListDuty("1");
    }

    @Test
    public void getSubscriptionsTest() throws SQLException {
        List<Long> subscriptions = this.dutyDAO.getSubscriptions(1L);
        Assert.assertNotNull(subscriptions);
        Assert.assertTrue(1L == subscriptions.get(0));
    }

    @Test(expected = SQLException.class)
    public void getSubscriptionsExceptionTest() throws SQLException {
        when(this.mockedResultSet.next()).thenThrow(new SQLException());
        this.dutyDAO.getSubscriptions(1L);
    }

    @Test
    public void getUsersTest() throws SQLException {
        List<Long> fakeUsers = new ArrayList<>();
        fakeUsers.add(1L);
        List<User> users = this.dutyDAO.getUsers(fakeUsers);
        Assert.assertNotNull(users);
        User fakeUser = users.get(0);
        Assert.assertNotNull(fakeUser);
        Assert.assertTrue(1L == fakeUser.getId());
        Assert.assertEquals("some string", fakeUser.getEmail());
        Assert.assertEquals("some string", fakeUser.getFirstName());
        Assert.assertEquals("some string", fakeUser.getGender());
        Assert.assertEquals("some string", fakeUser.getLastName());
        Assert.assertEquals("some string", fakeUser.getPassphrase());
    }

    @Test(expected = SQLException.class)
    public void getUsersExceptionTest() throws SQLException {
        when(this.mockedResultSet.next()).thenThrow(new SQLException());
        List<Long> fakeUsers = new ArrayList<>();
        fakeUsers.add(1L);
        this.dutyDAO.getUsers(fakeUsers);
    }

    @Test
    public void isSubscribedTest() throws SQLException {
        Boolean isSubscribed = this.dutyDAO.isSubscribed(1L, 1L);
        Assert.assertNotNull(isSubscribed);
        Assert.assertTrue(isSubscribed);
    }

    @Test(expected = SQLException.class)
    public void isSubscribedExceptionTest() throws SQLException {
        when(this.mockedResultSet.next()).thenThrow(new SQLException());
        this.dutyDAO.isSubscribed(1L, 1L);
    }

    @Test
    public void isSubscribedNoResultsTest() throws SQLException {
        when(this.mockedResultSet.next()).thenReturn(Boolean.FALSE);
        Assert.assertFalse(this.dutyDAO.isSubscribed(1L, 1L));
    }

    @Test
    public void deleteDutyTest() throws SQLException {
        Long deleteResults = this.dutyDAO.deleteDuty(1L);
        Assert.assertNotNull(deleteResults);
        Assert.assertTrue(1L == deleteResults);
    }

    @Test(expected = SQLException.class)
    public void deleteDutyExceptionTest() throws SQLException {
        when(this.mockedPreparedStatement.executeUpdate()).thenThrow(new SQLException());
        this.dutyDAO.deleteDuty(1L);
    }

    @Test
    public void deleteDutyNoResultsTest() throws SQLException {
        when(this.mockedPreparedStatement.executeUpdate()).thenReturn(0);
        Assert.assertTrue(-1L == this.dutyDAO.deleteDuty(1L));
    }

    @Test
    public void deleteUserSubscribeDutyTest() throws SQLException {
        Long deleteResults = this.dutyDAO.deleteUserSubscribeDuty(1L, 1L);
        Assert.assertNotNull(deleteResults);
        Assert.assertTrue(1L == deleteResults);
    }

    @Test
    public void deleteUserSubscribeDutyNoUserTest() throws SQLException {
        Long deleteResults = this.dutyDAO.deleteUserSubscribeDuty(null, 1L);
        Assert.assertNotNull(deleteResults);
        Assert.assertTrue(1L == deleteResults);
    }

    @Test(expected = SQLException.class)
    public void deleteUserSubscribeDutyExceptionTest() throws SQLException {
        when(this.mockedPreparedStatement.executeUpdate()).thenThrow(new SQLException());
        this.dutyDAO.deleteUserSubscribeDuty(1L, 1L);
    }

    @Test
    public void deleteUserSubscribeDutyNoResultsTest() throws SQLException {
        when(this.mockedPreparedStatement.executeUpdate()).thenReturn(0);
        Assert.assertTrue(0 == this.dutyDAO.deleteUserSubscribeDuty(1L, 1L));
    }

    @Test
    public void postUserSubscribeDutyTest() throws SQLException {
        Long deleteResults = this.dutyDAO.postUserSubscribeDuty(1L, 1L);
        Assert.assertNotNull(deleteResults);
        Assert.assertTrue(1L == deleteResults);
    }

    @Test(expected = SQLException.class)
    public void postUserSubscribeDutyExceptionTest() throws SQLException {
        when(this.mockedPreparedStatement.executeUpdate()).thenThrow(new SQLException());
        this.dutyDAO.postUserSubscribeDuty(1L, 1L);
    }

    @Test
    public void postUserSubscribeDutyNoResultsTest() throws SQLException {
        when(this.mockedPreparedStatement.executeUpdate()).thenReturn(0);
        Assert.assertTrue(0 == this.dutyDAO.postUserSubscribeDuty(1L, 1L));
    }

    @Test
    public void postDutyTest() throws SQLException {
        Long dutyId = this.dutyDAO.postDuty("Some duty");
        Assert.assertNotNull(dutyId);
        Assert.assertTrue(1L == dutyId);
    }

    @Test(expected = SQLException.class)
    public void postDutyExceptionTest() throws SQLException {
        when(this.mockedResultSet.next()).thenThrow(new SQLException());
        this.dutyDAO.postDuty("Some duty");
    }

    @Test
    public void postDutyNoResultsTest() throws SQLException {
        when(this.mockedResultSet.next()).thenReturn(Boolean.FALSE);
        Assert.assertNull(this.dutyDAO.postDuty("Some duty"));
    }
}