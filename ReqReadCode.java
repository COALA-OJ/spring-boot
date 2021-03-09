package org.coala.controller;

import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j;

@RestController
@Log4j
public class ReqReadCode {
	static HttpHeaders headers = new HttpHeaders();
    
	@RequestMapping(value = "/spring/codedetail", produces = "application/json; charset=utf8", method = RequestMethod.POST)
    @CrossOrigin("*")
    public ResponseEntity<String> reqReadCode(@RequestBody JSONObject body) {
    	headers.add("Content-Type", "aplication/json");
    	JSONObject js= body;
    	Object SubNum = js.get("SubNum").toString();
    	log.info("SubNum is......" + SubNum);
    	
    	JSONObject code = new JSONObject();
    	code.put("Code", "#include<bit/stdc++>...");
    	log.info("Code is...... " + code);
    	return ResponseEntity.ok(code.toJSONString());
    }
}