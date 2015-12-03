import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static ArrayList<Link> Links = new ArrayList<Link>();//リンク情報のリスト
    //public static ArrayList<Node> Nodes = new ArrayList<Node>();//ノード情報のリスト

    public static void main(String[] args) throws IOException{
        // TODO 自動生成されたメソッド・スタブ
        //Link 読み込み

        String path = ("./Network_Data");
        File dir = new File(path);
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            System.out.println((i + 1) + ":    " + file);
        }

        System.out.println("読み込むネットワーク番号を入力 ");
        String line = new Scanner(System.in).nextLine();

        FileReader fr = new FileReader("./Network_Data/Network_" + line + ".txt");
        BufferedReader br = new BufferedReader(fr);
        int count = 0;
        while ((line = br.readLine()) != null){
            count += 1;
            Link l = new Link(count, line);//リンククラス型のオブジェクトをインスタンス化
            Links.add(l);//リンククラス型のリストに追加
        }

        for (int i=0; i<Links.size(); i++){
            for (int j = 0; j < Links.size(); j++) {
                if (Links.get(i).link_ID == j) {
                    for (int k = 0; k < Links.size(); k++) {
                        if (Links.get(i).end_node.equals(Links.get(k).start_node)) Links.get(i).NomOfBranch ++;
                    }
                }
            }
        }

        //読み込み確認用の表示
        System.out.println();
        System.out.println("--読み込み確認--");
        for(int i=0; i<Links.size(); i++){
            System.out.println(Links.get(i).link_ID + "   " + Links.get(i).link_name+ " "+Links.get(i).start_node + " " +
                    Links.get(i).end_node + " " + Links.get(i).link_state_flag + " " + Links.get(i).NomOfBranch);
        }
        System.out.println();


        //故障リンク入力
        System.out.println("\nリンクのラベルを\",\" 区切りで入力し、故障させるリンクを選んでください");
        System.out.println("-- リンクリスト");
        for(int i=0; i<Links.size(); i++){
            System.out.print(Links.get(i).link_name + " ");
            if (i == Links.size() -1) System.out.println();
        }

        line = new Scanner(System.in).nextLine();
        Scanner scan = new Scanner(line).useDelimiter("\\s*,\\s*");//カンマ区切りに設定
        while (true){
            try{
                line = scan.next();
                for (int i=0; i<Links.size(); i++){
                    if (Links.get(i).link_name.equals(line)){
                        Link l = Links.get(i);
                        l.link_state_flag = false;
                        Links.set(i,l);
                    }
                }
            }catch (NoSuchElementException e){//next() 次に読むのがないと投げる
                break;
            }
        }
        //故障リンクの入力が正しいか確認用
        for (int i=0; i<Links.size(); i++){
            System.out.println(Links.get(i).link_name+ " "+Links.get(i).start_node + " " +Links.get(i).end_node + " " + Links.get(i).link_state_flag);
            if (i == Links.size()-1) System.out.println();
        }

        new Tomography(Links);

    }
}