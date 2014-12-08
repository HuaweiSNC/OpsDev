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
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;

import javax.net.ssl.SSLServerSocketFactory;

import org.apache.http.ConnectionClosedException;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Header;
import org.apache.http.HttpConnectionFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpServerConnection;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultBHttpServerConnection;
import org.apache.http.impl.DefaultBHttpServerConnectionFactory;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import org.apache.http.protocol.UriHttpRequestHandlerMapper;
import org.apache.http.util.EntityUtils;

import com.huawei.network.opsdev.core.OpsDevice;
import com.huawei.network.opsdev.core.OpsRestCaller;
import com.huawei.network.opsdev.core.RetRpc;
import com.huawei.network.opsdev.core.utils.ConsoleFactory;
import com.huawei.tools.xml.config.OpsService;

/**
 * Basic, yet fully functional and spec compliant, HTTP/1.1 file server.
 */
public class ElementalHttpServer {

    private static int monitorPort;
    private static Thread t;
    private static Boolean isRun = false;
    private static HttpService httpService = null;
    private static ServerSocket serversocket = null;
    
    static {
        // 对符合的地址进行过滤，如果地址不符合，就不进行认证
        javax.net.ssl.HttpsURLConnection
                .setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {

                    public boolean verify(String hostname,
                            javax.net.ssl.SSLSession sslSession) {

                        // 这里可以放类似白名单的认证，如果没有在白名单中的，就不继续
                        if (hostname.equalsIgnoreCase(sslSession.getPeerHost())) {
                            return true;
                        }

                        return true;
                    }

                });
    }


    public static int getMonitorPort() {
        return monitorPort;
    }

    public static void setMonitorPort(int monitorPort) {
        ElementalHttpServer.monitorPort = monitorPort;
    }

    public static Boolean getIsRun() {
        return isRun;
    }

    public static void setIsRun(Boolean isRun) {
        ElementalHttpServer.isRun = isRun;
    }

    public static void main()
    {
        startServer(8099);
    }

    public static void closeServer() {

        isRun = false;

        // 如果是关闭状态
        try {

            serversocket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void startServer(int port) {

        // 设置端口号  后期需要对端口号进行判断是否已经被占用,并把端口号信息写进模板中
        isRun = true;
        

        // Set up the HTTP protocol processor
        HttpProcessor httpproc = HttpProcessorBuilder.create()
                .add(new ResponseDate())
                .add(new ResponseServer("Test/1.1"))
                .add(new ResponseContent())
                .add(new ResponseConnControl()).build();

        // Set up request handlers
        UriHttpRequestHandlerMapper reqistry = new UriHttpRequestHandlerMapper();
        reqistry.register("*", new HttpFileHandler(null));

        // Set up the HTTP service
        httpService = new HttpService(httpproc, reqistry);
        
        SSLServerSocketFactory sf = null;

        try {
            t = new RequestListenerThread(port, httpService, sf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        t.setDaemon(true);
        t.start();

    }

    static class HttpFileHandler implements HttpRequestHandler {

        private final String docRoot;

        public HttpFileHandler(final String docRoot) {
            super();
            this.docRoot = docRoot;
        }

        public synchronized void handle(
                final HttpRequest request,
                final HttpResponse response,
                final HttpContext context) throws HttpException, IOException {
            // 获取请求的方式
            String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
            
            // 获取头信息
            Header[] headers = request.getAllHeaders();
            

            //验证请求的方式是不是我们所以提供的方式
/*            if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST") && !method.equals("DELETE") && !method.equals("PUT") 
                    && !method.equals("GET")) {
                throw new MethodNotSupportedException(method + " method not supported");
            }*/
            HttpServiceRequestBean currHttpServiceRequestBean = (HttpServiceRequestBean) context
                    .getAttribute("currentRequestBean");

            String traget = request.getRequestLine().getUri();
            System.out.println(traget);
//            System.out.println(headers.length);
            ConsoleFactory.printToConsole(traget,true);
            
            String ip = null;
            String port = null;
            String protocal = null;
            String author = null;
            
            for(Header head : headers)
            {
                if ("server_ipaddress".equals(head.getName())) {
                    ip = head.getValue();
                }else if("server_port".equals(head.getName()))
                {
                    port = head.getValue();
                }else if("server_protocal".equals(head.getName()))
                {
                    protocal = head.getValue();
                }else if ("Authorization".equals(head.getName())) {
                    author = head.getValue();
                }
                
//                System.out.println(head.getName());
//                System.out.println(head.getValue());
            }

            //修改url
            
            String address = null;
            String url = traget;
            if ("http".equals(protocal)) {
                address = "http://" + ip + ":" + port + url;
            }else
            {
                address = "https://" + ip + ":" + port + url;
            }
            
            //currHttpServiceRequestBean.setUrl(address);
            HttpEntity entity = null;
            String requestLine = null;

            if (request instanceof HttpEntityEnclosingRequest) {

              
                HttpEntity oldentity = ((HttpEntityEnclosingRequest) request).getEntity();
                requestLine = EntityUtils.toString(oldentity);
                currHttpServiceRequestBean.setRequest(requestLine);
                       
                //byte[] entityContent = EntityUtils.toByteArray(entity);
                // System.out.println("Incoming entity content (bytes): " + entityContent.length);

            }

            // conn from ops 2.0
            RetRpc rpc = null;
            String errorMessage = "";
            try {
                if (!"".equals(OpsService.trustStore) && !"".equals(OpsService.storePassword)) {
                    System.setProperty("javax.net.ssl.trustStore", OpsService.trustStore);
                    System.setProperty("javax.net.ssl.trustStorePassword", OpsService.storePassword);
                }
                OpsDevice device = new OpsDevice();
                device.setPort(Integer.valueOf(port));
                device.setProtocal(protocal);
                device.setTrustStore(OpsService.trustStore);
                device.setStorePassword(OpsService.storePassword);
                OpsRestCaller ops = new OpsRestCaller(null);
                rpc = ops.connect(ip,  Integer.valueOf(port),  url, method,
                        requestLine, protocal, author );
 
                response.setStatusCode(rpc.getStatusCode());
                response.setEntity(new StringEntity(rpc.getContent()));
            }   catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                errorMessage = e.getMessage();
                response.setStatusCode(HttpStatus.SC_NOT_FOUND);
                errorMessage = "(500, 'Internal Server Error', '{\"rpc-error\": [ {\"error-url\": \""+ url + "\"}, {\"error-message\": \"" +errorMessage+ "\"}]}]}')";
                StringEntity entitys = new StringEntity(errorMessage, ContentType.create("text/html", "UTF-8"));
                response.setEntity(entitys);
            }
  
            /***
            url : address  
            service : json,xml
            operation : method
          currHttpServiceRequestBean.getResponseTime();
            currHttpServiceRequestBean.getElapsed();
            currHttpServiceRequestBean.getRequestTime();
              responseText
            */
            // response to netmonitor
            String responseText = EntityUtils.toString(response.getEntity());
            if (responseText != null && "{}".equals(responseText))
            {
                int statusCode = response.getStatusLine().getStatusCode();
                String reasonPhrase = response.getStatusLine().getReasonPhrase();
                responseText ="{\"rpc-success\": [{\"StatusCode\": \"" + statusCode + "\"}, {\"ReasonPhrase\": \""+reasonPhrase+ "\"} ]}]}";
            }
            
            currHttpServiceRequestBean.setResponseTime();
            currHttpServiceRequestBean.setElapsed();
            currHttpServiceRequestBean.setUrl(address);
            currHttpServiceRequestBean.setResponse(responseText);

            currHttpServiceRequestBean.setOperation(method);

            HttpServiceRequestBeanManager.addRequestBean(currHttpServiceRequestBean);

        }

    }
     
 
    static class RequestListenerThread extends Thread {

        private HttpConnectionFactory<DefaultBHttpServerConnection> connFactory;

        private HttpService httpService;

        public RequestListenerThread(
                int port,
                HttpService httpService,
                SSLServerSocketFactory sf) throws IOException {
            this.connFactory = DefaultBHttpServerConnectionFactory.INSTANCE;
            while (!createServerSocket(sf, port))
            {
                port++;
                if (port >= 65535) {
                    System.out.println("Now the port is already in use,port number add 1");
                    ConsoleFactory.printToConsole("Now the port is already in use,port number add 1",true);
                    break;
                }
            }
            monitorPort = port;
            this.httpService = httpService;
        }

        public boolean createServerSocket(SSLServerSocketFactory sf, int port)
        {
            try {
                serversocket = (sf != null) ? sf.createServerSocket(port) : new ServerSocket(port);
            } catch (Exception e) {
                e.printStackTrace();
                return false;

            }
            return true;
        }

        public void run() {
            System.out.println("Listening on port " + serversocket.getLocalPort());
            ConsoleFactory.printToConsole("Listening on port " + serversocket.getLocalPort(),true);
            while (isRun && !Thread.interrupted()) {
                try {
                    // Set up HTTP connection
                    Socket socket = serversocket.accept();
                    System.out.println("Incoming connection from " + socket.getInetAddress());
                    ConsoleFactory.printToConsole("Incoming connection from " + socket.getInetAddress(),true);
                    HttpServerConnection conn = this.connFactory.createConnection(socket);

                    // Start worker thread
                    Thread t = new WorkerThread(this.httpService, conn);
                    t.setDaemon(true);
                    t.start();
                } catch (InterruptedIOException ex) {
                    break;
                } catch (IOException e) {
                    System.err.println("I/O error initialising connection thread: "
                            + e.getMessage());
                    break;
                }
            }

        }
    }

    static class WorkerThread extends Thread {

        private final HttpService httpservice;
        private final HttpServerConnection conn;

        public WorkerThread(
                final HttpService httpservice,
                final HttpServerConnection conn) {
            super();
            this.httpservice = httpservice;
            this.conn = conn;
        }

        public void run() {
            //            ElementalHttpServer.currentHttpServiceRequestBean = new HttpServiceRequestBean();
            //            ElementalHttpServer.currentHttpServiceRequestBean.setRequestTime();
            HttpServiceRequestBean httpServiceRequestBean = HttpServiceRequestBeanManager.createCurrentBean();
            httpServiceRequestBean.setRequestTime();
            System.out.println("New connection thread");
            ConsoleFactory.printToConsole("New connection thread",true);
            HttpContext context = new BasicHttpContext(null);
            context.setAttribute("currentRequestBean", httpServiceRequestBean);
            try {
                if (!Thread.interrupted() && this.conn.isOpen()) {
                    this.httpservice.handleRequest(this.conn, context);
         
                }
            } catch (ConnectionClosedException ex) {
                System.err.println("Client closed connection");
            } catch (IOException ex) {
                System.err.println("I/O error: " + ex.getMessage());
            } catch (HttpException ex) {
                System.err.println("Unrecoverable HTTP protocol violation: " + ex.getMessage());
            } finally {
                try {
                    this.conn.shutdown();
                } catch (IOException ignore) {
                }
            }
        }

    }

}
