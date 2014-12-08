/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package com.huawei.network.opsdev.core.inspectview.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.EnglishReasonPhraseCatalog;
import org.apache.http.impl.nio.DefaultHttpClientIODispatch;
import org.apache.http.impl.nio.DefaultHttpServerIODispatch;
import org.apache.http.impl.nio.pool.BasicNIOConnPool;
import org.apache.http.impl.nio.pool.BasicNIOPoolEntry;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.DefaultListeningIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.ContentEncoder;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.NHttpClientConnection;
import org.apache.http.nio.NHttpConnection;
import org.apache.http.nio.NHttpServerConnection;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.nio.pool.NIOConnFactory;
import org.apache.http.nio.protocol.BasicAsyncResponseProducer;
import org.apache.http.nio.protocol.HttpAsyncExchange;
import org.apache.http.nio.protocol.HttpAsyncRequestConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequestExecutor;
import org.apache.http.nio.protocol.HttpAsyncRequestHandler;
import org.apache.http.nio.protocol.HttpAsyncRequestHandlerMapper;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.nio.protocol.HttpAsyncRequester;
import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
import org.apache.http.nio.protocol.HttpAsyncResponseProducer;
import org.apache.http.nio.protocol.HttpAsyncService;
import org.apache.http.nio.protocol.UriHttpAsyncRequestHandlerMapper;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.nio.reactor.ListeningIOReactor;
import org.apache.http.pool.PoolStats;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

import com.huawei.network.opsdev.core.utils.ConsoleFactory;

/**
 * Asynchronous, fully streaming HTTP/1.1 reverse proxy.
 */
