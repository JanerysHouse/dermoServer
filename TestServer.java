import java.io.*;
import java.net.Socket;

public class TestServer extends Thread {

        private Socket socket;
        private BufferedReader in;
        private BufferedWriter out;

        public TestServer(Socket socket) throws IOException {
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            MainServer.story.printStory(out);

            start();
        }

        @Override
        public void run() {
            String word;
            try {

                word = in.readLine();
                try {
                    out.write(word + "\n");
                    out.flush();
                } catch (IOException ignored) {
                }
                try {
                    while (true) {
                        word = in.readLine();
                        if (word.equals("getAllMessages"))
                        MainServer.story.printStory(out);
                        if (word.equals("stop")) {
                            this.downService();
                            break;
                        }
                        System.out.println("Echoing: " + word);
                        MainServer.story.addStoryEl(word);
                        for (TestServer vr : MainServer.serverList) {
                            vr.send(word);
                        }
                    }
                } catch (NullPointerException ignored) {
                }


            } catch (IOException e) {
                this.downService();
            }
        }


        private void send(String msg) {
            try {
                out.write(msg + "\n");
                out.flush();
            } catch (IOException ignored) {
            }

        }

        private void downService() {
            try {
                if (!socket.isClosed()) {
                    socket.close();
                    in.close();
                    out.close();
                    for (TestServer vr : MainServer.serverList) {
                        if (vr.equals(this)) vr.interrupt();
                        MainServer.serverList.remove(this);
                    }
                }
            } catch (IOException ignored) {
            }
        }
    }

