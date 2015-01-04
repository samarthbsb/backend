package com.sv.util;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.LOCATION;

import java.awt.Font;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.http.multipart.Attribute;
import org.jboss.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.FileUpload;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.jboss.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.jboss.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import org.jboss.netty.util.CharsetUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.sv.common.Version;

/**
 * @author vaibhav
 */
public class Utils {

    private static final Logger        logger         = LoggerFactory.getLogger(Utils.class.getCanonicalName());

    private static Map<String, String> contentTypeMap = new HashMap<String, String>();

    public static enum SORT {
        ASC("asc"), DESC("desc");

        private String name;

        private SORT(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    static {
        contentTypeMap.put("jpeg", "image/jpeg");
        contentTypeMap.put("png", "image/png");
    }

    /**
     * Initializes log4j if log4j.xml is missing in the classpath
     */
    public static void initLogger() {
        if(Thread.currentThread().getContextClassLoader().getResource("log4j.xml") == null) {
            Layout layout = new PatternLayout("%d{E MMM dd HH:mm:ss yyyy} %c %-4p %m%n");
            Appender appender = new ConsoleAppender(layout);
            BasicConfigurator.configure(appender);
        }
    }

    public static JSONArray convertToJSONArray(List<String> list) {
        JSONArray result = new JSONArray();
        if(list != null) {
            for(String str : list) {
                result.add(str);
            }
        }
        return result;
    }

    public static JSONArray convertToJSONArray(Set<String> set) {
        JSONArray result = new JSONArray();
        if(set != null) {
            for(String str : set) {
                result.add(str);
            }
        }
        return result;
    }

    public static <K, V> JSONObject convertToJSONMap(Map<K, V> map) {
        JSONObject result = new JSONObject();
        if(map != null) {
            Iterator<Map.Entry<K, V>> it = map.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry<K, V> pairs = it.next();
                result.put(pairs.getKey(), pairs.getValue());
            }
        }
        return result;
    }

    public static List<String> convertToStringList(JSONArray array) {
        if(array != null) {
            List<String> result = new ArrayList<String>(array);
            return result;
        }
        return new ArrayList<String>();
    }

    public static Set<String> convertToStringSet(JSONArray array) {
        if(array != null) {
            Set<String> result = new LinkedHashSet<String>(array);
            return result;
        }
        return new HashSet<String>();
    }

    public static <K, V> Map<K, V> convertToLinkedHashMap(JSONObject object) {
        if(object != null) {
            Map<K, V> result = new LinkedHashMap<K, V>(object);
            return result;
        }
        return new LinkedHashMap<K, V>();
    }

