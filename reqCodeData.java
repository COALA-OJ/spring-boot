package com.spring.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class reqCodeData {

	public ResponseEntity<String> reqCodeData(String subNum){

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("subNum", subNum);

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        RestTemplate rt = new RestTemplate();
        String url = " ";
        ResponseEntity<String> response = rt.exchange(url,HttpMethod.POST,entity, String.class);

        return response;
	
}
