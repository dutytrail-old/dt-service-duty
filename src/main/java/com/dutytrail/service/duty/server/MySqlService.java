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

    @RequestMapping(method = RequestMethod.GET, value = "/mysql/create", produces = MediaType.APPLICATION_JSON)
    public void createDataBase() {
        log.info("Creating database...");
        this.mySqlDAO.createDataBase();
        log.info("Database created.");
    }

}