package com.github.adrianbk.stubby.standalone;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;
import org.apache.log4j.Logger;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.concurrent.Executor;

public class TrustedHttpsServerInstance extends ServerInstance {

    private static final Logger LOGGER = Logger.getLogger(TrustedHttpsServerInstance.class);

    private HttpsServer server;

    public TrustedHttpsServerInstance(int port, ServerHandler handler, Executor executor) throws IOException {
        this.server = HttpsServer.create(allInterfaces(port), SOCKET_BACKLOG);
        try {
            this.server.setHttpsConfigurator(new HttpsConfigurator(trustedContext()));
        } catch (Exception e) {
            String message = "Could not configure SSL context";
            LOGGER.error(message, e);
            throw new RuntimeException(message, e);
        }
        this.server.createContext("/", handler);
        this.server.setExecutor(executor);
        this.server.start();
    }

    public HttpsServer getServer() {
        return server;
    }

    private SSLContext trustedContext() throws NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableKeyException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        char[] password = "password".toCharArray();
        KeyStore keyStore = KeyStore.getInstance("JKS");

        InputStream stream = getClass().getResourceAsStream("/ssl_cert.jks");
        try {
            keyStore.load(stream, password);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } finally {
            if (null != stream) {
                stream.close();
            }
        }
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(keyStore);
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
        return sslContext;
    }
}