/*
 *     This file is part of mongowp.
 *
 *     mongowp is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     mongowp is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with mongowp. If not, see <http://www.gnu.org/licenses/>.
 *
 *     Copyright (c) 2014, 8Kdata Technology
 *     
 */


package com.eightkdata.mongowp.mongoserver;

import com.eightkdata.mongowp.mongoserver.callback.RequestProcessor;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP;
import com.eightkdata.mongowp.mongoserver.util.LengthFieldPrependerLittleEndian;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import java.nio.ByteOrder;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
//TODO: Modify this server to be able to stop the thread that runs it
public class MongoServer implements RequestIdGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoServer.class);
	
    private final int port;
    private final RequestProcessor requestProcessor;
    private final AtomicInteger requestId;
    private EventLoopGroup connectionGroup;
    private EventLoopGroup workerGroup;

    @Inject
    public MongoServer(MongoServerConfig mongoServerConfig, RequestProcessor requestProcessor) {
        this.port = mongoServerConfig.getPort();
        this.requestProcessor = requestProcessor;
        requestId = new AtomicInteger(0);
    }

    @Override
    public int getNextRequestId() {
        return requestId.incrementAndGet();
    }

    private void buildChildHandlerPipeline(ChannelPipeline pipeline) {
        pipeline.addLast(new LengthFieldBasedFrameDecoder(
                ByteOrder.LITTLE_ENDIAN, MongoWP.MAX_MESSAGE_SIZE_BYTES, 0,
                MongoWP.MESSAGE_LENGTH_FIELD_BYTES, -MongoWP.MESSAGE_LENGTH_FIELD_BYTES,
                MongoWP.MESSAGE_LENGTH_FIELD_BYTES, true
        ));
        pipeline.addLast(new RequestMessageByteHandler());
        pipeline.addLast(new LengthFieldPrependerLittleEndian(MongoWP.MESSAGE_LENGTH_FIELD_BYTES, true));
        pipeline.addLast(new ReplyMessageObjectHandler(this));
        pipeline.addLast(new RequestMessageObjectHandler(requestProcessor));
    }

    public void run() {
        // TODO: provide custom ThreadFactories to the EventLoopGroup to name threads correctly?
        connectionGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(connectionGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        buildChildHandlerPipeline(socketChannel.pipeline());
                    }
                })
                // TODO: set TCP channel options?
                ;

        ChannelFuture channelFuture = bootstrap.bind(port).awaitUninterruptibly();
        if (!channelFuture.isSuccess()) {
            workerGroup.shutdownGracefully();
            connectionGroup.shutdownGracefully();
        }
    }
    
    public void stop() {
        if (workerGroup != null) workerGroup.shutdownGracefully().syncUninterruptibly();
        if (connectionGroup != null) connectionGroup.shutdownGracefully().syncUninterruptibly();
    }
}
