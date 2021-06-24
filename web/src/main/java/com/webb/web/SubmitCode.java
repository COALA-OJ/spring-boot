package com.webb.web;

import java.io.UnsupportedEncodingException;
import java.lang.instrument.Instrumentation;
import java.util.concurrent.ArrayBlockingQueue;

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
public class SubmitCode {
	
	static HttpHeaders headers = new HttpHeaders();
	static final String furl = HomeController.furl;
	static final String eurl = HomeController.eurl;
	static int SubmitNum = 1;
	final static int capacity = 1;
	static int curengine = 0;
	static ArrayBlockingQueue<Integer> enq = new ArrayBlockingQueue<Integer>(capacity);
	static JSONParser jp = new JSONParser();
	static Instrumentation inst;
	
	@RequestMapping(value = "/spring/submitcode", produces = "application/json; charset=utf8", method = RequestMethod.POST)
	@CrossOrigin("*")
	public ResponseEntity<String> submitcode(@RequestBody JSONObject body) throws ParseException, UnsupportedEncodingException, InterruptedException{
		headers.add("Content-Type", "application/json");
		// from React
		JSONParser jp = new JSONParser();
		JSONObject js = body;
		String ID = js.get("ID").toString();
		String Pnum = js.get("Pnum").toString();
		Object Pcode = js.get("code");
		long Codesize = inst.getObjectSize(Pcode);
		System.out.println(js.toString());
		
		//to Engine, Flask
		ResponseEntity<String> fresp1 = submitCode(ID ,SubmitNum, Integer.parseInt(Pnum));
		int tc_cnt=5;
		//cycle();
		System.out.println("current engine number: " + curengine);
		//enq.put(curengine);
		ResponseEntity<String> eresp = submitEngine(Integer.toString(SubmitNum), Pnum, tc_cnt, Pcode);
		//enq.poll();
		//System.out.println("engine response: " + eresp.getBody().toString());
		JSONObject ejson = (JSONObject) jp.parse(eresp.getBody().toString());
		String result = ejson.get("Result").toString();
		String time = ejson.get("Time").toString();
		String memory = ejson.get("Memory").toString();
		ResponseEntity<String> fresp2 = receiveResult(Integer.toString(SubmitNum), result);
		
		// response to React
		JSONObject jsmain = new JSONObject();
		JSONObject jsarr = new JSONObject();
		JSONArray jamain = new JSONArray();
		jsarr.put("SubNum", Integer.toString(SubmitNum));
		jsarr.put("Result", result);
		jamain.add(jsarr);
		jsmain.put("Problems", jamain);
		System.out.println(jsmain);
		SubmitNum++;
		return ResponseEntity.ok(jsmain.toJSONString());
	}
	
	
	public ResponseEntity<String> submitCode(String ID, int SubNum, int Pnum) {
		// make Body
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("ID", ID);
		params.add("SubNum", SubNum);
		params.add("Pnum", Pnum);
		System.out.println("Flask data: "+params.toString());
		
		// make Entity
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity(params, headers); 
		RestTemplate rt = new RestTemplate();
		String url = furl+"/flask_Submit";
		return rt.exchange(url, HttpMethod.POST, entity, String.class);
		
	}
	
	public ResponseEntity<String> submitEngine(String SubNum, String Pnum, int tccnt, Object Pcode) throws InterruptedException {
		
		
		// make Body
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		System.out.println(Pcode);
		params.add("SubNum", SubNum);
		params.add("Pnum", Pnum);
		params.add("tc-cnt", tccnt);
		params.add("Pcode", Pcode);
		System.out.println("Engine form: "+params.toString());
		
		// make Entity
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity(params, headers); 
		RestTemplate rt = new RestTemplate();
		String url = eurl+"/judger-engine";
		return rt.exchange(url, HttpMethod.POST, entity, String.class);
		
	}
	
	public ResponseEntity<String> receiveResult(String SubNum, String Result) {
		// make Body
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("SubNum", SubNum);
		params.add("Result", Result);
		JSONObject js = new JSONObject();
		JSONArray ja1 = new JSONArray();
		JSONArray ja2 = new JSONArray();
		ja1.add(Integer.parseInt(SubNum));
		ja2.add(Result);
		js.put("SubNum", ja1);
		js.put("Result", ja2);
		System.out.println("pass result to flask: " + js.toJSONString());
		
		// make Entity
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity(js, headers); 
		RestTemplate rt = new RestTemplate();
		String url = furl+"/flask_submit_result";
		return rt.exchange(url, HttpMethod.POST, entity, String.class);
		
	}
	static void cycle() {
		if(curengine==capacity) {
			curengine = 1;
		}
		else curengine++;
	}
}
