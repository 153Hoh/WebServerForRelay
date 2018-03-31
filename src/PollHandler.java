
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PollHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        System.out.println("PollHandler-start");
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                Map<String, Object> parameters = new HashMap<>();
                OutputStream os = he.getResponseBody();
                String response;
                InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String query = br.readLine();
                Main.parseQuery(query, parameters);
                if(parameters.containsKey("id")){
                    String id = (String) parameters.get("id");
                    System.out.println(id);
                    for(Device d:Main.getUnregisteredDevices()){
                        if(d.getDeviceId().equalsIgnoreCase(id)){
                            System.out.println("Megvan");
                            int i = 0;
                            while(i<=3) {
                                System.out.println(d.getDeviceId() + ";;;" + d.getAction());
                                if (!d.getAction().equals("")) {
                                    System.out.println("ACTION");
                                    response = d.getAction();
                                    he.sendResponseHeaders(200, response.length());
                                    os.write(response.getBytes());
                                    os.close();
                                    d.setAction("");
                                    return;
                                } else {
                                    System.out.println("Vár");
                                    Thread.sleep(2000);
                                    i++;
                                }
                            }
                        }

                    }
                    System.out.println("SEMMI");
                    response = "";
                    he.sendResponseHeaders(200, response.length());
                    os.write(response.getBytes());
                    os.close();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println("PollHandler-end");
    }

}
