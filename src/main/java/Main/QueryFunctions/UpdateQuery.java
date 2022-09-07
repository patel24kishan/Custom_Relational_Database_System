package Main.QueryFunctions;

import Main.CheckForeignKey.ForeignKey;
import Main.CheckPrimaryKey.PrimaryKey;
import Main.CheckTable.Table;
import Main.Dumps.MakeDumps;
import Main.ValidateUpdate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class UpdateQuery {
    String schema;
    ValidateUpdate validateUpdate = new ValidateUpdate();


    public UpdateQuery(String schema) {
        this.schema = schema;
    }

    public boolean update(String query) throws IOException {

        if(validateUpdate.isValid(query)){

            String[] spiltQuery = query.split(" ");
            Table table = new Table(spiltQuery[1],schema);
            if(table.exists()){
                String path = "schemas/"+schema+"/"+spiltQuery[1]+".txt";
                File file = new File(path);

                Scanner tmp = new Scanner(file);
                tmp.useDelimiter("\\Z");
                String data = tmp.next();
               // System.out.println(data);
                String[] tmpf = spiltQuery[3].split("=");
                String[] tmpw = spiltQuery[5].split("=");
                PrimaryKey primaryKey = new PrimaryKey(schema,tmpf[0],spiltQuery[1]);
                PrimaryKey primaryKeyw = new PrimaryKey(schema,tmpw[0],spiltQuery[1]);
                ForeignKey foreignKey = new ForeignKey(schema,tmpf[0],spiltQuery[1]);
                ForeignKey foreignKeyw = new ForeignKey(schema,tmpw[0],spiltQuery[1]);
                if(primaryKeyw.containsPk()){
                    spiltQuery[5]="$"+spiltQuery[5];
                }
                if(foreignKeyw.containsFk()){
                    spiltQuery[5]="&"+spiltQuery[5];
                }
                if(primaryKey.containsPk()){
                    spiltQuery[3]="$"+spiltQuery[3];
                    HashMap<String,String> map = pkcolumn(data);

                    String[] rows = data.split("\n");
                    for(String fields: rows){
                        if(map.containsKey(spiltQuery[3])){
                            System.out.println("Duplicate updation not allowed");
                            return false;
                        }
                    }
                  performUpdate(data,spiltQuery[3],spiltQuery[5],path);
                }
                else {
                    if (foreignKey.containsFk()) {
                        spiltQuery[3] = "&" + spiltQuery[3];
                    }
                    performUpdate(data,spiltQuery[3],spiltQuery[5],path);
                }
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
        return true;
    }

    public void performUpdate(String data, String set,String where,String path) throws IOException {
        String[] tmpset = set.split("=");
        ArrayList<String> condition =new ArrayList<String>();
        String tmpr ="";
        for(String rows: data.split("\n")){
            if(rows.contains(where)){
                String tmpf="";
                int c=0;
                for(String feilds: rows.split(",")){
                    if(feilds.contains(tmpset[0])){
                        feilds = set;
                    }
                    if(c<rows.split(",").length-1){
                    tmpf=tmpf+feilds+",";}
                    else {
                        tmpf=tmpf+feilds;
                    }
                    c++;
                }
                tmpr=tmpr+tmpf+'\n';

            }
            else {
                tmpr=tmpr+rows+'\n';
            }
        }
        FileWriter fw = new FileWriter(path);
        fw.write(tmpr);
        fw.close();


    }

    public HashMap<String,String> pkcolumn(String data){
        HashMap<String,String> map = new HashMap<String,String>();

        for(String rows:data.split("\n")){

            for (String fields : rows.split(",")){
                if(fields.contains("$")){
                    map.put(fields,"tmp");
                }
            }
        }


return map;
    }
}
