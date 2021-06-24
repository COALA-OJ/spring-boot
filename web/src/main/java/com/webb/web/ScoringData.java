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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ScoringData {
	static String furl = HomeController.furl;
	
	@RequestMapping(value = "/spring/userinfo", produces = "application/json; charset=utf8", method = RequestMethod.POST)
	@CrossOrigin("*")
	public ResponseEntity<String> reqUserScore(@RequestBody JSONObject body) throws IOException, ParseException{
		JSONObject js= body;
    	Object ID = js.get("ID").toString();
    	
    	ResponseEntity<String> resp = reqUserData(ID);

    	//JSONParser CodeInfo = new JSONParser();
		//JSONObject USob = (JSONObject) CodeInfo.parse(resp.getBody().toString());
    	JSONObject jsmain = new JSONObject();
    	jsmain.put("ID", ID);
    	JSONArray ja = new JSONArray();
    	ja.add("1001");
    	ja.add("1002");
    	jsmain.put("SolvedList", ja);

    	return ResponseEntity.ok(jsmain.toJSONString());
	}
	
	public ResponseEntity<String> reqUserData(Object ID){
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("ID", ID); 
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity(params, headers); 
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> response = rt.exchange(furl, HttpMethod.GET, entity, String.class);
		return response;
		
	}
}
