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

public class SendCommandHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange he) throws IOException {
        System.out.println("SendCommandHandler-start");
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> parameters = new HashMap<>();
                    OutputStream os = he.getResponseBody();
                    String response;
                    InputStreamReader isr = null;
                    isr = new InputStreamReader(he.getRequestBody(), "utf-8");
                    BufferedReader br = new BufferedReader(isr);
                    String query = br.readLine();
                    Main.parseQuery(query, parameters);
                    if (parameters.containsKey("commandDevice")) {
                        String command = (String) parameters.get("command");
                        Main.getUnregisteredDevices().get(0).setAction(command);
                        response = "ok";
                        he.sendResponseHeaders(200, response.length());
                        os.write(response.getBytes());
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println("SendCommandHandler-end");
    }
}
