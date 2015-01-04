/**
 * 
 */
package com.sv.http;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.springframework.context.ApplicationContext;

/**
 * @author vaibhav
 * 
 */
public class MyPipelineFactory implements ChannelPipelineFactory {

    private ApplicationContext applicationContext;

    /**
     * @param applicationContext
     */
    public MyPipelineFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.netty.channel.ChannelPipelineFactory#getPipeline()
     */
    @Override
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();

        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("aggregator", new HttpChunkAggregator(1048576));
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("deflater", new HttpContentCompressor());
        MyHttpRequestHandler channelHandler = new MyHttpRequestHandler();
        channelHandler.setApplicationContext(applicationContext);
        pipeline.addLast("handler", channelHandler);

        return pipeline;
    }

}
