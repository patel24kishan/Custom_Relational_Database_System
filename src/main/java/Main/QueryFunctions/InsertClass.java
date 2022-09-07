package Main.QueryFunctions;

import java.util.*;
import java.io.*;

public class InsertClass {
  String schemaPath = "schemas\\";

   public List<String> getColumnList ( Map<String,String> query) {
    List<String> col=new ArrayList<>();
    String tableColList=query.get("INTO").trim() ;
    if((tableColList.contains("("))&&(tableColList.contains(")"))) {
      char[] wordArray = new char[tableColList.length()];
      int indexOpenBrace = 0;
      int indexCloseBrace = 0;
      String column = "";
      for (int i = 0; i < tableColList.length(); i++) {
        wordArray[i] = tableColList.charAt(i);
        if (Character.valueOf(tableColList.charAt(i)).equals('(')) {
          indexOpenBrace = i;
        }
        if (Character.valueOf(tableColList.charAt(i)).equals(')')) {
          indexCloseBrace = i;
        }
      }

      for (int i = indexOpenBrace + 1; ((i > indexOpenBrace) && (i <= indexCloseBrace)); i++) {
        if (Character.isLetterOrDigit(wordArray[i])) {
          column += wordArray[i];
        }
        else if(Character.valueOf(wordArray[i]).equals('$')){
          column += wordArray[i];
        }
        else if (Character.valueOf(wordArray[i]).equals(',')) {
          col.add(column);
          column = "";
        } else if (Character.valueOf(wordArray[i]).equals(')')) {
          col.add(column);
          column = "";
        }
      }
    }
    else{
      col.add("*");
    }
    return col;
  }

  public List<String> getValues ( Map<String,String> query) {
    List<String> values=new ArrayList<>();
    String valueString=query.get("VALUES").trim() ;
    String value="";
    for (int i = 0; i < valueString.length(); i++) {
      if(Character.isLetterOrDigit(valueString.charAt(i))) {
        value += valueString.charAt(i);
      }
      else if(Character.valueOf(valueString.charAt(i)).equals(',')){
        values.add(value);
        value="";
      }
      else if(Character.valueOf(valueString.charAt(i)).equals(')' )){
        values.add(value);
        value="";
      }
    }

    return values;
  }

  public String returnSchemaColumn (String tableName ) {
     String colLine="";
    String lineCol="";
    try {
      schemaPath += tableName.toLowerCase().trim()+".txt";
      File myObj = new File(schemaPath);
      Scanner reader = new Scanner(myObj);
      while(reader.hasNextLine()){
        colLine+=reader.nextLine()+"\n";
      }

      String[] colName=colLine.split("\n");
      lineCol=colName[0];
    }
    catch(FileNotFoundException e){
        System.out.println("Table not found.");
    }
    return lineCol;
  }

  public boolean pkCheck(String entry  ){
     String tableRow="";
     boolean status=true;
    try {
      //schemaPath += tablename.toLowerCase().trim() ;
      File myObj = new File(schemaPath);
      Scanner reader = new Scanner(myObj);
      while(reader.hasNextLine()){
        tableRow+=reader.nextLine() ;
        if(tableRow.contains(entry)){
          status=false;
          break;
        }
      }

    }
    catch(FileNotFoundException e){
      e.getMessage();
    }
    return status;
  }
  public boolean insertWritetoFile(List<String> schemaCol, List<String> valueList,String tablename) {
     String entry="";
     boolean status=false;
     String pkColumn="";

     for (int i=0;i<schemaCol.size();i++) {
       if(i==schemaCol.size()-1) {
         entry += schemaCol.get(i) + "=" + valueList.get(i) ;
       }
       else{
         entry += schemaCol.get(i) + "=" + valueList.get(i)+"," ;
       }
       if(schemaCol.get(i).contains("$")){
         pkColumn=schemaCol.get(i)+ "=" + valueList.get(i);
       }
     }
    if(pkCheck(pkColumn )){
       try {
         FileWriter file = new FileWriter(schemaPath.toLowerCase().trim()  ,true);
         file.write("\n"+entry);
         file.close();
         status=true;
       }
       catch(IOException e){
         e.getMessage();
       }
    }
     return status;
  }

  public boolean insertTable (String query,String schema ) {

     schemaPath+=schema+"\\";
    ParseQuery parseQuery = new ParseQuery();
    String tableName ;
    Map<String, String> queryMap ;
    List<String> colList;
    List<String> valueList;
    String schemaTable="";
    List<String> schemaCol;
    queryMap = parseQuery.processQuery(query);
    tableName = parseQuery.getInsertTableName(queryMap);
    boolean status=false;
    if(!tableName.equals("")) {
      schemaTable = returnSchemaColumn(tableName);
      schemaCol = Arrays.asList(schemaTable.split(","));
      colList = getColumnList(queryMap);
      valueList = getValues(queryMap);
      if( (colList.size() == 1)&&(colList.get(0)=="*")) {
        if (schemaCol.size() == valueList.size()) {
          status = insertWritetoFile(schemaCol, valueList, tableName);
        }
      }
      else if ((colList.size()) == (valueList.size())) {
        status=insertWritetoFile(colList, valueList, tableName);
      }
    }
    return status;

  }

}

