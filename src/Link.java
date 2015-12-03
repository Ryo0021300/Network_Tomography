public class Link {//リンククラス
    //パラメータ
    public int link_ID;//ID
    public String link_name;//リンク名
    public String start_node ;//そのリンクの始点ノード
    public String end_node ;//そのリンクの終点ノード
    public Boolean link_state_flag ; //リンクの状態フラグ
    public int NomOfBranch = 0;//この先の未選択分岐数

    Link(int i, String l){//コンストラクタ
        String line = l;
        this.link_ID = i;
        String[] Link = line.split(",",0);//カンマ区切り
        this.link_name = Link[0];//リンク名を保存
        this.start_node = Link[1];//始点ノードを保存
        this.end_node = Link[2];//終点ノードを保存
        this.link_state_flag = true;
    }
}