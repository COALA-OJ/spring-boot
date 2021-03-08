package git.coala.flask;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping(value = "/spring/usersolvdata", produces = "application/json; charset=utf8", method = RequestMethod.POST)
public class reqScoringData {

	public void reqScroing(HttpServletResponse response, HttpServletRequest request,String reqid) {
		
		JSONParser jsonParserid=new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParserid.parse(reqid);//JSON data를 string으로 변경하여 resId에 넣어줄 수 있도록 하는 코드
		
		String resId;
		resId = jsonObject.get("ID");//앞서 JSONObejct에 넣어준 JSON data를 resId에 할당해줌
		
		try {
			response.sendRedirect(resId);//resId에 해당하는 url로 이동하는 코드
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
