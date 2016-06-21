package com.dutytrail.service.duty.server;

import com.dutytrail.service.duty.dao.DutyDAO;
import com.dutytrail.service.duty.entity.Duty;
import com.dutytrail.service.duty.entity.DutyInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

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
    public Duty duty(@PathVariable("dutyId") String dutyId) {
        return this.dutyDAO.getDuty(dutyId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list/duty/{userId}", produces = MediaType.APPLICATION_JSON)
    public ArrayList<Duty> listDuty(@PathVariable("userId") String userId) {
        return this.dutyDAO.getListDuty(userId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/duty", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public synchronized Long postDuty(@RequestBody String name) {
        return this.dutyDAO.postDuty(name);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/subscribe/{userId}/{dutyId}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public synchronized Long subscribe(@PathVariable("userId") Long userId, @PathVariable("dutyId") Long dutyId) {
        return this.dutyDAO.postUserSubscribeDuty(userId, dutyId);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/unsubscribe/{userId}/{dutyId}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public synchronized Long unsubscribe(@PathVariable("userId") Long userId, @PathVariable("dutyId") Long dutyId) {
        return this.dutyDAO.deleteUserSubscribeDuty(userId, dutyId);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/duty/{dutyId}", produces = MediaType.APPLICATION_JSON)
    public synchronized Long deleteDuty(@PathVariable("dutyId") Long dutyId) {
        return this.dutyDAO.deleteDuty(dutyId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/is/subscribed/{userId}/{dutyId}", produces = MediaType.APPLICATION_JSON)
    public Boolean isSubscribed(@PathVariable("userId") Long userId, @PathVariable("dutyId") Long dutyId){
        return this.dutyDAO.isSubscribed(userId, dutyId);
    }

}