package ldc.accenture.kanarapp.controller;

import ldc.accenture.kanarapp.model.NotificationEvent;
import ldc.accenture.kanarapp.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
    private String access_token;
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
        this.access_token = RequestUtil.getAccessToken(params);
        log.info("code: " + this.authCode + " access_token: " + this.access_token);
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

    @RequestMapping(value="/notification", method= RequestMethod.GET)
    public ModelAndView getNewNotification(@RequestParam(value="tram_nr", defaultValue="1") String tram_nr,
                                           @RequestParam(value="comment", defaultValue="") String comment,
                                           @RequestParam(value="longitude", defaultValue = "0") String longitude,
                                           @RequestParam(value="latitude",defaultValue = "0") String latitude){
        ModelAndView mov = new ModelAndView("callback");
        log.info("notification rm hello");
        NotificationEvent notificationEvent = new NotificationEvent();
        notificationEvent.setTramNr__c(tram_nr);
        notificationEvent.setComment__c(comment);
        notificationEvent.setLongitude__c(longitude);
        notificationEvent.setLatitude__c(latitude);

        RequestUtil.sendNotification(notificationEvent);
        log.info(notificationEvent.toString());
        return mov;
    }
}
