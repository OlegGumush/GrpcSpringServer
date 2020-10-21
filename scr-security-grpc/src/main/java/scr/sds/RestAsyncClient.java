package scr.sds;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.Queue;
import java.util.concurrent.*;
import java.util.function.BiConsumer;

public class RestAsyncClient extends ChannelInitializer<SocketChannel> {

    private Bootstrap bootstrap;
    private final String address;
    private EventLoopGroup workerGroup;
    private final int port, timeoutSeconds, eventLoopSize;
    private final boolean ssl;
    private final Queue<ClientHandler> queue;

    public RestAsyncClient(String address, int port, int timeoutSeconds, boolean ssl, int eventLoopSize) {
        this.address = address;
        this.port = port;
        this.ssl = ssl;
        this.timeoutSeconds = timeoutSeconds;
        this.eventLoopSize = eventLoopSize;
        this.queue = new ConcurrentLinkedQueue<>();
    }

    public void start() {
        bootstrap = new Bootstrap();
        if (Epoll.isAvailable()) {
            workerGroup = new EpollEventLoopGroup(eventLoopSize);
            bootstrap.group(workerGroup);
            bootstrap.channel(EpollSocketChannel.class);
        } else {
            workerGroup = new NioEventLoopGroup(eventLoopSize);
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
        }
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeoutSeconds * 1000);
        bootstrap.handler(this);
    }

    public void stop() throws InterruptedException, ExecutionException, TimeoutException {
        queue.clear();
        if (workerGroup != null) {
            workerGroup.shutdownGracefully().get(10, TimeUnit.SECONDS);
        }
    }

    public void exchange(FullHttpRequest httpRequest, BiConsumer<HttpResponse, Throwable> actionCallback) {
        exchange(httpRequest, actionCallback, null);
    }

    public void exchange(FullHttpRequest httpRequest, BiConsumer<HttpResponse, Throwable> actionCallback, ExecutorService executorService) {
        if (bootstrap == null) {
            throw new RuntimeException("The http client has not been initialized");
        }

        httpRequest.headers().add("Host", address);
        int count = httpRequest.content() == null ? 0 : httpRequest.content().readableBytes();
        if (count > 0) {
            httpRequest.headers().add("Content-Length", httpRequest.content().readableBytes());
        }

        ClientHandler clientHandler = new ClientHandler(httpRequest, actionCallback, executorService);
        boolean added = queue.offer(clientHandler);
        if (!added) {
            throw new RuntimeException("The http client is full");
        }
        try {
            bootstrap.connect(address, port);
        } catch (RuntimeException ex) {
            queue.remove(clientHandler);
            throw ex;
        }
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ClientHandler clientHandler = queue.poll();
        if (clientHandler == null) {
            throw new RuntimeException("Unable to init SocketChannel of netty client");
        }
        ChannelPipeline p = socketChannel.pipeline();
        if (ssl) {
            SslContext sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
            p.addLast("ssl", sslCtx.newHandler(socketChannel.alloc()));
        }
        p.addLast("codec", new HttpClientCodec());
        p.addLast("timeout", new ReadTimeoutHandler(timeoutSeconds));
        p.addLast("aggregator", new HttpObjectAggregator(2097152));
        p.addLast("handler", clientHandler);
    }

}