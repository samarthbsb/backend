package com.sv.handlers;

import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

/**
 * Implement this to establish handler as a netty handler.
 * 
 * @author vaibhav
 */
public interface IUrlRequestHandler {

    public HttpResponse handleRequest(String requestUri, String requestPayload, MessageEvent event, HttpRequest request) throws Exception;
}
