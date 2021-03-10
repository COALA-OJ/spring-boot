package ProblemData.spring;


import jdk.nashorn.internal.parser.JSONParser;
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

import java.text.ParseException;

@RestController
public class modifiedReqProbList {
    static final String furl = HomeController.furl;

    @RequestMapping(value = "/spring/problemdata", produces = "application/json; charset=utf8", method = RequestMethod.POST)
    @CrossOrigin("*")
    public ResponseEntity<String> center(@RequestBody String body) throws ParseException {
        // from React
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
