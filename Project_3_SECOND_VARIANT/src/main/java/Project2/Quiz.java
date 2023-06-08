package Project2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Quiz {
    public ArrayList<ArrayList<String>> Variants = new ArrayList<>();
    public ArrayList<String> answers = new ArrayList<>();
    public ArrayList<String> suraqtar = new ArrayList<>();
    public Test T = new Test();
    public FillIn F = new FillIn();
    public ArrayList<Integer> list = new ArrayList<>();
    public boolean TRUE_FALSE = false;

    public void Suraqtardy_Qosu(int j){
        if(Variants.get(list.get(j)).size() == 4){
            T.setDescription(suraqtar.get(list.get(j)));
            T.setOptions(Variants.get(list.get(j)));
            T.setAnswer(answers.get(list.get(j)));
            TRUE_FALSE = true;

        }else{
            F.setDescription(suraqtar.get(list.get(j)));
            F.setAnswer(answers.get(list.get(j)));
            TRUE_FALSE=false;
        }
    }
    public void s(){
        int j = 0;
        while(j!= suraqtar.size()){
            list.add(j);
            j++;
        }

    }
    public void filedan(String name)throws IOException {
        boolean A =true;
        File file = new File(name);
        BufferedReader in = new BufferedReader(new FileReader(file));
        String sentences;
        int k = -1;

        while ((sentences = in.readLine())!=null){
            if(Objects.equals(sentences,"")||Objects.equals(sentences," "))continue;
            if(A){
                Variants.add(new ArrayList<>());
                suraqtar.add(sentences);
                A = false;
                k++;
            }
            else{
                String[] arr = sentences.split(" * , *");
                answers.add(arr[0]);
                for (int i = 0; i < arr.length; i++) {
                    Variants.get(k).add(arr[i]);
                }
                A=true;
            }
        }
    }
}
