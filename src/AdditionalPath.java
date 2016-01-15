import java.util.ArrayList;
import java.util.List;

public class AdditionalPath {
    public ArrayList<Link> Additional_Path; //追加観測パスを格納
    public List<Link> Normal_Link; //正常リンク
    public List<Link> Failure_Candidate;//故障リンク候補集合
    public List<Link> Failure_Confirmed;//故障リンク確定集合
    ArrayList<Link> Tmp_Root; //2次元リストの2次元目作成用の仮のリスト

    AdditionalPath(List<Link> normal, List<Link> cand, List<Link> conf){
        this.Normal_Link = new ArrayList<>(normal);
        this.Failure_Candidate = new ArrayList<>(cand);
        this.Failure_Confirmed = new ArrayList<>(conf);
        this.Tmp_Root = new ArrayList<>();

        additional_path();
    }

    void additional_path(){

    }
}
