import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class SocketManager {
    boolean b = false;//включиться когда будет набрано достаточное кол-во подключений
    ArrayList<Socket> sockets = new ArrayList<>();
    int sizeOfSockets; //размер списка сокетоd
    public void newMessage(Socket socket, String text){
        //принимает строчку и сокет (который и передал эту строчку), чтобы передать её другим
        //перебирает сокеты, кроме переданного и рассылает её другим
        if(sockets.size()<2){
            return;//некому рассылать
        }
        for (Socket socketOfSockets :
                sockets) {
            if(socketOfSockets!=socket){
                try {
                    PrintWriter out = new PrintWriter(socketOfSockets.getOutputStream(), true);
                    
                    out.println(text);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }

}
