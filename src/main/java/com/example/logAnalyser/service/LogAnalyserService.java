package com.example.logAnalyser.service;

import java.util.concurrent.ExecutionException;

import com.example.logAnalyser.dbo.LogEventDO;

public interface LogAnalyserService {

	Iterable<LogEventDO> analyseLogs() throws InterruptedException, ExecutionException;

}
