import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ClientLog {
    public String log = "productNumber, productCount\n";

    public void log(int productNumber, int productCount) {
        log += String.format("%d,%d\n", productNumber, productCount);
    }

    public void exportAsCSV(File txtFile) {
        if (!txtFile.exists()) {
            log = "productNumber, productCount\n" + log;
        }
        try (FileWriter writer = new FileWriter(txtFile, true)) {
            writer.write(log);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
