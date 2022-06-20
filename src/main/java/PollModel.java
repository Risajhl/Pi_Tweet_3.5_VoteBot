
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PollModel {
    private int id;
    private String question;
    private List<String> options;
    private Map<String,Integer> votes;

    public PollModel(int id, String question, List<String> options){
        this.id=id;
        this.question=question;
        this.options=options;
        this.votes=new HashMap<>();
        for (String option : options) votes.put(option, 0);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public Map<String, Integer> getVotes() {
        return votes;
    }

    public void setVotes(Map<String, Integer> votes) {
        this.votes = votes;
    }

    public void  addToSpecificOption(int index){
        this.votes.put(options.get(index),votes.get(options.get(index))+1);
    }
}
