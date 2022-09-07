package Main.QueryFunctions;

import Main.QueryValidation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectQuery {

  QueryValidation validateQuery = new QueryValidation();
  private String[] columnName;
  private List<List<String>> valueList = new ArrayList<>();
  private List<String> dataRowList = new ArrayList<>();
  private int totalElements = 0;


  public boolean selectFunction(String query,String schema) {
    if (!validateQuery.selectQueryValidate(query)) {
      System.err.println("Incorrect query");
      return false;
    }

    //get table name from query
    String tableName = "";
    Pattern pattern = Pattern.compile("(from|From|FROM)(.*?)(WHERE|Where|where|;)", Pattern.DOTALL);
    Matcher matcher = pattern.matcher(query);
    String[] findTableName = new String[0];
    while (matcher.find()) {
      findTableName = matcher.group().split("\s+");
      tableName = findTableName[1].replaceAll("\\s+", "").replaceAll(";", "");
    }

    try {
      String filePath = "schemas\\"+schema+"\\" + tableName+".txt";
      BufferedReader reader = new BufferedReader(new FileReader(filePath));

      //Read row values from text file
      String tempLine = null;
      while ((tempLine = reader.readLine()) != null) {
        dataRowList.add(tempLine);
      }
      reader.close();

      //type of Select Query
      if (query.contains("*") && (query.contains("where") || query.contains("WHERE"))) {
        selectStarAndWhere(query);
      } else if (query.contains("*")) {
        selectStar();
      } else if ((query.contains("where") || query.contains("WHERE")) && !query.contains("*")) {
      } else {
        selectColumns(query);
      }
    } catch (FileNotFoundException e) {
      System.out.println("File does not exists....");
    } catch (IOException e) {
      System.out.println("I/O Error....");
    } catch (IndexOutOfBoundsException e) {
      System.err.println("Error Occurred : check your table");
      e.printStackTrace();
    }

    return true;
  }

  private boolean selectStar() {

    for (String data : dataRowList) {
      data = data.replaceAll(",", "   ");
      System.out.println(data);
    }
    return true;
  }

  private boolean selectStarAndWhere(String query) {

    //get condition
    String whereCondition = "";
    Pattern pattern = Pattern.compile("(WHERE|Where|where)(.*?)(;)", Pattern.DOTALL);
    Matcher matcher = pattern.matcher(query);
    List<String> resultantRowList = new ArrayList<>();

    while (matcher.find()) {
      String[] findCondition = matcher.group().split("\s+");
      whereCondition = findCondition[1].replaceAll("(\\s+|;|\')", "");
    }

    //get satisfied row
    for (String row : dataRowList) {
      if (row.contains(whereCondition)) {
        resultantRowList.add(row);
      }
    }

    //display resultant row
    for (String row : resultantRowList) {
      String tempRow = row.replaceAll(",", "\t");
      System.out.println(tempRow);
    }

    return true;
  }


  private boolean selectColumns(String query) {

    Pattern pattern = Pattern.compile("(select|Select|SELECT)(.*?)(from|From|FROM)", Pattern.DOTALL);
    Map<String, List<String>> rowMap = new HashMap<>();
    Matcher matcher = pattern.matcher(query);
    String[] array = new String[0];
    while (matcher.find()) {
      array = matcher.group(2).split(",");
    }
    List<String> userColumnList = new ArrayList<>();
    for (String object : array) {
      object = object.replaceAll("\\s+", "");
      userColumnList.add(object);
    }

    //Divide row into columns per row
    for (int i = 0; i < dataRowList.size(); i++) {
      String[] tempArray = dataRowList.get(i).split(",");
      List<String> tempList = new ArrayList<>(Arrays.asList(tempArray));
      rowMap.put("row " + Integer.toString(i), tempList);
    }

    //get index value of user column in the table
    List<String> indexList = rowMap.get("row 2");
    List<Integer> indexOfUserColumn = new ArrayList<>();

    for (String columnName : userColumnList) {
      for (String dataInlist : indexList) {
        if (dataInlist.contains(columnName)) {
          int x = indexList.indexOf(dataInlist);
          if (!indexOfUserColumn.contains(x))
            indexOfUserColumn.add(x);
        }
      }
    }

    //Display final result
    for (Map.Entry<String, List<String>> element : rowMap.entrySet()) {
      if (element.getValue().get(0).contains("=")) {
        for (int index : indexOfUserColumn) {
          System.out.print(element.getValue().get(index) + "\t");
        }
        System.out.println();
      }
    }

    return true;
  }
}
