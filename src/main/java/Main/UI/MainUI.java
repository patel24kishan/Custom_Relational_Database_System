package Main.UI;

import Main.Dumps.MakeDumps;
import Main.QueryFunctions.*;
import Main.UserLogin;
import Main.QueryFunctions.DeleteQuery;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class MainUI {
    public String path="schemas";
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private String userID = null;
    private Boolean isQueryExecuted = false;
    String query = "";


    public static void main(String[] args) throws IOException, InterruptedException {

        MainUI uiObject=new MainUI();
        uiObject.mainUImethod();
    }

    public void mainUImethod() throws IOException, InterruptedException {

        UserLogin login=new UserLogin();
        boolean isLoggedIn=false;
        userID=login.enterLoginCredentials();

        if (!userID.equals(null)){
            isLoggedIn=true;
        }

        if (isLoggedIn)  {
            displayQueryOption();
        }
    }

    private String selectschema() throws IOException {
        System.out.println("Select Schema");
        Scanner scn = new Scanner(System.in);
        String sc = reader.readLine();
        path=path+"/"+sc;
        File theDir = new File(path);
        if (!theDir.exists()){
            File meta = new File("schemas"+"/"+sc+"/"+"META");
            meta.mkdirs();
            File dumps = new File("schemas"+"/"+sc+"/"+"DUMPS");
            dumps.mkdirs();
            System.out.println("Schema created");
        }
        else{
            System.out.println("Schema Selected");
        }
       return sc;
    }

    private void displayQueryOption() throws IOException, InterruptedException {
        System.out.println("\n=======================\n\tQUERY TYPE\n=======================\n");
            String query = reader.readLine();
            String[] keyword = query.split(" ");
            selectQueryFunction(keyword[0].toLowerCase(), query);


    }

    private void selectQueryFunction(String keyword,String query) throws InterruptedException, IOException {



        switch (keyword) {
            case "select":
                //Select Function;
                SelectQuery selectQuery = new SelectQuery();
                String selectSchemaName = selectschema();
                isQueryExecuted = selectQuery.selectFunction(query, selectSchemaName);

                //Make SQL Dump
                if (isQueryExecuted){
                MakeDumps makeDumps = new MakeDumps(query, selectSchemaName);
                makeDumps.dumping();
        }
                break;

            case "insert":
                //Insert Function;
                InsertClass insertQuery=new InsertClass();
                String insertSchemaName=selectschema();
                isQueryExecuted=insertQuery.insertTable(query,insertSchemaName);

                //Make SQL Dump
                if(isQueryExecuted) {
                    MakeDumps makeDumps1 = new MakeDumps(query, insertSchemaName);
                    makeDumps1.dumping();
                }
                else{
                    System.out.println("insertion failed");
                }
                break;

            case "update":
                UpdateQuery updateQuery = new UpdateQuery(selectschema());
                isQueryExecuted =updateQuery.update(query);
                break;

            case "create":
                //Create Function;
                CreateTable createTable = new CreateTable(selectschema());
                isQueryExecuted=createTable.createTable(query);
                break;

            case "erd":
                 ERD erd = new ERD();
                 erd.display();
                 isQueryExecuted = true;
                 break;

            case "drop":
                DropClass dropQuery=new DropClass();
                String dropSchemaName=selectschema();
                isQueryExecuted = dropQuery.DropTable(query,dropSchemaName);

                //Make SQL Dump
                if(isQueryExecuted) {
                    MakeDumps makeDumps4 = new MakeDumps(query, dropSchemaName);
                    makeDumps4.dumping();
                }
                break;

            case "delete":
                //Delete Function;

                DeleteQuery deleteQuery = new DeleteQuery();
                String deleteSchemaName=selectschema();
                isQueryExecuted = deleteQuery.deleteFunction(query,deleteSchemaName);

                //Make SQL Dump
                if(isQueryExecuted) {
                    MakeDumps makeDumps2 = new MakeDumps(query, deleteSchemaName);
                    makeDumps2.dumping();
                }
                break;

            case "exit":
                System.out.println("");
                System.exit(1);
                break;

            default:
                System.err.println(" ***** Wrong Input *****");
                TimeUnit.SECONDS.sleep(2);
                displayQueryOption();
                break;
        }

        //update log
        if (!(keyword.contains("meta") || keyword.equals("dumps"))) {
            CreateLogFile createLogFile = new CreateLogFile(keyword,query, userID, isQueryExecuted);
            createLogFile.updateLogFile();
        }
    }

}
