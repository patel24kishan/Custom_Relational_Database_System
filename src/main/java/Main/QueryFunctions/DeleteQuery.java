package Main.QueryFunctions;

import Main.QueryValidation;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeleteQuery {

  QueryValidation validateQuery = new QueryValidation();
  private List<String> dataRow = new ArrayList<>();
  private List<List<String>> valueList = new ArrayList<>();
  private Map<String, List<String>> columnValueMap = new HashMap<>();
  private int totalElements = 0;
  private String condition = null;

  public boolean deleteFunction(String query, String schema) {
    if (!validateQuery.deleteQueryValidate(query)) {
      System.err.println("Incorrect query");
      return false;
    }
    try {

      //get table name from query
      String tableName = "";
      Pattern pattern = Pattern.compile("(from|From|FROM)(.*?)(WHERE|Where|where|;)", Pattern.DOTALL);
      Matcher matcher = pattern.matcher(query);
      String[] findTableName = new String[0];
      while (matcher.find()) {
        findTableName = matcher.group().split("\s+");
        tableName = findTableName[1].replaceAll("\\s+", "").replaceAll(";", "");
      }

      String filePath = "schemas\\"+schema+"\\" + tableName+".txt";
      BufferedReader reader = new BufferedReader(new FileReader(filePath));

      //Read column name
      String tempLine = "";
      while ((tempLine = reader.readLine()) != null) {
        dataRow.add(tempLine);
      }

      reader.close();

      // type of Delete Query
      if ((query.contains("where") || query.contains("WHERE"))) {
        String whereCondition = "";
        Pattern conditionPattern = Pattern.compile("(WHERE|Where|where)(.*?)(;)", Pattern.DOTALL);
        Matcher conditionMatcher = conditionPattern.matcher(query);

        while (conditionMatcher.find()) {
          String[] findCondition = conditionMatcher.group().split("\s+");
          whereCondition = findCondition[1].replaceAll("(\\s+|;|\')", "");
        }
        deleteWithWhere(filePath, whereCondition);
      } else {
        deleteAll(filePath);
      }

    } catch (IOException e) {
      System.out.println("IO Error");
      return false;
    } catch (IndexOutOfBoundsException e) {
      System.err.println("Error Occurred : check your table");
    }
    return true;
  }

  private void deleteAll(String filePath) {
    try {
      FileWriter fileWriter = new FileWriter(filePath, false);
      PrintWriter printWriter = new PrintWriter(fileWriter, false);
      printWriter.flush();
      printWriter.close();
      fileWriter.close();
    } catch (IOException e) {
      System.out.println("I/O exception");
    }
  }

  private void deleteWithWhere(String filePath, String condition) {
    List<Integer> indexList = new ArrayList<>();
    try {
      //find index
      for (String element : dataRow) {
        if (!element.contains(condition)) {
          int index = dataRow.indexOf(element);
          indexList.add(index);
        }
      }

      //update file
      FileWriter fileWriter = new FileWriter(filePath, false);
      PrintWriter printWriter = new PrintWriter(fileWriter, false);
      printWriter.flush();

      for (Integer dataIndex : indexList) {
        fileWriter.write(dataRow.get(dataIndex) + "\n");
      }
      fileWriter.close();
      printWriter.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}