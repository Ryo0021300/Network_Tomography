import java.util.ArrayList;

public class InitialPath {
    ArrayList<Link> Links ;//リンクリスト
    ArrayList<ArrayList<Link>> Initial_Path = new ArrayList<>();//各初期観測パスを格納

    InitialPath(ArrayList<Link> L){//コンストラクタ
        this.Links = new ArrayList<>(L);
        Initial_Path();
        Main.Initial_Path = new ArrayList<>(this.Initial_Path);
    }

    void Initial_Path(){//初期観測パスを選定する関数
//        ArrayList<Link> Links = new ArrayList<Link>();//リンクリスト
//        ArrayList<ArrayList<Link>> Initial_Path = new ArrayList<ArrayList<Link>>();//各初期観測パスを格納
        ArrayList<Link> Tmp_Root = new ArrayList<>();//2次元リストの2次元目作成用の仮のリスト
        ArrayList<Link> Tmp_Decision = new ArrayList<>();
        ArrayList<Link> Selected = new ArrayList<>();//選択済みリンク
        ArrayList<Link> Not_Selected;//未選択リンク

        Not_Selected = new ArrayList<>(this.Links);//まず全リンクを未選択リストに入れる
        // メモ Collections.reverse(Links); リストを逆順に
        while (true) {
            Tmp_Root.clear();//仮リスト初期化
            Tmp_Decision.clear();
            if (!Not_Selected.isEmpty()) { //未選択リストが空になるまで無限ループ

                for (int i=0; i<Not_Selected.size(); i++) {//未選択リストからsノードを探す
                    if (Not_Selected.get(i).start_node.equals("s")) {//sからでてるリンクを探す
                        Tmp_Root.add(Not_Selected.get(i));
                        Selected.add(Not_Selected.get(i));
                        Not_Selected.remove(i);
                        i = Not_Selected.size();
                    }
                }

                Boolean flag = true;
                while (flag){//上で選択すたリンクからgにたどり着くまでリンクをたどる
                    for (int i=0; i<Not_Selected.size(); i++){
                        //一個前に追加したリンクの終点ノードと選ぶリンクの始点ノードが一致したら
                        if (Not_Selected.get(i).start_node.equals(Tmp_Root.get(Tmp_Root.size() -1).end_node)){
                            Tmp_Root.add(Not_Selected.get(i));
                            Selected.add(Not_Selected.get(i));
                            Not_Selected.remove(i);
                            i = 0;
                        }
                        //選択済みから探して追加
                        if ((Not_Selected.size()-1 == -1 || i == Not_Selected.size()-1) && (! Tmp_Root.get(Tmp_Root.size()-1).end_node.equals("g"))) {
                            for (int j = 0; j < Selected.size(); j++) {
                                if (Selected.get(j).start_node.equals(Tmp_Root.get(Tmp_Root.size() - 1).end_node)) {
                                    Tmp_Root.add(Selected.get(j));
                                    i = 0;
                                    j = 0;
                                }
                            }
                        }
                        //ゴールにたどり着いたら抜ける
                        if (Tmp_Root.get(Tmp_Root.size()-1).end_node.equals("g")) i = Not_Selected.size();
                    }

                    // 未選択集合に残った最後の一個のスタートノードがsであった場合、上のfor文が回らないため、ここで処理
                    if (Not_Selected.size() == 0 && !Tmp_Root.get(Tmp_Root.size()-1).link_name.equals("g")){
                        for (int i=0; i<Selected.size(); i++) {
                            if (Selected.get(i).start_node.equals(Tmp_Root.get(Tmp_Root.size()-1).end_node)){
                                Tmp_Root.add(Selected.get(i));
                                i = 0;
                            }
                            if (Tmp_Root.get(Tmp_Root.size() - 1).end_node.equals("g")) i = Selected.size();
                        }
                    }

//                    for (int i=0; i<Tmp_Root.size() -1; i++){
//                        Tmp_Root.get(i).NomOfBranch--;
//                    }


//                    for (int i=0; i<Tmp_Root.size() -1; i++)//各リンクの未選択分岐数を1減らす
//                        for (int j=0; j<Links.size(); j++)
//                            if (Tmp_Root.get(i).link_ID == Links.get(j).link_ID)
//                                this.Links.get(j).NomOfBranch --;

//改良
                    //今作ったルートの分岐数をチェック,適正値に訂正
                    for (int i=0; i<Tmp_Root.size()-1; i++){
                        int branch_count = 0;
                        for (Link aNot_Selected : Not_Selected)
                            if (Tmp_Root.get(i).end_node.equals(aNot_Selected.start_node))
                                branch_count += 1;

                        for (Link Link : Links) {
                            if (Tmp_Root.get(i).link_ID == Link.link_ID)
                                Link.NomOfBranch = branch_count;
                            if (Tmp_Root.get(i).end_node.equals(Link.end_node))
                                Link.NomOfBranch = branch_count;
                        }
                    }

                    Initial_Path.add(new ArrayList<>(Tmp_Root));//作成したパスを初期観測パス集合に追加
//改良
                    if (! Not_Selected.isEmpty()) {
                        for (int i = 0; i < Initial_Path.size(); i++) {//分岐があれば回し、なければ抜ける
                            Tmp_Decision = new ArrayList<>(Initial_Path.get(i));
                            for (int j = 0; j < Tmp_Decision.size(); j++) {
                                for (int k=0; k<Links.size(); k++) {
//                                    System.out.println("Tmp_Dec link_ID j=" +j+" "+ Tmp_Decision.get(j).link_ID + " Links link_ID k=" +k+" "+ Links.get(k).link_ID);
                                    if ((Tmp_Decision.get(j).link_ID == Links.get(k).link_ID) && Links.get(k).NomOfBranch > 0) {
                                        Tmp_Root.clear();
                                        for (int l = 0; l <= j; l++) {//分岐を発見したところまでのルートを追加
                                            Tmp_Root.add(Tmp_Decision.get(l));
                                        }
                                        flag = true;
                                        k = Links.size();
                                        j = Tmp_Decision.size();
                                        i = Initial_Path.size();
                                    } else flag = false;
                                }
                            }
                        }
                    }else flag = false;

//                    if (! Not_Selected.isEmpty()) {
//                        for (int i = 0; i < Initial_Path.size(); i++) {//分岐があれば回し、なければ抜ける
//                            Tmp_Decision = new ArrayList<Link>(Initial_Path.get(i));
//                            for (int j = 0; j < Tmp_Decision.size(); j++) {
//                                if (Tmp_Decision.get(j).NomOfBranch > 0) {
//                                    Tmp_Root.clear();
//                                    for (int k = 0; k <= j; k++) {//分岐を発見したところまでのルートを追加
//                                        Tmp_Root.add(Tmp_Decision.get(k));
//                                    }
//                                    flag = true;
//                                    j = Tmp_Decision.size();
//                                    i = Initial_Path.size();
//                                } else flag = false;
//                            }
//                        }
//                    }else flag = false;
                }

//                for (int j=0; j<Initial_Path.size(); j++) {
//                    ArrayList<Link> al = Initial_Path.get(j);
//                    System.out.println(j);
//                    for (int i = 0; i < al.size(); i++) {
//                        System.out.println(al.get(i).link_ID + "   " + al.get(i).link_name + " " + al.get(i).start_node + " " +
//                                al.get(i).end_node + " " + al.get(i).link_state_flag + " " + al.get(i).NomOfBranch);
//                    } System.out.println();
//                }
//                System.out.println("11111111111111");
//                System.out.println(Initial_Path.size());
            }else break;
        }
    }
}