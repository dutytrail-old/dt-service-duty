package com.dutytrail.service.duty.server;

import com.dutytrail.service.duty.dao.MySqlDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;

@Slf4j
@RestController
public class MySqlService {

    @Autowired
    private MySqlDAO mySqlDAO;

    @RequestMapping(method = RequestMethod.GET, value = "/db/script", produces = MediaType.APPLICATION_JSON)
    public String dbScript(@RequestParam(value = "name") String name) {
        log.info("Executing script "+name);
        this.mySqlDAO.executeScript(name);
        log.info("Script "+name+" executed.");
        return ("Script "+name+" executed.");
    }

}