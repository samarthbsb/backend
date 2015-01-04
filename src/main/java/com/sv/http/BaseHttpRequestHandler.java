/**
 * 
 */
package com.sv.http;

import static org.jboss.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.COOKIE;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.SET_COOKIE;

import java.net.InetSocketAddress;
import java.util.Set;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.CookieDecoder;
import org.jboss.netty.handler.codec.http.CookieEncoder;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpChunkTrailer;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author vaibhav
 * 
 */
@Component
@Sharable
public abstract class BaseHttpRequestHandler extends SimpleChannelUpstreamHandler {

    private static final Logger logger = LoggerFactory.getLogger(BaseHttpRequestHandler.class.getCanonicalName());
    private HttpRequest         request;
    private String              clientIP;
    private InetSocketAddress   remoteAddress;
    private boolean             readingChunks;

    public BaseHttpRequestHandler() {
        super();
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        try {
            long start = System.currentTimeMillis();
            HttpRequest request = null;
            if(e.getMessage() instanceof HttpRequest) {
                request = this.request = (HttpRequest) e.getMessage();
            }
            // interceptOnRequestReceived(ctx,request);
            messageRecievedWithoutLogging(ctx, e);
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            logger.error("clientIP:" + clientIP + ":MessageEvent:" + e.getRemoteAddress() + ":" + e.getMessage());
            throw ex;
        }
    }

    private void messageRecievedWithoutLogging(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        StringBuffer requestBuff = new StringBuffer();
        if(!readingChunks) {
            HttpRequest request = this.request = (HttpRequest) e.getMessage();

            if(logger.isDebugEnabled()) {
                logger.debug("Request URI accessed: " + request.getUri() + " channel " + e.getChannel());
            }

            if(request.isChunked()) {
                readingChunks = true;
            }
            else {
                ChannelBuffer content = request.getContent();
                if(content.readable()) {
                    String requestPayload = content.toString(CharsetUtil.UTF_8);
                    requestBuff.append(requestPayload);
                }

                // adds the request to the inmemory buffer and returns 200 OK response
                HttpResponse response = handleRequest(request.getUri(), requestBuff.toString(), e, request);
                // interceptOnRequestSucceed(ctx, response);
                writeResponse(e, response);

            }
        }
        else {
            HttpChunk chunk = (HttpChunk) e.getMessage();
            if(chunk.isLast()) {
                readingChunks = false;
                HttpChunkTrailer trailer = (HttpChunkTrailer) chunk;
                throw new RuntimeException("I shouldn't be here");
                // writeToQueue(null,requestBuff.toString());
            }
            else {
                String requestPayload = chunk.getContent().toString(CharsetUtil.UTF_8);
                requestBuff.append(requestPayload);
            }
        }
    }

    protected void writeResponse(MessageEvent e, HttpResponse response) {
        // Decide whether to close the connection or not.
        boolean keepAlive = isKeepAlive(request);

        if(keepAlive) {
            // Add 'Content-Length' header only for a keep-alive connection.
            response.setHeader(CONTENT_LENGTH, response.getContent().readableBytes());
        }

        // Encode the cookie.
        try {
            String cookieString = request.getHeader(COOKIE);
            if(cookieString != null) {
                CookieDecoder cookieDecoder = new CookieDecoder();
                Set<Cookie> cookies = cookieDecoder.decode(cookieString);
                if(!cookies.isEmpty()) {
                    // Reset the cookies if necessary.
                    CookieEncoder cookieEncoder = new CookieEncoder(true);
                    for(Cookie cookie : cookies) {
                        cookieEncoder.addCookie(cookie);
                        response.addHeader(SET_COOKIE, cookieEncoder.encode());
                    }
                }
            }
        }
        catch (Exception e1) {
            e1.printStackTrace();
            logger.error("Error setting cookie : " + e.getMessage(), e);
        }

        // Write the response.
        ChannelFuture future = e.getChannel().write(response);
        // logger.debug(response.toString());
        // Close the non-keep-alive connection after the write operation is done.
        if(!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }

    }

    protected abstract HttpResponse handleRequest(String requestUri, String requestPayload, MessageEvent event, HttpRequest request);
}
