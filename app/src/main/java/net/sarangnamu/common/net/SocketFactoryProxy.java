package net.sarangnamu.common.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 24. <p/>
 */
public class SocketFactoryProxy {
    private static final Logger mLog = LoggerFactory.getLogger(SocketFactoryProxy.class);

    public static SSLSocketFactory create(final X509TrustManager manager) {
        SSLSocketFactory factory = null;

        try {
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] { manager }, null);

            factory = sslContext.getSocketFactory();
        } catch (Exception e) {
            mLog.error("ERROR: " + e.getMessage());
        }

        return factory;
    }
}
