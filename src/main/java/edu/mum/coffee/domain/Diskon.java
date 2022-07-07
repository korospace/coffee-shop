package edu.mum.coffee.domain;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.Date;

@Entity
public class Diskon {
    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty(message = "Code cannot be empty")
    private String code;
    
    private int persen;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Expired date cannot be empty")
    private Date expired;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getPersen() {
        return persen;
    }

    public void setPersen(int persen) {
        this.persen = persen;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    @Override
    public String toString() {
        return "Diskon [code=" + code + ", expired=" + expired + ", id=" + id + ", persen=" + persen + "]";
    }
}
