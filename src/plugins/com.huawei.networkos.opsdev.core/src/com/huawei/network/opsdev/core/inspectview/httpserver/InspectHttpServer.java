package com.huawei.network.opsdev.core.inspectview.httpserver;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.BindException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.List;

import com.huawei.network.opsdev.core.inspectview.bean.HttpServiceRequestBean;
import com.huawei.network.opsdev.core.utils.ConsoleFactory;

public class InspectHttpServer implements Runnable {
    private int port;
    private List<HttpServiceRequestBean> beans;
    private HttpServiceRequestBean currentHttpServiceRequestBean;
    
    public InspectHttpServer(int port) {
        this.port = port;
    }

 
    public void run() {
        try {
            ServerSocket server = new ServerSocket(port);
            while (true) {
                try {
                    Socket socket = server.accept();
                    Thread thread = new RedirectThread(socket);
                    thread.start();
                    System.out.println("start");
                    ConsoleFactory.printToConsole("start",true);
                } catch (IOException e) {
                    e.printStackTrace();               
                }
            }
        } catch (BindException e) {
            System.err.println("Could not start server. Port Occupied");
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    class RedirectThread extends Thread {
        private Socket connection;

        RedirectThread(Socket s) {
            this.connection = s;
        }

        public void run() {
            try {
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "ASCII"));
                Reader in = new InputStreamReader(new BufferedInputStream(connection.getInputStream()));
                StringBuffer request = new StringBuffer(80);
                while (true) {
                    int c = in.read();
                    if (c == '\t' || c == '\n' || c == -1) {
                        break;
                    }
                    request.append((char) c);
                 
                }
                
//                char[] xx = new char[50];
//                
//                while(in.read(xx)!=-1){
//                    request.append(xx);
//                    xx=new char[50]; 
//                }
                String get = request.toString();

                BufferedReader bufferedReader = null;
                StringBuffer response =  new StringBuffer();
                System.out.println(request);
                ConsoleFactory.printToConsole(request.toString(),true);

                try {
                  bufferedReader = new BufferedReader(new InputStreamReader(
                          getResponseInputStream(get)));
                  String line;
                  if(bufferedReader!=null){
                      while ((line = bufferedReader.readLine()) != null) {
                          out.write(line+"\n");
                          response.append(line+"\n");
                      }
                   }
                  currentHttpServiceRequestBean.setResponseTime();
                  currentHttpServiceRequestBean.setResponse(response.toString());
                } catch (Exception e) {
                  e.printStackTrace();
                 
                }finally {
                    if(bufferedReader!=null){
                        bufferedReader.close();
                    }
                }
                out.flush();
            } catch (IOException e) {
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }

                } catch (IOException e2) {
                }
            }
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
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        

    }
    
    
    
    
    

    /**       * @param args       */
    public static void main(String[] args) {
        int thePort;
        String theSite;
        try {
            theSite = "http://www.csdn.net/";
            //如果结尾有'/'，则去除             
            if (theSite.endsWith("/")) {
                theSite = theSite.substring(0, theSite.length() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        try {
            thePort = 8000;
        } catch (Exception e) {
            e.printStackTrace();
            thePort = 80;
        }
        Thread t = new Thread(new InspectHttpServer(thePort));
        t.start();
    }
}