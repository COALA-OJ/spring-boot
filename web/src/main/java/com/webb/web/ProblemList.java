package com.webb.web;

import javax.servlet.http.HttpServletResponse;

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
public class ProblemList {
	final static String furl = HomeController.furl;
	
	
	@RequestMapping(value = "/spring/problemdata", method = RequestMethod.GET)
	@CrossOrigin("*")
	public ResponseEntity<String> center(@RequestBody String body) throws ParseException {
        JSONParser jp = new JSONParser();
        JSONObject js = (JSONObject) jp.parse(body);
        String ID = js.get("ID").toString();
        System.out.println(ID);
		
		// get Problem data from Flask
		ResponseEntity<String> resp = reqUserInfo(ID);
		JSONParser UserSolvInfo = new JSONParser();
		JSONArray USIob = (JSONArray) UserSolvInfo.parse(resp.getBody().toString());
		JSONArray ja = new JSONArray();
		int size=USIob.size();
		int i=0;
		while(true) {
			ja.add(makePData((JSONArray) USIob.get(i)));
			++i;
			if(i==size) break;
		}
		
		// return to React
		String jsonstr = ja.toJSONString();
		JSONObject jsmain = new JSONObject();
		jsmain.put("Problems", ja);
		String jsstr = jsmain.toJSONString();
		return ResponseEntity.ok(jsstr);
	}
	
	public JSONObject makePData(JSONArray j) {
		int pnum = Integer.parseInt(j.get(0).toString());
		String name = j.get(1).toString();
		String solved = j.get(2).toString();
		JSONObject js = new JSONObject();
		js.put("Pnum", pnum);
		js.put("Pname", name);
		js.put("Solved",solved);
		return js;
	}
	
	public ResponseEntity<String> reqUserInfo(String id) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("ID", id);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers); 
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> response = rt.exchange(furl, HttpMethod.GET, entity, String.class);
		return response;
	}
}
