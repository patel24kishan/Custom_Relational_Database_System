package Main;

public class QueryValidation {

  public boolean selectQueryValidate(String userQuery) {
    String selectRegex = "^(SELECT|Select|select)(\s+)([*]|(,*[a-zA-Z0-9_]+)+)(\s+)(FROM|From|from)(\s+)[a-zA-Z0-9_]+(\s*)((\s+)(WHERE|Where|where)(\s+)[a-zA-Z0-9_]+(\s*)[=](\s*)(['][a-zA-Z_ ]+[']|[0-9.]+))?;$";
    Boolean isValidQuery = userQuery.matches(selectRegex);
    return isValidQuery;
  }



  public boolean deleteQueryValidate(String userQuery) {
    String deleteRegex = "^(DELETE|Delete|delete)(\s+)(FROM|From|from)(\s+)[a-zA-Z_0-9]+(\s*)((\s+)(WHERE|Where|where)(\s+)[a-zA-Z_0-9]+(\s*)[=](\s*)(['][a-zA-Z_ ]+[']|[0-9.]+))?;$";
    Boolean isValidQuery = userQuery.matches(deleteRegex);
    return isValidQuery;
  }

}
