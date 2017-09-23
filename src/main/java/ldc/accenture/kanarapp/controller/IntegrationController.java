package ldc.accenture.kanarapp.controller;

import ldc.accenture.kanarapp.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/i")
@Slf4j
public class IntegrationController {

    private String authCode;
    private HttpResponse response;

    @RequestMapping()
    public String getRequest(){

        return "welcome";
    }

    @RequestMapping(value ="/handshake", method= RequestMethod.GET)
    public ModelAndView salesforceHanshake(HttpServletRequest request){
        ModelAndView mov = new ModelAndView("callback");
        Map<String,String> params = RequestUtil.getParameterMap(request);
        this.authCode = RequestUtil.getAuthorizationCodeFromRequestMap(params);
        log.info("code: " + this.authCode);
        mov.addObject("parameters" , params);
        this.response = RequestUtil.sendOAuthRequest(this.authCode);
        String res = RequestUtil.getAuthInfo(this.response).toString();
        mov.addObject("sfresponse",res);
        return mov;
    }


    @RequestMapping("/getauthdetails")
    public String getAuthenticationDetails(HttpServletResponse response){
        log.error("response: " + response.toString());
        return "welcome";
    }
}
