import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 3000);

        try(OutputStream os = socket.getOutputStream()){
            BufferedOutputStream bos = new BufferedOutputStream(os);
            DataOutputStream dos = new DataOutputStream(bos);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            // input command , to server, type close to close the server connection
            String command = "";
            while (!command.equals("close")){
                command = br.readLine();
                dos.writeUTF(command);
                dos.flush();

                // get reply from server for the cookie text
                    InputStream is = socket.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    DataInputStream dis = new DataInputStream(bis);
                    String reply = dis.readUTF();
                    System.out.println(reply);
            }
        }catch (EOFException e){
            socket.close();
        }
        socket.close();
    }
}
