/**
 * 
 */
package com.sv.http;

import com.sv.handlers.IAuthenticatedUrlRequestHandler;
import com.sv.handlers.IUrlRequestHandler;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vaibhav
 * 
 */
public class MyHttpRequestHandler extends BaseHttpRequestHandler {

    private static final Logger             logger          = LoggerFactory.getLogger(MyHttpRequestHandler.class.getCanonicalName());

    private Map<String, IUrlRequestHandler> handlerClassMap = new HashMap<String, IUrlRequestHandler>();

    private ApplicationContext              applicationContext;

    public MyHttpRequestHandler() {
        super();
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sv.http.BaseHttpRequestHandler#handleRequest(java.lang.String, java.lang.String,
     * org.jboss.netty.channel.MessageEvent, org.jboss.netty.handler.codec.http.HttpRequest)
     */
    @Override
    protected HttpResponse handleRequest(String requestUri, String requestPayload, MessageEvent event, HttpRequest request) {
        if(logger.isDebugEnabled()) {
            logger.debug("Request URI : " + requestUri + " , message " + requestPayload);
        }

        // if
        // (IPWhitelistService.getInstance().isClientIpWhitelisted(urlPattern,
        // clientIP))
        {
            int i = requestUri.indexOf("?");
            String requestUriWoutParams = requestUri;
            if(i > 0) {
                requestUriWoutParams = requestUri.substring(0, i);
            }

            IUrlRequestHandler urlRequestHandler = lookupUrlHandler(requestUriWoutParams);
            if(urlRequestHandler == null) {
                return new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
            }

            try {

                if(requestUri.contains("headers")) {
                    HttpResponse response = urlRequestHandler.handleRequest(requestUri, requestPayload, event, request);
                    if(response == null) {
                        return new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NO_CONTENT);
                    }
                    return response;
                }

//                request.addHeader("x-bsy-ip", getRemoteAddress().getAddress().getHostAddress());
                // Commenting these headers as not in use
                // request.addHeader("x-bsy-host",getRemoteAddress().getHostName());
                // request.addHeader("x-bsy-port",getRemoteAddress().getPort());

//                PortalContext.setRequest(request);

                // Map<String, List<String>> urlParameters = Utils.getUrlParameters(requestUri);
                // if(urlParameters != null) {
                // String lang = Utils.getStringParameter(urlParameters, "lang");
                // if(!StringUtils.isEmpty(lang))
                // PortalContext.setLang(lang);
                // }
                // boolean forceUpdateNDSInfo =
                // ObjectUtils.getBoolean(Utils.getURLParam(urlParameters, "fuNDS"), false);
                // PortalContext.getRequestContext().setForceUpdateNDSInfo(forceUpdateNDSInfo);

                if(urlRequestHandler instanceof IAuthenticatedUrlRequestHandler) {
                    boolean authenticated = ((IAuthenticatedUrlRequestHandler) urlRequestHandler).authenticate(requestUri, requestPayload, event, request);
                    if(!authenticated)
                        return new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN);
                }

                HttpResponse response = urlRequestHandler.handleRequest(requestUri, requestPayload, event, request);

                // Add support for cross-origin Ajax requests (CORS)
                // http://www.w3.org/TR/cors/#access-control-allow-origin-response-header
//                if(requestUri.contains("music/")) {
//                    response.headers().add("Access-Control-Allow-Origin", "*");
//                    response.headers().add("Access-Control-Max-Age", 1728000);
//                    response.headers().add("Access-Control-Allow-Methods", "OPTIONS, HEAD, GET, POST, PUT, DELETE");
//                    response.headers().add("Access-Control-Allow-Headers", "X-Requested-With, x-msisdn, x-bsy-utkn, x-bsy-wap, Content-Type, Content-Length");
//                }

                logger.info("URI : {}, Response : {}", requestUri, response.getStatus());
                return response;
            }
            catch (Exception e) {
                logger.error("Error occurred while handling request : " + requestUri + "requestPayload : " + requestPayload + ". Error : " + e.getMessage(), e);
                return new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
            }
        }
        // else
        // {
        // return new Response(HttpResponseStatus.UNAUTHORIZED,"Unauthorized");
        // }

    }
    
    public IUrlRequestHandler lookupUrlHandler(String url) {
        IUrlRequestHandler handler = handlerClassMap.get(url);
        if(handler != null) {
            return handler;
        }
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for(String beanName : beanDefinitionNames) {
            Object obj = applicationContext.getBean(beanName);
            if(obj == null) {
                logger.info(beanName + " is not initializes by spring");
                continue;// spring is mad giving null all over places
            }
            Controller controller = obj.getClass().getAnnotation(Controller.class);
            if(controller != null) {
                if(url.matches(controller.value())) {
                    handler = (IUrlRequestHandler) obj;
                    handlerClassMap.put(url, handler);
                    return handler;
                }
            }
        }

        return null;
    }
}
