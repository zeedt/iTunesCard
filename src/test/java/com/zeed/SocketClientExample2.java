package com.zeed;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketClientExample2 {

    private static ObjectOutputStream oos = null;
    static ObjectInputStream ois = null;
    public static void main (String[] args) throws IOException {

        Socket client = new Socket();
        client.connect(new InetSocketAddress("127.0.0.1", 2313));

        while (true) {
            oos = new ObjectOutputStream(client.getOutputStream());
            System.out.println("Sending request to Socket Server");
            oos.writeBytes("<iso8583>\n" +
                    "<isomsg>\n" +
                    "<field id=\"0\" value=\"0800\"/>\n" +
                    "<field id=\"7\" value=\"1023223222\"/>\n" +
                    "<field id=\"11\" value=\"004311\"/>\n" +
                    "<field id=\"12\" value=\"223222\"/>\n" +
                    "<field id=\"13\" value=\"1023\"/>\n" +
                    "<field id=\"70\" value=\"001\"/>\n" +
                    "</isomsg>\n" +
                    "</iso8583>");

            ois = new ObjectInputStream(client.getInputStream());
            String message = ois.readUTF();
            System.out.println("Message: " + message);

        }

    }

}
