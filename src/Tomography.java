import java.util.ArrayList;

public class Tomography{

    public ArrayList<ArrayList<Link>> Route;
    public ArrayList<Link> Failure_Candidate = new ArrayList<Link>();//故障リンク候補集合
    public ArrayList<Link> Failure_Confirmed = new ArrayList<Link>();//故障リンク確定集合

    Tomography(ArrayList<ArrayList<Link>> l){
        this.Route = new ArrayList<>(l);
    }

    void Scan(){
    }
}