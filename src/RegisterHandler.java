import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RegisterHandler implements HttpHandler {

    @Override

    public void handle(HttpExchange he) throws IOException {
        // parse request
        Map<String, Object> parameters = new HashMap<>();
        OutputStream os = he.getResponseBody();
        String response = "";
        InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        Main.parseQuery(query, parameters);
        if(parameters.get("register").toString().equalsIgnoreCase("register")){
            System.out.println("register");
            String id = getSaltString();
            String ip = (String) parameters.get("ip");
            String deviceType = (String) parameters.get("devicetype");
            Main.addDevice(new Device(id, ip, deviceType, true));
            response = "id:" + id;
            he.sendResponseHeaders(200, response.length());
            os.write(response.getBytes());
            os.close();
        }else{
            he.sendResponseHeaders(404,response.length());
            os.write(response.getBytes());
            os.close();
        }
    }

    private String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 4) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();

    }
}
