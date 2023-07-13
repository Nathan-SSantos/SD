// Classe Cliente
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    private String serverIp;
    private int serverPort;

    public Cliente(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    public void start() {
        try (
            // Conecta ao servidor
            Socket socket = new Socket(serverIp, serverPort);
            // Cria os fluxos de entrada e saída para comunicação com o servidor
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Conectado ao servidor: " + serverIp);
            
            // Interage com o servidor
            boolean running = true;

            while (running) {
                System.out.println("\nMenu:");
                System.out.println("1. Upload de arquivo");
                System.out.println("2. Listar arquivos no servidor");
                System.out.println("3. Download de arquivo");
                System.out.println("0. Sair");

                System.out.print("Digite a opção desejada: ");
                if (scanner.hasNextInt()) {
                    int option = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    switch (option) {
                        case 1:
                            // Criou um arquivo novo .java
                            // nome_do_arquivo.nome_funçao(parametros)
                            funcao_uploadFile.uploadFile(socket, scanner);
                            break;
                        case 2:
                            funcao_listFiles.listFiles(socket, serverIp, serverPort);
                            break;
                        case 3:
                            downloadFile(socket, reader, scanner);
                            break;
                        case 0:
                            running = false;
                            break;
                        default:
                            System.out.println("Opção inválida. Tente novamente.");
                    }
                } else {
                    System.out.println("Opção inválida. Tente novamente.");
                    scanner.nextLine(); // Consume the invalid input
                }
            }

            // Fecha o socket do cliente
            socket.close();
        } catch (IOException e) {
            System.err.println("Erro ao conectar ao servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void downloadFile(Socket socket, BufferedReader reader, Scanner scanner) throws IOException {
        System.out.print("Digite o nome do arquivo para download: ");
        String fileName = scanner.nextLine();
    
        // Envia o comando de download para o servidor
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        writer.println("DOWNLOAD");
    
        // Envia o nome do arquivo para o servidor
        writer.println(fileName);
    
        // Recebe a resposta do servidor
        String response = reader.readLine();
    
        if (response != null && response.equals("OK")) { // Verifica se a resposta não é nula antes de chamar o método equals
            // Cria o arquivo de destino para salvar o arquivo recebido
            File file = new File(fileName);
    
            // Cria o fluxo de saída para salvar o arquivo
            FileOutputStream fileOutputStream = new FileOutputStream(file);
    
            // Recebe o conteúdo do arquivo do servidor e salva no arquivo de destino
            byte[] buffer = new byte[4096];
            int bytesRead;
    
            while ((bytesRead = socket.getInputStream().read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
    
            fileOutputStream.close();
    
            System.out.println("Arquivo recebido e salvo com sucesso: " + file.getAbsolutePath());
        } else {
            System.out.println("Arquivo não encontrado no servidor.");
        }
    }
}