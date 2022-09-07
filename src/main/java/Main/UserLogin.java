package Main;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class UserLogin {

  private Map<String, String> loginInformationMap = new HashMap<>();

  public String enterLoginCredentials() {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    String userID = null;
    String password = null;
    String providedPassword = "";

    System.out.println("\n=======================\n\tUSER LOGIN\n=======================\n");

    try {
      do {
        if (userID != null || password != null)
          System.err.println("\n*** Invalid user ID or Password ***\n");

        TimeUnit.SECONDS.sleep(2);
        System.out.print("Enter User ID : ");
        userID = reader.readLine();
        System.out.print("Enter Password : ");
        password = reader.readLine();

        providedPassword = verifyUser(userID);
      } while (!password.equals(providedPassword));
    } catch (IOException e) {
      System.err.println("I/O Error");
      return userID;
    } catch (InterruptedException e) {
      System.err.println("Interruption Occurred");
    }

    System.out.println("\n******************************\n\tACCESS GRANTED\n******************************\n");
    return userID;
  }

  private String verifyUser(String userID) {

    String[] userIDArray;
    String[] passwordArray;
    String fetchedPassword = "";

    try {
      String filePath = "LoginCredentials\\UserID_Password";
      BufferedReader reader = new BufferedReader(new FileReader(filePath));

      //Read & store UserID & password
      userIDArray = reader.readLine().split("#");
      passwordArray = reader.readLine().split("#");
      for (int i = 0; i < userIDArray.length; i++) {
        loginInformationMap.put(userIDArray[i], passwordArray[i]);
      }

      //fetched password from database
      fetchedPassword = loginInformationMap.get(userID);
    } catch (FileNotFoundException e) {
      System.err.println("File not Found");
    } catch (IOException e) {
      System.err.println("I/O Error");
    }

    return fetchedPassword;
  }
}
