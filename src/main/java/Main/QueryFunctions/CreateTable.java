package Main.QueryFunctions;

import Main.CheckPrimaryKey.PrimaryKey;
import Main.CheckTable.Table;
import Main.Dumps.MakeDumps;
import Main.QueryValidation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateTable {
    public String schema;
    public QueryValidation queryValidation= new QueryValidation();

    public CreateTable(String schema) {
        this.schema = schema;
    }
    public boolean createTable(String query){
        if(validQuery(query)){
            String keydata[]=query.split(" ");
            if(keydata.length>4){
                System.out.println("wrong syntax");
                return false;
            }
            else{
               String tableName = "schemas"+"/"+schema+"/"+keydata[2] + ".txt";
                try {
                    File myObj = new File(tableName);

                    if (myObj.createNewFile()) {
                        System.out.println("Table created: " + myObj.getName());
                        String columns = keydata[3];
                        columns = columns.replace("(","");
                        columns = columns.replace(")","");
                        String[] col = columns.split(",");
                        String row="";
                        int c =0;
                        String refTable="";
                        for(String tmp:col){
                            if(tmp.contains("foreignkey")){
                                String[] fk = tmp.split("-");
                                String[] tableandpk = fk[1].split("&");
                                Table table = new Table(tableandpk[0],schema);
                                refTable = refTable+" "+tableandpk[0];
                                PrimaryKey primaryKey = new PrimaryKey(schema,tableandpk[1],tableandpk[0]);
                                if(table.exists()){
                                    if(primaryKey.containsPk()){
                                        if(c<col.length-1){
                                            row = row +"&"+tableandpk[1]+",";
                                        }
                                        else{
                                            row = row +"&"+tableandpk[1]+'\n';
                                        }
                                        c++;
                                    }else{
                                        myObj.delete();
                                        System.out.println("The foreign key is not the primary key in reference table");
                                        return false;
                                    }
                                }else {
                                    myObj.delete();
                                    System.out.println("The reference table does not exist");
                                    return false;
                                }

                            }
                            else{
                            if(tmp.contains("primarykey")){

                                String[] pk = tmp.split("-");
                                if(c<col.length-1) {
                                    row = row + "$" + pk[1] + ",";
                                }
                                else{
                                    row = row+"$"+pk[1]+'\n';
                                }
                                c++;
                            }else{
                            if(c<col.length-1){

                                    row = row + tmp + ",";

                            }else{

                                    row = row + tmp + '\n';

                            }
                            c++;
                        }}}
                        MakeDumps makeDumps = new MakeDumps(query,schema);
                        makeDumps.dumping();
                        FileWriter fw = new FileWriter(tableName);
                        fw.write(row);
                        fw.close();
                        metadata(keydata[2],row,refTable);
                    } else {
                        System.out.println("Table already exists.");
                        return false;
                    }
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                    return false;
                }
            }


        }
        else{
            return false;
        }
        return true;
    }

    public void metadata(String tableName,String columnName,String refTables) throws IOException {
        String filename="schemas/"+schema+"/"+"META"+"/"+tableName+"META"+".txt";
        FileWriter fwk = new FileWriter(filename);
        String[] columns = columnName.split(",");
        String col,pk,fk,dt;
        col="columns: ";
        pk="primarykey: ";
        fk="foreighkey: ";
        dt="datatype: ";
        for(String tmp: columns) {
            if (tmp.contains("$")) {
                System.out.println(tmp);
                pk = pk + tmp + " ";
            }
            if (tmp.contains("&")) {
                System.out.println(tmp);
                fk = fk + tmp + " ";
            } else if(!tmp.contains("$")) {
                System.out.println(tmp);
                if(tmp.contains(":")){
                    String[] dtype=tmp.split(":");
                    col=col+dtype[0]+" ";
                    dt=dt+dtype[1]+"("+dtype[0]+")"+" ";
                }
                else {
                    col = col + tmp + " ";
                }
            }
        }
            String meta="table name: "+tableName+'\n'+col+'\n'+dt+'\n'+pk+'\n'+fk+'\n'+"Reference Tables: "+refTables+'\n';

            System.out.println(col);
            System.out.println(pk);
            System.out.println(fk);

            String path = meta;

            fwk.write(path);
            fwk.close();

    }

    public boolean validQuery(String query){
        String[] keydata= query.split(" ");
        if(keydata.length!=4){
            System.out.println("Intendation syntax error");
            return false;
        }
        else{
            if(keydata[0].toLowerCase().equals("create")){
                if(keydata[1].toLowerCase().equals("table")){
                    if(keydata[2].matches("^[a-zA-Z0-9]+$")) {
                        if(keydata[3].contains("(,")||keydata[3].contains(",)"))
                        {
                            return false;
                        }
                        keydata[3] = keydata[3].replace("(", "");
                        keydata[3] = keydata[3].replace(")", "");
                        String[] tmp = keydata[3].split(",");
                        boolean count = false;
                        if(keydata[3].contains(",,")){
                            return false;
                        }
                    }
                    else {
                        System.out.println("Illegal Table name");
                        return false;
                    }

                }else {
                    System.out.println("syntax error");
                    return false;
                }
            }else {
                System.out.println("syntax error");
                return false;
            }
        }

        return true;
    }
}
