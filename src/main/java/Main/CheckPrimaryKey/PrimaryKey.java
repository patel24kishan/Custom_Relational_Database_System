package Main.CheckPrimaryKey;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PrimaryKey {
    private String schema;
    private String primaryKey;
    private String tableName;


    public PrimaryKey(String schema, String primaryKey, String tableName) {
        this.schema = schema;
        this.primaryKey = primaryKey;
        this.tableName = tableName;
    }

    public boolean containsPk() throws FileNotFoundException {
        String path = "schemas/"+schema+"/"+tableName+".txt";
        File file = new File(path);
        Scanner sc = new Scanner(file);
        sc.useDelimiter("\\Z");
        String s = sc.next();
        s = s.replaceAll("\n", " ").replaceAll("\r", " ");
        return s.contains("$"+primaryKey);

    }
}
