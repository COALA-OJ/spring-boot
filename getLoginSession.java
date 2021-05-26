package hello.hellospring.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class getSession {
    @RequestMapping(value = "/spring/login", method = RequestMethod.POST)
    public ModelAndView getLoginSession(HttpServletRequest request){

        // 세션 생성
        HttpSession session = request.getSession();

        //세션 속성 값 member
        String member = (String) session.getAttribute("member");

        // member를 요청 파라미터의 body 값으로 저장.
        request.setAttribute("body",member);

        // ??
        ModelAndView mav = new ModelAndView();
        mav.addObject("request", request);

        return mav;
    }
}
