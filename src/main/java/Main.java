import ir.pi.project.server.controller.ClientHandler;

public class Main {
    static int id;
    String username="Vote_Bot";
    poll poll;


    public Main() {
        BotSender botSender=new BotSender();
        ClientHandler clientHandler = new ClientHandler(botSender);
        clientHandler.start();
        this.poll= new poll(clientHandler,botSender);

    }

    public void start(){
        poll.check();
    }

    public static void setId(int id) {
        Main.id = id;
    }

    public String getUsername() {
        return username;
    }
}
