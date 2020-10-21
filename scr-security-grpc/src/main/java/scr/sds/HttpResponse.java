package scr.sds;

import io.netty.handler.codec.http.HttpHeaders;

public class HttpResponse {

    private final int status;
    private final HttpHeaders httpHeaders;
    private final byte[] content;

    public HttpResponse(int status, HttpHeaders httpHeaders, byte[] content) {
        this.status = status;
        this.httpHeaders = httpHeaders;
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public byte[] getContent() {
        return content;
    }
}
