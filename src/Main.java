import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static ArrayList<Link> Links = new ArrayList<>();//リンク情報のリスト
    public static ArrayList<ArrayList<Link>> Init_Path;//初期観測パス
    public static ArrayList<ArrayList<Link>> Add_Path = new ArrayList<>(); //追加観測パス
    public static List<Link> Normal_Link; //正常リンク
    public static List<Link> Failure_Candidate;//故障リンク候補集合
    public static List<Link> Failure_Confirmed;//故障リンク確定集合
    public static ArrayList<ArrayList<Link>> Failure_Path; //不通ルート
    public static boolean finishFlag = false; //終了フラグ

    public static void main(String[] args) throws IOException{
        // TODO 自動生成されたメソッド・スタブ
        //Link 読み込み

        String path = ("./Network_Data");
        File dir = new File(path);
        File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                System.out.println((i + 1) + ":    " + file);
            }
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
                    for (Link Link : Links) {
                        if (Links.get(i).end_node.equals(Link.start_node)) Links.get(i).NomOfBranch++;
                    }
                }
            }
        }

        //読み込み確認用の表示
        System.out.println();
        System.out.println("--読み込み確認--");
        for (Link Link : Links) {
            System.out.println(Link.link_ID + "   " + Link.link_name + " " + Link.start_node + " " +
                    Link.end_node + " " + Link.link_state_flag + " " + Link.NomOfBranch);
        }
        System.out.println();

        //故障リンク入力
        System.out.println("\nリンクのラベルを\",\" 区切りで入力し、故障させるリンクを選んでください");
        System.out.println("-- リンクリスト");
        for (Link l : Links)
            System.out.print(l.link_name + " ");
        System.out.println();

        line = new Scanner(System.in).nextLine();
        Scanner scan = new Scanner(line).useDelimiter("\\s*,\\s*");//カンマ区切りに設定
        while (true) {
            try {
                line = scan.next();
                for (Link l : Links)
                    if (l.link_name.equals(line))
                        l.link_state_flag = false;
            } catch (NoSuchElementException e) {//next() 次に読むのがないと投げる
                break;
            }
        }
        //故障リンクの入力が正しいか確認用
        System.out.println("\n--リンクの状態確認--");
        Links.forEach(l -> System.out.println(l.link_name + " " + l.start_node + " " + l.end_node + " " + l.link_state_flag));
        System.out.println();

        //イスンタンス化
        new InitPath(Links);
        new Tomography(Init_Path);

        if (Failure_Candidate.size() == 0)
            finishFlag = true;

        while (! finishFlag) {
            new AddPath(Normal_Link, Failure_Candidate, Init_Path);
            new Tomography(Init_Path);
        }


        //確認用
//        for (int j=0; j<Initial_Path.size(); j++) {
//            ArrayList<Link> al = Initial_Path.get(j);
//            System.out.println(" パス " + (j+1));
//            for (Link anAl : al) {
//                System.out.println(anAl.link_ID + "   " + anAl.link_name + " " + anAl.start_node + " " +
//                        anAl.end_node + " " + anAl.link_state_flag + " " + anAl.NomOfBranch);
//            }
//            System.out.println();
//        }

    }
}