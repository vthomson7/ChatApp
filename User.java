import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.*;




public class User extends Thread {
    
    // The user socket
    private static Socket userSocket = null;
    // The output stream
    private static PrintStream output_stream = null;
    // The input stream
    private static BufferedReader input_stream = null;
    
    private static BufferedReader inputLine = null;
    
    
    public static void main(String[] args) {
        
        // The default port.
        int portNumber = 8000;
        // The default host.
        String host = "localhost";
        

        /*
         * Open a socket on a given host and port. Open input and output streams.
         */

        //YOUR CODE
        
        if (args.length < 2) {
          System.out.println("usage: java <host> <portnumbers> \n" + "Now using host =" + host + ", portnumbers");
        } else {
          host = args[0];
          portNumber = Integer.valueOf(args[1]).intValue();
        }
        
        try {
        userSocket = new Socket(host, portNumber);
        
        inputLine = new BufferedReader(new InputStreamReader(System.in));
        
        output_stream = new PrintStream(userSocket.getOutputStream());
        
        input_stream = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
        
        } catch (IOException e) {
          System.err.println("Couldn't get I/O for the connection to the host "
                               + host);
        }

        

 // YOUR CODE
        try {
          if (userSocket != null && input_stream != null && output_stream != null) {
            Thread listener = new Thread(new User()); //reads from the server
            listener.start();
            
            while (true) {
              output_stream.println(inputLine.readLine());
              if (listener == null) break;
            }
            
            output_stream.close();
            input_stream.close();
          }
        } catch (IOException e) {
          System.err.println("Couldn't get I/O for the connection to the host "
                               + host);
        }
    }
    

    public void run() {

      //YOUR CODE
      try {
       
        while (true) {
          String incomingMsg = input_stream.readLine();
          System.out.println(incomingMsg);

          if (incomingMsg.startsWith("### Bye ...")) {
            output_stream.close();
            input_stream.close();
            userSocket.close();
            break;
          }
        }


      } catch (IOException e) {
        System.err.println("IOException:  " + e);
      } 
      
    }
}


