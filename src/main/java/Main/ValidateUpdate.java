package Main;

public class ValidateUpdate {

    public boolean isValid(String query){

String[] splitQuery = query.split(" ");
if(splitQuery.length!=6){
    System.out.println("Intendation syntax error");

    return false;
}
if(!splitQuery[2].equals("set")){
    System.out.println("Syntax error");
    return false;
}
String[] tmp=splitQuery[3].split("=");
if(tmp[0].isEmpty()||tmp[1].isEmpty()){
    System.out.println("Syntax error");
    return  false;
}
        if(!splitQuery[4].equals("where")){
            System.out.println("Syntax error");
            return false;
        }
        String[] tmp1=splitQuery[5].split("=");
        if(tmp1[0].isEmpty()||tmp1[1].isEmpty()){
            System.out.println("Syntax error");
            return  false;
        }

        return true;
    }
}
