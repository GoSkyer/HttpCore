package org.galaxy.httpcore;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.ArrayList;

/**
 * Created by dxl584327830 on 16/7/24.
 */
public class HttpCore {

    private void connect() {

        try {

            Socket socket = new Socket();

            URL url = new URL("http://www.lagou.com");

            SocketAddress address = new InetSocketAddress(url.getHost(), url.getDefaultPort());

            socket.connect(address);

            System.out.println("连接服务器");

            // 发送http请求

            OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());

            StringBuffer sb = new StringBuffer();
            sb.append("GET / HTTP/1.1\r\n");
            sb.append("Host:" + url.getHost() + "\r\n");
            sb.append("Connection:Keep-Alive\r\n");
            sb.append("\r\n");

            osw.write(sb.toString());
            osw.flush();

            // 接收http返回

            InputStream is = socket.getInputStream();

            String line = null;
            int contentLength = 0;//服务器发送回来的消息长度
            // 读取所有服务器发送过来的请求参数头部信息
            do {
                line = readLine(is, 0);
                //如果有Content-Length消息头时取出
                if (line.startsWith("Content-Length")) {
                    contentLength = Integer.parseInt(line.split(":")[1].trim());
                }
                //打印请求部信息
                System.out.print(line);
                //如果遇到了一个单独的回车换行，则表示请求头结束
            } while (!line.equals("\r\n"));

            //--输消息的体
            System.out.print(readLine(is, contentLength));

            is.close();
            socket.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String readLine(InputStream is, int contentLe) throws IOException {
        ArrayList lineByteList = new ArrayList();
        byte readByte;
        int total = 0;
        if (contentLe != 0) {
            do {
                readByte = (byte) is.read();
                lineByteList.add(Byte.valueOf(readByte));
                total++;
            } while (total < contentLe);//消息体读还未读完
        } else {
            do {
                readByte = (byte) is.read();
                lineByteList.add(Byte.valueOf(readByte));
            } while (readByte != 10);
        }

        byte[] tmpByteArr = new byte[lineByteList.size()];
        for (int i = 0; i < lineByteList.size(); i++) {
            tmpByteArr[i] = ((Byte) lineByteList.get(i)).byteValue();
        }
        lineByteList.clear();

        return new String(tmpByteArr, encoding);
    }

    private static String encoding = "GBK";

}
