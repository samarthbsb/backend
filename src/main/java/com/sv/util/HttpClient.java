package com.sv.util;

import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**	
 * @author vaibhav
 */
public class HttpClient {

    private static Logger logger = LoggerFactory.getLogger(HttpClient.class.getCanonicalName());

    public static boolean enableProxy = false;
    private static HttpHost proxy = null;
    private static final int CONNECTION_TIMEOUT_MILLIS = 10000;
    private static final int SOCKET_TIMEOUT_MILLIS = 10000;
    private static boolean enableHeader = false;
    private static Header header;

    static {
        String proxyIP = "103.15.227.157";
        String proxyPort = "3229";
        proxy = new HttpHost(proxyIP, Integer.parseInt(proxyPort), "http");
    }

    public static String getContent(String url) throws Exception{
        return getContent(url, CONNECTION_TIMEOUT_MILLIS);
    }
    
    public static void setHeader(Header head) {
    	enableHeader = true;
    	header = head;
    }
    
    public static String getRedirectLocation(String url) {
    	String result = url;
    	try {
    		org.apache.http.client.HttpClient client = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet(url);
            HttpParams params = new BasicHttpParams();
            params.setParameter("http.protocol.handle-redirects",false);
            getRequest.setParams(params);
    		HttpResponse response = client.execute(getRequest);
    		result = response.getFirstHeader("Location").getValue();
    		getRequest.releaseConnection();
    	} catch (Exception e) {
    		logger.warn("Should not happen." + e.getMessage());
            return null;
    	}
    	return result;
    }
    
    public static String getContent(String url, int timeout)
        throws Exception {
        HttpGet getRequest = null;
        try {
            org.apache.http.client.HttpClient client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, timeout);
            HttpConnectionParams.setSoTimeout(params, timeout);
            
            getRequest = new HttpGet(url);
            
            if(enableHeader)
            	getRequest.addHeader(header);
            
            if (enableProxy)
                client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

            HttpResponse response = client.execute(getRequest);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                String content = new String();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    content += line;
                    content += '\n';
                }
                
                return content;
            } else {
                logger.error("Error response : {} . For : {}", statusLine, url);
            }
        } catch (Exception e) {
            logger.error("Error fetching data : {}", url, e);
            throw e;
        } finally {
            if (getRequest != null) {
                getRequest.releaseConnection();
            }
        }
        return null;
    }
    
    public static String getContent(String url, int timeout,Map<String,String> headers)
            throws Exception {

        HttpGet get = null;
        try {
            org.apache.http.client.HttpClient client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, timeout);
            HttpConnectionParams.setSoTimeout(params, timeout);
            get = new HttpGet(url);
            if(headers != null && headers.size() > 0)
            {
                Iterator<String> headerItr = headers.keySet().iterator();
                while (headerItr.hasNext()) {
                    String name = headerItr.next();
                    String value = headers.get(name);
                    get.addHeader(name,value);
                }
            }

            HttpResponse response = client.execute(get);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                String content = "";
                BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String line = null;
                while((line = br.readLine()) != null) {
                    content += line;
                    content += '\n';
                }
                return content;
            }
            else {
                logger.error("Error response : " + statusLine + ". For : " + url);
            }
        }
        catch (Exception e) {
            logger.error("Error invoking URL : "+url+". Error : "+e.getMessage(), e);
        }
        finally {
            if(get != null) {
                get.releaseConnection();
            }
        }
        return null;

    }
    
    public static String getCompressedContent(String url,int timeout) {
        HttpPost postRequest = null;
        String charset = "utf-8";
        try {
            org.apache.http.client.HttpClient client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, timeout);
            HttpConnectionParams.setSoTimeout(params, timeout);
            postRequest = new HttpPost(url);
            postRequest.addHeader("charset", charset);
            postRequest.addHeader("Accept-Encoding", "gzip");
            HttpResponse response = client.execute(postRequest);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                GZIPInputStream gis = new GZIPInputStream(is);
                String content = new String();
                BufferedReader br = new BufferedReader(new InputStreamReader(gis, charset));
                String line = null;
                while((line = br.readLine()) != null) {
                    content += line;
                    content += '\n';
                }
                return content;
            }
            else {
//                logger.warn("Error response : " + statusLine + ". For : " + url);
            }
        }
        catch (Exception e) {
//            logger.error(e.getMessage(), e);
        }
        finally {
            if(postRequest != null) {
                postRequest.releaseConnection();
            }
        }
        return null;
    }

    public static String postData(String url, Map<String, String> data) {
        return postData(url, data, null, null);

    }


    public static String postData(String url, Map<String, String> postValues, String proxyIP, String proxyPort) {
        HttpPost post = null;

        try {
            org.apache.http.client.HttpClient client = new DefaultHttpClient();
            HttpParams httpParams = client.getParams();
            // set the connection timeout value to 2 seconds (2000 milliseconds)
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT_MILLIS);
            HttpConnectionParams.setSoTimeout(httpParams, SOCKET_TIMEOUT_MILLIS);


            if (enableProxy)
                client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

            post = new HttpPost(url);
            //post.setHeader("Content-Type", "text/xml");
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Iterator<String> keys = postValues.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = postValues.get(key);
                nvps.add(new BasicNameValuePair(key, value));
            }

            post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));

            HttpResponse response = client.execute(post);
            logger.debug("posted to " + url + " , status = " + response.getStatusLine().toString());
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                String content = new String();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    content += line;
                    content += '\n';
                }
                return content;
            } else {
                logger.error("Error response : " + statusLine);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (post != null)
                post.releaseConnection();
        }
        return null;
    }

    public static String postData(String url, String data, String contentType, int timeOutMillis) {
        HttpPost post = null;
        try {
            org.apache.http.client.HttpClient client = new DefaultHttpClient();
            HttpParams httpParams = client.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, timeOutMillis);
            HttpConnectionParams.setSoTimeout(httpParams, SOCKET_TIMEOUT_MILLIS);
            post = new HttpPost(url);
            post.setHeader("Content-Type", contentType);
            ByteArrayInputStream bis = new ByteArrayInputStream(data.getBytes());
            HttpEntity entity = new InputStreamEntity(bis, bis.available());
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            logger.info("posted to " + url + " , status = " + response.getStatusLine().toString());
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                String content = new String();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    content += line;
                    content += '\n';
                }
                return content;
            } else {
                logger.error("Error response : " + statusLine);
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        finally {
            if(post != null)
                post.releaseConnection();
        }
        return null;
    }
    
}
