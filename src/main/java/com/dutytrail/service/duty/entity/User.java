package com.dutytrail.service.duty.entity;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"id","firstName", "email", "passphrase", "gender"})
public class User {

    @XmlElement(name = "id") private Long id;
    @XmlElement(name = "firstName") private String firstName;
    @XmlElement(name = "lastName") private String lastName;
    @XmlElement(name = "email") private String email;
    @XmlElement(name = "passphrase") private String passphrase;
    @XmlElement(name = "gender") private String gender;

    public User(Long id, String firstName, String lastName, String email, String passphrase, String gender) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passphrase = passphrase;
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public String getGender() {
        return gender;
    }
}