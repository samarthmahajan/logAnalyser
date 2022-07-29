package com.example.logAnalyser.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.logAnalyser.dbo.LogEventDO;
import com.example.logAnalyser.service.LogAnalyserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/logfile/analyse/event")
public class LogAnalyserController {
	
	@Autowired
	public LogAnalyserService logAnalyseService;
		
	@GetMapping
	public ResponseEntity<Iterable<LogEventDO>> startLogAnalysis() throws Exception{
		log.info("Event started to analyze the logfiles.");
		Iterable<LogEventDO> dto=	logAnalyseService.analyseLogs();
    	log.info("Event completed ");
		return ResponseEntity.ok(dto);
		
	}
	

}
