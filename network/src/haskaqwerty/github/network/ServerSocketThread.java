package haskaqwerty.github.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ServerSocketThread extends Thread {
    private final int port;
    private final ServerSocketThreadListener eventListener;
    private final int timeout;
    public ServerSocketThread(ServerSocketThreadListener eventListener, String name,int port, int timeout){
        super(name);
        this.eventListener = eventListener;
        this.port = port;
        this.timeout = timeout;
        start();
    }
    @Override
    public void run() {
        eventListener.onStartServerSocketThread(this);
        try (ServerSocket serverSocket = new ServerSocket(port);){
            serverSocket.setSoTimeout(timeout);
            eventListener.onReadyServerSocketThread(this,serverSocket);
            while (!isInterrupted()){
                Socket socket;
                try {
                     socket = serverSocket.accept();
                } catch (SocketTimeoutException e){
                    eventListener.onTimeoutException(this,serverSocket);
                    continue;
                }
                eventListener.onAcceptedSocket(this,serverSocket,socket);
            }
        } catch (IOException e) {
            eventListener.onExceptionServerSocketThread(this,e);
        } finally {
            eventListener.onStopServerSocketThread(this);
        }


//        System.out.println("Поток Sst запущен");
//        try (ServerSocket serverSocket = new ServerSocket(port);
//             Socket socket = serverSocket.accept();){
//            DataInputStream in  = new DataInputStream(socket.getInputStream());
//            DataOutputStream out =  new DataOutputStream(socket.getOutputStream());
//            while (true) out.writeUTF("echo " + in.readUTF());
//        }
//        catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println("Поток sst остановлен");
    }
}
