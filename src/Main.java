import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private final static String SERVER_IP = "192.168.6.16";
    private final static int SERVER_PORT = 9000;

    static List<Device> devices;

    static final String[] MAIN_MENU = new String[]{ "1. igen",
                                                    "2: Órarend megtekintés",
                                                    "3: end"};


    public static void main(String[] args) {
        new Main().init();
        new Main().service();
    }

    private void init(){
        devices = new ArrayList<>();
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
            server.createContext("/Ping", new PingHandler());
            server.createContext("/Register", new RegisterHandler());
            server.setExecutor(null);
            server.start();
        } else {
            System.out.println("Server start failed!");
        }

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                boolean finished = false;
                while(!finished){
                    int cmd = SelectCommand("Fő menű:", MAIN_MENU);
                    switch(cmd){
                        case 1:
                            for(Device d:devices){
                                System.out.println(d.getDeviceId() + ":" + d.getDeviceIp() + ":" + d.getDeviceType());
                            }
                            break;
                        case 2: break;
                        case 3: finished = true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void addDevice(Device device){
        devices.add(device);
    }

    public static List<Device> getDevices(){
        return devices;
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
