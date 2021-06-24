package com.webb.web;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Member;
import java.security.Provider.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import org.springframework.web.servlet.ModelAndView;

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
		jsmain.put("Result", "true");
		jsmain.put("Name", "");
		System.out.println(jsmain.toJSONString());
		return ResponseEntity.ok(jsmain.toJSONString());
	}
	
	@RequestMapping(value = "/auth/check", produces = "application/json; charset=utf8", method = RequestMethod.POST)
	@CrossOrigin("*")
	public ModelAndView test1(HttpServletRequest req, @RequestBody JSONObject body) {
		System.out.println(1);
		HttpSession session = req.getSession();

		session.setAttribute("member", body);
		System.out.println(body.toJSONString());
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("thumbnail", body);
		mav.addObject("username", "null");
		mav.setViewName("/member/modifyOk");
		System.out.println(mav.toString());
		return mav;
		
	}
	
	@RequestMapping(value = "/auth/logout", produces = "application/json; charset=utf8", method = RequestMethod.POST)
	@CrossOrigin("*")
	public String test2(HttpServletRequest req, @RequestBody JSONObject body) {
		HttpSession session = req.getSession();
		
		session.invalidate();
		return "member/removeOK";
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
		String url = "http://210.117.181.103:1234"+"/Login";
		System.out.println(url);
		return rt.exchange(url, HttpMethod.POST, entity, String.class);
	}
}
