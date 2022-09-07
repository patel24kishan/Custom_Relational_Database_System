package Main.QueryFunctions;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateLogFile {

  private String currentDateAndTime;
  private String queryName = "";
  private String fullQuery = "";
  private String userName = "";
  private String queryStatus = "N/A";
  private boolean status = false;
  private String log = "";
  private String queryLog="";

  public CreateLogFile( String query, String userQuery, String user, boolean status) {
    this.currentDateAndTime = calculateDateAndTime();
    this.queryName = query;
    this.fullQuery=userQuery;
    this.userName = user;
    this.status = status;
  }

  public boolean updateLogFile() {

    if (status){
      queryStatus = "SUCCESSFUL";
    }else {
      queryStatus = "UNSUCCESSFUL";
    }

    log = "\n" + userName + "\t" + currentDateAndTime + "\t\t" + queryName + "\t\t" + queryStatus;
    queryLog="\n"+currentDateAndTime +"\t\t"+fullQuery;

    try {
      String filePath = "LoginCredentials\\Event Log.txt";
      String filePathForQueryLog="LoginCredentials\\Query Log.txt";

      //update file
      FileWriter fileWriter = new FileWriter(filePath, true);
      FileWriter fileWriter2 = new FileWriter(filePathForQueryLog, true);

      fileWriter.write(log);
      fileWriter2.write(queryLog);
      fileWriter.close();
      fileWriter2.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("\n***** LOG UPDATED *****");
    return true;
  }

  private String calculateDateAndTime() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    String currentDate = "";
    Date date = new Date();
    currentDate = sdf.format(date);

    return currentDate;
  }
}
