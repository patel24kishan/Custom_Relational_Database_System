package Main.QueryFunctions;

import java.io.File;
import java.util.Locale;
import java.util.Map;

public class DropClass {

  String schemaPath = "schemas\\";

  public boolean DropTable (String userQuery,String schema) {
    String key="";
    String tablename="";
    boolean status=false;
    ParseQuery parseQuery=new ParseQuery();
    schemaPath+=schema+"\\";
    Map<String,String> query=parseQuery.processQuery(userQuery);
    for(Map.Entry<String, String> entry:query.entrySet()){
      key+=entry.getKey()+ " ";
    }
    if((key.contains("DROP"))&&(key.contains("TABLE"))){
      tablename=query.get("TABLE");
    }
    File file=new File(schemaPath+=tablename.toLowerCase( ).trim()+".txt");
    if(file.delete()){
      status=true;
    }
    return status;
  }


}
