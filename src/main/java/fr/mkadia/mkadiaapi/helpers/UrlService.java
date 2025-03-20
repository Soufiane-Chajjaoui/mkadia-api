package fr.mkadia.mkadiaapi.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.catalina.connector.Connector;

@Service
public class UrlService {

    @Autowired
    private ServletWebServerApplicationContext appContext;

    public String getBaseUrl() throws UnknownHostException {
        WebServer webServer = appContext.getWebServer();

        if (webServer instanceof TomcatWebServer) {
            TomcatWebServer tomcatWebServer = (TomcatWebServer) webServer;
            Connector connector = tomcatWebServer.getTomcat().getConnector();

            String scheme = connector.getScheme();
            String ip = InetAddress.getLocalHost().getHostAddress();
            int port = connector.getPort();
            String contextPath = appContext.getServletContext().getContextPath();

            return STR."\{scheme}://\{ip}:\{port}\{contextPath}";
        }

        throw new IllegalStateException("Web server is not an instance of TomcatWebServer");
    }
}