    /**
     * Returns the exception stack trace as a String.
     * 
     * @param e
     *            the exception to get the stack trace from.
     * @return the exception stack trace
     */
    public static String getStackTrace(Throwable e) {
        if(e == null) {
            return "";
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter printWriter = new PrintWriter(baos);
        e.printStackTrace(printWriter);
        printWriter.flush();
        String stackTrace = new String(baos.toByteArray());
        printWriter.close();
        return stackTrace;
    }

    /**
     * Returns the error message associated with the given Throwable. The error message returned
     * will try to be as precise as possible, handling cases where e.getMessage() is not meaningful,
     * like 'NullPointerException' for instance.
     * 
     * @param t
     *            the throwable to get the error message from
     * @return the error message of the given exception
     */
    public static String getErrorMessage(Throwable t) {
        if(t == null) {
            return "";
        }
        if(t instanceof InvocationTargetException) {
            InvocationTargetException ex = (InvocationTargetException) t;
            t = ex.getTargetException();
        }
        String errMsg = t instanceof RuntimeException? t.getMessage() : t.toString();
        if(errMsg == null || errMsg.length() == 0 || "null".equals(errMsg)) {
            errMsg = t.getClass().getName() + " at " + t.getStackTrace()[0].toString();
        }
        return errMsg;
    }

    public static String generateUUID(boolean appendTimestamp, boolean replaceDash) {
        String uuid = UUID.randomUUID().toString();
        if(appendTimestamp) {
            uuid = uuid + "-" + System.currentTimeMillis();
        }
        if(replaceDash) {
            uuid = uuid.replaceAll("-", "");
        }

        return uuid;
    }

    public static String getURLParam(Map<String, List<String>> urlParameters, String param) {
        String paramVal = "";
        if((urlParameters.containsKey(param)) && (urlParameters.get(param).get(0) != null) && ((urlParameters.get(param).get(0).trim().length() != 0))) {
            paramVal = urlParameters.get(param).get(0).trim();
        }
        return paramVal;
    }

    /**
     * return 10 digit msisdn. throws IllegalArgumentException if msisdn passed is less than 10
     * characters. It does not validate msisdn for being numeric.
     * 
     * @param msisdn
     * @return 10 digit msisdn
     */
    public static String getTenDigitMsisdn(String msisdn) {
        if(StringUtils.isEmpty(msisdn))
            return msisdn;
        msisdn = msisdn.trim();
        int length = msisdn.length();
        if(length == 10) {
            return msisdn;
        }
        if(length > 10) {
            return msisdn.substring(length - 10);
        }
        throw new IllegalArgumentException("Illegal value for msisdn : " + msisdn);
    }

    /**
     * Get 12 digit msisdn
     * 
     * @param msisdn
     * @return
     */
    public static String get12DigitMsisdn(String msisdn) {
        if(org.apache.commons.lang.StringUtils.isNotEmpty(msisdn) && msisdn.length() == 10) {
            return "91" + msisdn;
        }
        return msisdn;
    }

    /**
     * Adds +91 prefix to 10 digit msisdn. It does not validate msisdn for being numeric.
     * 
     * @param msisdn
     * @return +91 prefixed 13 character long msisdn
     */
    public static String getPrefixedMsisdn(String msisdn) {
        if(msisdn == null)
            return msisdn;
        msisdn = msisdn.trim();
        int length = msisdn.length();
        if(length == 10) {
            return "+91" + msisdn;
        }
        if(length > 10) {
            return "+91" + msisdn.substring(length - 10);
        }
        throw new IllegalArgumentException("Illegal value for msisdn : " + msisdn);
    }

    public static boolean containsAlpha(String str) {
        if(str == null) {
            return false;
        }
        for(int i = str.length() - 1; i >= 0; i--) {
            char c = str.charAt(i);
            if(Character.isLetter(c)) {
                return true;
            }
        }
        return false;
    }

    public static int getRandomNumber(int maxValue) {
        int numb = 1 + (int) (maxValue * Math.random());
        if(numb > 0 && numb <= maxValue) {
            return numb;
        }
        return 0;
    }

    public static List<String> stringToList(String csvString, boolean lowerCase) {
        if(csvString == null) {
            return null;
        }

        String[] tokens = null;
        if(lowerCase) {
            tokens = csvString.toLowerCase().split(",");
        }
        else {
            tokens = csvString.split(",");
        }
        List<String> words = Arrays.asList(tokens);
        return words;
    }

    public static String listToString(List<String> list, boolean lowerCase) {
        if(list == null) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        boolean isempty = true;
        for(int i = 0; i < list.size(); i++) {
            String kw = list.get(i);
            if(!isempty) {
                buffer.append(',');
            }
            buffer.append(kw);
            isempty = false;

        }
        return buffer.toString();
    }

    // WARNING: Resetting index in this call to be able to call multiple times perform and file
    // operations
    public static Map<String, List<String>> getMultipartUrlParameters(HttpRequest request) throws Exception {
        try {
            Map<String, List<String>> params = new HashMap<String, List<String>>();

            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request);

            List<InterfaceHttpData> bodyHttpDatas = decoder.getBodyHttpDatas();
            for(InterfaceHttpData data : bodyHttpDatas) {
                if(data.getHttpDataType() == HttpDataType.Attribute) {
                    Attribute attribute = (Attribute) data;
                    String value = attribute.getValue();
                    String key = attribute.getName();
                    List<String> values = params.get(key);
                    if(values == null) {
                        values = new ArrayList<String>();
                        params.put(key, values);
                    }
                    values.add(value);
                }
            }
            request.getContent().resetReaderIndex();
            return params;
        }
        catch (Exception e) {
            throw new Exception("Error extracting MultiPart Url Params from url : " + request.getUri() + ". Error : " + e.getMessage(), e);
        }
    }

    // WARNING: Resetting index in this call to be able to call multiple times perform and file
    // operations
    public static Reader getMultipartUrlFile(HttpRequest request) throws Exception {
        try {
            Map<String, List<String>> params = new HashMap<String, List<String>>();
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request);

            List<InterfaceHttpData> bodyHttpDatas = decoder.getBodyHttpDatas();
            for(InterfaceHttpData data : bodyHttpDatas) {
                if(data.getHttpDataType() == HttpDataType.FileUpload) {
                    FileUpload attribute = (FileUpload) data;
                    byte[] file = attribute.get();
                    if(file == null || file.length == 0) {
                        return null;
                    }
                    ByteArrayInputStream bis = new ByteArrayInputStream(file);
                    InputStreamReader isr = new InputStreamReader(bis, attribute.getCharset());
                    request.getContent().resetReaderIndex();
                    return isr;
                }
            }
            request.getContent().resetReaderIndex();
            return null;
        }
        catch (Exception e) {
            throw new Exception("Error extracting MultiPart Url Params from url : " + request.getUri() + ". Error : " + e.getMessage(), e);
        }
    }

    public static Map<String, List<String>> getUrlParameters(String url) throws Exception {
        try {
            Map<String, List<String>> params = new HashMap<String, List<String>>();
            String[] urlParts = url.split("\\?");
            if(urlParts.length > 1) {
                String query = urlParts[1];
                for(String param : query.split("&")) {
                    String pair[] = param.split("=");
                    String key = URLDecoder.decode(pair[0], "UTF-8");
                    String value = "";
                    if(pair.length > 1) {
                        value = URLDecoder.decode(pair[1], "UTF-8");
                    }
                    List<String> values = params.get(key);
                    if(values == null) {
                        values = new ArrayList<String>();
                        params.put(key, values);
                    }
                    values.add(value);
                }
            }
            return params;
        }
        catch (UnsupportedEncodingException e) {
            throw new Exception("Error extracting Url Params from url : " + url + ". Error : " + e.getMessage(), e);
        }
    }

