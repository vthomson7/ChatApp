import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.*;



/*
 * A chat server that delivers public and private messages.
 */
public class Server {
    
    // Create a socket for the server 
    private static ServerSocket serverSocket = null;
    // Create a socket for the user 
    private static Socket userSocket = null;
    // Maximum number of users 
    private static int maxUsersCount = 5;
    // An array of threads for users
    private static userThread[] threads = null;
    


    public static void main(String args[]) {
      
      // The default port number.
      int portNumber = 8000;
      
      threads = new userThread[maxUsersCount];
      
      
      //YOUR CODE
      if (args.length < 2) {
        System.out.println("usage: java <host> <portnumbers> \n" + "Now using port number =" + portNumber);
      } else {
        portNumber = Integer.valueOf(args[1]).intValue();
      }      
      
      try {
        serverSocket = new ServerSocket(portNumber);
        
        System.out.println("Listening");
        int numUsers = 0;
        
        while (true) {
          int j = 0;
          for (int i = 0; i < threads.length; i++) {
            if (threads[i] != null) {
              j++;
            }
          }
          numUsers = j;
          userSocket = serverSocket.accept();
          
          if (numUsers < maxUsersCount) {
            for (int i = 0; i < maxUsersCount; i++) {
              if (threads[i] == null) {
                (threads[i] = new userThread(userSocket, threads)).start();
                numUsers++;
                System.out.println("Connected");
                break;
              } 
            }
          } else {
            System.out.println("Server is busy. Sorry.");
            userSocket.close();
          }
        }
        
      } catch (IOException e) {
        System.err.println("Couldn't get I/O for the connection to the host.");
      }
    }
}

      
      /*
       * Create a user socket for each connection and pass it to a new user
       * thread.
       */
      
      
      //YOUR CODE

/*
 * Threads
 */
class userThread extends Thread {
  
  private String userName = null;
  private BufferedReader input_stream = null;
  private PrintStream output_stream = null;
  private Socket userSocket = null;
  private final userThread[] threads;
  private int maxUsersCount;
  
  // only relevant for Part IV: adding friendship
  ArrayList<String> friends = new ArrayList<String>();
  ArrayList<String> friendrequests = new ArrayList<String>();  //keep track of sent friend requests 
  //
  
  
  public userThread(Socket userSocket, userThread[] threads) {
    this.userSocket = userSocket;
    this.threads = threads;
    maxUsersCount = threads.length;
  }
  
  public void run() {
    
    /*
     * Create input and output streams for this client, and start conversation.
     */
    
    //YOUR CODE
    int maxUsersCount = this.maxUsersCount;
    
    try {
      output_stream = new PrintStream(userSocket.getOutputStream());
      
      input_stream = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
      
      output_stream.println("Enter username: ");
      
      String username = input_stream.readLine();
      
      output_stream.println("Welcome " + username + "! To exit chat, type in LogOut");
      for (int i = 0; i < maxUsersCount; i++) {
        if (threads[i] != null) {
          threads[i].output_stream.println("New user entered chat: " + username);
        }
      }
      
      
      while (true) { 
        String userInput = input_stream.readLine();
        if (userInput.equals("LogOut")) {
          output_stream.println("### Bye ..." + username + "###");
          break;
        }
        for (int i = 0; i < maxUsersCount; i++) {
          if (threads[i] != null) {
            threads[i].output_stream.println("<" + username + ">: " + userInput);
          }
        }
      }
      
      for (int i = 0; i < maxUsersCount; i++) {
        if (threads[i] != null && threads[i] != this) {
          threads[i].output_stream.println("The user " + username + " has left the chat.");
        }
      }
      
      output_stream.close();
      input_stream.close();
      userSocket.close();
    } catch (IOException e) {
      System.err.println("IOException:  " + e);
    }
    
  }
}



