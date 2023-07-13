public class ServidorMain {
    public static void main(String[] args) {
        String serverDirectory = "C:/Users/felip/OneDrive/Área de Trabalho/Projeto_SD"; // Replace with the desired server directory
        Servidor servidor = new Servidor(serverDirectory);
        servidor.start();
    }
}