import java.util.ArrayList;
import java.util.List;

public class AddPath {
    public ArrayList<Link> Add_Path; //追加観測パスを格納
    public ArrayList<ArrayList<Link>> Add_Path_Candidate; //追加パスの候補
    public List<Link> Normal_Link; //正常リンク
    public List<Link> Failure_Candidate;//故障リンク候補集合
    public List<Link> Selected; //候補で選択済みのを入れる
    public ArrayList<ArrayList<Link>> Failure_Path; //既存パス
    ArrayList<Link> Tmp_Root; //2次元リストの2次元目作成用の仮のリスト

    AddPath(List<Link> normal, List<Link> cand, ArrayList<ArrayList<Link>> f_path){
        this.Add_Path = new ArrayList<>();
        this.Add_Path_Candidate = new ArrayList<>();
        this.Normal_Link = new ArrayList<>(normal);
        this.Failure_Candidate = new ArrayList<>(cand);
        this.Selected = new ArrayList<>();
        this.Failure_Path = f_path;
        this.Tmp_Root = new ArrayList<>();

//        System.out.println("\n追加パスの候補");
        additional_path();
        print();

        ArrayList<Link> addPath = new ArrayList<>(selection_path());

        Main.Init_Path.add(addPath);
        if (addPath.size() == 0)
            Main.finishFlag = true;
    }

    void additional_path(){
        int halfCount; //候補集合の半数個を追加パスに含ませるためのカウンター
        boolean reach; //半数に達したか

        //halfCountの初期化
        if (Failure_Candidate.size() > 2)
            halfCount = (int)Math.floor(((double) Failure_Candidate.size()) / 2);
        else
            halfCount = 1;

        //候補集合の中から一つリンクを選び、そこからパスを構築
        for (Link l : Failure_Candidate) {
            optimize();
            Tmp_Root.clear();
            Tmp_Root.add(l);
            reach = false;

            if (Tmp_Root.size() < halfCount) { // halfCountが1以上
                //スタートまでたどる
                for (int i=0; i<Failure_Candidate.size(); i++) {
                    if (!Tmp_Root.get(0).start_node.equals("s")) {
                        if (Tmp_Root.get(0).start_node.equals(Failure_Candidate.get(i).end_node) && (!reach)) {
                            if (! Failure_Candidate.get(i).usedFlag) {
                                Failure_Candidate.get(i).usedFlag = true;
                                Tmp_Root.add(0, Failure_Candidate.get(i));
                                if (fail_count(Tmp_Root) < halfCount)
                                    i = -1;
                                else {
                                    i = Failure_Candidate.size();
                                    reach = true; }
                            }else
                                Failure_Candidate.get(i).usedFlag = false;
                        }

                        if (i == Failure_Candidate.size() - 1 || reach)
                            for (int j = 0; j < Normal_Link.size(); j++) {
                                if (Tmp_Root.get(0).start_node.equals(Normal_Link.get(j).end_node)) {
                                    Tmp_Root.add(0, Normal_Link.get(j));
                                    j = Normal_Link.size();
                                    i = -1; }

                                if (j == Normal_Link.size()-1)
                                    for (Link l2 : Failure_Candidate)
                                        if (Tmp_Root.get(0).start_node.equals(l2.end_node)) {
                                            if (! l2.usedFlag) {
                                                Tmp_Root.add(0, l2);
                                                l2.usedFlag = true;
                                                i = -1;
                                                if (!(fail_count(Tmp_Root) < halfCount))
                                                    reach = true;
                                            }else
                                                l2.usedFlag = false;
                                            if (reach && (!Tmp_Root.get(0).start_node.equals("s")))
                                                j = -1;
                                            else
                                                break; }
                            }
                    }else
                        i = Failure_Candidate.size();
                }

                // ゴールまでたどる
                reach = false;

                //デバックプリント
                System.out.println();
                Tmp_Root.forEach(link -> System.out.print(link.link_name + " "));
                System.out.println();

                for (int i=0; i<Failure_Candidate.size(); i++) {
                    if (! Tmp_Root.get(Tmp_Root.size()-1).end_node.equals("g")) {
                        if (Tmp_Root.get(Tmp_Root.size()-1).end_node.equals(Failure_Candidate.get(i).start_node) && (!reach)) {

                            if (! Failure_Candidate.get(i).usedFlag) {
                                Tmp_Root.add(Failure_Candidate.get(i));
                                System.out.println("111");
                                Failure_Candidate.get(i).usedFlag = true;
                                if (fail_count(Tmp_Root) < halfCount)
                                    i = -1;
                                else {
                                    i = Failure_Candidate.size();
                                    reach = true; }
                            }else
                                Failure_Candidate.get(i).usedFlag = false;
                        }

                        if (i == Failure_Candidate.size() - 1 || reach) ////////
                            for (int j = 0; j < Normal_Link.size(); j++) {
                                if (Tmp_Root.get(Tmp_Root.size() - 1).end_node.equals(Normal_Link.get(j).start_node)) {
                                    Tmp_Root.add(Normal_Link.get(j));
                                    System.out.println("222");
                                    j = Normal_Link.size();
                                    i = -1; }

                                if (j == Normal_Link.size()-1)
                                    for (Link l2 : Failure_Candidate)
                                        if (Tmp_Root.get(Tmp_Root.size()-1).end_node.equals(l2.start_node)) {
                                            if (! l2.usedFlag) {
                                                Tmp_Root.add(l2);
                                                l2.usedFlag = true;
                                                System.out.println("333 " + l2.link_name);
                                                i = -1;
                                                if (!(fail_count(Tmp_Root) < halfCount))
                                                    reach = true;
                                            }else
                                                l2.usedFlag = false;

                                            if (reach && (! Tmp_Root.get(Tmp_Root.size()-1).end_node.equals("g"))) {
                                                j = -1;
                                                break;
                                            }else
                                                break; }
                            }
                    }else
                        i = Failure_Candidate.size();

                }

                Add_Path_Candidate.add(new ArrayList<>(Tmp_Root));

                //デバックプリント
//                Tmp_Root.forEach(link -> System.out.print(link.link_name + " - "));
//                System.out.println("\n");

            } else { // halfCountが1のとき Normalから追加
                // スタートまで
                for (int i=0; i<Normal_Link.size(); i++) {
                    if (!Tmp_Root.get(0).start_node.equals("s")) {
                        if (Tmp_Root.get(0).start_node.equals(Normal_Link.get(i).end_node)) {
                            Tmp_Root.add(0, Normal_Link.get(i));
                            i = -1; }

                        if (i == Normal_Link.size()-1)
                            for (int j=0; j<Failure_Candidate.size(); j++) {
                                if (Tmp_Root.get(0).start_node.equals(Failure_Candidate.get(j).end_node)) {
                                    if (!Failure_Candidate.get(j).usedFlag) {
                                        Tmp_Root.add(0, Failure_Candidate.get(j));
                                        Failure_Candidate.get(j).usedFlag = true;
                                        i = -1;
                                        break;
                                    } else
                                        Failure_Candidate.get(j).usedFlag = false; }

                                if (j == Failure_Candidate.size()-1)
                                    j = -1;
                            }
                    } else
                        i = Normal_Link.size();
                }

                // ゴールまで
                for (int i=0; i<Normal_Link.size(); i++) {
                    if (!Tmp_Root.get(Tmp_Root.size()-1).end_node.equals("g")) {
                        if (Tmp_Root.get(Tmp_Root.size()-1).end_node.equals(Normal_Link.get(i).start_node)) {
                            Tmp_Root.add(Normal_Link.get(i));
                            i = -1;  }

                        if (i == Normal_Link.size()-1)
                            for (int j=0; j<Failure_Candidate.size(); j++) {
                                if (Tmp_Root.get(Tmp_Root.size()-1).end_node.equals(Failure_Candidate.get(j).start_node)) {
                                    if (!Failure_Candidate.get(j).usedFlag) {
                                        Tmp_Root.add(Failure_Candidate.get(j));
                                        Failure_Candidate.get(j).usedFlag = true;
                                        i = -1;
                                        break;
                                    } else
                                        Failure_Candidate.get(j).usedFlag = false; }

                                if (j == Failure_Candidate.size()-1)
                                    j = -1;
                            }
                    } else
                        i = Normal_Link.size();
                }

                Add_Path_Candidate.add(new ArrayList<>(Tmp_Root));

                //デバックプリント
//                Tmp_Root.forEach(link -> System.out.print(link.link_name + " - "));
//                System.out.println("\n");
            }

        }

    }

