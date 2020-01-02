package com.zeed;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SocketServer2 {
    private static Socket socket   = null;
    private static AsynchronousServerSocketChannel    server   = null;
    private static DataInputStream in       =  null;
    private static ISOPackager isoPackager = new PostBridgePackagerAABitmap();

    private static Logger logger = LoggerFactory.getLogger(SocketServer2.class.getName());

    public static void main (String [] args) throws IOException {
        setUpMockConnection();
    }

    private static void setUpMockConnection() throws IOException {

        try (AsynchronousServerSocketChannel server =
                     AsynchronousServerSocketChannel.open()) {
            server.bind(new InetSocketAddress("127.0.0.1",
                    2313));
            Future<AsynchronousSocketChannel> acceptCon =
                    server.accept();
            AsynchronousSocketChannel client = acceptCon.get(10,
                    TimeUnit.DAYS);
            if ((client!= null) && (client.isOpen())) {
//                while (true) {
                    ISOMsg responseIso = new ISOMsg();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    Future<Integer> readval = client.read(buffer);
                    System.out.println("Received from client: "
                            + new String(buffer.array()).trim());
                    isoPackager.unpack(responseIso, buffer.array());
                    logger.info("Raw request is " + IsoPrinter.dump(responseIso));
                    readval.get();
                    buffer.flip();
                    String str= "I'm fine. Thank you!";
                    Future<Integer> writeVal = client.write(
                            ByteBuffer.wrap(str.getBytes()));
                    System.out.println("Writing back to client: "
                            +str);
                    writeVal.get();
                    buffer.clear();
                }
//            }
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



//        AsynchronousServerSocketChannel server
//                = AsynchronousServerSocketChannel.open();
//        server.bind(new InetSocketAddress("127.0.0.1", 2313));
//        ConnectionData attach = new ConnectionData(server);
//        while (true) {
//            server.accept(attach, new ConnectionHandler());
//        }
//        server = AsynchronousServerSocketChannel.open();
//        InetSocketAddress sAddr = new InetSocketAddress(host, port);
//        server.bind(sAddr);
//        logger.info("Listening for connections on port: {} ", port);
//        logger.info("Waiting for messages from client");
//        ConnectionData attach = new ConnectionData(server);
//        isStarted = true;
//        server.accept(attach, new ConnectionHandler());
    }

    static class ConnectionData {
        AsynchronousServerSocketChannel server;

        public ConnectionData(AsynchronousServerSocketChannel server) {
            this.server = server;
        }
    }

    static class ConnectionHandler implements CompletionHandler<AsynchronousSocketChannel, ConnectionData> {
        @Override
        public void completed(AsynchronousSocketChannel client, ConnectionData connectionData) {
            try {
                /**
                 * new connection, so re-attach to accept another one
                 */
                connectionData.server.accept(new ConnectionData(server), this);
                SocketAddress clientAddr = client.getRemoteAddress();
//                notifyConnect(client);
                System.out.println(String.format("Accepted a connection from %s", clientAddr));
                /**
                 * Handle data reading and writing for accepted connection
                 */
//                setupFirstTwoBytesReading(client);
            } catch (IOException e) {
                System.out.println(String.format("Error ==> %s ", e.getMessage()));
//                logger.error(TraceNode.formatExceptionForTrace("Could not process client connection", e), e);
            }
        }

        @Override
        public void failed(Throwable exc, ConnectionData attachment) {
//            logger.error(TraceNode.formatExceptionForTrace("Could not connect successfully to client ", exc), exc);
        }
    }

}