public class NHttpReverseProxy {
    private List<HttpServiceRequestBean> beans;
    private HttpServiceRequestBean currentHttpServiceRequestBean;
    
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: NHttpReverseProxy <hostname> [port]");
            ConsoleFactory.printToConsole("Usage: NHttpReverseProxy <hostname> [port]",true);
            System.exit(1);
        }
        URI uri = new URI(args[0]);
        int port = 8080;
        if (args.length > 1) {
            port = Integer.parseInt(args[1]);
        }

        // Target host
        HttpHost targetHost = new HttpHost(
                uri.getHost(),
                uri.getPort() > 0 ? uri.getPort() : 80,
                uri.getScheme() != null ? uri.getScheme() : "http");

        System.out.println("Reverse proxy to " + targetHost);
        ConsoleFactory.printToConsole("Reverse proxy to " + targetHost,true);

        IOReactorConfig config = IOReactorConfig.custom()
            .setIoThreadCount(1)
            .setSoTimeout(3000)
            .setConnectTimeout(3000)
            .build();
        final ConnectingIOReactor connectingIOReactor = new DefaultConnectingIOReactor(config);
        final ListeningIOReactor listeningIOReactor = new DefaultListeningIOReactor(config);

        // Set up HTTP protocol processor for incoming connections
        HttpProcessor inhttpproc = new ImmutableHttpProcessor(
                new HttpResponseInterceptor[] {
                        new ResponseDate(),
                        new ResponseServer("Test/1.1"),
                        new ResponseContent(),
                        new ResponseConnControl()
         });

        // Set up HTTP protocol processor for outgoing connections
        HttpProcessor outhttpproc = new ImmutableHttpProcessor(
                new HttpRequestInterceptor[] {
                        new RequestContent(),
                        new RequestTargetHost(),
                        new RequestConnControl(),
                        new RequestUserAgent("Test/1.1"),
                        new RequestExpectContinue(true)
        });

        ProxyClientProtocolHandler clientHandler = new ProxyClientProtocolHandler();
        HttpAsyncRequester executor = new HttpAsyncRequester(
                outhttpproc, new ProxyOutgoingConnectionReuseStrategy());

        ProxyConnPool connPool = new ProxyConnPool(connectingIOReactor, ConnectionConfig.DEFAULT);
        connPool.setMaxTotal(100);
        connPool.setDefaultMaxPerRoute(20);

        UriHttpAsyncRequestHandlerMapper handlerRegistry = new UriHttpAsyncRequestHandlerMapper();
        handlerRegistry.register("*", new ProxyRequestHandler(targetHost, executor, connPool));

        ProxyServiceHandler serviceHandler = new ProxyServiceHandler(
                inhttpproc,
                new ProxyIncomingConnectionReuseStrategy(),
                handlerRegistry);

        final IOEventDispatch connectingEventDispatch = new DefaultHttpClientIODispatch(
                clientHandler, ConnectionConfig.DEFAULT);

        final IOEventDispatch listeningEventDispatch = new DefaultHttpServerIODispatch(
                serviceHandler, ConnectionConfig.DEFAULT);

        Thread t = new Thread(new Runnable() {

            public void run() {
                try {
                    connectingIOReactor.execute(connectingEventDispatch);
                } catch (InterruptedIOException ex) {
                    System.err.println("Interrupted");
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        listeningIOReactor.shutdown();
                    } catch (IOException ex2) {
                        ex2.printStackTrace();
                    }
                }
            }

        });
        t.start();
        try {
            listeningIOReactor.listen(new InetSocketAddress(port));
            listeningIOReactor.execute(listeningEventDispatch);
        } catch (InterruptedIOException ex) {
            System.err.println("Interrupted");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                connectingIOReactor.shutdown();
            } catch (IOException ex2) {
                ex2.printStackTrace();
            }
        }
    }

    static class ProxyHttpExchange {

        private final ByteBuffer inBuffer;
        private final ByteBuffer outBuffer;

        private volatile String id;
        private volatile HttpHost target;
        private volatile HttpAsyncExchange responseTrigger;
        private volatile IOControl originIOControl;
        private volatile IOControl clientIOControl;
        private volatile HttpRequest request;
        private volatile boolean requestReceived;
        private volatile HttpResponse response;
        private volatile boolean responseReceived;
        private volatile Exception ex;

        public ProxyHttpExchange() {
            super();
            this.inBuffer = ByteBuffer.allocateDirect(10240);
            this.outBuffer = ByteBuffer.allocateDirect(10240);
        }

        public ByteBuffer getInBuffer() {
            return this.inBuffer;
        }

        public ByteBuffer getOutBuffer() {
            return this.outBuffer;
        }

        public String getId() {
            return this.id;
        }

        public void setId(final String id) {
            this.id = id;
        }

        public HttpHost getTarget() {
            return this.target;
        }

        public void setTarget(final HttpHost target) {
            this.target = target;
        }

        public HttpRequest getRequest() {
            return this.request;
        }

        public void setRequest(final HttpRequest request) {
            this.request = request;
        }

        public HttpResponse getResponse() {
            return this.response;
        }

        public void setResponse(final HttpResponse response) {
            this.response = response;
        }

        public HttpAsyncExchange getResponseTrigger() {
            return this.responseTrigger;
        }

        public void setResponseTrigger(final HttpAsyncExchange responseTrigger) {
            this.responseTrigger = responseTrigger;
        }

        public IOControl getClientIOControl() {
            return this.clientIOControl;
        }

        public void setClientIOControl(final IOControl clientIOControl) {
            this.clientIOControl = clientIOControl;
        }

        public IOControl getOriginIOControl() {
            return this.originIOControl;
        }

        public void setOriginIOControl(final IOControl originIOControl) {
            this.originIOControl = originIOControl;
        }

        public boolean isRequestReceived() {
            return this.requestReceived;
        }

        public void setRequestReceived() {
            this.requestReceived = true;
        }

        public boolean isResponseReceived() {
            return this.responseReceived;
        }

        public void setResponseReceived() {
            this.responseReceived = true;
        }

        public Exception getException() {
            return this.ex;
        }

        public void setException(final Exception ex) {
            this.ex = ex;
        }

        public void reset() {
            this.inBuffer.clear();
            this.outBuffer.clear();
            this.target = null;
            this.id = null;
            this.responseTrigger = null;
            this.clientIOControl = null;
            this.originIOControl = null;
            this.request = null;
            this.requestReceived = false;
            this.response = null;
            this.responseReceived = false;
            this.ex = null;
        }

    }

    static class ProxyRequestHandler implements HttpAsyncRequestHandler<ProxyHttpExchange> {

        private final HttpHost target;
        private final HttpAsyncRequester executor;
        private final BasicNIOConnPool connPool;
        private final AtomicLong counter;

        public ProxyRequestHandler(
                final HttpHost target,
                final HttpAsyncRequester executor,
                final BasicNIOConnPool connPool) {
            super();
            this.target = target;
            this.executor = executor;
            this.connPool = connPool;
            this.counter = new AtomicLong(1);
        }

        public HttpAsyncRequestConsumer<ProxyHttpExchange> processRequest(
                final HttpRequest request,
                final HttpContext context) {
            ProxyHttpExchange httpExchange = (ProxyHttpExchange) context.getAttribute("http-exchange");
            if (httpExchange == null) {
                httpExchange = new ProxyHttpExchange();
                context.setAttribute("http-exchange", httpExchange);
            }
            synchronized (httpExchange) {
                httpExchange.reset();
                String id = String.format("%08X", this.counter.getAndIncrement());
                httpExchange.setId(id);
                httpExchange.setTarget(this.target);
                return new ProxyRequestConsumer(httpExchange, this.executor, this.connPool);
            }
        }

        public void handle(
                final ProxyHttpExchange httpExchange,
                final HttpAsyncExchange responseTrigger,
                final HttpContext context) throws HttpException, IOException {
            synchronized (httpExchange) {
                Exception ex = httpExchange.getException();
                if (ex != null) {
                    System.out.println("[client<-proxy] " + httpExchange.getId() + " " + ex);
                    ConsoleFactory.printToConsole("[client<-proxy] " + httpExchange.getId() + " " + ex,true);
                    int status = HttpStatus.SC_INTERNAL_SERVER_ERROR;
                    HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_0, status,
                            EnglishReasonPhraseCatalog.INSTANCE.getReason(status, Locale.US));
                    String message = ex.getMessage();
                    if (message == null) {
                        message = "Unexpected error";
                    }
                    response.setEntity(new NStringEntity(message, ContentType.DEFAULT_TEXT));
                    responseTrigger.submitResponse(new BasicAsyncResponseProducer(response));
                    System.out.println("[client<-proxy] " + httpExchange.getId() + " error response triggered");
                    ConsoleFactory.printToConsole("[client<-proxy] " + httpExchange.getId() + " error response triggered",true);
                }
                HttpResponse response = httpExchange.getResponse();
                if (response != null) {
                    responseTrigger.submitResponse(new ProxyResponseProducer(httpExchange));
                    System.out.println("[client<-proxy] " + httpExchange.getId() + " response triggered");
                    ConsoleFactory.printToConsole("[client<-proxy] " + httpExchange.getId() + " response triggered",true);
                }
                // No response yet.
                httpExchange.setResponseTrigger(responseTrigger);
            }
        }

    }

    static class ProxyRequestConsumer implements HttpAsyncRequestConsumer<ProxyHttpExchange> {

        private final ProxyHttpExchange httpExchange;
        private final HttpAsyncRequester executor;
        private final BasicNIOConnPool connPool;

        private volatile boolean completed;

        public ProxyRequestConsumer(
                final ProxyHttpExchange httpExchange,
                final HttpAsyncRequester executor,
                final BasicNIOConnPool connPool) {
            super();
            this.httpExchange = httpExchange;
            this.executor = executor;
            this.connPool = connPool;
        }

        public void close() throws IOException {
        }

        public void requestReceived(final HttpRequest request) {
            synchronized (this.httpExchange) {
                System.out.println("[client->proxy] " + this.httpExchange.getId() + " " + request.getRequestLine());
                ConsoleFactory.printToConsole("[client->proxy] " + this.httpExchange.getId() + " " + request.getRequestLine(),true);
                this.httpExchange.setRequest(request);
                this.executor.execute(
                        new ProxyRequestProducer(this.httpExchange),
                        new ProxyResponseConsumer(this.httpExchange),
                        this.connPool);
            }
        }

        public void consumeContent(
                final ContentDecoder decoder, final IOControl ioctrl) throws IOException {
            synchronized (this.httpExchange) {
                this.httpExchange.setClientIOControl(ioctrl);
                // Receive data from the client
                ByteBuffer buf = this.httpExchange.getInBuffer();
                int n = decoder.read(buf);
                System.out.println("[client->proxy] " + this.httpExchange.getId() + " " + n + " bytes read");
                ConsoleFactory.printToConsole("[client->proxy] " + this.httpExchange.getId() + " " + n + " bytes read",true);
                if (decoder.isCompleted()) {
                    System.out.println("[client->proxy] " + this.httpExchange.getId() + " content fully read");
                    ConsoleFactory.printToConsole("[client->proxy] " + this.httpExchange.getId() + " content fully read",true);
                }
                // If the buffer is full, suspend client input until there is free
                // space in the buffer
                if (!buf.hasRemaining()) {
                    ioctrl.suspendInput();
                    System.out.println("[client->proxy] " + this.httpExchange.getId() + " suspend client input");
                    ConsoleFactory.printToConsole("[client->proxy] " + this.httpExchange.getId() + " suspend client input",true);
                }
                // If there is some content in the input buffer make sure origin
                // output is active
                if (buf.position() > 0) {
                    if (this.httpExchange.getOriginIOControl() != null) {
                        this.httpExchange.getOriginIOControl().requestOutput();
                        System.out.println("[client->proxy] " + this.httpExchange.getId() + " request origin output");
                        ConsoleFactory.printToConsole("[client->proxy] " + this.httpExchange.getId() + " request origin output",true);
                    }
                }
            }
        }

        public void requestCompleted(final HttpContext context) {
            synchronized (this.httpExchange) {
                this.completed = true;;
                System.out.println("[client->proxy] " + this.httpExchange.getId() + " request completed");
                ConsoleFactory.printToConsole("[client->proxy] " + this.httpExchange.getId() + " request completed",true);
                this.httpExchange.setRequestReceived();
                if (this.httpExchange.getOriginIOControl() != null) {
                    this.httpExchange.getOriginIOControl().requestOutput();
                    System.out.println("[client->proxy] " + this.httpExchange.getId() + " request origin output");
                    ConsoleFactory.printToConsole("[client->proxy] " + this.httpExchange.getId() + " request origin output",true);
                }
            }
        }

        public Exception getException() {
            return null;
        }

        public ProxyHttpExchange getResult() {
            return this.httpExchange;
        }

        public boolean isDone() {
            return this.completed;
        }

        public void failed(final Exception ex) {
            System.out.println("[client->proxy] " + ex.toString());
            ConsoleFactory.printToConsole("[client->proxy] " + ex.toString(),true);
        }

    }

    static class ProxyRequestProducer implements HttpAsyncRequestProducer {

        private final ProxyHttpExchange httpExchange;

        public ProxyRequestProducer(final ProxyHttpExchange httpExchange) {
            super();
            this.httpExchange = httpExchange;
        }

        public void close() throws IOException {
        }

        public HttpHost getTarget() {
            synchronized (this.httpExchange) {
                return this.httpExchange.getTarget();
            }
        }

        public HttpRequest generateRequest() {
            synchronized (this.httpExchange) {
                HttpRequest request = this.httpExchange.getRequest();
                System.out.println("[proxy->origin] " + this.httpExchange.getId() + " " + request.getRequestLine());
                ConsoleFactory.printToConsole("[proxy->origin] " + this.httpExchange.getId() + " " + request.getRequestLine(),true);
                // Rewrite request!!!!
                if (request instanceof HttpEntityEnclosingRequest) {
                    BasicHttpEntityEnclosingRequest r = new BasicHttpEntityEnclosingRequest(
                            request.getRequestLine());
                    r.setEntity(((HttpEntityEnclosingRequest) request).getEntity());
                    return r;
                } else {
                    return new BasicHttpRequest(request.getRequestLine());
                }
            }
        }

        public void produceContent(
                final ContentEncoder encoder, final IOControl ioctrl) throws IOException {
            synchronized (this.httpExchange) {
                this.httpExchange.setOriginIOControl(ioctrl);
                // Send data to the origin server
                ByteBuffer buf = this.httpExchange.getInBuffer();
                buf.flip();
                int n = encoder.write(buf);
                buf.compact();
                System.out.println("[proxy->origin] " + this.httpExchange.getId() + " " + n + " bytes written");
                ConsoleFactory.printToConsole("[proxy->origin] " + this.httpExchange.getId() + " " + n + " bytes written",true);
                // If there is space in the buffer and the message has not been
                // transferred, make sure the client is sending more data
                if (buf.hasRemaining() && !this.httpExchange.isRequestReceived()) {
                    if (this.httpExchange.getClientIOControl() != null) {
                        this.httpExchange.getClientIOControl().requestInput();
                        System.out.println("[proxy->origin] " + this.httpExchange.getId() + " request client input");
                        ConsoleFactory.printToConsole("[proxy->origin] " + this.httpExchange.getId() + " request client input",true);
                    }
                }
                if (buf.position() == 0) {
                    if (this.httpExchange.isRequestReceived()) {
                        encoder.complete();
                        System.out.println("[proxy->origin] " + this.httpExchange.getId() + " content fully written");
                        ConsoleFactory.printToConsole("[proxy->origin] " + this.httpExchange.getId() + " content fully written",true);
                    } else {
                        // Input buffer is empty. Wait until the client fills up
                        // the buffer
                        ioctrl.suspendOutput();
                        System.out.println("[proxy->origin] " + this.httpExchange.getId() + " suspend origin output");
                        ConsoleFactory.printToConsole("[proxy->origin] " + this.httpExchange.getId() + " suspend origin output",true);
                    }
                }
            }
        }

        public void requestCompleted(final HttpContext context) {
            synchronized (this.httpExchange) {
                System.out.println("[proxy->origin] " + this.httpExchange.getId() + " request completed");
                ConsoleFactory.printToConsole("[proxy->origin] " + this.httpExchange.getId() + " request completed",true);
            }
        }

        public boolean isRepeatable() {
            return false;
        }

        public void resetRequest() {
        }

        public void failed(final Exception ex) {
            System.out.println("[proxy->origin] " + ex.toString());
            ConsoleFactory.printToConsole("[proxy->origin] " + ex.toString(),true);
        }

    }

    static class ProxyResponseConsumer implements HttpAsyncResponseConsumer<ProxyHttpExchange> {

        private final ProxyHttpExchange httpExchange;

        private volatile boolean completed;

        public ProxyResponseConsumer(final ProxyHttpExchange httpExchange) {
            super();
            this.httpExchange = httpExchange;
        }

        public void close() throws IOException {
        }

        public void responseReceived(final HttpResponse response) {
            synchronized (this.httpExchange) {
                System.out.println("[proxy<-origin] " + this.httpExchange.getId() + " " + response.getStatusLine());
                ConsoleFactory.printToConsole("[proxy<-origin] " + this.httpExchange.getId() + " " + response.getStatusLine(),true);
                this.httpExchange.setResponse(response);
                HttpAsyncExchange responseTrigger = this.httpExchange.getResponseTrigger();
                if (responseTrigger != null && !responseTrigger.isCompleted()) {
                    System.out.println("[client<-proxy] " + this.httpExchange.getId() + " response triggered");
                    ConsoleFactory.printToConsole("[client<-proxy] " + this.httpExchange.getId() + " response triggered",true);
                    responseTrigger.submitResponse(new ProxyResponseProducer(this.httpExchange));
                }
            }
        }

        public void consumeContent(
                final ContentDecoder decoder, final IOControl ioctrl) throws IOException {
            synchronized (this.httpExchange) {
                this.httpExchange.setOriginIOControl(ioctrl);
                // Receive data from the origin
                ByteBuffer buf = this.httpExchange.getOutBuffer();
                int n = decoder.read(buf);
                System.out.println("[proxy<-origin] " + this.httpExchange.getId() + " " + n + " bytes read");
                ConsoleFactory.printToConsole("[proxy<-origin] " + this.httpExchange.getId() + " " + n + " bytes read",true);
                if (decoder.isCompleted()) {
                    System.out.println("[proxy<-origin] " + this.httpExchange.getId() + " content fully read");
                    ConsoleFactory.printToConsole("[proxy<-origin] " + this.httpExchange.getId() + " content fully read",true);
                }
                // If the buffer is full, suspend origin input until there is free
                // space in the buffer
                if (!buf.hasRemaining()) {
                    ioctrl.suspendInput();
                    System.out.println("[proxy<-origin] " + this.httpExchange.getId() + " suspend origin input");
                    ConsoleFactory.printToConsole("[proxy<-origin] " + this.httpExchange.getId() + " suspend origin input",true);
                }
                // If there is some content in the input buffer make sure client
                // output is active
                if (buf.position() > 0) {
                    if (this.httpExchange.getClientIOControl() != null) {
                        this.httpExchange.getClientIOControl().requestOutput();
                        System.out.println("[proxy<-origin] " + this.httpExchange.getId() + " request client output");
                        ConsoleFactory.printToConsole("[proxy<-origin] " + this.httpExchange.getId() + " request client output",true);
                    }
                }
            }
        }

        public void responseCompleted(final HttpContext context) {
            synchronized (this.httpExchange) {
                if (this.completed) {
                    return;
                }
                this.completed = true;
                System.out.println("[proxy<-origin] " + this.httpExchange.getId() + " response completed");
                ConsoleFactory.printToConsole("[proxy<-origin] " + this.httpExchange.getId() + " response completed",true);
                this.httpExchange.setResponseReceived();
                if (this.httpExchange.getClientIOControl() != null) {
                    this.httpExchange.getClientIOControl().requestOutput();
                    System.out.println("[proxy<-origin] " + this.httpExchange.getId() + " request client output");
                    ConsoleFactory.printToConsole("[proxy<-origin] " + this.httpExchange.getId() + " request client output",true);
                }
            }
        }

        public void failed(final Exception ex) {
            synchronized (this.httpExchange) {
                if (this.completed) {
                    return;
                }
                this.completed = true;
                this.httpExchange.setException(ex);
                HttpAsyncExchange responseTrigger = this.httpExchange.getResponseTrigger();
                if (responseTrigger != null && !responseTrigger.isCompleted()) {
                    System.out.println("[client<-proxy] " + this.httpExchange.getId() + " " + ex);
                    ConsoleFactory.printToConsole("[client<-proxy] " + this.httpExchange.getId() + " " + ex,true);
                    int status = HttpStatus.SC_INTERNAL_SERVER_ERROR;
                    HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_0, status,
                            EnglishReasonPhraseCatalog.INSTANCE.getReason(status, Locale.US));
                    String message = ex.getMessage();
                    if (message == null) {
                        message = "Unexpected error";
                    }
                    response.setEntity(new NStringEntity(message, ContentType.DEFAULT_TEXT));
                    responseTrigger.submitResponse(new BasicAsyncResponseProducer(response));
                }
            }
        }

        public boolean cancel() {
            synchronized (this.httpExchange) {
                if (this.completed) {
                    return false;
                }
                failed(new InterruptedIOException("Cancelled"));
                return true;
            }
        }

        public ProxyHttpExchange getResult() {
            return this.httpExchange;
        }

        public Exception getException() {
            return null;
        }

        public boolean isDone() {
            return this.completed;
        }

    }

    static class ProxyResponseProducer implements HttpAsyncResponseProducer {

        private final ProxyHttpExchange httpExchange;

        public ProxyResponseProducer(final ProxyHttpExchange httpExchange) {
            super();
            this.httpExchange = httpExchange;
        }

        public void close() throws IOException {
            this.httpExchange.reset();
        }

        public HttpResponse generateResponse() {
            synchronized (this.httpExchange) {
                HttpResponse response = this.httpExchange.getResponse();
                System.out.println("[client<-proxy] " + this.httpExchange.getId() + " " + response.getStatusLine());
                ConsoleFactory.printToConsole("[client<-proxy] " + this.httpExchange.getId() + " " + response.getStatusLine(),true);
                // Rewrite response!!!!
                BasicHttpResponse r = new BasicHttpResponse(response.getStatusLine());
                r.setEntity(response.getEntity());
                return r;
            }
        }

        public void produceContent(
                final ContentEncoder encoder, final IOControl ioctrl) throws IOException {
            synchronized (this.httpExchange) {
                this.httpExchange.setClientIOControl(ioctrl);
                // Send data to the client
                ByteBuffer buf = this.httpExchange.getOutBuffer();
                buf.flip();
                int n = encoder.write(buf);
                buf.compact();
                System.out.println("[client<-proxy] " + this.httpExchange.getId() + " " + n + " bytes written");
                ConsoleFactory.printToConsole("[client<-proxy] " + this.httpExchange.getId() + " " + n + " bytes written",true);
                // If there is space in the buffer and the message has not been
                // transferred, make sure the origin is sending more data
                if (buf.hasRemaining() && !this.httpExchange.isResponseReceived()) {
                    if (this.httpExchange.getOriginIOControl() != null) {
                        this.httpExchange.getOriginIOControl().requestInput();
                        System.out.println("[client<-proxy] " + this.httpExchange.getId() + " request origin input");
                        ConsoleFactory.printToConsole("[client<-proxy] " + this.httpExchange.getId() + " request origin input",true);
                    }
                }
                if (buf.position() == 0) {
                    if (this.httpExchange.isResponseReceived()) {
                        encoder.complete();
                        System.out.println("[client<-proxy] " + this.httpExchange.getId() + " content fully written");
                        ConsoleFactory.printToConsole("[client<-proxy] " + this.httpExchange.getId() + " content fully written",true);
                    } else {
                        // Input buffer is empty. Wait until the origin fills up
                        // the buffer
                        ioctrl.suspendOutput();
                        System.out.println("[client<-proxy] " + this.httpExchange.getId() + " suspend client output");
                        ConsoleFactory.printToConsole("[client<-proxy] " + this.httpExchange.getId() + " suspend client output",true);
                    }
                }
            }
        }

        public void responseCompleted(final HttpContext context) {
            synchronized (this.httpExchange) {
                System.out.println("[client<-proxy] " + this.httpExchange.getId() + " response completed");
                ConsoleFactory.printToConsole("[client<-proxy] " + this.httpExchange.getId() + " response completed",true);
            }
        }

        public void failed(final Exception ex) {
            System.out.println("[client<-proxy] " + ex.toString());
            ConsoleFactory.printToConsole("[client<-proxy] " + ex.toString(),true);
        }

    }

    static class ProxyIncomingConnectionReuseStrategy extends DefaultConnectionReuseStrategy {

       
        public boolean keepAlive(final HttpResponse response, final HttpContext context) {
            NHttpConnection conn = (NHttpConnection) context.getAttribute(
                    HttpCoreContext.HTTP_CONNECTION);
            boolean keepAlive = super.keepAlive(response, context);
            if (keepAlive) {
                System.out.println("[client->proxy] connection kept alive " + conn);
                ConsoleFactory.printToConsole("[client->proxy] connection kept alive " + conn,true);
            }
            return keepAlive;
        }

    };

    static class ProxyOutgoingConnectionReuseStrategy extends DefaultConnectionReuseStrategy {

        
        public boolean keepAlive(final HttpResponse response, final HttpContext context) {
            NHttpConnection conn = (NHttpConnection) context.getAttribute(
                    HttpCoreContext.HTTP_CONNECTION);
            boolean keepAlive = super.keepAlive(response, context);
            if (keepAlive) {
                System.out.println("[proxy->origin] connection kept alive " + conn);
                ConsoleFactory.printToConsole("[proxy->origin] connection kept alive " + conn,true);
            }
            return keepAlive;
        }

    };

    static class ProxyServiceHandler extends HttpAsyncService {

        public ProxyServiceHandler(
                final HttpProcessor httpProcessor,
                final ConnectionReuseStrategy reuseStrategy,
                final HttpAsyncRequestHandlerMapper handlerResolver) {
            super(httpProcessor, reuseStrategy, null, handlerResolver, null);
        }

     
        protected void log(final Exception ex) {
            ex.printStackTrace();
        }

      
        public void connected(final NHttpServerConnection conn) {
            System.out.println("[client->proxy] connection open " + conn);
            ConsoleFactory.printToConsole("[client->proxy] connection open " + conn,true);
            super.connected(conn);
        }

       
        public void closed(final NHttpServerConnection conn) {
            System.out.println("[client->proxy] connection closed " + conn);
            ConsoleFactory.printToConsole("[client->proxy] connection closed " + conn,true);
            super.closed(conn);
        }

    }

    static class ProxyClientProtocolHandler extends HttpAsyncRequestExecutor {

        public ProxyClientProtocolHandler() {
            super();
        }

       
        protected void log(final Exception ex) {
            ex.printStackTrace();
        }

     
        public void connected(final NHttpClientConnection conn,
                final Object attachment) throws IOException, HttpException {
            System.out.println("[proxy->origin] connection open " + conn);
            ConsoleFactory.printToConsole("[proxy->origin] connection open " + conn,true);
            super.connected(conn, attachment);
        }

       
        public void closed(final NHttpClientConnection conn) {
            System.out.println("[proxy->origin] connection closed " + conn);
            ConsoleFactory.printToConsole("[proxy->origin] connection closed " + conn,true);
            super.closed(conn);
        }

    }

    static class ProxyConnPool extends BasicNIOConnPool {

        public ProxyConnPool(
                final ConnectingIOReactor ioreactor,
                final ConnectionConfig config) {
            super(ioreactor, config);
        }

        public ProxyConnPool(
                final ConnectingIOReactor ioreactor,
                final NIOConnFactory<HttpHost, NHttpClientConnection> connFactory,
                final int connectTimeout) {
            super(ioreactor, connFactory, connectTimeout);
        }

        
        public void release(final BasicNIOPoolEntry entry, boolean reusable) {
            System.out.println("[proxy->origin] connection released " + entry.getConnection());
            ConsoleFactory.printToConsole("[proxy->origin] connection released " + entry.getConnection(),true);
            super.release(entry, reusable);
            StringBuilder buf = new StringBuilder();
            PoolStats totals = getTotalStats();
            buf.append("[total kept alive: ").append(totals.getAvailable()).append("; ");
            buf.append("total allocated: ").append(totals.getLeased() + totals.getAvailable());
            buf.append(" of ").append(totals.getMax()).append("]");
            System.out.println("[proxy->origin] " + buf.toString());
            ConsoleFactory.printToConsole("[proxy->origin] " + buf.toString(),true);
        }

    }
    
    public InputStream getResponseInputStream(String urlStr) {
        URL url = null;
        HttpURLConnection conn = null;
        currentHttpServiceRequestBean = new HttpServiceRequestBean();

            if (urlStr.trim().startsWith("PUT")) {
                try {
                    try {
                        currentHttpServiceRequestBean.setOperation("PUT");
                        urlStr = urlStr.replace("PUT", "").trim();
                        url = new URL("http://"+urlStr.substring(0,urlStr.lastIndexOf(" ")));
                        currentHttpServiceRequestBean.setUrl(url.toString());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                      
                    }
                    
                    try {
                        currentHttpServiceRequestBean.setRequestTime();
                        conn = (HttpURLConnection) url.openConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                     
                    }
                    conn.setRequestMethod("PUT");
                } catch (ProtocolException e) {
                    e.printStackTrace();

                }
            } else if (urlStr.trim().startsWith("DELETE")) {
                try {

                    try {
                        currentHttpServiceRequestBean.setOperation("DELETE");
                        urlStr = urlStr.replace("DELETE", "").trim();
                        url = new URL("http://"+urlStr.substring(0,urlStr.lastIndexOf(" ")));
                        currentHttpServiceRequestBean.setUrl(url.toString());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                      
                    }
                   
                    try {
                        currentHttpServiceRequestBean.setRequestTime();
                        conn = (HttpURLConnection) url.openConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                     
                    }
                    conn.setRequestMethod("DELETE");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                  
                }
            } else if (urlStr.trim().startsWith("GET")) {
                try {

                    try {
                        currentHttpServiceRequestBean.setOperation("GET");
                        urlStr = urlStr.replace("GET", "").trim();
                        url = new URL("http://"+urlStr.substring(0,urlStr.lastIndexOf(" ")));
                        currentHttpServiceRequestBean.setUrl(url.toString());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                      
                    }
                   
                    try {
                        currentHttpServiceRequestBean.setRequestTime();
                        conn = (HttpURLConnection) url.openConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                     
                    }
                    conn.setRequestMethod("GET");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                    
                }
            } else if(urlStr.trim().startsWith("POST")){
                try {

                    try {
                        currentHttpServiceRequestBean.setOperation("POST");
                        urlStr = urlStr.replace("POST", "").trim();
                        url = new URL("http://"+urlStr.substring(0,urlStr.lastIndexOf(" ")));
                        currentHttpServiceRequestBean.setUrl(url.toString());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                      
                    }
                    
                    try {
                        currentHttpServiceRequestBean.setRequestTime();
                        conn = (HttpURLConnection) url.openConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                     
                    }
                    conn.setRequestMethod("POST");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                   
                }
            }
            try {
                if(conn.getErrorStream()==null){
                    return conn.getInputStream();
                }else{
                    return conn.getErrorStream();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        

    }


}
