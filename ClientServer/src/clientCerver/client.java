package clientCerver;

import java.io.*;
import java.net.*;
import java.util.List;

public class client {
    private static final String HOST = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            int clientId = new java.util.Random().nextInt(1000);
            out.writeInt(clientId);
            out.flush();

            String response = (String) in.readObject();
            if ("REFUSED".equals(response)) {
                System.out.println("Client " + clientId + " refused");
                return;
            } else if ("OK".equals(response)) {
                System.out.println("Client " + clientId + " connected");

                for (String className : new String[]{"get_Kot", "get_Pies", "get_Rybka"}) {
                    out.writeObject(className);
                    out.flush();

                    try {
                        List<?> objects = (List<?>) in.readObject();
                        System.out.println("Client " + clientId + " received: " + objects);
                    } catch (ClassCastException e) {
                        System.out.println("Client " + clientId + " received unexpected data type for " + className);
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
