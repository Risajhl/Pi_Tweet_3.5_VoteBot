
import ir.pi.project.server.controller.ClientHandler;
import ir.pi.project.server.db.Context;
import ir.pi.project.server.model.CurrentChat;
import ir.pi.project.shared.enums.Pages.MessagesPage;
import ir.pi.project.shared.enums.others.MessageStatus;
import ir.pi.project.shared.event.messages.NewMessageEvent;
import ir.pi.project.shared.model.GroupChat;
import ir.pi.project.shared.model.Message;
import ir.pi.project.shared.model.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class poll {
    ClientHandler clientHandler;
    BotSender botSender;
    Context context=new Context();
    DB db=new DB();

    public poll(ClientHandler clientHandler, BotSender botSender){
        this.clientHandler=clientHandler;
        this.botSender=botSender;
    }
    public void check(){
        User bot=context.Users.get(Main.id);

        for (List<Integer> chat: bot.getChats()) {
                for (Integer messageId : chat) {
                    Message message = context.Messages.get(messageId);
                    if (message.getSenderId() != Main.id) {
                        if (message.getStatus() != MessageStatus.SEEN) {
                            message.setStatus(MessageStatus.SEEN);
                            context.Messages.update(message);
                            clientHandler.setCurrentUserId(Main.id);
                            clientHandler.setCurrentChat(new CurrentChat());
                            clientHandler.getCurrentChat().setTheOther(message.getSenderId());

                            String ans = "To use my services, add me to your group chats!";
                            botSender.addEvent(new NewMessageEvent(MessagesPage.DIRECT_CHATS, ans, null));
                        }
                    }
                }
            }




        for (Integer groupChatId: bot.getGroupChats()) {
            int q=0;
            GroupChat groupChat=context.GroupChats.get(groupChatId);
            for (Integer messageId: groupChat.getMessages()) {
                Message message=context.Messages.get(messageId);
                if(message.getSenderId()==Main.id)q++;
                if(message.getText().startsWith("/voteBot") && !message.isBotSeen()){
                   message.setBotSeen(true);
                   context.Messages.update(message);
                    String ans= checkDemand(message.getText());
                    clientHandler.setCurrentUserId(Main.id);
                    clientHandler.getCurrentChat().setName(groupChat.getGroupName());
                    botSender.addEvent(new NewMessageEvent(MessagesPage.GROUP_CHATS,ans,null));
                }
            }
            if(q==0) {
                clientHandler.setCurrentUserId(Main.id);
                clientHandler.getCurrentChat().setName(groupChat.getGroupName());
                String ans= """
                        Hello, Thanks for adding me to your group!
                        to make new poll use this sample:
                        /voteBot-new-question-option1-option2-...
                        """;
                botSender.addEvent(new NewMessageEvent(MessagesPage.GROUP_CHATS,ans,null));
            }
        }
    }

    public String checkDemand(String string){
        String text=string.substring(8);
        if(text.startsWith("-new-")){
            text=text.substring(5);
            return getNewPoll(text);
        }
        else if(text.startsWith("-vote-")){
            text=text.substring(6);
            return getUpdatedPoll(text);
        }
        return "Sorry I can't understand your demand.";
    }

    public String getNewPoll(String text){
        String[] parts=text.split("-");
        String question=parts[0];
        if(!question.isEmpty() && parts.length>2) {
            List<String> options = new ArrayList<>(Arrays.asList(parts).subList(1, parts.length));
            PollModel voteModel = new PollModel(db.newID()+100000, question, options);
            db.update(voteModel);
            return getTheMessage(voteModel);
        }
        else return "Sorry I can't understand your demand.";
    }

    public String getUpdatedPoll(String text){
        try {
            String[] parts = text.split("-", 2);
            int voteId = Integer.parseInt(parts[0]);
            int choiceIndex = Integer.parseInt(parts[1]);
            PollModel voteModel=db.get(voteId);
            voteModel.addToSpecificOption(choiceIndex);
            db.update(voteModel);
            return getTheMessage(voteModel);
        } catch (Exception ignored){ }

        return "Sorry I can't understand your demand.";
    }

    public String getTheMessage(PollModel voteModel){
        StringBuilder ans= new StringBuilder(voteModel.getQuestion() + "\n");
        for(int i=0;i<voteModel.getOptions().size();i++){
            ans.append(i).append(". ").append(voteModel.getOptions().get(i)).append("---")
                    .append(voteModel.getVotes().get(voteModel.getOptions().get(i))).append(" votes \n");
        }
        ans.append("To vote send /voteBot-vote-").append(voteModel.getId()).append("-Number of your choice.");
        return ans.toString();
    }






}
