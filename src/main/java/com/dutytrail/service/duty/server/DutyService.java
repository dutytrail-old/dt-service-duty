package com.dutytrail.service.duty.server;

import com.dutytrail.service.duty.dao.DutyDAO;
import com.dutytrail.service.duty.entity.Duty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
public class DutyService {

    @Value("${ping.alive}")
    private String configPingAlive;

    @Autowired
    private DutyDAO dutyDAO;

    @RequestMapping(method = RequestMethod.GET, value = "/ping", produces = MediaType.APPLICATION_JSON)
    public String ping() {
        return "Duty Service Alive. Profile in use: "+this.configPingAlive;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/duty/{dutyId}", produces = MediaType.APPLICATION_JSON)
    public Duty duty(@PathVariable("dutyId") String dutyId) throws SQLException {
        return this.dutyDAO.getDuty(dutyId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list/duty/{userId}", produces = MediaType.APPLICATION_JSON)
    public ArrayList<Duty> listDuty(@PathVariable("userId") String userId) throws SQLException {
        return this.dutyDAO.getListDuty(userId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/duty", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public synchronized Long postDuty(@RequestBody String name) throws SQLException {
        return this.dutyDAO.postDuty(name);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/subscribe/{userId}/{dutyId}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public synchronized Long subscribe(@PathVariable("userId") Long userId, @PathVariable("dutyId") Long dutyId) throws SQLException {
        return this.dutyDAO.postUserSubscribeDuty(userId, dutyId);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/unsubscribe/{userId}/{dutyId}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public synchronized Long unsubscribe(@PathVariable("userId") Long userId, @PathVariable("dutyId") Long dutyId) throws SQLException {
        return this.dutyDAO.deleteUserSubscribeDuty(userId, dutyId);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/duty/{dutyId}", produces = MediaType.APPLICATION_JSON)
    public synchronized Long deleteDuty(@PathVariable("dutyId") Long dutyId) throws SQLException {
        return this.dutyDAO.deleteDuty(dutyId);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/duty/cascade/{dutyId}", produces = MediaType.APPLICATION_JSON)
    public synchronized Long deleteCascade(@PathVariable("dutyId") Long dutyId) throws SQLException {
        Long delete = this.deleteDuty(dutyId);
        Long unsubscribe = this.unsubscribe(null, dutyId);
        return delete + unsubscribe;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/is/subscribed/{userId}/{dutyId}", produces = MediaType.APPLICATION_JSON)
    public Boolean isSubscribed(@PathVariable("userId") Long userId, @PathVariable("dutyId") Long dutyId) throws SQLException {
        return this.dutyDAO.isSubscribed(userId, dutyId);
    }

}