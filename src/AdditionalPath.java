import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;

public class AdditionalPath {
    public ArrayList<Link> Additional_Path; //追加観測パスを格納
    public ArrayList<ArrayList<Link>> Additional_Path_Candidate; //追加パスの候補
    public List<Link> Normal_Link; //正常リンク
    public List<Link> Failure_Candidate;//故障リンク候補集合
    public List<Link> Selected; //候補で選択済みのを入れる
    public ArrayList<ArrayList<Link>> Failure_Path; //不通ルート
    ArrayList<Link> Tmp_Root; //2次元リストの2次元目作成用の仮のリスト

    AdditionalPath(List<Link> normal, List<Link> cand, ArrayList<ArrayList<Link>> f_path){
        this.Additional_Path = new ArrayList<>();
        this.Additional_Path_Candidate = new ArrayList<>();
        this.Normal_Link = new ArrayList<>(normal);
        this.Failure_Candidate = new ArrayList<>(cand);
        this.Selected = new ArrayList<>();
        this.Failure_Path = f_path;
        this.Tmp_Root = new ArrayList<>();

        additional_path();
    }

    void additional_path(){
        int halfCount; //候補集合の半数個を追加パスに含ませるためのカウンター

        //halfCountの初期化
        if (Failure_Candidate.size() > 2)
            halfCount = Math.round(Failure_Candidate.size()/2);
        else
            halfCount = 1;

        //候補集合の中から一つリンクを選び、そこからパスを構築
        for (Link l : Failure_Candidate) {
            Tmp_Root.add(l);

            if (Tmp_Root.size() < halfCount) { // halfCountが1以上
                //スタートまでたどる

                // ゴールまでたどる
            } else { // halfCountが1のとき
                //スタートまで

                //ゴールまで

            }

        }

    }
}
