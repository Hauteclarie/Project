package clientCerver;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class server {
    private static final int PORT = 12345;
    private static final int MAX_CLIENTS = 5;
    private static final Map<String, List<? extends Serializable>> data = new HashMap<>();
    private static final Set<Integer> activeClients = ConcurrentHashMap.newKeySet();
    private static final Random random = new Random();

    public static void main(String[] args) {
        // Initialize data
        data.put("Kot", Arrays.asList(new Kot("Kot_1"), new Kot("Kot_2"), new Kot("Kot_3"), new Kot("Kot_4")));
        data.put("Pies", Arrays.asList(new Pies("Pies_1"), new Pies("Pies_2"), new Pies("Pies_3"), new Pies("Pies_4")));
        data.put("Rybka", Arrays.asList(new Rybka("Rybka_1"), new Rybka("Rybka_2"), new Rybka("Rybka_3"), new Rybka("Rybka_4")));

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serwer uruchomiony na porcie " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                 ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

                int clientId = in.readInt();
                if (activeClients.size() >= MAX_CLIENTS) {
                    out.writeObject("REFUSED");
                    System.out.println("Client " + clientId + " refused");
                } else {
                    activeClients.add(clientId);
                    out.writeObject("OK");
                    System.out.println("Client " + clientId + " connected");

                    for (int i = 0; i < 3; i++) {
                        Thread.sleep(random.nextInt(5000)); // Random delay

                        String request = (String) in.readObject();
                        String className = request.split("_")[1];
                        List<? extends Serializable> objects = data.getOrDefault(className, (List<? extends Serializable>) List.of(new Object()));

                        out.writeObject(new ArrayList<>(objects));
                        System.out.println("Sent " + objects + " to client " + clientId);
                    }

                    activeClients.remove(clientId);
                    System.out.println("Client " + clientId + " disconnected");
                }
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}