    public static Map<String, List<String>> getAllUrlParameters(String url) throws Exception {
        try {
            Map<String, List<String>> params = new HashMap<String, List<String>>();
            String[] urlParts = url.split("\\?");
            if(urlParts.length > 1) {
                for(String query : urlParts) {
                    for(String param : query.split("&")) {
                        String pair[] = param.split("=");
                        String key = URLDecoder.decode(pair[0], "UTF-8");
                        String value = "";
                        if(pair.length > 1) {
                            value = URLDecoder.decode(pair[1], "UTF-8");
                        }
                        List<String> values = params.get(key);
                        if(values == null) {
                            values = new ArrayList<String>();
                            params.put(key, values);
                        }
                        values.add(value);
                    }
                }
            }
            return params;
        }
        catch (UnsupportedEncodingException e) {
            throw new Exception("Error extracting Url Params from url : " + url + ". Error : " + e.getMessage(), e);
        }
    }

    public static String getFileName(String fileUrl) {
        String fileName = fileUrl;
        if(fileUrl.contains("/")) {
            fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        }
        return fileName;
    }

    public static String getFileExtension(String fileUrl) {
        String fileExtn = fileUrl;
        if(fileUrl.contains(".")) {
            fileExtn = fileUrl.substring(fileUrl.lastIndexOf(".") + 1);
        }
        return fileExtn;
    }

    public static String getContentType(String type) {
        return contentTypeMap.get(type.toLowerCase());
    }

    public static String getContentExtension(String type) {
        switch (type){
            case "image/jpeg":
                return "jpeg";
            case "image/png":
                return "png";
            case "image/gif":
                return "gif";
            case "image/pjpeg":
                return "jpeg";
            default:
                return null;
        }
    }

    public static Font getFont(String lang, float fontSize) {
        // TODO Make font selection based on language
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new File("resources/font/mangal.ttf")).deriveFont(fontSize);
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        mapper.setSerializationInclusion(Include.NON_NULL);

