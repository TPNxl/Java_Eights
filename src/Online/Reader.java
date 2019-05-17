package Online;

import java.io.IOException;

public class Reader {
    public Reader() {
    }
    public String next() throws IOException {
        Server.out.writeUTF("|n");
        return Server.in.readUTF();
    } public int nextInt() throws IOException {
        Server.out.writeUTF("|n");
        while(true) {
            try {
                return Integer.parseInt(Server.in.readUTF());
            } catch (NumberFormatException nfe) {
                Server.out.writeUTF("Invalid input.");
            }
        }
    }
}
