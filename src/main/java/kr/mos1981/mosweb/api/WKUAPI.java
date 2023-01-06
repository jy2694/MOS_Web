package kr.mos1981.mosweb.api;

import kr.mos1981.mosweb.dto.SignInDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WKUAPI {

    private static final String COMMUNICATION_CHARSET = "EUC-KR";

    /*
     * 만약 반환된 배열 내부의 값들이 모두 null 인 경우
     * 아이디 비밀번호가 올바르지 않거나 본인확인이 되지 않은 계정임
     * 후자의 경우 확률이 낮으므로 전자로 가정하고 처리함.
     */
    public static String[] getRegistrationInformation(SignInDTO dto) throws IOException {
        String cookies = getWKUCookies(dto.getUserId(), dto.getUserPw());
        return getRegistrationInformation(cookies);
    }
    public static String[] getRegistrationInformation(String cookies) throws IOException  {
        URL url = new URL("https://auth.wku.ac.kr/Cert/User/Exist/ng_srv_cmplt.jsp");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Cookie", cookies);
        InputStream is = con.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(is, COMMUNICATION_CHARSET));
        String line;
        String[] information = new String[7];
        int i = 0;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.startsWith("<td height=\"33\" class=\"nb bg\" style=\"text-align:left\">")) {
                line = line.replace("<td height=\"33\" class=\"nb bg\" style=\"text-align:left\">", "").replace("</td>", "");
                information[i++] = line;
            }
        }
        return information;
    }

    public static String getWKUCookies(String userID, String userPW) throws IOException {
        Map<String,Object> params = new LinkedHashMap<>(); // 파라미터 세팅
        params.put("nextURL", "https://intra.wku.ac.kr/SWupis/V005/loginReturn.jsp");
        params.put("site", "SWUPIS");
        params.put("userid", userID);
        params.put("passwd", userPW);

        StringBuilder postData = new StringBuilder();
        for(Map.Entry<String,Object> param : params.entrySet()) {
            if(postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), COMMUNICATION_CHARSET));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), COMMUNICATION_CHARSET));
        }

        HashMap<String, String> cookieMap = new HashMap<>();

        CookieManager manager = new CookieManager();
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(manager);

        URL url = new URL("https://auth.wku.ac.kr/Cert/User/Login/login.jsp?"+postData);
        URLConnection connection = url.openConnection();
        connection.getContent();
        url.openConnection();

        url = new URL("https://intra.wku.ac.kr/SWupis/V005/loginReturn.jsp");
        connection = url.openConnection();
        connection.getContent();
        url.openConnection();
        CookieStore cookieJar = manager.getCookieStore();
        List<HttpCookie> cookies = cookieJar.getCookies();
        for (HttpCookie cookie: cookies) {
            String[] anotherData = cookie.toString().split(";");
            for(String rdata : anotherData) {
                String[] data = rdata.split("=");
                cookieMap.put(data[0], data[1]);
            }
        }
        String cookie = "";
        for(String key : cookieMap.keySet()) {
            cookie += String.format("%s=%s;", key, cookieMap.get(key));
        }
        return cookie;
    }
}
