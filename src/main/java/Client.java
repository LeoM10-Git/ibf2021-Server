import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;


    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        } catch (IOException e) {
            close(socket, bufferedWriter, bufferedReader);
        }
    }

    public void sendMessage(){
        try{
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()){
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(username + ": " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch (IOException e){
            close(socket, bufferedWriter, bufferedReader);
        }
    }

    public void listenForMessage(){
        new Thread(() -> {
            String messageFromGroup;
            while (socket.isConnected()){
                try{
                    messageFromGroup = bufferedReader.readLine();
                    System.out.println(messageFromGroup);
                } catch (IOException e) {
                    close(socket, bufferedWriter, bufferedReader);
                }
            }
        }).start();
    }

        public void close(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader){
            try{
                if (bufferedReader != null){
                    bufferedReader.close();
                }
                if (bufferedWriter != null){
                    bufferedWriter.close();
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
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username for the group chat: ");
        String username = scanner.nextLine();

        Client client = new Client(socket, username);
        client.listenForMessage();
        client.sendMessage();
    }
}
