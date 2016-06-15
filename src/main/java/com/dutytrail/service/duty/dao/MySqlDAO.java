package com.dutytrail.service.duty.dao;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

@Component
public class MySqlDAO extends BaseDAO {

    public void executeScript(String name) {
        String script = streamToString(getClass().getClassLoader().getResourceAsStream("db/"+name));
        Arrays.stream(script.split(";")).forEach(this::executeLine);
    }

    private void executeLine(String line) {
        PreparedStatement ps = null;
        try{
            ps = con.prepareStatement(line);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            super.closeAll(ps, null);
        }
    }

    private String streamToString(InputStream inputStream) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

}
