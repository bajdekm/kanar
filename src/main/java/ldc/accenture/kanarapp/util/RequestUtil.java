package ldc.accenture.kanarapp.util;

import com.google.gson.Gson;
import ldc.accenture.kanarapp.model.AuthInfo;
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
        String endpoint = "https://login.salesforce.com/services/oauth2/token";
        HttpResponse response = null;
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(endpoint);

        List<NameValuePair> arguments = new ArrayList<>();
        arguments.add(new BasicNameValuePair("code",authCode));
        arguments.add(new BasicNameValuePair("grant_type","authorization_code"));
        arguments.add(new BasicNameValuePair("client_id","3MVG9HxRZv05HarQtjSTFTJtOkxdwJakZL1ryiv8qhpFdlUyChqtdt2kbda12lsGWhH20YJ446wJUDs1f8J33"));
        arguments.add(new BasicNameValuePair("client_secret","5145465745540914530"));
        arguments.add(new BasicNameValuePair("redirect_uri","https://kanardefender.herokuapp.com/i/handshake"));
        arguments.add(new BasicNameValuePair("access_type","offline"));

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
        log.info("AuthInfo info: " + toReturn);
        return toReturn;
    }
}