        return mapper;
    }

    public static String getJsonString(Object obj) throws JsonProcessingException {
        return Utils.getObjectMapper().writeValueAsString(obj);
    }

    public static int getNumParameter(Map<String, List<String>> urlParameters, String paramName, int defaultValue) {
        int numresults = defaultValue;
        String stringParameter = getStringParameter(urlParameters, paramName);
        try {
            if(stringParameter != null && !stringParameter.equals("")) {
                stringParameter = stringParameter.replace(",", "");
                numresults = Integer.parseInt(stringParameter);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return numresults;
    }

    public static long getLongParameter(Map<String, List<String>> urlParameters, String paramName, long defaultValue) {
        String stringParameter = getStringParameter(urlParameters, paramName);
        try {
            if(stringParameter != null && !stringParameter.equals("")) {
                defaultValue = Long.parseLong(stringParameter);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static double getDoubleParameter(Map<String, List<String>> urlParameters, String paramName, double defaultValue) {
        String stringParameter = getStringParameter(urlParameters, paramName);
        try {
            if(stringParameter != null && !stringParameter.equals("")) {
                defaultValue = Double.parseDouble(stringParameter);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static Date now() {
        return Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime();
    }

    public static Date getPastOrFutureDateFromNow(int daysShiftFromNow) {
        return new Date(System.currentTimeMillis() + daysShiftFromNow * 24L * 60 * 60 * 1000);
    }

    public static String getStringParameter(Map<String, List<String>> urlParameters, String paramName) {
        if(urlParameters == null)
            return null;
        if(urlParameters.get(paramName) != null && urlParameters.get(paramName).size() > 0) {
            String nresultsParam = urlParameters.get(paramName).get(0);
            if(nresultsParam != null) {
                return nresultsParam.trim();
            }
        }
        return null;
    }

    public static List<String> getListParameter(Map<String, List<String>> urlParameters, String paramName) {
        if(urlParameters.get(paramName) != null && urlParameters.get(paramName).size() > 0)
            return urlParameters.get(paramName);
        else
            return null;
    }

    public static HashMap<String, Integer> getSortMapFromUrlParams(Map<String, List<String>> urlParameters) {
        HashMap<String, Integer> sortMap = new HashMap<String, Integer>();
        List<String> sortParams = urlParameters.get("sort");
        if(sortParams != null && sortParams.size() > 0) {
            for(String s : sortParams) {
                s = s.trim();
                if(s.equals("")) {
                    continue;
                }
                int sign = 1;
                if(s.charAt(0) == '+') {
                    s = s.substring(1);
                }
                else if(s.charAt(0) == '-') {
                    sign = -1;
                    s = s.substring(1);
                }
                if(s.toLowerCase().equals("lastupdated")) {
                    sortMap.put("lastUpdated", sign);
                }
                if(s.toLowerCase().equals("creationtime")) {
                    sortMap.put("creationTime", sign);
                }
                if(s.toLowerCase().equals("price")) {
                    sortMap.put("price", sign);
                }
                if(s.toLowerCase().equals("rank")) {
                    sortMap.put("rank", sign);
                }
                if(s.toLowerCase().equals("releasedate")) {
                    sortMap.put("releaseDate", sign);
                }
                if(s.toLowerCase().equals("starttime")) {
                    sortMap.put("startTime", sign);
                }
                if(s.toLowerCase().equals("durationseconds")) {
                    sortMap.put("durationSeconds", sign);
                }
                if(s.toLowerCase().equals("recommendation")) {
                    sortMap.put("recommendation", sign);
                }
                if(s.toLowerCase().equals("publishedyear")) {
                    sortMap.put("publishedYear", sign);
                }
            }
        }
        return sortMap;
    }

    public static HttpResponse createOKResponse(String responseStr) {
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.setHeader(CONTENT_TYPE, "text/plain; charset=UTF-8");
        if(!StringUtils.isEmpty(responseStr))
            response.setContent(ChannelBuffers.copiedBuffer(responseStr, CharsetUtil.UTF_8));
        return response;
    }

    public static HttpResponse createResponse(String responseJson, HttpResponseStatus responseStatus) {
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, responseStatus);
        response.setHeader(CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.setContent(ChannelBuffers.copiedBuffer(responseJson, CharsetUtil.UTF_8));
        return response;
    }

    public static HttpResponse createResponse(String responseStr, String contentType, HttpResponseStatus status) {
        if(responseStr == null)
            responseStr = "";
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status);
        response.setHeader(CONTENT_TYPE, contentType);
        if(responseStr != null) {
            response.setContent(ChannelBuffers.copiedBuffer(responseStr, CharsetUtil.UTF_8));
        }
        return response;
    }

    public static HttpResponse createResponse(String responseStr, String contentType, HttpResponseStatus status, String fileName) {
        if(responseStr == null)
            responseStr = "";
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status);
        response.setHeader(CONTENT_TYPE, contentType);
        if(responseStr != null) {
            response.setContent(ChannelBuffers.copiedBuffer(responseStr, CharsetUtil.UTF_8));
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        return response;
    }

    public static HttpResponse createRedirectResponse(String redirectUrl) {
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
        response.setHeader(CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.addHeader(LOCATION, redirectUrl);
        response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Expires", 0);
        return response;
    }

    public static String createErrorResponse(String error) {
        return createResponseJsonObj(false, error).toString();
    }

    public static JSONObject createResponseJsonObj(boolean success, String error) {
        JSONObject obj = new JSONObject();
        obj.put("success", success);
        if(error != null)
            obj.put("error", error);
        return obj;
    }

    public static String createSuccessResponse() {
        return createResponseJsonObj(true, null).toString();
    }

    public static Date parseDate(String dateStr, SimpleDateFormat sdf) {
        try {
            if(dateStr != null) {
                Date date = sdf.parse(dateStr);
                return date;
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static boolean isValidText(String text) {
        if(text == null) {
            return true;
        }
        int errorThreshold = text.length() / 2;
        int errorOccured = 0;
        for(char c : text.toCharArray()) {
            if(Character.isDefined(c)) {
                continue;
            }

            errorOccured++;// ? is error case
        }
        if(errorOccured > errorThreshold) {
            return false;
        }
        // Below is heuristic for catching error like ??????? or
        String[] words = text.split("\\s+");
        int wordCount = words.length;
        if(wordCount == 0) {
            return true;
        }
        int suspectedWordCount = 0;
        label0:
        for(String word : words) {
            if(word.length() > 1) {
                int c = word.charAt(0);
                for(int i = 1; i < word.length(); i++) {
                    if(c != word.charAt(i)) {
                        continue label0;
                    }
                }
                suspectedWordCount++;
            }
        }
        if(suspectedWordCount > (1 + Math.log(wordCount))) {
            return false;
        }
        return true;
    }

    public static boolean _isValidText(String text) {
        if(text == null) {
            return true;
        }
        int errorThreshold = text.length() / 2;
        int errorOccured = 0;
        for(char c : text.toCharArray()) {
            if(c >= 32 && c <= 62) {
                continue;
            }
            if(c >= 64 && c <= 126) {
                continue;
            }
            // if(Character.isAlphabetic(c)){ //this is in java 7. for java 6 below is ugly hack.
            // not guarunteed to work.
            // continue;
            // }
            if(c >= 0x900 && c <= 0x97F) {// Devnagari character
                continue;
            }
            if(c >= 0xA8E0 && c <= 0xA8FB) {// Devnagari extended characters for sanskrit
                continue;
            }
            if(c >= 0xB80 && c <= 0xBFF) {// Tamil characters
                continue;
            }
            if(c >= 0x0C00 && c <= 0x0C7F) {// Telugu characters
                continue;
            }
            if(c >= 0x0C80 && c <= 0x0CFF) {// Kannada characters
                continue;
            }
            if(Character.isDigit(c)) {
                continue;
            }
            if(Character.isWhitespace(c)) {
                continue;
            }

            errorOccured++;// ? is error case
        }
        if(errorOccured > errorThreshold) {
            return false;
        }
        // Below is heuristic for catching error like ??????? or
        String[] words = text.split("\\s+");
        int wordCount = words.length;
        if(wordCount == 0) {
            return true;
        }
        int suspectedWordCount = 0;
        label0:
        for(String word : words) {
            if(word.length() > 1) {
                int c = word.charAt(0);
                for(int i = 1; i < word.length(); i++) {
                    if(c != word.charAt(i)) {
                        continue label0;
                    }
                }
                suspectedWordCount++;
            }
        }
        if(suspectedWordCount > (1 + Math.log(wordCount))) {
            return false;
        }
        return true;
    }

    public static String createMSResponse(long num, JSONObject result, Version version) {
        if(version == Version.V1) {
            return result.toString();
        }
        JSONObject responseObj = new JSONObject();
        responseObj.put("num", num);
        responseObj.put("result", result);
        return responseObj.toString();
    }

    public static String createMSUpdateResponse(boolean result, Version version) {
        if(version == Version.V1) {
            return Boolean.toString(result);
        }
        JSONObject responseObj = new JSONObject();
        responseObj.put("result", result);
        return responseObj.toString();
    }

    public static String createResponse(JSONObject jsonObj, Version version) {
        if(version == Version.V1) {
            return jsonObj.get("result").toString();
        }
        return jsonObj.toString();
    }

    public static String createResponse(long total, long num, long pos, JSONArray result, Version version) {
        if(version == Version.V1) {
            return result.toString();
        }
        JSONObject responseObj = new JSONObject();
        responseObj.put("total", total);
        responseObj.put("num", num);
        responseObj.put("pos", pos);
        responseObj.put("result", result);
        return responseObj.toString();
    }

    public static String createSmartShopResponse(long total, long num, long pos, JSONArray result) {
        JSONObject responseObj = new JSONObject();
        responseObj.put("total", total);
        responseObj.put("num", num);
        responseObj.put("pos", pos);
        responseObj.put("result", result);
        return responseObj.toString();
    }

    public static String createSmartShopResponse(long total, long num, long pos, JSONObject result, Version version) {
        JSONObject responseObj = new JSONObject();
        responseObj.put("total", total);
        responseObj.put("num", num);
        responseObj.put("pos", pos);
        responseObj.put("result", result);
        return responseObj.toString();
    }

    public static String createSmartShopResponse(JSONArray result) {
        JSONObject responseObj = new JSONObject();
        responseObj.put("result", result);
        return responseObj.toString();
    }

    public static String createQriousResponse(long total, long num, long pos, JSONArray result) {
        JSONObject responseObj = new JSONObject();
        responseObj.put("total_results", total);
        responseObj.put("request_num", num);
        responseObj.put("start_index", pos);
        responseObj.put("q", result);
        return responseObj.toString();
    }

    public static String createImageUploadResponse(String imageUrl, String eTag) {
        JSONObject responseObj = new JSONObject();
        responseObj.put("image_url", imageUrl);
        responseObj.put("etag", eTag);
        return responseObj.toString();
    }

    public static Map getMongoSearchListObject(String searchType, List<String> toBeSearched) {
        List<String> searchTypes = Arrays.asList("in", "all");
        String key = "$";
        if(searchTypes.contains(searchType)) {
            key += searchType;
        }
        else {
            key += "in";
        }
        String[] toBeSearchedArray = new String[toBeSearched.size()];
        toBeSearched.toArray(toBeSearchedArray);
        Map searchMap = new HashMap<>();
        searchMap.put(key, toBeSearchedArray);
        return searchMap;
    }

    public static JSONArray getJsonArrayFromStringList(List<String> list) {
        JSONArray arr = new JSONArray();
        if(list != null) {
            for(String s : list) {
                arr.add(s);
            }
        }
        return arr;
    }

    public static <E> List<E> getListFromJsonArr(JSONArray jsonArr) {
        List<E> list = new ArrayList<E>();
        if(jsonArr != null) {
            for(int i = 0; i < jsonArr.size(); i++) {
                E e = (E) jsonArr.get(i);
                list.add(e);
            }
        }
        return list;
    }

    public static String encodeBase64(String key) {
        return Base64.encode(key.getBytes());
    }

    public static String decodeBase64(String key) throws UnsupportedEncodingException {
        return new String(Base64.decode(key), "UTF-8");
    }

    public static byte[] decodeBase64ToBytes(String key) {
        return Base64.decode(key);
    }

    public static String encodeUrl(String str) {

        String encodedStr = null;
        if(str != null) {
            try {
                encodedStr = URLEncoder.encode(str, "UTF-8").replace("+", "%20");
            }
            catch (UnsupportedEncodingException e) {
                System.out.println("Exception in encoding Utils.encode: " + e.getMessage());
            }
        }
        return encodedStr;
    }

    public static List<String> convertToStringList(String str) {
        List<String> list = new ArrayList<String>();
        if(str == null) {
            return list;
        }
        str = str.replace("[", "");
        str = str.replace("]", "");
        String[] arr = str.split(",");
        for(String s : arr) {
            list.add(s.trim());
        }
        return list;
    }

    public static <T> Map<String, T> updateMapKeysToMongoFieldFormat(Map<String, T> map) {
        Map<String, T> newVideoMap = null;
        if(map != null) {
            newVideoMap = new LinkedHashMap<String, T>();
            Iterator<Map.Entry<String, T>> it = map.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry<String, T> entry = it.next();
                newVideoMap.put(entry.getKey().replace(".", "|"), entry.getValue());
            }
        }
        return newVideoMap;
    }

    public static <T> Map<String, T> updateMapKeysFromMongoFieldFormat(Map<String, T> map) {
        Map<String, T> newVideoMap = null;
        if(map != null) {
            newVideoMap = new LinkedHashMap<String, T>();
            Iterator<Map.Entry<String, T>> it = map.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry<String, T> entry = it.next();
                newVideoMap.put(entry.getKey().replace("|", "."), entry.getValue());
            }
        }
        return newVideoMap;
    }

    public static String convertBytesIntoMB(long fileSizeInBytes) {
        float mb = fileSizeInBytes / (1024.0f * 1024.0f);
        return String.format("%.2f", mb) + " MB";
    }

    public static String convertSecondsIntoMinutes(long durationInSeconds) {
        long min = durationInSeconds / 60;
        long seconds = durationInSeconds % 60;
        return min + ":" + seconds;
    }

    public static String encodeURL(String url) {
        if(url != null) {
            // TODO We can do better than this
            return url.replace(" ", "%20");
        }
        else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFromJSONObj(Object obj, String tag) {
        return (T) ((JSONObject) obj).get(tag);
    }

    public static String md5(String input) {

        String md5 = null;

        if(null == input)
            return null;

        try {

            // Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            // Update input string in message digest
            digest.update(input.getBytes(), 0, input.length());

            // Converts message digest value in base 16 (hex)
            md5 = new BigInteger(1, digest.digest()).toString(16);
            while(md5.length() < 32) { // 40 for SHA-1
                md5 = "0" + md5;
            }

        }
        catch (Exception e) {
        }
        return md5;
    }

    public static String removeQuotes(String str) {
        if(StringUtils.isEmpty(str) || str.length() <= 0)
            return str;
        boolean isAlphaN = StringUtils.isAlphanumeric("" + str.charAt(0));
        if(!isAlphaN || str.startsWith("\""))
            str = str.substring(1);
        if(str.endsWith("\""))
            str = str.substring(0, str.length() - 1);
        return str;
    }

    public static String getLargeResizedImage(String imageUrl) {
        if(StringUtils.isEmpty(imageUrl))
            return imageUrl;
        int width = 600;
        int height = 600;
        imageUrl = imageUrl.replace(" ", "%20");
        return "http://ic.bsbportal.com/unsafe/" + width + "x" + height + "/top/" + imageUrl;
    }

    public static String getMediumResizedImage(String imageUrl) {
        if(StringUtils.isEmpty(imageUrl))
            return imageUrl;
        int width = 300;
        int height = 300;
        imageUrl = imageUrl.replace(" ", "%20");
        return "http://ic.bsbportal.com/unsafe/" + width + "x" + height + "/top/" + imageUrl;
    }

    public static String getSmallResizedImage(String imageUrl) {
        if(StringUtils.isEmpty(imageUrl))
            return imageUrl;
        int width = 200;
        int height = 200;
        imageUrl = imageUrl.replace(" ", "%20");
        return "http://ic.bsbportal.com/unsafe/" + width + "x" + height + "/top/" + imageUrl;
    }

    public static String getResizedImage(String imageUrl, int width, int height) {
        if(StringUtils.isEmpty(imageUrl))
            return imageUrl;
        if(imageUrl.contains("ic.bsbportal.com"))
            return imageUrl;
        // todo:
        imageUrl = imageUrl.replace("s3-ap-southeast-1.amazonaws.com/bsbcms", "d2n2xdxvkri1jk.cloudfront.net");
        imageUrl = imageUrl.replace(" ", "%20");
        return "http://ic.bsbportal.com/unsafe/" + width + "x" + height + "/top/" + imageUrl;
    }

    public static String getMaskedMsisdn(String msisdn) {
        if(StringUtils.isEmpty(msisdn))
            return msisdn;
        msisdn = getTenDigitMsisdn(msisdn);
        msisdn = msisdn.substring(0, 2) + "XXXXX" + msisdn.substring(7);
        return msisdn;
    }

    public static String getCountLabel(int count) {
        if(count < 1000)
            return "" + count;
        else
            return ((count / 1000)) + "K+";
    }

    public static boolean isValidImage(String url) {
        if(url == null)
            return false;

        if(url.endsWith(".png") || url.endsWith(".jpg") || url.endsWith(".jpeg") || url.endsWith(".gif") || url.endsWith(".JPG"))
            return true;
        return false;
    }

    public static String stripSpecialCharacters(String str) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if(Character.isLetterOrDigit(ch) || Character.isWhitespace(ch) || (ch == '_'))
                sb.append(ch);
        }
        return sb.toString();
    }

    public static Object parseWithoutException(String data, Object defaultValue) {
        try {
            Object object = JSONValue.parseWithException(data);
            return object;
        }
        catch (Exception e) {
            // logger.error("Error parsing json data [{}]", data, e);
            return defaultValue;
        }
    }

    public static <T> JSONArray getJsonArrayFromList(List<T> list) {
        JSONArray arr = new JSONArray();
        if(list != null) {
            for(T s : list) {
                arr.add(s);
            }
        }
        return arr;
    }

    public static <T> JSONArray getJsonArrayFromMap(Map<T, T> map) {
        JSONArray arr = new JSONArray();
        JSONObject jsonObj = new JSONObject();
        if(map != null) {
            for(T s : map.keySet()) {
                jsonObj.put(s, map.get(s));
            }
            arr.add(jsonObj);
        }
        return arr;
    }

    public static JSONArray getJsonArrayFromArray(Object[] array) {
        JSONArray arr = new JSONArray();
        if(array != null) {
            for(Object s : array) {
                arr.add(s);
            }
        }
        return arr;
    }

    public static String appendParamToUrl(String url, String param, String value) throws UnsupportedEncodingException {
        if(StringUtils.isBlank(url))
            return url;

        if(url.contains("&" + param + "=") || url.contains("?" + param + "="))
            return url;

        try {
            if(url.contains("?"))
                url = url + "&" + param + "=" + URLEncoder.encode(value, "UTF-8");
            else
                url = url + "?" + param + "=" + URLEncoder.encode(value, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            logger.error("Error appending param" + param + " to url " + url + ",value : " + value + ". Error : " + e.getMessage(), e);
            throw e;
        }

        return url;
    }

    public static List<String> getPojoFieldNames(Class<?> pojoClass) {
        if(pojoClass == null)
            return null;

        List<String> fieldNames = new ArrayList<>();

        BeanInfo info;
        try {
            info = Introspector.getBeanInfo(pojoClass);
            for(PropertyDescriptor pd : info.getPropertyDescriptors())
                fieldNames.add(pd.getName());
        }
        catch (IntrospectionException e) {
            // Do nothing
        }

        return fieldNames;
    }

    public static boolean isObjPresent(Object obj) {
        return obj == null? false : true;
    }

    /**
     * Input : System.out.println(getFTPHost(
     * "54.251.164.78/assure/Assure_gallery_BENGALI_Entertainment_ad50peg01.xml")); Output:
     * 54.251.164.78
     * 
     * Note : Dont append any protocol like http:// or ftp://
     * 
     * @param dataUrl
     * @return
     */
    public static String getFTPHost(String dataUrl) {
        return dataUrl.substring(0, dataUrl.indexOf("/"));
    }

    /**
     * Input : System.out.println(getFTPPath(
     * "54.251.164.78/assure/Assure_gallery_BENGALI_Entertainment_ad50peg01.xml")); Output:
     * /assure/Assure_gallery_BENGALI_Entertainment_ad50peg01.xml
     * 
     * Note : Dont append any protocol like http:// or ftp://
     * 
     * @param dataUrl
     * @return
     */
    public static String getFTPPath(String dataUrl) {
        return dataUrl.substring(dataUrl.indexOf("/"), dataUrl.length());
    }

    public static Date getDate(Date date, int period, int val) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("IST"));
        calendar.setTime(date);
        calendar.add(period, val);
        return calendar.getTime();
    }

    /**
     * Get yesterday relative to passed date. If passed date is null get yesterday based on current
     * system date in IST
     * 
     * @param date
     * @return
     */
    public static Date getYesterday(Date date) {
        Date dateToProcess = Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime();
        if(date != null) {
            dateToProcess = date;
        }
        return getDate(dateToProcess, Calendar.DATE, -1);
    }

    public static String generateMD5Hash(String str) {

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());

            byte byteData[] = md.digest();

            // convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();
        }
        catch (Exception e) {
            logger.error("No algorithm named 'MD5' exists.", e);
        }

        return null;
    }

    public static Date getDate(String dateStr, Date defaultVal) {
        String[] formats = new String[]{ "yyyy-MM-dd'T'HH:mm:ss", "MM/dd/yyyy hh:mm:ss a", "yyyy-MM-dd", "yyyy-MM-dd'T'hh:mm:ssX", "MMMMM dd, yyyy" };

        Exception lastEx = null;
        Date d = defaultVal;
        for(String format : formats) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            try {
                d = sdf.parse(dateStr);
                logger.info("Finally parsed date [{}] using format [{}]", dateStr, format);
                return d;
            }
            catch (Exception e) {
                lastEx = e;
            }
        }
        return d;
    }

    public static String formatDate(String convertThis, String format) {

        if(convertThis == null) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = formatter.parse(convertThis);
        }
        catch (ParseException e) {
            logger.error("parsing exception in pasring date");
            return null;
        }
        return formatter.format(date);
    }

    public static void compressGzipFile(String file, String gzipFile) {
        try {
            FileInputStream fis = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(gzipFile);
            GZIPOutputStream gzipOS = new GZIPOutputStream(fos);
            byte[] buffer = new byte[1024];
            int len;
            while((len = fis.read(buffer)) != -1) {
                gzipOS.write(buffer, 0, len);
            }
            // close resources
            gzipOS.close();
            fos.close();
            fis.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Map<String, List<String>> getParamsFromRequestPayload(String query) {
        Map<String, List<String>> params = new HashMap<String, List<String>>();
        if(StringUtils.isNotBlank(query)) {
            try {
                for(String param : query.split("&")) {
                    String pair[] = param.split("=");
                    String key = URLDecoder.decode(pair[0], "UTF-8");
                    String value = "";
                    if(pair.length > 1) {
                        value = URLDecoder.decode(pair[1], "UTF-8");
                    }
                    List<String> values = params.get(key);
                    if(values == null) {
                        values = new ArrayList<String>();
                        params.put(key, values);
                    }
                    values.add(value);
                }
            }
            catch (UnsupportedEncodingException e) {
                logger.error("Exception while populating payment response params ", e);
            }
        }
        return params;
    }

    /**
     * http://stackoverflow.com/questions/5902090/how-to-extract-parameters-from-a-given-url
     * 
     * @param url
     * @return
     */
    public static Map<String, List<String>> getQueryParams(String url) {
        Map<String, List<String>> params = new HashMap<String, List<String>>();
        try {
            String[] urlParts = url.split("\\?");
            if(urlParts.length > 1) {
                String query = urlParts[1];
                for(String param : query.split("&")) {
                    String[] pair = param.split("=");
                    String key = URLDecoder.decode(pair[0], "UTF-8");
                    String value = "";
                    if(pair.length > 1) {
                        value = URLDecoder.decode(pair[1], "UTF-8");
                    }

                    List<String> values = params.get(key);
                    if(values == null) {
                        values = new ArrayList<String>();
                        params.put(key, values);
                    }
                    values.add(value);
                }
            }
        }
        catch (UnsupportedEncodingException ex) {
            logger.error("Error in getQueryParams : " + ex.getMessage(), ex);
        }
        return params;
    }

    /**
     * @param str
     *            i.e. string
     * @return true if string has text false if either empty or null
     */
    public static boolean stringHasText(String str) {

        if(str == null || str.isEmpty()) {
            return false;
        }
        return true;
    }

    public static String getYesterDayDate(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }

    /**
     * Parses date from one format to other
     * 
     * @return date after parsing or same date - if date is already in required format
     * @exception ParseException
     *                - if fromFormat is empty or when unable to parse date
     */
    public static String parsesStringDateToFormat(String dateInString, String toFormat, String fromFormat) throws ParseException {

        DateFormat toFormatter = new SimpleDateFormat(toFormat); // wanted format
        toFormatter.setLenient(false);

        if(isDateInValidFormat(toFormatter, dateInString)) {
            return dateInString;
        }

        if(!Utils.stringHasText(fromFormat)) {
            throw new ParseException("Date: '" + dateInString + "' cannot be parsed to to-format: " + toFormat + " when from-format: <empty or null>", 0);
        }

        DateFormat fromFormatter = new SimpleDateFormat(fromFormat); // current format
        fromFormatter.setLenient(false);

        return toFormatter.format(fromFormatter.parse(dateInString));
    }

    /**
     * Checks whether date is in mentioned format
     * 
     * @param formatter
     * @param dateString
     * @return true if valid, else false
     */
    public static boolean isDateInValidFormat(DateFormat formatter, String dateString) {

        try {

            formatter.parse(dateString);
        }
        catch (ParseException ex) {

            return false;
        }
        return true;
    }

    /**
     * Returns List of dates between fromDate and toDate
     */
    public static List<String> getListOfDatesBetweenTwoDates(String fromDate, String toDate, String toFormat, String fromFormat) throws Exception {

        List<String> dates = new ArrayList<String>();

        if(!stringHasText(fromDate)) {

            throw new Exception("fromDate cannot be empty.");
        }
        else if(!stringHasText(toDate)) {

            dates.add(parsesStringDateToFormat(fromDate, toFormat, fromFormat));
        }
        else {

            DateFormat formatter;

            String _fromDate = parsesStringDateToFormat(fromDate, toFormat, fromFormat);
            String _toDate = parsesStringDateToFormat(toDate, toFormat, fromFormat);

            formatter = new SimpleDateFormat(toFormat);
            Date startDate = (Date) formatter.parse(_fromDate);
            Date endDate = (Date) formatter.parse(_toDate);
            long interval = 24 * 1000 * 60 * 60; // 1 hour in millis
            long endTime = endDate.getTime(); // create your endtime here, possibly using Calendar
                                              // or
            // Date
            long curTime = startDate.getTime();
            while(curTime <= endTime) {
                Date lDate = new Date(curTime);
                String ds = formatter.format(lDate);
                dates.add(ds);
                curTime += interval;
            }
        }
        return dates;
    }

    /**
     * As url replaces + to <space>. So it replaces " 0530" to "+0530"
     * 
     * @param timestamp
     * @return date in form of string after validating
     */
    public static String returnValidatedDateString(String timestamp) {

        String date = timestamp;

        if(!timestamp.contains("+0530")) {
            if(timestamp.contains(" 0530")) {
                date = timestamp.replace(" 0530", "+0530");
            }
        }
        return date;
    }
}
