package com.example.logAnalyser.controller;

import org.springframework.stereotype.Repository;

import com.example.logAnalyser.dbo.LogEventDO;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface LogEventDoRepository extends JpaRepository<LogEventDO, String> {
}
