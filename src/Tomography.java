import java.util.ArrayList;

public class Tomography{

    public ArrayList<ArrayList<Link>> Route;//経路集合
    public static ArrayList<Link> Normal_Link; //正常リンク
    public ArrayList<Link> Failure_Candidate;//故障リンク候補集合
    public ArrayList<Link> Failure_Confirmed;//故障リンク確定集合
    ArrayList<Link> Tmp_Root; //2次元リストの2次元目作成用の仮のリスト

    Tomography(ArrayList<ArrayList<Link>> l){
        this.Route = new ArrayList<>(l);
        Failure_Candidate = new ArrayList<>();
        Failure_Confirmed = new ArrayList<>();
        Tmp_Root = new ArrayList<>();
        Normal_Link = new ArrayList<>();
        Scan();
        Main.Failure_Link = new ArrayList<>(Failure_Confirmed);
    }

    void Scan(){
        boolean reachFlag;
        boolean normalFlag;

        for (ArrayList<Link> aRoute : Route) { //初期観測パス分回す
            reachFlag= false;
            Tmp_Root = aRoute; //借りリストにルートを入れる

            for (Link tmp : Tmp_Root)
                if (tmp.link_state_flag)//ルートの中のリンクが正常なら
                    reachFlag = true;
                else {                 //ルートの中のリンクに故障リンクが含まれる場合
                    reachFlag = false;
                    break;
                }

            if (reachFlag) //疎通したら
                Tmp_Root.forEach(tmp -> {
                    Normal_Link.add(tmp); //正常リンク集合に全リンクを追加
                    for (int i = 0; i < Failure_Candidate.size(); i++)
                        if (tmp.link_ID == Failure_Candidate.get(i).link_ID) {
                            Failure_Candidate.remove(i); //リンクが候補集合にあったら削除
                            i --; }
                });
            else //疎通しなかったら
                for (Link tmp : Tmp_Root) {
                    normalFlag = true;
                    for (int i=0; i<Normal_Link.size(); i++)
                        if (tmp.link_ID == Normal_Link.get(i).link_ID)
                            break;
                         else if (i == Normal_Link.size() - 1)
                            normalFlag = false;

                    if (! normalFlag)
                        Failure_Candidate.add(tmp);
                }
        }

        System.out.println("正常");
        Normal_Link.forEach(normal -> System.out.print(normal.link_name + " "));
        System.out.println();

        System.out.println("故障候補");
        Failure_Candidate.forEach(fail -> System.out.print(fail.link_name + " "));
        System.out.println();

        //故障リンク候補と確定を更新する 1個だった場合確定
//        Link l;


    }
}