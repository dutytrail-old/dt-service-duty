package com.dutytrail.service.duty.entity;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "dutyInput")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"name"})
public class DutyInput {

    @XmlElement(name = "name") private String name;

    public DutyInput() {

    }

    public DutyInput(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}