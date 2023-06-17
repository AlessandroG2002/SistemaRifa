import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws NotBoundException, UnknownHostException, IOException {

        // Criação da rifa
        String url = "rmi://localhost:1099/database";
        Database database = (Database) Naming.lookup(url);
        Scanner scanner = new Scanner(System.in);
        boolean valueExists = false;
        String nome;
        int numero;

        System.out.println("Digite o seu nome para a rifa:");
        nome = scanner.nextLine();
        
        while (valueExists == false) {
            System.out.println("Digite o número desejado:");
            numero = scanner.nextInt();

            // Envio da rifa
            valueExists = database.insertOrUpdate("key "+nome, "value "+numero);

            // Verificação se o número já foi escolhido
            if ( valueExists == false)
            {
                System.out.println("Número já registrado! Escolha outro número.");
            }
            else
            {
                System.out.println("Rifa enviada com sucesso! - Nome: "+nome+" | Número: "+numero);
            }
        }
        
        // Recepção da resposta
        Socket socket = new Socket("localhost", 12345);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // Criando conexão ao enviar nome pro socket do server
        out.println(nome);

        // Aguardando resposta do servidor
        System.out.println("Aguardando resultado...");
        String resposta = in.readLine();
        System.out.println(resposta);

        // Fechar tudo
        in.close();
        out.close();
        socket.close();
    }
}