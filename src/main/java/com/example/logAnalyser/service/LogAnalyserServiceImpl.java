package com.example.logAnalyser.service;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.logAnalyser.dbo.LogEventDO;
import com.example.logAnalyser.utils.ConverstionUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LogAnalyserServiceImpl implements LogAnalyserService{

	@Value("${logfile.file.name}")
	private String FILE_NAME ;
	
	@Autowired
	public ConverstionUtil convertion;

	@Override
	public Iterable<LogEventDO> analyseLogs() throws InterruptedException, ExecutionException {
		URL url =getClass().getClassLoader().getResource(FILE_NAME);
		if(url == null) {
			log.error("File not found ");
		}
		File file = new File(getClass().getClassLoader().getResource(FILE_NAME).getFile());
		
		CompletableFuture<List<LogEventDO>> completebleFuture= CompletableFuture.supplyAsync(()->convertion.getlogEventMap(file));
		CompletableFuture<Iterable<LogEventDO>> future = completebleFuture.thenApply( json -> convertion.saveDetails(json));
		
		return future.get();
	}
	
	

}
