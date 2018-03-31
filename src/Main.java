import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static String SERVER_IP;
    private final static int SERVER_PORT = 9000;

    static List<Device> unregisteredDevices;
    static List<User> users;

    static final String[] MAIN_MENU = new String[]{ "1. igen",
                                                    "2: Órarend megtekintés",
                                                    "3: end"};


    public static void main(String[] args) {
        new Main().init();
        new Main().service();
    }

    private void init(){
        try {
            SERVER_IP = InetAddress.getAllByName("153Hoh")[1].getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        unregisteredDevices = new ArrayList<>();
        users = new ArrayList<>();
    }

    private void service() {
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(SERVER_IP, SERVER_PORT), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (server != null) {
            System.out.println("Server started at " + SERVER_IP + ":" + SERVER_PORT);
            server.createContext("/", new RootHandler());
            server.createContext("/asd", new EchoHeaderHandler());
            server.createContext("/Ping", new PingHandler());
            server.createContext("/Register", new RegisterHandler());
            server.createContext("/Poll", new PollHandler());
            server.createContext("/GetData", new GetDataHandler());
            server.createContext("/SendCommandToDevice", new SendCommandHandler());
            server.setExecutor(null);
            server.start();
        } else {
            System.out.println("Server start failed!");
        }

        /*ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                boolean finished = false;
                while(!finished){
                    int cmd = SelectCommand("Fő menű:", MAIN_MENU);
                    switch(cmd){
                        case 1:
                            for(Device d: unregisteredDevices){
                                System.out.println(d.getDeviceId() + ":" + d.getDeviceIp() + ":" + d.getDeviceType());
                            }
                            break;
                        case 2:
                            unregisteredDevices.get(0).setAction("relayOn");
                            System.out.println(unregisteredDevices.get(0).getDeviceId() + ":" + unregisteredDevices.get(0).getAction());
                            break;
                        case 3:
                            unregisteredDevices.get(0).setAction("relayOff");
                            System.out.println(unregisteredDevices.get(0).getDeviceId() + ":" + unregisteredDevices.get(0).getAction());
                            break;
                        case 4: finished = true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });*/
    }

    public static void addDevice(Device device){
        unregisteredDevices.add(device);
    }

    public static void addUsers(User u){
        users.add(u);
    }

    public static List<Device> getUnregisteredDevices(){
        return unregisteredDevices;
    }

    public static List<User> getUsers(){
        return users;
    }

    public static int SelectCommand(String menuTitle, String[] menu) throws IOException{
        System.out.println(menuTitle);
        for (String item:menu)
            System.out.println(item);
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Írja be a parancsot: ");
        String command = input.readLine();
        int cmd = -1;
        try{
            cmd = Integer.valueOf(command);
        }catch(NumberFormatException e){
            System.out.println("Nem sikerült a parancs értelmezése!");
        }
        return cmd;
    }

    public static void parseQuery(String query, Map<String, Object> parameters) throws UnsupportedEncodingException {

        if (query != null) {
            String pairs[] = query.split("[&]");
            for (String pair : pairs) {
                String param[] = pair.split("[=]");
                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0],
                            System.getProperty("file.encoding"));
                }

                if (param.length > 1) {
                    value = URLDecoder.decode(param[1],
                            System.getProperty("file.encoding"));
                }

                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);
                    if (obj instanceof List<?>) {
                        List<String> values = (List<String>) obj;
                        values.add(value);

                    } else if (obj instanceof String) {
                        List<String> values = new ArrayList<String>();
                        values.add((String) obj);
                        values.add(value);
                        parameters.put(key, values);
                    }
                } else {
                    parameters.put(key, value);
                }
            }
        }
    }
}
