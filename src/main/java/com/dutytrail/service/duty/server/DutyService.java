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

    @RequestMapping(method = RequestMethod.GET, value = "/createDataBase", produces = MediaType.APPLICATION_JSON)
    public void createDataBase() {
        this.dutyDAO.createDataBase();
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
    public Long postDuty(@RequestBody DutyInput dutyInput) {
        return this.dutyDAO.postDuty(dutyInput.getName());
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/duty/{dutyId}", produces = MediaType.APPLICATION_JSON)
    public Long deleteDuty(@PathVariable("dutyId") Long dutyId) {
        return this.dutyDAO.deleteDuty(dutyId);
    }

}