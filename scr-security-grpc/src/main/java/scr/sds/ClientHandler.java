package scr.sds;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.ReferenceCountUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.BiConsumer;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private final FullHttpRequest httpRequest;
    private final BiConsumer<HttpResponse, Throwable> actionCallback;
    private boolean communicated;
    private final ExecutorService executorService;

    public ClientHandler(FullHttpRequest httpRequest, BiConsumer<HttpResponse, Throwable> actionCallback, ExecutorService executorService) {
        this.httpRequest = httpRequest;
        this.actionCallback = actionCallback;
        this.communicated = false;
        this.executorService = executorService;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(httpRequest);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            FullHttpResponse fullHttpResponse = (FullHttpResponse) msg;
            ByteBuf buf = fullHttpResponse.content();
            byte[] content = new byte[buf.readableBytes()];
            buf.readBytes(content);
            submit(new HttpResponse(fullHttpResponse.status().code(), fullHttpResponse.headers(), content), null);
        } finally {
            this.communicated = true;
            ReferenceCountUtil.release(msg);
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        try {
            submit(null, cause);
        } finally {
            this.communicated = true;
            ctx.close();
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        if (this.communicated) {
            return;
        }
        submit(null, new RuntimeException("Server unreachable"));
    }

    private void submit(HttpResponse response, Throwable th) {
        try {
            if (executorService != null) {
                executorService.submit(() -> execute(response, th));
            } else {
                execute(response, th);
            }
        } catch (RejectedExecutionException ex) {
            execute(null, ex);
        }
    }

    private void execute(HttpResponse response, Throwable th) {
        try {
            actionCallback.accept(response, th);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}