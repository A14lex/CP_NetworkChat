import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client1 {
    static Logger log = new Logger();
    static boolean b = true;
    static String name = "Client 1";

    public static void main(String[] args) {
        log.logger("Запуск приложения Client1");

        //здесь надо получить из файла настроек хост и № порта - settingsForCommunication

        String host = settingForCommunication(1);
        int port = Integer.parseInt(settingForCommunication(2));
        System.out.println("Хост: " + host + "; порт: " + port);

//

        try {
//
            Socket clientSocket = new Socket(host, port);
            Thread thread = new Thread() {
                @Override
                public void run() {
//
                    try {
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        Scanner console = new Scanner(System.in);
                        System.out.print("Пжл, введите свое имя: ");
                        name = console.nextLine();
                        log.logger("Выбрано имя: " + name);

                        while (true) {



                                if (console.hasNextLine()) {

                                    String text = console.nextLine();
                                    out.println(name + " говорит: " + text);
                                    log.logger(name + " говорит: " + text);

                                    if ("/exit".equals(text)) {
                                        b = false;
                                        break;

                                    }
                                }



                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                ;
            };
            thread.start();

            String text;


            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            while (b) {

                    if (in.ready()) {
                        String replic = in.readLine();

                        System.out.println(replic);
                        log.logger(replic);

                    }




//
            }


        } catch (RuntimeException | IOException e) {
            System.out.println("Ошибка клиента");
            System.out.println(e.getMessage());
        }
        System.out.println("Диалог окончен");

    }

    private static String settingForCommunication(int i) {
        String port = null;
        String host = null;
        String string = null;
        try {
            FileReader fileReader = new FileReader("settingForCommunication.txt");
            BufferedReader reader = new BufferedReader(fileReader);
            for (int j = 1; j <= 2; j++) {
                string = reader.readLine();
                if (string.startsWith("host")) {
                    if (i == 1) {
                        host = string.substring(6);
                        return host;
                    }
                }
                if (string.startsWith("port")) {
                    if (i == 2) {
                        port = string.substring(6);
                        return port;
                    }
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void nameInit() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Пжл, введите свое имя: ");
        name = scanner.nextLine();
        log.logger("Выбрано имя: " + name);


//        scanner.close();
    }
}
