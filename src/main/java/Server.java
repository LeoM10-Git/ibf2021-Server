import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Server {
    public static void main(String[] args) throws IOException {
        Cookie getCookie = new Cookie();
        Random num = new Random();
        String cookieText;

        // create server
        System.out.println("Listen to port 3000: ");
        ServerSocket server = new ServerSocket(3000);
        Socket socket = server.accept();

        try(InputStream is = socket.getInputStream() ){
            DataInputStream dis = new DataInputStream(new BufferedInputStream(is));
            String command = "";
//            if (command.equals("get-cookie")){
//                System.out.println(cookieText);
//            }
//
//            else if (command.equals("close")){
//                socket.close();

            while (!command.equals("close")){
                command = dis.readUTF();
                cookieText = getCookie.generateCookie().get(num.nextInt(10));
//                if (command.equals("get-cookie")) System.out.println(cookieText); // try to print out the text for testing

                //if you get request for "get-cookie", reply the cookie text to the client console
                if (command.equals("get-cookie")){
                        OutputStream os = socket.getOutputStream();
                        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(os));
                        dos.writeUTF(cookieText);
                        dos.flush();
                    }
                }
            } catch (EOFException e){
            socket.close();
        }
        socket.close();
        server.close();

    }
}
