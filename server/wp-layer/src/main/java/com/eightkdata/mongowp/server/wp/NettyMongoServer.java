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
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import java.nio.ByteOrder;
import javax.inject.Inject;
import javax.inject.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
//TODO: Modify this server to be able to stop the thread that runs it
public class NettyMongoServer extends AbstractIdleService {
    private static final Logger LOGGER = LogManager.getLogger(NettyMongoServer.class);
	
    private final int port;
    private final RequestProcessor requestProcessor;
    private EventLoopGroup connectionGroup;
    private EventLoopGroup workerGroup;
    private final RequestMessageObjectHandler requestMessageObjectHandler;
    private final RequestMessageByteHandler requestMessageByteHandler;
    private final Provider<ReplyMessageObjectHandler> replyMessageObjectHandler;
    private final LengthFieldPrependerLittleEndian lengthFieldPrependerLittleEndian;

    @Inject
    public NettyMongoServer(MongoServerConfig mongoServerConfig, RequestProcessor requestProcessor,
            RequestMessageByteHandler requestMessageByteHandler,
            Provider<ReplyMessageObjectHandler> replyMessageObjectHandler,
            RequestMessageObjectHandler requestMessageObjectHandler) {
        this.lengthFieldPrependerLittleEndian = new LengthFieldPrependerLittleEndian(MongoConstants.MESSAGE_LENGTH_FIELD_BYTES, true);
        this.port = mongoServerConfig.getPort();
        this.requestProcessor = requestProcessor;
        this.requestMessageByteHandler = requestMessageByteHandler;
        this.replyMessageObjectHandler = replyMessageObjectHandler;
        this.requestMessageObjectHandler = requestMessageObjectHandler;
    }

    private void buildChildHandlerPipeline(ChannelPipeline pipeline) {
        pipeline.addLast(new LengthFieldBasedFrameDecoder(
                ByteOrder.LITTLE_ENDIAN, MongoConstants.MAX_MESSAGE_SIZE_BYTES, 0,
                MongoConstants.MESSAGE_LENGTH_FIELD_BYTES, -MongoConstants.MESSAGE_LENGTH_FIELD_BYTES,
                MongoConstants.MESSAGE_LENGTH_FIELD_BYTES, true
        ));
        pipeline.addLast(requestMessageByteHandler);
        pipeline.addLast(lengthFieldPrependerLittleEndian);
        pipeline.addLast(replyMessageObjectHandler.get());
        pipeline.addLast(requestMessageObjectHandler);
    }

    @Override
    protected void startUp() throws Exception {
        connectionGroup = new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("netty-connection-%d").build());
        workerGroup = new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("netty-worker-%d").build());

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(connectionGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
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
