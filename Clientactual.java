import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Clientactual {
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;
    private String user;

    public Clientactual(Socket socket, String user) {
        try {
            this.socket = socket;
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.user = user;
        } catch (Exception e) {
         closeEverything(socket, br, bw);
        }
    }

    public void sendMsg() {
        try {
            bw.write(user);
            bw.newLine();
            bw.flush();
            Scanner sobj = new Scanner(System.in);
            while (socket.isConnected()) {
                String msgtosend = sobj.nextLine();
                bw.write(user + ": " + msgtosend);
                bw.newLine();
                bw.flush();
            }
        } catch (Exception e) {
            closeEverything(socket, br, bw);
        }
    }

    public void listenformsg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msggrpchat;
                while (socket.isConnected()) {
                    try {
                        msggrpchat = br.readLine();
                        System.out.println(msggrpchat);

                    } catch (IOException e) {
                        closeEverything(socket, br, bw);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader br, BufferedWriter bw) {
        // removecli();
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

    public static void main(String[] args) throws IOException {
        Scanner sobj = new Scanner(System.in);
        System.out.println("Enter your useername for grp chat");
        String user = sobj.nextLine();
        Socket socket = new Socket("localhost", 4000);
        Clientactual cl = new Clientactual(socket, user);
        cl.listenformsg();
        cl.sendMsg();
    }
}
