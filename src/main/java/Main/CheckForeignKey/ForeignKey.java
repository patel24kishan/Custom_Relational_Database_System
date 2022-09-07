package Main.CheckForeignKey;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ForeignKey {
    private String schema;
    private String foreignKey;
    private String tableName;


    public ForeignKey(String schema, String foreignKey, String tableName) {
        this.schema = schema;
        this.foreignKey = foreignKey;
        this.tableName = tableName;
    }

    public boolean containsFk() throws FileNotFoundException {
        String path = "schemas/"+schema+"/"+tableName+".txt";
        File file = new File(path);
        Scanner sc = new Scanner(file);
        sc.useDelimiter("\\Z");
        String s = sc.next();
        s = s.replaceAll("\n", " ").replaceAll("\r", " ");
        return s.contains("&"+foreignKey)||s.contains("&"+foreignKey.toUpperCase());

    }

}
