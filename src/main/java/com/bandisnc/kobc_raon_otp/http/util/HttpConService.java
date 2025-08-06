package com.bandisnc.kobc_raon_otp.http.util;

import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class HttpConService {

    private String encoding = "UTF-8";
    private int timeOut = 60000; // 밀리 세컨드 단위
    
    protected HttpURLConnection getHttpConnection(URL url, String method) throws  IOException {

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();

        // 서버로부터 메세지를 받을 수 있도록 한다. 기본값은 true이다.
        conn.setDoInput(true);

        // 서버로 데이터를 전송할 수 있도록 한다. GET방식이면 사용될 일이 없으나, true로 설정하면 자동으로 POST로 설정된다. 기본값은 false이다.

        if( "POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method)) {
            conn.setDoOutput(true);
        }

        conn.setUseCaches(false);
        conn.setDefaultUseCaches(false);
        conn.setConnectTimeout(timeOut);
        conn.setReadTimeout(timeOut);

        return conn;

    }

    protected String getCookieString(Map<String, String> cookieMap) {
        if( cookieMap == null || cookieMap.isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean isFirst = true; // 구분자 붙이지 않기 위해서
        for( Map.Entry<String, String> entry: cookieMap.entrySet() ){
            if( !isFirst) {
                sb.append("; ");
            }

            sb.append(entry.getKey() + "=" + entry.getValue().trim());
            isFirst = false;
        }

        return sb.toString();
    }

    /**
     * Header 와 cookie를 설정 한다.
     * @param conn
     * @param headerMap
     * @param cookieMap
     */
    protected void setHeader(HttpURLConnection conn, Map<String, String> headerMap, Map<String, String> cookieMap) {

        if( headerMap != null && !headerMap.isEmpty()){
            for( Map.Entry<String, String> entry: headerMap.entrySet() ){
                conn.setRequestProperty( entry.getKey(), entry.getValue() );
            }
        }

        // 쿠키 설정
        if( cookieMap != null && !cookieMap.isEmpty() ) {
            conn.setRequestProperty("Cookie", getCookieString(cookieMap));
        }
    }

    /**
     * get 방식 호출한다.
     * @param urlStr
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public Result get( String urlStr) throws IOException{
        return get(urlStr, null);
    }

    /**
     * get 방식 호출한다.
     * @param urlStr
     * @param headerMap
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public Result get( String urlStr, Map<String, String> headerMap) throws IOException {
        return get(urlStr, headerMap, null);
    }

    /**
     * get 방식 호출한다.
     * @param urlStr
     * @param headerMap
     * @param cookieMap
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public Result get( String urlStr, Map<String, String> headerMap, Map<String, String> cookieMap) throws IOException {

        return callHttp( urlStr, "GET", (String)null, null, headerMap, cookieMap);

    }

    /**
     * POST 방식으로 호출한다.
     * @param url
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public Result post( String url) throws IOException {
        return post( url,  null);
    }

    /**
     * POST 방식으로 호출한다.
     * @param url
     * @param paramMap
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public Result post( String url, Map<String, String> paramMap) throws IOException {
        return post( url, paramMap, null);
    }

    /**
     * POST 방식으로 호출한다.
     * @param url
     * @param paramMap
     * @param headerMap
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public Result post( String url, Map<String, String> paramMap, Map<String, String> headerMap) throws IOException {
        return post( url, paramMap, headerMap, null);
    }

    /**
     * POST 방식으로 호출한다.
     * @param urlStr
     * @param paramMap
     * @param headerMap
     * @param cookieMap
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws Exception
     */
    public Result post( String urlStr, Map<String, String> paramMap, Map<String, String> headerMap, Map<String, String> cookieMap) throws IOException {

        String paramStr = this.getParamMapToString(paramMap);

        return callHttp( urlStr, "POST", paramStr, "application/x-www-form-urlencoded", headerMap, cookieMap);

    }

    /**
     * http data로 변경
     * @param paramMap
     * @return
     * @throws UnsupportedEncodingException
     */
    protected String getParamMapToString(Map<String, String> paramMap) throws UnsupportedEncodingException {
        String paramStr = null;
        if( paramMap != null && paramMap.size() > 0) {

            StringBuilder postData = new StringBuilder();
            for(Map.Entry<String,String> param : paramMap.entrySet()) {
                if(postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), encoding));
                postData.append('=');
                postData.append(URLEncoder.encode(param.getValue(), encoding));
            }

            paramStr = postData.toString();

        }

        return paramStr;
    }


    /**
     * http mehtod를 호출 한다.
     * @param url
     * @param method
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public Result callHttp( String url, String method) throws IOException{
        return callHttp( url, method,  null);
    }

    /**
     * http mehtod를 호출 한다.
     * @param url
     * @param method
     * @param paramMap
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public Result callHttp( String url, String method, Map<String, String> paramMap) throws IOException{
        return callHttp( url, method, paramMap, "application/x-www-form-urlencoded");
    }

    /**
     * http mehtod를 호출 한다.
     * @param url
     * @param method
     * @param paramMap
     * @param contentType
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public Result callHttp( String url, String method, Map<String, String> paramMap,
                            String contentType) throws IOException{
        return callHttp( url, method, paramMap, contentType, null);
    }

    /**
     * http mehtod를 호출 한다.
     * @param urlStr
     * @param method
     * @param paramMap
     * @param contentType
     * @param headerMap
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public Result callHttp( String urlStr, String method, Map<String, String> paramMap,
                            String contentType, Map<String, String> headerMap) throws IOException{

        return callHttp( urlStr, method, paramMap, contentType, headerMap, null);

    }


    /**
     * http mehtod를 호출 한다.
     * @param urlStr
     * @param method
     * @param paramMap
     * @param contentType
     * @param headerMap
     * @param cookieMap
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public Result callHttp( String urlStr, String method, Map<String, String> paramMap,
                            String contentType, Map<String ,String> headerMap, Map<String, String> cookieMap) throws IOException {

        String paramStr = this.getParamMapToString(paramMap);

        return callHttp( urlStr, method, paramStr, contentType, headerMap, cookieMap);
    }


    /**
     * http mehtod를 호출 한다.
     * @param urlStr
     * @param method
     * @param paramStr
     * @param contentType
     * @param headerMap
     * @param cookieMap
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public Result callHttp( String urlStr, String method, String paramStr, String contentType, Map<String ,String> headerMap, Map<String, String> cookieMap) throws IOException{

        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlStr);

            conn = getHttpConnection(url, method);

            conn.setRequestMethod(method);

            this.setHeader(conn, headerMap, cookieMap);

            // 파라미터가 있을 경우
            if( conn.getDoOutput() && paramStr != null) {
                // 파라미터가 있을 경우

                conn.setRequestProperty("Content-Type", contentType);
                conn.setRequestProperty("User-Agent", "Application");
                conn.setRequestProperty("Cache-Control", "no-cache");

                Map<String, List<String>> requestProperties = conn.getRequestProperties();
                OutputStream out = null;
                try {
                    out = conn.getOutputStream();
                    out.write(paramStr.getBytes(encoding));
                    out.flush();

                } finally {
                    if( out != null ) out.close();
                }
            }

            int statusCode = conn.getResponseCode();
            //  HttpURLConnection.HTTP_OK

            Map<String, List<String>> resHeaderMap = conn.getHeaderFields();
            List<String> setCookies = resHeaderMap.get("Set-Cookie");

            BufferedReader in = null;

            try {
                if( statusCode ==200 ) {
                    in = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
                } else {
                    in = new BufferedReader(new InputStreamReader(conn.getErrorStream(), encoding));
                }

                StringBuilder sb = new StringBuilder();
                String readLine;
                while((readLine = in.readLine()) != null) { // response 출력
                    sb.append(readLine);
                }


                return new Result(statusCode, sb.toString(), resHeaderMap, setCookies);

            } finally  {
                if( in !=null ) in.close();
            }

        } finally {

            if (conn != null) conn.disconnect();
        }

    }


    /**
     * 결과 돌려 주는 클래스
     * @author nowone
     *
     */
    public static class Result {

        int httpStatusCode = 200;

        /** cookie 값 저장 */
        private List<String> cookies = null;

        /** Http Response */
        private Map<String, List<String>> headerMap = null;

        private String content = null;

        // custom 추가 data
        private Object data = null;

        public Result(String result) {
            this(200, result);
        }

        public Result(int httpStatusCode, String content) {
            this(httpStatusCode,content, null);
        }

        public Result(int httpStatusCode, String content, Map<String, List<String>> headerMap) {
            this(httpStatusCode, content, headerMap, null);
        }


        public Result(int httpStatusCode, String content, Map<String, List<String>> headerMap, List<String> cookies) {
            this.httpStatusCode = httpStatusCode;
            this.content = content;
            this.headerMap = headerMap;
            this.cookies = cookies;
        }

        public String getContent() {
            return content;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }


        public int getHttpStatusCode() {
            return this.httpStatusCode;
        }

        public List<String> getCookies() {
            return this.cookies;
        }

        public String getFirstHeader(String name) {

            if( headerMap != null && headerMap.containsKey(name) ) {
                return headerMap.get(name).get(0);
            }

            return null;
        }

        public String getLastHeader(String name) {
            if( headerMap != null && headerMap.containsKey(name) ) {
                List<String> list = headerMap.get(name);
                if( list != null && !list.isEmpty() ) {
                    return list.get(list.size()-1);
                }
            }

            return null;
        }

        public List<String> getHeaders(String name) {

            if( headerMap != null && headerMap.containsKey(name) ) {
                return headerMap.get(name);
            }

            return new ArrayList<String>();
        }

        public Map<String, List<String>> getHeaderMap() {
            return headerMap;
        }

    }
}
