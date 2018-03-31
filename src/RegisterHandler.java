import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterHandler implements HttpHandler {

    @Override

    public void handle(HttpExchange he) throws IOException {
        System.out.println("RegisterHandler-start");
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                Map<String, Object> parameters = new HashMap<>();
                OutputStream os = he.getResponseBody();
                String response = "";
                InputStreamReader isr = null;
                isr = new InputStreamReader(he.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String query = br.readLine();
                System.out.println(query);
                Main.parseQuery(query, parameters);
                for (Map.Entry<String, Object> a : parameters.entrySet()) {
                    System.out.println("    " + a.getKey() + ":" + a.getValue());
                }
                if (parameters.containsKey("registerHand")) {
                    System.out.println("registerHandler");
                    String id = getSaltString();
                    String ip = (String) parameters.get("ip");
                    String deviceType = (String) parameters.get("devicetype");
                    Main.addDevice(new Device(id, ip, deviceType, true, ""));
                    response = "id:" + id;
                    he.sendResponseHeaders(200, response.length());
                    os.write(response.getBytes());
                    os.close();
                }else if (parameters.containsKey("registerCont")) {
                    System.out.println("registerController");
                    String name = (String) parameters.get("name");
                    String pass = (String) parameters.get("pass");
                    Main.addUsers(new User(name, pass));
                    response = "done";
                    he.sendResponseHeaders(200, response.length());
                    os.write(response.getBytes());
                    os.close();
                } else {
                    System.out.println("NEM");
                    he.sendResponseHeaders(404, response.length());
                    os.write(response.getBytes());
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println("RegisterHandler-end");
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
