public class Link implements Comparable<Link>{//リンククラス
    //パラメータ
    public int link_ID;//ID
    public String link_name;//リンク名
    public String start_node ;//そのリンクの始点ノード
    public String end_node ;//そのリンクの終点ノード
    public Boolean link_state_flag ; //リンクの状態フラグ
    public int NomOfBranch = 0;//この先の未選択分岐数

    Link(int i, String l){//コンストラクタ
        this.link_ID = i;
        String[] Link = l.split(",", 0);//カンマ区切り
        this.link_name = Link[0];//リンク名を保存
        this.start_node = Link[1];//始点ノードを保存
        this.end_node = Link[2];//終点ノードを保存
        this.link_state_flag = true;
    }

    Link(Link l){
        this.link_ID = l.link_ID;
        this.link_name = l.link_name;
        this.start_node = l.start_node;
        this.end_node = l.end_node;
        this.link_state_flag = l.link_state_flag;
        this.NomOfBranch = l.NomOfBranch;
    }

    @Override
    public int compareTo(Link l) {
        if (this.link_ID > l.link_ID) return 1;
        else if (this.link_ID < l.link_ID) return -1;
        else return 0;
    }
}