package org.coala.controller;

import org.coala.domain.Engine;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import lombok.extern.log4j.Log4j;

@RestController
@Log4j
public class SubmitEngine {
	@PostMapping(value="/spring/submitEngine")
	public void findServerNum(@RequestBody Engine e, int SubNum, int Pnum, String Pcode) {
		int serverNum = 0;	
		JsonObject jo = new JsonObject();
		jo.addProperty("SubNum", SubNum);
		jo.addProperty("Pnum", Pnum);
		jo.addProperty("Pcode", Pcode);
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json;charset=UTF-8");
		HttpEntity<String> request;
		
		boolean flag = true;
		for(int i = 0;i<=5;i++) {
			if(e.getServer()[i]) {
			}else{
				serverNum = i+1;
				flag = false;
				break;
			}
		}
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> res;
		if(flag) {
			e.getWaitQueue().add(jo);
			try {
				request = new HttpEntity<>(new Gson().toJson(e),header);
				log.info("HttpEntity : " + request);
				res = rt.exchange("http://localhost:8080",HttpMethod.GET,request,String.class);
			}catch(HttpStatusCodeException err) {
				log.error(err);
			}
			
		}
		else {
			log.info("serverNum is " + serverNum);
			try {
				request = new HttpEntity<>(jo.toString(),header);
				log.info("HttpEntity : " + request);
				res = rt.exchange("http://localhost:8080/{serverNum}",HttpMethod.POST,request,String.class,serverNum);
		    } catch(HttpStatusCodeException err) {
		       log.error(err);
		    }
			
		}
		
	}
	
	@PostMapping(value="/{serverNum}", produces="application/json;charset=UTF-8")
	public String submitEngine(@PathVariable int serverNum, HttpEntity<String> request ) {
		String data = request.getBody();
		log.info("submit data" + data);
		return data;
	}

}
