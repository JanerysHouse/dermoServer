import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client {


        private Socket socket;
        private BufferedReader readerFromSocket; // поток чтения из сокета
        private BufferedWriter writerFromSocket; // поток чтения в сокет
        private BufferedReader inputUserConsole; // поток чтения с консоли
        private String ipAddressClient; // ip адрес клиента
        private int port; // порт соединения
        private String nickname; // имя клиента
        private Date time;
        private String dtime;
        private SimpleDateFormat dt1;


        public Client(String ipAddressClient, int port) {
            this.ipAddressClient = ipAddressClient;
            this.port = port;
            try {
                this.socket = new Socket(ipAddressClient, port);
            } catch (IOException e) {
                System.err.println("Socket failed");
            }
            try {
                inputUserConsole = new BufferedReader(new InputStreamReader(System.in));
                assert socket != null;
                readerFromSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writerFromSocket = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                this.pressNickname();
                new ReadMessageFromServer().start();
                new WriteMessageConsoleOnServer().start();
            } catch (IOException e) {
                Client.this.downService();
            }

        }

        private void pressNickname() {
            System.out.print("Press your name my Lord: ");
            try {
                nickname = inputUserConsole.readLine();
                writerFromSocket.write("Hello " + nickname + "\n");
                writerFromSocket.flush();
            } catch (IOException ignored) {
            }

        }

        private void downService() {
            try {
                if (!socket.isClosed()) {
                    socket.close();
                    readerFromSocket.close();
                    writerFromSocket.close();
                }
            } catch (IOException ignored) {}
        }

        private class ReadMessageFromServer extends Thread {
            @Override
            public void run() {

                String messageFromServer;
                try {
                    while (true) {
                        messageFromServer = readerFromSocket.readLine();

                        if (messageFromServer.equals("stop")) {
                            Client.this.downService();
                            break;
                        }
                        System.out.println(messageFromServer);
                    }
                } catch (IOException e) {
                    Client.this.downService();
                }
            }
        }


        public class WriteMessageConsoleOnServer extends Thread {

            @Override
            public void run() {
                while (true) {
                    String userMessageOnServer;
                    try {
                        time = new Date();
                        dt1 = new SimpleDateFormat("HH:mm:ss");
                        dtime = dt1.format(time);
                        userMessageOnServer = inputUserConsole.readLine();
//                        if (userMessageOnServer.equals("getAllMessages")) {
//                            MainServer.story.printStory();
                        //}
                        if (userMessageOnServer.equals("stop")) {
                            writerFromSocket.write("stop" + "\n");
                            Client.this.downService();
                            break;

                        } else {
                            writerFromSocket.write("(" + dtime + ") " + nickname + ": " + userMessageOnServer + "\n");
                        }
                        writerFromSocket.flush();
                    } catch (IOException e) {
                        Client.this.downService();

                    }

                }
            }
        }
    }



