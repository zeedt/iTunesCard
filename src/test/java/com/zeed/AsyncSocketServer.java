package com.zeed;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
public class AsyncSocketServer {
  public static void main(String[] args) throws Exception {
    AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel
        .open();//w w w  .  j  a  v  a2s .com
    String host = "localhost";
    int port = 2313;
    InetSocketAddress sAddr = new InetSocketAddress(host, port);
    server.bind(sAddr);
    System.out.format("Server is listening at %s%n", sAddr);
    ServerAttachment attach = new ServerAttachment();
    attach.server = server;
    server.accept(attach, new ConnectionHandler());
    Thread.currentThread().join();
  }
}
class ServerAttachment {
  AsynchronousServerSocketChannel server;
  AsynchronousSocketChannel client;
  ByteBuffer buffer;
  SocketAddress clientAddr;
  boolean isRead;
}

class ConnectionHandler implements
    CompletionHandler<AsynchronousSocketChannel, ServerAttachment> {
  private static ISOPackager isoPackager = new PostBridgePackagerAABitmap();
  private static Logger logger = LoggerFactory.getLogger(AsyncSocketServer.class.getName());

  @Override
  public void completed(AsynchronousSocketChannel client, ServerAttachment attach) {
    try {
      SocketAddress clientAddr = client.getRemoteAddress();
      System.out.format("Accepted a  connection from  %s%n", clientAddr);
      attach.server.accept(attach, this);
      ReadServerWriteHandler rwHandler = new ReadServerWriteHandler();
      ServerAttachment newAttach = new ServerAttachment();
      newAttach.server = attach.server;
      newAttach.client = client;
      newAttach.buffer = ByteBuffer.allocate(2048);
      newAttach.isRead = true;
      newAttach.clientAddr = clientAddr;
      client.read(newAttach.buffer, newAttach, rwHandler);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void failed(Throwable e, ServerAttachment attach) {
    System.out.println("Failed to accept a  connection.");
    e.printStackTrace();
  }
}

class ReadServerWriteHandler implements CompletionHandler<Integer, ServerAttachment> {

  private static ISOPackager isoPackager = new PostBridgePackagerAABitmap();
  private static Logger logger = LoggerFactory.getLogger(AsyncSocketServer.class.getName());

  @Override
  public void completed(Integer result, ServerAttachment attach) {
    if (result == -1) {
      try {
        attach.client.close();
        System.out.format("Stopped   listening to the   client %s%n",
            attach.clientAddr);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
      return;
    }

    if (attach.isRead) {
      attach.buffer.flip();
      int limits = attach.buffer.limit();
      byte bytes[] = new byte[limits];
      attach.buffer.get(bytes, 0, limits);
      Charset cs = Charset.forName("UTF-8");
      String msg = new String(bytes, cs);
      System.out.format("Client at  %s  says: %s%n", attach.clientAddr,
          msg);

      ISOMsg responseIso = new ISOMsg();
      try {
        isoPackager.unpack(responseIso, attach.buffer.array());
      } catch (ISOException e) {
        e.printStackTrace();
      }
      logger.info("Raw request is " + IsoPrinter.dump(responseIso));
      attach.isRead = false; // It is a write
      attach.buffer.rewind();
      ISOMsg isoMsg = new ISOMsg();
      try {
        isoMsg.setMTI("0810");
        isoMsg.set(7, "1024144111");
        isoMsg.set(11, "004395");
        isoMsg.set(70, "001");
        logger.error("Responding with " + IsoPrinter.dump(isoMsg));
      } catch (ISOException e) {
        e.printStackTrace();
      }

      try {
        isoMsg.setPackager(isoPackager);
        attach.client.write(ByteBuffer.wrap(isoMsg.pack()));
      } catch (ISOException e) {
        e.printStackTrace();
      }
//      attach.client.write(ByteBuffer.wrap(("<iso8583>\n" +
//              "<isomsg>\n" +
//              "<field id=\"0\" value=\"0421\"/>\n" +
//              "<field id=\"2\" value=\"539923******8440\"/>\n" +
//              "<field id=\"3\" value=\"000000\"/>\n" +
//              "<field id=\"4\" value=\"000000100000\"/>\n" +
//              "<field id=\"7\" value=\"0921164548\"/>\n" +
//              "<field id=\"11\" value=\"468197\"/>\n" +
//              "<field id=\"12\" value=\"154600\"/>\n" +
//              "<field id=\"13\" value=\"0921\"/>\n" +
//              "<field id=\"14\" value=\"1911\"/>\n" +
//              "<field id=\"18\" value=\"5812\"/>\n" +
//              "<field id=\"22\" value=\"051\"/>\n" +
//              "<field id=\"23\" value=\"001\"/>\n" +
//              "<field id=\"25\" value=\"00\"/>\n" +
//              "<field id=\"26\" value=\"12\"/>\n" +
//              "<field id=\"32\" value=\"636092\"/>\n" +
//              "<field id=\"33\" value=\"111130\"/>\n" +
//              "<field id=\"37\" value=\"000210002597\"/>\n" +
//              "<field id=\"41\" value=\"2232JL48\"/>\n" +
//              "<field id=\"42\" value=\"SB13651426OG000\"/>\n" +
//              "<field id=\"43\" value=\"ADEKUNLEGRACEIAM       OG           OGNG\"/>\n" +
//              "<field id=\"49\" value=\"566\"/>\n" +
//              "<field id=\"56\" value=\"4021\"/>\n" +
//              "<field id=\"59\" value=\"Reconciler>GENERIC&Option>000\"/>\n" +
//              "<field id=\"90\" value=\"020046819709211546440000011113000000000000\"/>\n" +
//              "<field id=\"95\" value=\"000000000000000000000000C00000000C00000000\"/>\n" +
//              "<field id=\"123\" value=\"511101513344101\"/>\n" +
//              "</isomsg>\n" +
//              "</iso8583>").getBytes()));
      attach.isRead = true;
      attach.buffer.clear();
      attach.client.read(attach.buffer, attach, this);
    } else {
      // Write to the client
      attach.client.write(attach.buffer, attach, this);
      attach.isRead = true;
      attach.buffer.clear();
      attach.client.read(attach.buffer, attach, this);
    }
  }

  @Override
  public void failed(Throwable e, ServerAttachment attach) {
    e.printStackTrace();
  }
}