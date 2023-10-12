import java.net.*;
import java.io.*;

public class Server {
    private ServerSocket sersock;

    public Server(ServerSocket sersock) {
        this.sersock = sersock;
    }

    public void startServer() {
        try {
            while (!sersock.isClosed()) {
                Socket socket = sersock.accept();
                System.out.println("A new client has connected");
                client clienthandle = new client(socket);
                Thread thread = new Thread(clienthandle);
                thread.start();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void closeServerSocket() {
        try {
            if (sersock != null) {
                sersock.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws IOException {
        ServerSocket sersock = new ServerSocket(4000);// port
        Server server = new Server(sersock);
        // server Server = new server(sersock);
        server.startServer();// meth
    }
}