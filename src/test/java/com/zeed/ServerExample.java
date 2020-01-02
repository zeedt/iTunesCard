package com.zeed;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ServerExample {

    ExecutorService executorService = Executors.newFixedThreadPool(150);

    private static ISOPackager isoPackager = new PostBridgePackagerAABitmap();

    private static Logger logger = LoggerFactory.getLogger(ServerExample.class.getName());
    public static void main (String [] args)
            throws Exception {
     
        new ServerExample().go();
    }
     
    private void go()
            throws IOException, InterruptedException, ExecutionException, ISOException {
 
        AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open();
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 2313);
        serverChannel.bind(hostAddress);
         
        System.out.println("Server channel bound to port: " + hostAddress.getPort());
        System.out.println("Waiting for client to connect... ");
         
        Future acceptResult = serverChannel.accept();
        AsynchronousSocketChannel clientChannel = (AsynchronousSocketChannel) acceptResult.get();
 
        System.out.println("Messages from client: ");
 
        if ((clientChannel != null) && (clientChannel.isOpen())) {
 
            while (true) {
                ISOMsg responseIso = new ISOMsg();

                ByteBuffer buffer = ByteBuffer.allocate(100);
                Future result = clientChannel.read(buffer);
 
                while (! result.isDone()) {
                    // do nothing
                }
 
                buffer.flip();
                String message = new String(buffer.array()).trim();


                if (!StringUtils.isEmpty(message)) {
                    System.out.println("Message : " + message);
                    isoPackager.unpack(responseIso, buffer.array());
                    logger.info("Raw request is " + IsoPrinter.dump(responseIso));
                }
//                result.get();
                buffer.flip();
                if (message.equals("Bye.")) {
 
                    break; // while loop
                }
 
                buffer.clear();
 
            } // while()
 
            clientChannel.close();
         
        } // end-if
         
        serverChannel.close();
    }
}