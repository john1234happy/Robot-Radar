import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class RobotManager extends Thread
{
    TCPClient client;
    CommandInterpreter CI;
    boolean stopThread;

    public RobotManager()
    {
        CI = new CommandInterpreter();
        client = new TCPClient();
    }
    
    @Override
    public void run() //this will be the main thread for handling recive command
    {
        String command;
        stopThread = false;

        try
        {
            while(!stopThread)
            {
                if(isConnected())
                {
                    command = handleRecivePackage();
                    
                    //for testing 
                    if(command != null)
                        System.out.println("recive: " + command);
                    
                    if(command.compareTo("disconnect") == 0 || command.compareTo("EXIT") == 0)
                    {
                        stopThread = true;
                    }
                }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

    }

    public static void main(String[] args) //main don't do anything at the moment due to missing file
    {
        RobotManager robotManager = new RobotManager();

        //create UI
        //...

        bashTest(robotManager);
    }

    /**
     * this function is just for testing purpose
     * @param robotManager the robot manager it self 
     */
    private static void bashTest(RobotManager robotManager)
    {
        robotManager.tryConnect("192.168.1.15");

        Scanner scanner = new Scanner(System.in);
        String command  = "";

        while(robotManager.isConnected())
        {
            System.out.print("Send command: ");
            command = scanner.nextLine();
            String argument = "";
            boolean send = true;
            if(command.indexOf("(") > 0)
            {
                argument = command.substring(command.indexOf("(")).trim();
                command = command.substring(0, command.indexOf("("));
            }
            
            switch(command.toUpperCase())
            {
                case "W":
                    command = "moveForward";
                    break;
                case "S":
                    command = "moveBackward";
                    break;
                case "A":
                    command = "rotateLeft";
                    break;
                case "D":
                    command = "rotateRight";
                    break;
                case "H":
                    command = "Halt";
                    break;
                case "T":
                    command = "turnHead" + argument;
                    break;
                case "STOPSERVER":
                    command = "stopServer";
                    break;
                default:
                    send = false;
                    break;
            }

            if(send)
                robotManager.trySendCommand(command);
            
            if(command.compareTo("EXIT") == 0)
                break;
            
        }
        
        scanner.close();
    }

    /**
     * this function will check if the client currently connected to any server
     * @return true of connected else false
     */
    public boolean isConnected()
    {
        if(client.socket == null)
            return false;
        else if(!client.socket.isConnected() || client.socket.isClosed())
            return false;
        else
            return true;
    }

    /**
     * this function will connect with server base on hostIP and begin thread to handle recive command
     * @param hostIP the IP address of server
     * @throws Exception throw exception when can't connect to any server
     */
    public void connect(String hostIP) throws Exception
    {
        client.host = hostIP;
        client.connect();

        if(isConnected())
            this.start();
    }

    /**
     * this function will attempt to connect with server base on hostIP and begin thread to handle recive command
     * @param hostIP the IP address of server
     * @return return true if connect is complete else false
     */
    public boolean tryConnect(String hostIP)
    {
        try
        {
            connect(hostIP);
            return true;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * this function will disconnect current connection
     * @throws Exception exception throw when can't disconnect
     */
    public void disconnect() throws Exception
    {
        this.stopThread = true;
        client.close();
    }

    /**
     * this function will attemp to disconnect current connection
     * @return return true if disconnect successful else false
     */
    public boolean tryDisconnect()
    {
        try
        {
            disconnect();
            return true;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * this function will handle recive json backage from server
     * @return command after interpreter
     * @throws Exception exception throw when there is a problem with pipe line when trying to get data from TCP
     */
    private String handleRecivePackage() throws Exception
    {
        //this will be send to Robot data interpreter
        String jsonPackage = client.RecieveFromServer();

        //after that grap the list from DI and pass it into a function call in UI (update user interface)
        if(jsonPackage != null)
        {
            //send to robot data interpreter
            
            
        }

        return jsonPackage;
    }

    /**
     * this function will interpreting keypress into command and send it to server
     * @param evt keystoke event
     */
    public void sendCommand(java.awt.event.KeyEvent evt)
    {
        //send evt key to CI to interpreting into json package
        String jsonPackage = CI.InterpretingCommand(evt);
        //call sendcommand function to send it to client
        trySendCommand(jsonPackage);
    }

    /**
     * this function will interpreting command into json package and send it to server
     * @param command command to be interpreting and send
     * @throws Exception throw exception when unable to send to server
     */
    public void SendCommand(String command) throws Exception
    {
        //interpreting command to jsonpackage
        String jsonPackage = CI.InterpretingCommand(command);

        //send jsonPackge to dataInterpreter
        //... TPA

        //send jsonpackage to server
        client.SendToServer(jsonPackage);
    }

    /**
     * this function will attemp to interpreting command into json package and send it to server
     * @param command command to be interpreting and send
     * @return true if send complete else false
     */
    public boolean trySendCommand(String command)
    {
        try
        {
            SendCommand(command);
            return true;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
    }

    

    
    //---------------------------------------------------------------------------------------------

    private class TCPClient 
    {
        //String name="";
        String host = "localhost";
        int port = 1111;
        private Socket socket = null;

        private PrintWriter outToServer;
        private BufferedReader inFromServer;
    
        TCPClient(String _host, int _port)
        {
            host = _host;
            port = _port;
        }

        TCPClient()
        {
            
        }

        /**
         * this function will send data to server
         * @param data data to be send
         */
        void SendToServer(String data)
        {
            
            //send msg to server
            outToServer.print(data + '\n');
            outToServer.flush();
        }
        
        /**
         * this function will return data recive from server
         * @return data recive from server
         * @throws Exception throw exception when unable to recive data
         */
        String RecieveFromServer() throws Exception
        {
            return inFromServer.readLine();
        }

        /**
         * this function will connect socket to server
         * @throws Exception throw exception when unable to connect to server
         */
        void connect() throws Exception
        {
            if(socket == null || !socket.isConnected() || socket.isClosed())
            {
                socket = new Socket(host, port);
                outToServer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                inFromServer = new BufferedReader(new InputStreamReader (socket.getInputStream()));
            }
            else
                throw new Exception("Server is already connected");
        }
        
        /**
         * this function will close all connection from current server
         * @throws Exception exception throw when server is not connected or there something wrong with establising read data from server
         */
        void close() throws Exception
        {
            if(socket.isConnected())
            {
                outToServer.close();
                inFromServer.close();
                socket.close();
            }
            else
                throw new Exception("Server is not connected");
        }
    }
}
