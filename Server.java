import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static void main(String[] args) {
        List<Socket> clientSockets = new ArrayList<>();
        int clientesMax = 3;

        try {

            // Criação do database
            DatabaseImpl databaseImpl = new DatabaseImpl();

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("database", databaseImpl);

            System.out.println("Server iniciado...");

            // Criação do socket
            ServerSocket serverSocket = new ServerSocket(12345);

            // Enquanto o socket estiver aberto...
            while (true) {
                Socket clientSocket = serverSocket.accept();
                BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String clientConectado = br.readLine();

                // Adiciona o socket do cliente na lista
                clientSockets.add(clientSocket);
                System.out.println("Nova conexão: "+clientConectado);

                    // Verifica se atingiu o número máximo de clientes
                    if (clientSockets.size() == clientesMax) {
                        // Realiza o sorteio
                        System.out.println("Definindo vencedor...");
                        String winner = databaseImpl.drawWinner();
                        winner = winner.replace("key","");
                        System.out.println("Vencedor definido:"+winner);

                        // Notifica todos os cliente e fecha o socket
                        System.out.println("Notificando clientes...");
                        notifyWinner(winner, clientSockets);
                        System.out.println("Clientes notificados!");

                        // Reinicia tudo para realizar novo sorteio
                        databaseImpl.reset();
                        clientSockets.clear();
                        System.out.println("--- Sorteio reiniciado ---");
                    }
            }

        } catch (Exception e) {
            System.err.println("Exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private static void notifyWinner(String winner, List<Socket> clientSockets) {
        // Roda em todos os sockets abertos
        for (Socket clientSocket : clientSockets) {
            try {
                // Envia a mensagem de quem é o vencedor
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                writer.println("O vencedor da rifa é" +winner);
                writer.close();
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("IOException: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}