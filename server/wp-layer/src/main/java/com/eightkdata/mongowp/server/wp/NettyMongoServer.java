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


package com.eightkdata.mongowp.server.wp;

import com.eightkdata.mongowp.MongoConstants;
import com.eightkdata.mongowp.server.MongoServerConfig;
import com.eightkdata.mongowp.server.callback.RequestProcessor;
import com.eightkdata.mongowp.server.util.LengthFieldPrependerLittleEndian;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
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
import javax.inject.Inject;
import javax.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
//TODO: Modify this server to be able to stop the thread that runs it
public class NettyMongoServer extends AbstractIdleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyMongoServer.class);
	
    private final int port;
    private final RequestProcessor requestProcessor;
    private EventLoopGroup connectionGroup;
    private EventLoopGroup workerGroup;
    private final Provider<RequestMessageByteHandler> requestMessageByteHandlerProvider;
    private final Provider<ReplyMessageObjectHandler> replyMessageObjectHandler;

    @Inject
    public NettyMongoServer(MongoServerConfig mongoServerConfig, RequestProcessor requestProcessor,
            Provider<RequestMessageByteHandler> requestMessageByteHandlerProvider,
            Provider<ReplyMessageObjectHandler> replyMessageObjectHandler) {
        this.port = mongoServerConfig.getPort();
        this.requestProcessor = requestProcessor;
        this.requestMessageByteHandlerProvider = requestMessageByteHandlerProvider;
        this.replyMessageObjectHandler = replyMessageObjectHandler;
    }

    private void buildChildHandlerPipeline(ChannelPipeline pipeline) {
        pipeline.addLast(new LengthFieldBasedFrameDecoder(
                ByteOrder.LITTLE_ENDIAN, MongoConstants.MAX_MESSAGE_SIZE_BYTES, 0,
                MongoConstants.MESSAGE_LENGTH_FIELD_BYTES, -MongoConstants.MESSAGE_LENGTH_FIELD_BYTES,
                MongoConstants.MESSAGE_LENGTH_FIELD_BYTES, true
        ));
        pipeline.addLast(requestMessageByteHandlerProvider.get());
        pipeline.addLast(new LengthFieldPrependerLittleEndian(MongoConstants.MESSAGE_LENGTH_FIELD_BYTES, true));
        pipeline.addLast(replyMessageObjectHandler.get());
        pipeline.addLast(new RequestMessageObjectHandler(requestProcessor));
    }

    @Override
    protected void startUp() throws Exception {
        connectionGroup = new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("netty-connection-%d").build());
        workerGroup = new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("netty-worker-%d").build());

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

    @Override
    protected void shutDown() throws Exception {
        if (workerGroup != null) workerGroup.shutdownGracefully().syncUninterruptibly();
        if (connectionGroup != null) connectionGroup.shutdownGracefully().syncUninterruptibly();
    }
}
