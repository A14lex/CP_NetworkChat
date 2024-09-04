import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static Logger log = new Logger();
    public static List<Socket> sockets = new ArrayList<>();
    public static SocketManager socketManager = new SocketManager();
    static int countOfAccept = 0; //кол-во подключений

    public static void main(String[] args) {
        String pathOfFilesWithSet = "settings.txt";//путь к файлу с настройками
        int portOfServer = portOfServer(pathOfFilesWithSet);//получение № порта из файла с настройками, не должен быть равен -1
        int limitOfAccept = 4;//лимит кол-ва подлкючений
        serverStart(portOfServer, limitOfAccept);//запуск сервера с указанием порта и лимита кол-ва подключений.


    }

    public static void serverStart(int portOfServer, int limitOfAccept) {
        String startText = "Сервер запускается. № порта: " + portOfServer;
        System.out.println(startText);
        log.logger(startText);

        try (ServerSocket serverSocket = new ServerSocket(portOfServer)) {
            while (countOfAccept <= limitOfAccept) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    countOfAccept++;


//                    newAccept(clientSocket);
                    System.out.println("New connection accepted");

                    socketManager.sockets.add(clientSocket);//передаем подключение в управление сокетами.
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            System.out.println(clientSocket);
                            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                                boolean b = true;

                                while (b) {
                                    if (in.ready()) {
                                        String text = in.readLine();
                                        if (text.endsWith("/exit")) {
                                            text = "клиент желает завершить общение";
                                            b = false;
                                            countOfAccept--;

                                            in.close();
                                        }
                                        System.out.println(text);
                                        LocalTime localTime = LocalTime.now();
                                        log.logger(localTime + ": " + text);


                                        synchronized (socketManager) {
                                            if (countOfAccept > 1) {
                                                socketManager.newMessage(clientSocket, text);
                                            }

                                        }


                                    }
                                }


                            } catch (IOException e) {
                                System.out.println();
                                e.printStackTrace();
                            } finally {

                            }
                        }
                    };
                    thread.start();


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }



        } catch (IOException e) {
            e.printStackTrace();
        }


    }

//    private static void newAccept(Socket clientSocket) {
//        //Запускаем  поток на прием
//        //threadIn(clientSocket); - в этом методе происходит ошибка, поэтому логику перемещаем сюда
//        System.out.println("New connection accepted");
//        System.out.println(clientSocket);
//
//
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
//                    out.println("Привет от сервера");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        thread.start();
//
//
//    }



//    public void threadIn(Socket clientSocket) {
//        System.out.println("New connection accepted");
//        System.out.println(clientSocket);
//
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                System.out.println(clientSocket);
//                try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
//
//                    boolean b = true;
//                    while (b) {
//                        if (in.ready()) {
//                            String text = in.readLine();
//                            System.out.println(text);
//                            log.logger(text);
//                            //идет отправка этого сообщения  для других клиентов
//
//                            synchronized (socketManager) {
//                                socketManager.newMessage(clientSocket, text);
//                            }
//
//
//                        }
//                    }
//
//                } catch (IOException e) {
//                    System.out.println();
//                    e.printStackTrace();
//                }
//            }
//        };
//        thread.start();
//
//    }

    public static int portOfServer(String pathOfFilesWithSet) {
        int port = -1;
        try {
            FileReader fileReader = new FileReader(pathOfFilesWithSet);
            BufferedReader reader = new BufferedReader(fileReader);
            String line = reader.readLine();


            port = Integer.parseInt(line);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return port;
    }


}
