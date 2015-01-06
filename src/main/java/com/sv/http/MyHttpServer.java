/**
 * 
 */
package com.sv.http;

import com.sv.config.NettyConfig;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Netty Http Server
 * 
 * @author vaibhav
 * 
 */
public class MyHttpServer {

    private static final Logger           logger = LoggerFactory.getLogger(MyHttpServer.class.getCanonicalName());
    private ChannelGroup                  mqChannelGroup;
    private NioServerSocketChannelFactory serverSocketChannelFactory;
    private ApplicationContext            appContext;


    private void init() throws Exception {
        initSpringContext();

        System.out.println("Working dir : " + System.getProperty("user.dir"));
        System.out.println("Server started.");

        NettyConfig nettyConfig = appContext.getBean(NettyConfig.class);

        // Configure the server.
        serverSocketChannelFactory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool(), nettyConfig.getNumThreads());
        ServerBootstrap bootstrap = new ServerBootstrap(serverSocketChannelFactory);

        bootstrap.setPipelineFactory(new MyPipelineFactory(appContext));

        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
        bootstrap.setOption("child.reuseAddress", true);
        bootstrap.setOption("child.connectTimeoutMillis", 100);

        mqChannelGroup = new DefaultChannelGroup(MyHttpServer.class.getName());

        logger.info("SVConfig.getInstance().port : " + nettyConfig.getHttpport());

        // Bind and start to accept incoming connections.
        Channel channel = bootstrap.bind(new InetSocketAddress(nettyConfig.getHttpport()));
        mqChannelGroup.add(channel);

        logger.info("[sv-server] ready to accept connections on " + nettyConfig.getHttpport());

        logger.info("[sv-server] started.");
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                shutdown();
            }
        });

        // block the process
        // todo: we can add support to listen to a command and gracefully kill the server from
        // command line
        while(true) {
            try {
                System.in.read();
            }
            catch (Throwable e) {
                throw new Exception(e.getMessage(), e);
            }
        }
    }

    public void shutdown() {
        logger.info("[sv-server] Shutting down ...");
        ChannelGroupFuture future = mqChannelGroup.close();
        future.awaitUninterruptibly();
        logger.info("End of pipeline executor");
        serverSocketChannelFactory.releaseExternalResources();
        logger.info("[sv-server] Shutdown successful.");
    }

    private void initSpringContext() {
        try {
            String classpath = System.getProperty("java.class.path");
            System.out.println(" uri : " + System.getProperty("user.dir") + " , " + classpath);
            appContext = new ClassPathXmlApplicationContext("config/beans.xml");
            if(appContext instanceof AbstractApplicationContext) {
                AbstractApplicationContext abstractAppContext = (AbstractApplicationContext) appContext;
                abstractAppContext.registerShutdownHook();
            }
        }
        catch (Throwable e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * stand-alone test for netty io server
     * 
     * @param args
     */
    public static void main(String[] args) {
        MyHttpServer myServer = new MyHttpServer();
        try {
            myServer.init();
        }
        catch (Throwable e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        finally {
            myServer.shutdown();
        }
    }

}
