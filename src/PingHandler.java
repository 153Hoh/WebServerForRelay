import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class PingHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange he) throws IOException {
        Map<String, Object> parameters = new HashMap<>();
        OutputStream os = he.getResponseBody();
        String response = "";
        InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        Main.parseQuery(query, parameters);
        if(parameters.containsKey("id")){
            String id = (String) parameters.get("id");
            for(Device d:Main.getDevices()){
                if(d.getDeviceId().equalsIgnoreCase(id)){
                    d.setIsOnline(true);
                    response = "ok";
                    he.sendResponseHeaders(200, response.length());
                    os.write(response.getBytes());
                    os.close();
                    return;
                }
            }
            response = "no";
            he.sendResponseHeaders(404, response.length());
            os.write(response.getBytes());
            os.close();
        }
    }
}
