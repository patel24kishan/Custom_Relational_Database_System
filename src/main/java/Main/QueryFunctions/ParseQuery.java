package Main.QueryFunctions;

import java.io.File;
import java.util.*;

public class ParseQuery {

  List<String> commandList = new ArrayList<>();
  Map<String,String> breakdown = new HashMap<>();

  public ParseQuery(){}
  private void keyList() {
    /* create a list of keywords*/
    commandList.add("SELECT");
    commandList.add("FROM");
    commandList.add("WHERE");
    commandList.add("USE");
    commandList.add("INSERT");
    commandList.add("INTO");
    commandList.add("VALUES");
    commandList.add("UPDATE");
    commandList.add("SET");
    commandList.add("DELETE");
    commandList.add("DROP");
    commandList.add("CREATE");
    commandList.add("TABLE");
  }

  public Map<String,String> processQuery (String query) {

    String key="";
    String value="";
    keyList();
    String queryList[]=query.split(" ");
    for(String item:queryList){
      if(commandList.contains(item.toUpperCase( ))){
        if(!key.equals("")){
          breakdown.put(key.toUpperCase(), value.toUpperCase( ));
          key=item;
          value="";
        }
        else{
          key=item;
        }
      }
      else{
        value+=item+" ";
      }
    }
    breakdown.put(key.toUpperCase( ), value.toUpperCase( ));
    return breakdown;
  }

  public String getInsertTableName ( Map<String,String> query) {
    String tableColList=query.get("INTO").trim() ;
    char[] wordArray=new char[tableColList.length()];
    String tablename="";
    if((tableColList.contains("("))&&(tableColList.contains(");"))){
      for(int i=0; i<tableColList.length();i++){
        wordArray[i]=tableColList.charAt(i);
      }
      for(char l:wordArray){
        if (!Character.valueOf(l).equals('(') ) {
          tablename+=l;
        }
        else{
          break;
        }
      }
    }
    else{
      tablename=tableColList;
    }
    return tablename;
  }


}