    //ルートの中に故障候補がいくつ入っているか
    int fail_count(ArrayList<Link> path){
        int count = 0;

        for (Link l : Failure_Candidate)
            for (Link l2 : path)
                if (l.link_ID == l2.link_ID)
                    count ++;

        return count;
    }

    //Path選定の際のusedFlagの最適化
    void optimize(){
        for (Link chek : Failure_Candidate)
            chek.usedFlag = false;

        for (ArrayList<Link> ar : Add_Path_Candidate) {
            Tmp_Root = new ArrayList<>(ar);
            for (Link use : Tmp_Root)
                Failure_Candidate.stream().filter(check -> check.link_ID == use.link_ID).forEach(check -> check.usedFlag = true);
        }
    }

    //追加パス候補から良い物を選択
    ArrayList<Link> selection_path(){
        // 選定
        int halfCount;
        ArrayList<Link> addPath = new ArrayList<>();

        if (Failure_Candidate.size() > 2)
            halfCount = (int)Math.floor(((double) Failure_Candidate.size()) / 2);
        else
            halfCount = 1;

        for (int i=0; i<Add_Path_Candidate.size(); i++) {
            Tmp_Root = new ArrayList<>(Add_Path_Candidate.get(i));
            if (fail_count(Tmp_Root) == halfCount)
                if (checkPath(Tmp_Root)) {
                    addPath = new ArrayList<>(Tmp_Root);
                    break; }

            if (i == Add_Path_Candidate.size()-1){
                halfCount --;
                i = -1; }

            if (halfCount <= 0){
                System.out.println("追加パスの選定エラー");
                addPath = new ArrayList<>(Add_Path_Candidate.get(0));
                Main.finishFlag = true;
                break; }
        }

        System.out.println("追加パス");
        addPath.forEach(l -> System.out.print(l.link_name + " "));
        System.out.println("\n");

        return addPath;
    }

    //構築したパスが既存Pathに内包されているか
    boolean checkPath(ArrayList<Link> checkPath){
        boolean flag = false;
        ArrayList<Link> already;

        for (ArrayList<Link> al : Failure_Path) {
            already = new ArrayList<>(al);
            if (already.size() == checkPath.size())
                for (int i = 0; i < checkPath.size(); i++) {
                    flag = checkPath.get(i).link_ID != already.get(i).link_ID;
                    if (flag)
                        break; }
            else
                flag = true;

            if (! flag)
                break;
        }
        return flag;
    }

    //プリント
    void print(){
        System.out.println("\n追加パスの候補");
        for (int i=0; i<Add_Path_Candidate.size(); i++) {
            Tmp_Root.clear();
            Tmp_Root = new ArrayList<>(Add_Path_Candidate.get(i));
            System.out.print("No." + (i+1) + " -- ");
            Tmp_Root.forEach(l -> System.out.print(l.link_name + " "));
            System.out.println("\n");
        }
    }

}