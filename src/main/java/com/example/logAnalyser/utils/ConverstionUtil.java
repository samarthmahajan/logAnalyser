package com.example.logAnalyser.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.logAnalyser.controller.LogEventDoRepository;
import com.example.logAnalyser.dbo.LogEventDO;
import com.example.logAnalyser.dto.LogfileJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ConverstionUtil {

	@Autowired
	public ObjectMapper mapper;

	@Autowired
	public LogEventDoRepository repo;
	
	@Value("${logfile.batchSize}")
	private int batchSize ;
	
	public List<LogEventDO> getlogEventMap(File file) {

		Map<String, LogfileJson> logJsonMap = new ConcurrentHashMap<>();
		List<LogEventDO> eventList = new ArrayList<>();
		if (file.exists()) {
			Path logFilePath = Paths.get(file.getPath());
			try (Stream linesStream = Files.lines(logFilePath)) {
				linesStream.forEach(line -> {
					try {
						LogfileJson logJson = mapper.readValue(line.toString(), LogfileJson.class);
						if (logJsonMap.containsKey(logJson.getId())) {
							eventList.add(getList(logJsonMap, logJson));
							logJsonMap.remove(logJson.getId());
						} else {
							logJsonMap.put(logJson.getId(), logJson);
						}
					} catch (IOException e) {
						log.error("Internal error occurred during file read {} " + e.getLocalizedMessage());
					}
				});

			} catch (IOException e) {
				log.error("Internal error occurred during file read {} " + e.getLocalizedMessage());
			}
		}

		return eventList;
	}

	public LogEventDO getList(Map<String, LogfileJson> eventJsonMap, LogfileJson newEventJson) {
		LogfileJson previousEventLog = eventJsonMap.get(newEventJson.getId());
		return checkEventDuration(previousEventLog, newEventJson);
	}

	private LogEventDO checkEventDuration(LogfileJson previousLog, LogfileJson newEventJson) {
		long timeDiff;
		if (previousLog.getTimestamp() < newEventJson.getTimestamp()) {
			timeDiff = Math.subtractExact(newEventJson.getTimestamp(), previousLog.getTimestamp());
		} else {
			timeDiff = Math.subtractExact(previousLog.getTimestamp(), newEventJson.getTimestamp());
		}
		return convertIntoLogEvent(newEventJson, timeDiff);
	}

	private LogEventDO convertIntoLogEvent(LogfileJson eventJson, long timeDiff) {
		LogEventDO event = new LogEventDO();
		event.setId(eventJson.getId());
		event.setSpan(timeDiff);
		if (StringUtils.isNotEmpty(eventJson.getHost())) {
			event.setHost(eventJson.getHost());
		}
		event.setType(eventJson.getType());
		if (timeDiff > 4L) {
			event.setAlert(true);
		}
		return event;
	}

	public List<LogEventDO> saveDetails(List<LogEventDO> json) {

        final long start = System.currentTimeMillis();
        log.info("Converting the list to baches of size : {}", batchSize);
        List<List<LogEventDO>> logBatches = Lists.partition(json, batchSize);
        log.info("BATCHES FOUND : {}",logBatches.size());
        log.info("Saving a list of Event Logs of size {} records", json.size());
        
        logBatches.parallelStream().forEach(batch -> repo.saveAll(batch));        
      
        log.info("Elapsed time: {}", (System.currentTimeMillis() - start));
        log.info("Vales Stored in DB : fetched"); 
        return repo.findAll();

	}

}
