package com.example.logAnalyser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
public class LogfileJson {
	
	private String id;
    private LogState state;
    private String type;
    private String host;
    private long timestamp;
	

}
