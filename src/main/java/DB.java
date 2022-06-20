import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.Scanner;

public class DB {
    public PollModel get(int id) {
        try {
            File directory = new File("./src/main/resources/Info/voteModels");
            File Data = new File(directory, id + ".json");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(Data));
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            PollModel voteModel = gson.fromJson(bufferedReader, PollModel.class);
            bufferedReader.close();
            return voteModel;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }



    public void update(PollModel voteModel) {
        try {
            File directory = new File("./src/main/resources/Info/voteModels");
            if(!directory.exists()) directory.mkdirs();
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            File Data = new File(directory, voteModel.getId() + ".json");
            if (!Data.exists())
                Data.createNewFile();
            FileWriter writer = new FileWriter(Data);
            writer.write(gson.toJson(voteModel));
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int newID(){
        int s=0;
        try {
            File lastId = new File("./src/main/resources/lastId");
            Scanner sc = new Scanner(lastId);
            int q = sc.nextInt();
            s=q;
            FileOutputStream fout = new FileOutputStream(lastId, false);
            PrintStream out = new PrintStream(fout);
            q++;
            out.println(q);
            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return s;
    }
}
