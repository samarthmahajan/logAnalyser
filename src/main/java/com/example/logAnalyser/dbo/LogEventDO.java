package com.example.logAnalyser.dbo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "logtracer")
@Data
public class LogEventDO implements Serializable{
	@Id
    private String id;
    private long span;
    private String type;
    private String host;
    private boolean alert;
}
