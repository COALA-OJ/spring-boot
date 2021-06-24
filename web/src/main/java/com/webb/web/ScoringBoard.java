package com.webb.web;

import java.io.UnsupportedEncodingException;

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
public class ScoringBoard {
	static HttpHeaders headers = new HttpHeaders();
	static final String furl = HomeController.furl;
	
	@RequestMapping(value = "/spring/scoringboard", produces = "application/json; charset=utf8", method = RequestMethod.POST)
    @CrossOrigin("*")
    public ResponseEntity<String> center(@RequestBody String body) throws ParseException {
        // from React
        JSONParser jp = new JSONParser();
        JSONObject js = (JSONObject) jp.parse(body);
        String ID = js.get("ID").toString();
        System.out.println(ID);

        // get Problem data from Flask
        //ResponseEntity<String> resp = reqUserInfo(ID);

        JSONParser UserSolvInfo = new JSONParser();
        //JSONArray USIob = (JSONArray) UserSolvInfo.parse(resp.getBody().toString());
        JSONArray ja = new JSONArray();
//        int size = USIob.size();
//        int i=0;
//        while(true) {
//            ja.add(makePData((JSONArray) USIob.get(i)));
//            ++i;
//            if(i==size) break;
//        }

        JSONObject jo = new JSONObject();
        jo.put("SubNum", "1");
        jo.put("Pnum", "1001");
        jo.put("ID", "asdf1234");
        jo.put("Result", "TIME_LIMIT_EXCEED");
        jo.put("LangType", "C++");
        jo.put("CodeSize", "132byte");
        jo.put("Permission", "True");
        ja.add(jo);
        
        // return to React
        JSONObject jsmain = new JSONObject();
        jsmain.put("Status", ja);
        String jsstr = jsmain.toJSONString();
        return ResponseEntity.ok(jsstr);
    }

    public JSONObject makePData(JSONArray j) {
        int SubNum = Integer.parseInt(j.get(0).toString());
        int Pnum = Integer.parseInt(j.get(1).toString());
        String ID = j.get(2).toString();
        String Result= j.get(3).toString();
        String LangType= j.get(4).toString();
        String CodeSize= j.get(5).toString();
        Boolean Permission= Boolean.parseBoolean(j.get(6).toString());
        JSONObject js = new JSONObject();
        js.put("SubNum", SubNum);
        js.put("Pnum", Pnum);
        js.put("Result",Result);
        js.put("LangType",LangType);
        js.put("CodeSize",CodeSize);
        js.put("Permission",Permission);
        return js;
    }

    public ResponseEntity<String> reqUserInfo(String id) {
        // make Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("ID", id);

        // make Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        // make Entity
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        RestTemplate rt = new RestTemplate();
        String url = furl+"";
        return rt.exchange(url, HttpMethod.POST, entity, String.class);
    }


}
