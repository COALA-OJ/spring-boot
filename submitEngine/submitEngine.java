package com.submitEngine.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import com.submitEngine.domain.SubmitData;

@RestController
public class submitEngine {
	
	
	
	public ResponseEntity<String> submitEngine(int SubNum, int Pnum, Object Pcode) throws InterruptedException {
		
		SubmitData submitD = new SubmitData();
		submitD.setSubNum(SubNum);
		submitD.setPnum(Pnum);
		submitD.setPcode(Pcode);
		
		/* 외부에 추가
		 * ArrayBlockingQueue<Integer> waitQueue = new ArrayBlockingQueue(6);
		 * Queue<Integer> engineQueue = new LinkedList<Integer>();
		 *
		 * engineQueue.offer(1); engineQueue.offer(2); engineQueue.offer(3);
		 * engineQueue.offer(4); engineQueue.offer(5); engineQueue.offer(6);
		 */
		
		// 사용가능한 서버가 없는 경우 대기
		waitQueue.put(1);
		
		int servNum = engineQueue.poll();
		String url = "http://localhost:8080/" + servNum;
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity(submitD, headers);
		RestTemplate rt = null;
		ResponseEntity<String> res = rt.exchange(url, HttpMethod.POST, entity, String.class);
		
		engineQueue.offer(servNum);
		waitQueue.poll();
		
		return res;
	}
}
