package ldc.accenture.kanarapp.util;

import com.google.gson.Gson;
import ldc.accenture.kanarapp.constans.Constants;
import ldc.accenture.kanarapp.model.AuthInfo;
import ldc.accenture.kanarapp.model.NotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Slf4j
public class RequestUtil {

    private static String access_token;

    public static Map<String,String> getParameterMap(HttpServletRequest request){
        Map<String,String> paramMap = new HashMap<>();
        Enumeration enumeration = request.getParameterNames();
        String singleParamName;
        while(enumeration.hasMoreElements()){
            singleParamName = enumeration.nextElement().toString();
            paramMap.put(singleParamName , request.getParameter(singleParamName));
        }
        log.info("parameterMap: " + paramMap);
        return paramMap;
    }

    public static String getAuthorizationCodeFromRequestMap(Map<String,String> reqMap){
        String toReturn = new String();
        if(!reqMap.isEmpty() && reqMap.containsKey("code")){
            toReturn = reqMap.get("code");
            log.info("direct log with code: " + toReturn);
        }

        return toReturn;
    }

    public static HttpResponse sendOAuthRequest(String authCode) {
        HttpResponse response = null;
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(Constants.TOKEN_ENDPOINT);

        List<NameValuePair> arguments = new ArrayList<>();
        arguments.add(new BasicNameValuePair("code",authCode));
        arguments.add(new BasicNameValuePair("grant_type",Constants.GRANT_TYPE));
        arguments.add(new BasicNameValuePair("client_id",Constants.CLIENT_ID));
        arguments.add(new BasicNameValuePair("client_secret",Constants.CLIENT_SECRET));
        arguments.add(new BasicNameValuePair("redirect_uri",Constants.CALLBACK_ENDPOINT));
        arguments.add(new BasicNameValuePair("access_type",Constants.ACCESS_TYPE));

        try{
            post.setEntity(new UrlEncodedFormEntity(arguments));
            response = client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static AuthInfo getAuthInfo(HttpResponse response){
        HttpEntity entity = response.getEntity();
        String content = null;
        try {
            content = EntityUtils.toString(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        AuthInfo toReturn = gson.fromJson(content , AuthInfo.class);
        access_token = toReturn.getAccess_token();
        access_token = "dupal";
        log.info("AuthInfo info: " + toReturn);
        return toReturn;
    }

    public static boolean sendNotification(NotificationEvent notificationEvent,String access_token){
        log.error("notification begin " + access_token);
        HttpResponse response = null;
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(Constants.INSERT_OBJECT_ENDPOINT);
        post.addHeader("Authorization" , "Bearer " + access_token);
        post.addHeader("Content-Type" , "application/json");
        List<NameValuePair> arguments = new ArrayList<>();
        arguments.add(new BasicNameValuePair("TramNr__c",notificationEvent.getTramNr__c()));
        arguments.add(new BasicNameValuePair("Longitude__c",notificationEvent.getLongitude__c()));
        arguments.add(new BasicNameValuePair("Latitude__c",notificationEvent.getLatitude__c()));
        arguments.add(new BasicNameValuePair("Comment__c",notificationEvent.getComment__c()));

        try{
            post.setEntity(new UrlEncodedFormEntity(arguments));
            response = client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("RES "+response.toString());

        return true;
    }

    public static String getAccessToken(){
        return access_token;
    }
}
