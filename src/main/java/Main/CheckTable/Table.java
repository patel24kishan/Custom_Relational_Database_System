package Main.CheckTable;

import java.io.File;
import java.io.IOException;

public class Table {
   private String tableName;
    private String schema;

    public Table(String tableName, String schema) {
        this.tableName = tableName;
        this.schema = schema;
    }
    public boolean exists() throws IOException {

         String path = "schemas/"+schema+"/"+tableName+".txt";
         File file = new File(path);
         if(!file.createNewFile()){
             return true;
         }
         else{
             file.delete();
             return false;
         }
    }
}
