import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Tomography{

    public ArrayList<ArrayList<Link>> Route;//経路集合
    public static List<Link> Normal_Link; //正常リンク
    public static List<Link> Failure_Candidate;//故障リンク候補集合
    public static List<Link> Failure_Confirmed;//故障リンク確定集合
    ArrayList<Link> Tmp_Root; //2次元リストの2次元目作成用の仮のリスト

    Tomography(ArrayList<ArrayList<Link>> l){ //コンストラクタ
        this.Route = new ArrayList<>(l);
        Failure_Candidate = new ArrayList<>();
        Failure_Confirmed = new ArrayList<>();
        Tmp_Root = new ArrayList<>();
        Normal_Link = new ArrayList<>();
        Scan();
//        Update();
        Result();
        Main.Failure_Link = new ArrayList<>(Failure_Confirmed);
    }

    void Scan(){ //疎通確認
        boolean reachFlag;
        boolean normalFlag;
        boolean alreadyFlag;

        for (ArrayList<Link> route : Route) { //初期観測パス分回す
            alreadyFlag = false;

            //表示
            for (Link l : route)
                System.out.print(l.link_name + " ");
            //

            reachFlag= true;
            Tmp_Root = new ArrayList<>(route); //借りリストにルートを入れる

            for (Link tmp : Tmp_Root)
                if (! tmp.link_state_flag) //ルートの中のリンクに故障があれば
                    reachFlag = false; // フラグをfalseに

            if (reachFlag) System.out.println("-- OK");
            else System.out.println(" 不通");

            if (reachFlag) //疎通したら
                for (Link tmp : Tmp_Root) {
                    alreadyFlag = false;
                    for (Link normal : Normal_Link) //既に正常リンク集合にあるか確認
                        if (tmp.link_ID == normal.link_ID) alreadyFlag = true;

                    if (!alreadyFlag)
                        Normal_Link.add(tmp); //正常リンク集合にリンクを追加

                    for (int i = 0; i < Failure_Candidate.size(); i++) //故障候補に入ってたら消す
                        if (tmp.link_ID == Failure_Candidate.get(i).link_ID) {
                            Failure_Candidate.remove(i); //リンクが候補集合にあったら削除
                            i--;
                        }
                }
            else //疎通しなかったら
                for (Link tmp : Tmp_Root) {
                    normalFlag = false;
                    for (Link normal : Normal_Link) //正常リンク集合にあるか確認
                        if (tmp.link_ID == normal.link_ID) {
                            normalFlag = true;
                            alreadyFlag = true;
                            break; }

                    if (!normalFlag) //正常リンク集合になかったら
                        alreadyFlag = false;
                        for (Link fail : Failure_Candidate) //既に故障候補に入っているか
                            if (fail.link_ID == tmp.link_ID) {
                                alreadyFlag = true;
                                break; }

                    if (!alreadyFlag) //故障候補になかったら追加
                        Failure_Candidate.add(tmp);
                }
        }
    }

    void Update(){  //確定集合の判定
        ArrayList<Link> tmp;

        for (ArrayList<Link> rout : Route){
            Tmp_Root = new ArrayList<>(rout);
            tmp = new ArrayList<>();

            route : for (Link l : Tmp_Root){
                for (Link fail : Failure_Candidate) {
                    if (l.link_ID == fail.link_ID) {
                        tmp.add(new Link(fail));
                        if (tmp.size() > 1)
                            break route;
                        else
                            break; }
                }
            }

            if (tmp.size() == 1){
                Failure_Confirmed.add(tmp.get(0));
                for (int i=0; i<Failure_Candidate.size(); i++)
                    if (tmp.get(0).link_ID == Failure_Candidate.get(i).link_ID)
                        Failure_Candidate.remove(i); }
        }
    }

    void Result(){ //結果表示
        Normal_Link = Normal_Link.stream().sorted().collect(Collectors.toList());
        Failure_Candidate = Failure_Candidate.stream().sorted().collect(Collectors.toList());
        Failure_Confirmed = Failure_Confirmed.stream().sorted().collect(Collectors.toList());

        System.out.println("正常");
        Normal_Link.forEach(normal -> System.out.print(normal.link_name + " "));
        System.out.println();

        System.out.println("故障候補");
        Failure_Candidate.forEach(fail -> System.out.print(fail.link_name + " "));
        System.out.println();

        System.out.println("故障確定");
        Failure_Confirmed.forEach(fail -> System.out.print(fail.link_name + " "));
        System.out.println();
    }
}