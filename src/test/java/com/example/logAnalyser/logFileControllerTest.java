package com.example.logAnalyser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;

import com.example.logAnalyser.dto.LogfileJson;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.var;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = { LogAnalyserApplication.class })
public class logFileControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Test
	public void testLogEvent_success() throws Exception {
		var res =  mvc
                .perform(get("/logfile/analyse/event"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		List<LogfileJson> list = mapper.readValue(res, new TypeReference<List<LogfileJson>>() {
		});
		
		Assert.notNull(list);
		Assert.isTrue(list.size()>1);
	}
}
