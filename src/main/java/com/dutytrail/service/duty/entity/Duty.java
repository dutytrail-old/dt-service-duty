package com.dutytrail.service.duty.entity;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "duty")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"id","name","subscriptions"})
public class Duty {

    @XmlElement(name = "id") private Long id;
    @XmlElement(name = "name") private String name;
    @XmlElementWrapper(name = "subscriptions")
    @XmlElement(name = "user")
    private List<User> subscriptions;

    public Duty(Long id, String name, List<User> subscriptions) {
        this.id = id;
        this.name = name;
        this.subscriptions = subscriptions;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public List<User> getSubscriptions() {
        return subscriptions;
    }
}