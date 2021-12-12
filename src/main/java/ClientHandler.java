import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class ClientHandler implements  Runnable{
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private String clientUsername;
    private DataInputStream dis;
    private DataOutputStream dos;

    public ClientHandler(Socket socket){

        try{
            this.socket = socket;
            this.dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            this.dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            this.clientUsername = dis.readUTF();
            clientHandlers.add(this);
            broadcastMessage("SERVER: " + clientUsername + " has entered the chat!");
        }catch (NullPointerException | IOException | ConcurrentModificationException cme ){
            closeAll(socket, dos, dis);
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()){
                try {
                    System.out.println("A new client is connected!");
                    messageFromClient = dis.readUTF();
                    broadcastMessage(messageFromClient);
                } catch (NullPointerException | IOException | ConcurrentModificationException cme) {
                    closeAll(socket, dos, dis);
                    break;
                }
        }
    }

    public void broadcastMessage(String messageToSend){
        for (ClientHandler clientHandler:clientHandlers){
            try{
                if (!clientHandler.clientUsername.equals(clientUsername)){
                    clientHandler.dos.writeUTF(messageToSend);
                    clientHandler.dos.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // remove the client handler
    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUsername + " has left the chat!");
    }


    // close socket, writer and reader
    public void closeAll(Socket socket, DataOutputStream dos, DataInputStream dis){
        removeClientHandler();
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
}


