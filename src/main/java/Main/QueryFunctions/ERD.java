package Main.QueryFunctions;

import java.io.*;
import java.util.Scanner;

public class ERD {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public void display() throws IOException {

        String path = "schemas/";
        System.out.println("select schema");
        String schema = reader.readLine();
        File folder = new File(path+schema+"/"+"META/");
        if(!folder.exists()){
            System.out.println("Schema does not exist");
            return;
        }
        else {
            File[] meta = folder.listFiles();
            if(meta.length==0){
                System.out.println("No Tables in This schema");
                return;
            }
            FileWriter fw = new FileWriter("ERDS/"+schema+"ERD"+".txt",true);
            for(File metadata: meta){
                Scanner sc = new Scanner(metadata);

                sc.useDelimiter("\\Z");
                String s = sc.next();
                fw.write(s+'\n');
                fw.write("****************************************************************************************"+'\n');
            }
            fw.close();
        }
    }

}
