package com.dutytrail.service.duty.server;

import com.dutytrail.service.duty.dao.DutyDAO;
import com.dutytrail.service.duty.entity.User;
import com.dutytrail.service.duty.starter.Duty;
import org.junit.After;
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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = Duty.class, loader = SpringApplicationContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest({"eureka.client.enabled=false","spring.cloud.config.enabled=false"})
public class DutyServiceTest {

    @Autowired private DutyService dutyService;
    @Mock private DutyDAO dutyDAO;
    private ArrayList<com.dutytrail.service.duty.entity.Duty> fakeDuties;
    private ArrayList<User> fakeSubscriptions;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(dutyService, "dutyDAO", this.dutyDAO);
        this.fakeSubscriptions = new ArrayList<>();
        this.fakeSubscriptions.add(new User(1L, "firstName", "lastName", "email", "passphrase", "male"));
        this.fakeDuties = new ArrayList<>();
        this.fakeDuties.add(new com.dutytrail.service.duty.entity.Duty(1L, "Duty name", this.fakeSubscriptions));
    }

    @After
    public void tearDown(){
        this.dutyService = null;
        this.dutyDAO = null;
        this.fakeDuties = null;
        this.fakeSubscriptions = null;
    }

    @Test
    public void pingTest() {
        String pingResponse = this.dutyService.ping();
        Assert.assertNotNull(pingResponse);
        Assert.assertEquals("Duty Service Alive. Profile in use: Config properties loaded from LOCAL profile", pingResponse);
    }

    @Test
    public void dutyTest() throws SQLException {
        when(dutyDAO.getDuty(Mockito.anyString())).thenReturn(new com.dutytrail.service.duty.entity.Duty(1L, "name", this.fakeSubscriptions));
        com.dutytrail.service.duty.entity.Duty duty = this.dutyService.duty("1");
        Assert.assertNotNull(duty);
        Assert.assertTrue(1L == duty.getId());
        Assert.assertEquals("name", duty.getName());
    }

    @Test
    public void listDutyTest() throws SQLException {
        when(dutyDAO.getListDuty(Mockito.anyString())).thenReturn(this.fakeDuties);
        List<com.dutytrail.service.duty.entity.Duty> duties = this.dutyService.listDuty("1");
        Assert.assertNotNull(duties);
        com.dutytrail.service.duty.entity.Duty duty = duties.get(0);
        Assert.assertTrue(1L == duty.getId());
    }

    @Test
    public void postDutyTest() throws SQLException {
        when(dutyDAO.postDuty(Mockito.anyString())).thenReturn(1L);
        Long dutyId = this.dutyService.postDuty("Some Duty");
        Assert.assertNotNull(dutyId);
        Assert.assertTrue(1L == dutyId);
    }

    @Test
    public void subscribeTest() throws SQLException {
        when(dutyDAO.postUserSubscribeDuty(Mockito.anyLong(), Mockito.anyLong())).thenReturn(1L);
        Long subscribeReturn = this.dutyService.subscribe(1L, 1L);
        Assert.assertNotNull(subscribeReturn);
    }

    @Test
    public void unSubscribeTest() throws SQLException {
        when(dutyDAO.deleteUserSubscribeDuty(Mockito.anyLong(), Mockito.anyLong())).thenReturn(1L);
        Long unSubscribeReturn = this.dutyService.unsubscribe(1L, 1L);
        Assert.assertNotNull(unSubscribeReturn);
    }

    @Test
    public void deleteDutyTest() throws SQLException {
        when(dutyDAO.deleteDuty(Mockito.anyLong())).thenReturn(1L);
        Long deleteDutyReturn = this.dutyService.deleteDuty(1L);
        Assert.assertNotNull(deleteDutyReturn);
    }

    @Test
    public void deleteCascade() throws SQLException {
        when(dutyDAO.deleteDuty(Mockito.anyLong())).thenReturn(1L);
        when(dutyDAO.deleteUserSubscribeDuty(Mockito.anyLong(), Mockito.anyLong())).thenReturn(1L);
        Long deleteCascadeReturn = this.dutyService.deleteCascade(1L);
        Assert.assertNotNull(deleteCascadeReturn);
    }

    @Test
    public void isSubscribedTest() throws SQLException {
        when(dutyDAO.isSubscribed(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Boolean.TRUE);
        Boolean isSubscribedReturn = this.dutyService.isSubscribed(1L, 1L);
        Assert.assertNotNull(isSubscribedReturn);
        Assert.assertTrue(isSubscribedReturn);
    }

}
