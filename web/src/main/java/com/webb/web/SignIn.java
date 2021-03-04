package com.webb.web;

import java.io.UnsupportedEncodingException;

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
public class SignIn {
	
	static HttpHeaders headers = new HttpHeaders();
	static final String furl = HomeController.furl;
	
	@RequestMapping(value = "/spring/login", produces = "application/json; charset=utf8", method = RequestMethod.POST)
	@CrossOrigin("*")
	public ResponseEntity<String> signin(@RequestBody JSONObject body) throws ParseException, UnsupportedEncodingException, InterruptedException{
		headers.add("Content-Type", "application/json");
		// from React
		JSONParser jp = new JSONParser();
		JSONObject js = body;
		Object incodedID = js.get("ID").toString();
		Object incodedPwd = js.get("Pwd").toString();
		System.out.println(js.toString());
		
		//ResponseEntity<String> fresp = reqSignIn(incodedID, incodedPwd);
		//JSONObject jsmain = (JSONObject) jp.parse(fresp.getBody().toString());
		JSONObject jsmain = new JSONObject();
		jsmain.put("Name", "이병훈");
		return ResponseEntity.ok(jsmain.toJSONString());
	}
	
	public ResponseEntity<String> reqSignIn(Object incodedID, Object incodedPwd){
		// make Body
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("ID", incodedID);
		params.add("Pwd", incodedPwd);
		System.out.println("Flask data: "+params.toString());
		
		// make Entity
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity(params, headers); 
		RestTemplate rt = new RestTemplate();
		String url = furl+"/flask_Submit";
		return rt.exchange(url, HttpMethod.POST, entity, String.class);
	}
}
