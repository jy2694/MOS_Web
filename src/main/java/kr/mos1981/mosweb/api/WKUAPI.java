package kr.mos1981.mosweb.api;

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

    /***
     * @param userid / 웹정보서비스 아이디
     * @param userpw / 웹정보서비스 비밀번호
     * @return String Array [학번, 입학일자, 성명, 대학, 학과, 학적, 과정]
     *
     * 만약 반환된 배열 내부의 값들이 모두 null 인 경우
     * 아이디 비밀번호가 올바르지 않거나 본인확인이 되지 않은 계정임
     * 후자의 경우 확률이 낮으므로 전자로 가정하고 처리함.
     * 
     * @throws IOException
     */
    public static String[] getRegistrationInformation(String userid, String userpw) throws IOException {
        String cookies = getWKUCookies(userid, userpw);
        return getRegistrationInformation(cookies);
    }
    public static String[] getRegistrationInformation(String cookies) throws IOException  {
        URL url = new URL("https://auth.wku.ac.kr/Cert/User/Exist/ng_srv_cmplt.jsp");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Cookie", cookies);
        InputStream is = con.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(is, "euc-kr"));
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
            postData.append(URLEncoder.encode(param.getKey(), "EUC-KR"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "EUC-KR"));
        }
        byte[] postDataBytes = postData.toString().getBytes("EUC-KR");

        HashMap<String, String> cookiemap = new HashMap<String, String>();

        CookieManager manager = new CookieManager();
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(manager);

        URL url = new URL("https://auth.wku.ac.kr/Cert/User/Login/login.jsp?"+postData.toString());
        URLConnection connection = url.openConnection();
        Object obj = connection.getContent();
        connection = url.openConnection();

        url = new URL("https://intra.wku.ac.kr/SWupis/V005/loginReturn.jsp");
        connection = url.openConnection();
        obj = connection.getContent();
        connection = url.openConnection();
        CookieStore cookieJar = manager.getCookieStore();
        List<HttpCookie> cookies = cookieJar.getCookies();
        for (HttpCookie cookie: cookies) {
            String[] another_data = cookie.toString().split(";");
            for(String rdata : another_data) {
                String[] data = rdata.split("=");
                cookiemap.put(data[0], data[1]);
            }
        }
        String cookie = "";
        for(String key : cookiemap.keySet()) {
            cookie += String.format("%s=%s;", key, cookiemap.get(key));
        }
        return cookie;
    }
}
