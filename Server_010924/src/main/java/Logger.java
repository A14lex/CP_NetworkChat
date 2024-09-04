import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
public class Logger {
    static File file = new File("file.log");
    static void logger(String string){



        try {
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(string);
            fileWriter.write('\n');
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
