import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private String username;


    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            this.dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            this.username = username;
        } catch (IOException e) {
            closeAll(socket, dos, dis);
        }
    }

    public void sendMessage(){
        try{
            dos.writeUTF(username);
            dos.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()){
                String messageToSend = scanner.nextLine();
                dos.writeUTF(username + ": " + messageToSend);
                dos.flush();
            }
        }catch (IOException e){
            closeAll(socket, dos, dis);
        }
    }

    public void listenForMessage(){
        new Thread(() -> {
            String messageFromGroup;
            while (socket.isConnected()){
                try{
                    messageFromGroup = dis.readUTF();
                    System.out.println(messageFromGroup);
                } catch (IOException e) {
                    closeAll(socket, dos, dis);
                }
            }
        }).start();
    }

    public void closeAll(Socket socket, DataOutputStream dos, DataInputStream dis){
        try{
            if (dis != null){
                dis.close();
            }
            if (dos != null){
                dos.close();
            }
            if (socket != null){
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 3000);
        System.out.print("Enter your username for the group chat: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String username = br.readLine();

        Client client = new Client(socket, username);
        client.listenForMessage();
        client.sendMessage();
    }
}
