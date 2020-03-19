public class MainClient {
    public static String ipAddressClient = "localhost";
        public static int port = 3345;

        public static void main(String[] args) {
            new Client(ipAddressClient, port);
        }
    }

