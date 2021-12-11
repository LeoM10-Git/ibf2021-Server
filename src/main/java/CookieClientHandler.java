import java.io.*;
import java.net.Socket;
import java.util.Random;

public class CookieClientHandler implements  Runnable{
    private  final Socket socket;
    Cookie getCookie = new Cookie();
    Random num = new Random();
    String cookieText;

    public CookieClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try(InputStream is = socket.getInputStream() ){
            DataInputStream dis = new DataInputStream(new BufferedInputStream(is));
            String command = "";

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
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
}

