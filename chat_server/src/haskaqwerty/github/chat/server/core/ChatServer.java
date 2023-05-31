package haskaqwerty.github.chat.server.core;

import haskaqwerty.github.network.ServerSocketThread;
import haskaqwerty.github.network.ServerSocketThreadListener;
import haskaqwerty.github.network.SocketThread;
import haskaqwerty.github.network.SocketThreadListener;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ChatServer implements ServerSocketThreadListener , SocketThreadListener {
    private final ChatServerListener eventListener;
    private ServerSocketThread serverSocketThread;
    private final Vector<SocketThread> clients = new Vector<>();

    public ChatServer(ChatServerListener eventListener) {
        this.eventListener = eventListener;
    }

    public void startListening(int port) {
        if (serverSocketThread!=null && serverSocketThread.isAlive()) {
            putLog("Поток сервера уже запущен");
            return;
        }
        serverSocketThread = new ServerSocketThread(this,"ServerSocketThread",port,2000);
        //putLog("Сервер запущен.");
    }

    public void dropAllClients()
    {
        putLog("dropAllClients.");
    }

    public void stopListening()
    {
        if (serverSocketThread == null || !serverSocketThread.isAlive()) {
            putLog("Поток сервера не запущен");
            return;
        }
        serverSocketThread.interrupt();

    }
    private synchronized void putLog(String msg)
    {
        //System.out.println(msg);
        eventListener.onChatServerLog(this,msg);
    }

    @Override
    public void onStartServerSocketThread(ServerSocketThread thread) {
    putLog("Started..");
    }

    @Override
    public void onStopServerSocketThread(ServerSocketThread thread) {
    putLog("Stopped.");
    }

    @Override
    public void onReadyServerSocketThread(ServerSocketThread thread, ServerSocket serverSocket) {
    putLog("Server Socket is ready.");
    }

    @Override
    public void onTimeoutException(ServerSocketThread thread, ServerSocket serverSocket) {
    putLog("accept() timeout");
    }

    @Override
    public void onAcceptedSocket(ServerSocketThread thread, ServerSocket serverSocket, Socket socket) {
    putLog("Client connected: " + socket);
    String threadName = "Socket thread: " + socket.getInetAddress() + ": "  + socket.getPort();
    new SocketThread(this,threadName,socket);
    }

    @Override
    public void onExceptionServerSocketThread(ServerSocketThread thread, Exception e) {
    putLog("Exception: " + e.getClass().getName() + ": " + e.getMessage());
    }

    @Override
    public synchronized void onStartSocketThread(SocketThread socketThread) {
        putLog("Started..");

    }

    @Override
    public synchronized void onStopSocketThread(SocketThread socketThread) {
        putLog("Stopped.");
        clients.remove(socketThread);

    }

    @Override
    public synchronized void onReadySocketThread(SocketThread socketThread, Socket socket) {
        putLog("Socket is ready.");
        clients.add(socketThread);

    }

    @Override
    public synchronized void onRecieveString(SocketThread socketThread, Socket socket, String value) {
        //socketThread.sendMessage(value);
        for (int i = 0; i < clients.size(); i++) {
            clients.get(i).sendMessage(value);
        }
    }

    @Override
    public synchronized void onExceptionSocketThread(SocketThread socketThread, Socket socket, Exception e) {
        putLog("Exception: " + e.getClass().getName() + ": " + e.getMessage());

    }
}
