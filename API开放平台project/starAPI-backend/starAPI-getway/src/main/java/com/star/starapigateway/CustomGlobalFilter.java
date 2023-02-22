package com.star.starapigateway;

import com.star.starapiclientsdk.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {


    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");
    private static final String INTERFACE_HOST = "http://localhost:8123";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // info print
        log.info("custom global filter");
        log.info("request ID: " + request.getId());
        log.info("request Path: " + request.getPath().value());
        log.info("request Method: " + request.getMethod());
        log.info("request Params: " + request.getQueryParams());
        String sourceAddress = request.getLocalAddress().getHostString();
        log.info("request address1: " + sourceAddress);
        log.info("request address2: " + request.getRemoteAddress());
        // black / white list
        ServerHttpResponse response = exchange.getResponse();
        if (!IP_WHITE_LIST.contains(sourceAddress)) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }

        // certificate
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");
        // todo 实际情况应该是去数据库中查是否已分配给用户
        if (!"star".equals(accessKey)){
            return handleNoAuth(response);
        }
        if (Long.parseLong(nonce) > 10000L) {
            return handleNoAuth(response);
        }
//         todo 时间和当前时间不能超过 5 分钟
        Long currentTime = System.currentTimeMillis() / 1000;
        final Long FIVE_MIN = 60 * 5L;
        if ((currentTime - Long.parseLong(timestamp)) >= FIVE_MIN) {
            return handleNoAuth(response);
        }
//         todo 实际情况中是从数据库中查出 secretKey
        String serverSign = SignUtils.genSign(body, "1234");
        if (!sign.equals(serverSign)) {
            throw new RuntimeException("FORBIDDEN");
        }


        // todo look for aksk in backend

//        Mono<Void> filter = chain.filter(exchange);

        // response log
        return handleResponse(exchange, chain);

    }

    @Override
    public int getOrder() {
        return -1;
    }


    public Mono<Void> handleNoAuth(ServerHttpResponse response){
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();

    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response){
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();

    }

    /**
     * Processing response
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain){
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据的工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // get response code (ok)
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 拼接字符串
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);//释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        String data = new String(content, StandardCharsets.UTF_8); //data
                                        sb2.append(data);
                                        // 打印日志
                                        log.info("Response result: " + data);
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            // 8. 调用失败，返回一个规范的错误码
                            log.error("<--- {} Response code Error", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange); // 降级处理返回数据
        } catch (Exception e) {
            log.error("Gateway processing error: " + e);
            return chain.filter(exchange);
        }

    }



}