package com.sv.handlers;

import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpRequest;

/**
 * Implement this to add authentication to request handlers.
 * 
 * @author vaibhav
 */
public interface IAuthenticatedUrlRequestHandler {

    public boolean authenticate(String requestUri, String requestPayload, MessageEvent event, HttpRequest request) throws Exception;
}
