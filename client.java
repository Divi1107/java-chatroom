import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class client implements Runnable {
    public static ArrayList<client> clienthandle = new ArrayList<>();
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;
    private String username;

    public client(Socket socket) {
        try {
            this.socket = socket;
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = br.readLine();
            clienthandle.add(this);
            broadcastMessage("Server" + username + " has entered the chat.");
        } catch (Exception e) {
            closeEverything(socket, br, bw);
        }
    }

    @Override
    public void run() {
        String msgfromclient;
        while (socket.isConnected()) {
            try {
                msgfromclient = br.readLine();
                broadcastMessage(msgfromclient);
            } catch (IOException e) {
                closeEverything(socket, br, bw);
                break;
            }
        }
    }

    public void broadcastMessage(String msgtosend) {
        for (client clienthandler : clienthandle) {
            try {
                if (!clienthandler.username.equals(username)) {
                    clienthandler.bw.write(msgtosend);
                    clienthandler.bw.newLine();
                    clienthandler.bw.flush();
                }
            } catch (Exception e) {
                closeEverything(socket, br, bw);
            }
        }
    }

    public void removecli() {
        clienthandle.remove(this);
        broadcastMessage("Server:" + username + " has left the chat");
    }

    public void closeEverything(Socket socket, BufferedReader br, BufferedWriter bw) {
        removecli();
        try {
            if (br != null)
                br.close();
            if (bw != null)
                bw.close();// so we dont get nullptr exp
            if (socket != null)
                socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
