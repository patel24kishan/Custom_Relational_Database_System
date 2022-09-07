package Main.Dumps;

import java.io.FileWriter;
import java.io.IOException;

public class MakeDumps {
    private String query;
    private String schema;

    public MakeDumps(String query, String schema) {
        this.query = query;
        this.schema = schema;
    }


    public void dumping() throws IOException {
        String path = "schemas/"+schema+"/"+"DUMPS/"+schema+"DUMPS"+".txt";
        FileWriter fw = new FileWriter(path,true);
        fw.write(query+'\n');
        fw.close();
    }
}
