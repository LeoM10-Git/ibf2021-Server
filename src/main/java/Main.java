import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {
    public static void main(String[] args) throws IOException{
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        System.out.println("Listen to port 3000: ");
        ServerSocket server = new ServerSocket(3000);

        while (true){
            Socket socket = server.accept();
            CookieClientHandler clientHandler = new CookieClientHandler(socket);
            threadPool.submit(clientHandler);
        }
    }
}
