package com.webb.web;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class reqReadCode {
	static String furl = HomeController.furl;
	
	@RequestMapping(value = "/spring/codedetail", produces = "application/json; charset=utf8", method = RequestMethod.POST)
	@CrossOrigin("*")
	public ResponseEntity<String> reqReadCode(@RequestBody JSONObject body) throws IOException, ParseException{
    	JSONObject js= body;
    	Object SubNum = js.get("SubNum").toString();
    	
    	ResponseEntity<String> resp = reqCodeInfo(SubNum);

    	JSONObject code = new JSONObject();
    	JSONParser CodeInfo = new JSONParser();
		JSONObject CIob = (JSONObject) CodeInfo.parse(resp.getBody().toString());
    	code.put("Code", CIob);
    	return ResponseEntity.ok(code.toJSONString());

	}
	public ResponseEntity<String> reqCodeInfo(Object SubNum){
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("SubNum", SubNum);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity(params, headers); 
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> response = rt.exchange(furl, HttpMethod.GET, entity, String.class);
		return response;
	}
}
