import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Tomography{

    public ArrayList<ArrayList<Link>> Route;//経路集合
    public List<Link> Normal_Link; //正常リンク
    public List<Link> Failure_Candidate;//故障リンク候補集合
    public List<Link> Failure_Confirmed;//故障リンク確定集合
    ArrayList<Link> Tmp_Root; //2次元リストの2次元目作成用の仮のリスト
    ArrayList<ArrayList<Link>> Failure_Path; //不通ルート

    Tomography(ArrayList<ArrayList<Link>> l){ //コンストラクタ
        this.Route = new ArrayList<>(l);
        Normal_Link = new ArrayList<>();
        Failure_Candidate = new ArrayList<>();
        Failure_Confirmed = new ArrayList<>();
        Tmp_Root = new ArrayList<>();
        Failure_Path = new ArrayList<>();

        Scan();
        Update();
        Result();

        Main.Normal_Link = new ArrayList<>(this.Normal_Link);
        Main.Failure_Candidate = new ArrayList<>(this.Failure_Candidate);
        Main.Failure_Confirmed = new ArrayList<>(this.Failure_Confirmed);
        Main.Failure_Path = new ArrayList<>(this.Failure_Path);
    }

    void Scan(){ //疎通確認
        boolean reachFlag;
        boolean normalFlag;
        boolean alreadyFlag;

        for (ArrayList<Link> route : Route) { //初期観測パス分回す
            alreadyFlag = false;

            //表示
            route.forEach(r -> System.out.print(r.link_name + " "));
            //

            reachFlag= true;
            Tmp_Root = new ArrayList<>(route); //借りリストにルートを入れる

            for (Link tmp : Tmp_Root)
                if (! tmp.link_state_flag) //ルートの中のリンクに故障があれば
                    reachFlag = false; // フラグをfalseに

            if (reachFlag)
                System.out.println("-- OK");
            else{
                System.out.println(" 不通");
                Failure_Path.add(new ArrayList<>(route)); } //不通ルート集合に追加

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

        for (ArrayList<Link> rout : Route){ //ルート分回す
            Tmp_Root = new ArrayList<>(rout);//ルートを一つとってくる
            tmp = new ArrayList<>();

            route : for (Link l : Tmp_Root){ //ルート中のリンク分回す
                for (Link fail : Failure_Candidate) {
                    if (l.link_ID == fail.link_ID) { //故障してたらtmpにリンクを追加
                        tmp.add(new Link(fail));
                        if (tmp.size() > 1) //1以上なら確定でないので抜ける
                            break route;
                        else //これ以降の検索を省く
                            break; }
                }
            }

            if (tmp.size() == 1) //故障リンクがルート中に1個なら故障が確定するので
                Failure_Confirmed.add(new Link(tmp.get(0))); //確定集合にリンクを追加
        }

        for (Link l : Failure_Confirmed) //故障が確定したリンクを候補集合から消す
            for (int i=0; i<Failure_Candidate.size(); i++)
                if (l.link_ID == Failure_Candidate.get(i).link_ID) {
                    Failure_Candidate.remove(i);
                    i--; }
    }

    void Result(){ //結果表示
        Normal_Link = Normal_Link.stream().sorted().collect(Collectors.toList());
        Failure_Candidate = Failure_Candidate.stream().sorted().collect(Collectors.toList());
        Failure_Confirmed = Failure_Confirmed.stream().sorted().collect(Collectors.toList());

        System.out.println("\n正常リンク");
        Normal_Link.forEach(normal -> System.out.print(normal.link_name + " "));
        System.out.println();

        System.out.println("故障候補");
        Failure_Candidate.forEach(fail -> System.out.print(fail.link_name + " "));
        System.out.println();

        System.out.println("故障確定");
        Failure_Confirmed.forEach(fail -> System.out.print(fail.link_name + " "));
        System.out.println();

        System.out.println("不通ルート");
        ArrayList<Link> tmp;
        for (ArrayList<Link> route : Failure_Path) {
            tmp = new ArrayList<>(route);
            tmp.forEach(f -> System.out.print(f.link_name + " "));
            System.out.println();
        }

    }
}