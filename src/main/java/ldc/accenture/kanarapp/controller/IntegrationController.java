package ldc.accenture.kanarapp.controller;

import ldc.accenture.kanarapp.model.AuthInfo;
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
    private RequestUtil requestUtil = RequestUtil.getRequestUtilInstance();

    @RequestMapping()
    public String getRequest(){

        return "welcome";
    }

    @RequestMapping(value ="/handshake", method= RequestMethod.GET)
    public ModelAndView salesforceHanshake(HttpServletRequest request){
        ModelAndView mov = new ModelAndView("callback");
        Map<String,String> params = requestUtil.getParameterMap(request);
        log.info("    PARAMS    " + params);
        this.authCode = requestUtil.getAuthorizationCodeFromRequestMap(params);
        log.error("\n\n\n\n\n diablica\n\n\n\n");
        mov.addObject("parameters" , params);
        this.response = requestUtil.sendOAuthRequest(this.authCode);
        AuthInfo aInf = requestUtil.getAuthInfo(this.response);
        String res = aInf.toString();
        log.error("RESP: " + aInf);
        System.out.println("dupsztal nysztal");
        this.access_token = requestUtil.getAccessToken();
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

        requestUtil.sendNotification(notificationEvent,this.access_token);
        log.info(notificationEvent.toString());
        return mov;
    }
}
