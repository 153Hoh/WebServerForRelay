import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GetDataHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange he) throws IOException {
        System.out.println("GetDataHandler-run");
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> parameters = new HashMap<>();
                    OutputStream os = he.getResponseBody();
                    String response;
                    InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
                    BufferedReader br = new BufferedReader(isr);
                    String query = br.readLine();
                    Main.parseQuery(query, parameters);
                    if(parameters.containsKey("getmydevices")){
                        String user = (String)parameters.get("username");
                        for(User u:Main.getUsers()){
                            if(u.getUserName().equalsIgnoreCase(user)){
                                JSONObject data = new JSONObject();
                                for(Device d:u.getUserDevices()){

                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
