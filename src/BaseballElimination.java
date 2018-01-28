import java.util.ArrayList;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FordFulkerson;

/**
 * Created by HuaZ on 1/27/2018.

 ASSESSMENT SUMMARY

 Compilation: PASSED
 API: PASSED
 Findbugs: FAILED (2 warnings)
 PMD: PASSED
 Checkstyle: FAILED (0 errors, 80 warnings)
 Correctness: 23/23 tests passed
 Memory: 4/4 tests passed
 Timing: 1/1 tests passed A

 ggregate score: 100.00%
 [Compilation: 5%, API: 5%, Findbugs: 0%, PMD: 0%, Checkstyle: 0%, Correctness: 60%, Memory: 10%, Timing: 20%]



 */
public class BaseballElimination {
    private int n;
    private int[] win, lost, left;
    private int[][] g;
    private ArrayList<String> teams;
    private ArrayList<String> cert;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename){
        if(filename == null) throw new IllegalArgumentException();
        In in = new In(filename);
        teams = new ArrayList<String>();
        cert = new ArrayList<String>();

        n = Integer.parseInt(in.readLine());
        win = new int[n];
        lost = new int[n];
        left = new int[n];
        g = new int[n][n];
        int i = 0;
        while (!in.isEmpty()) {
            String[] line = in.readLine().trim().split(" +");  // split one or multiple spaces
            teams.add(line[0]);
            win[i] = Integer.parseInt(line[1]);
            lost[i] = Integer.parseInt(line[2]);
            left[i] = Integer.parseInt(line[3]);
            for(int j = 0; j<n; j++){
                g[i][j] = Integer.parseInt(line[j+4]);
            }
            i++;
        }
    }

    // number of teams
    public int numberOfTeams(){
        return n;
    }

    // all teams
    public Iterable<String> teams(){return teams;}

    // number of wins for given team
    public int wins(String team){return win[getIndexOfTeam(team)];}

    // number of losses for given team
    public int losses(String team){return lost[getIndexOfTeam(team)];}

    // number of remaining games for given team
    public int remaining(String team){return left[getIndexOfTeam(team)];}

    // number of remaining games between team1 and team2
    public int against(String team1, String team2){return g[getIndexOfTeam(team1)][getIndexOfTeam(team2)];}

    private int getIndexOfTeam(String team){
        if(team == null) throw new IllegalArgumentException();
        int index = teams.indexOf(team);
        if(index == -1) throw new IllegalArgumentException();
        return index;
    }

    // is given team eliminated?
    // means this team can't never win the first place
    // assume no tie in the game
    // core code in this exercise
    public boolean isEliminated(String team){
        // trivia version, if win[x] + left[x] < win[i] any teams, then team is isEliminated
        int x = getIndexOfTeam(team);
        boolean isEliminated = false;
        cert = new ArrayList<String>();
        int maxWinOfX = win[x] + left[x];

        for(int i=0; i<n; i++){
            //System.out.println("maxWin="+maxWinOfX + "  win[i]="+win[i]);
            if(maxWinOfX < win[i]) {
                cert.add(teams.get(i));
                isEliminated = true;
            }
        }
        if(isEliminated) return true;


        // non-trivia method.
        // build FlowNetwork without team, from right to left of the graph
        int v = n*(n-1)/2 + 2 + 1; // 2 for extra s and t, 1 for vacant x
        int s = v-2, t = v - 1;
        FlowNetwork network = new FlowNetwork(v);

        // build n-1 team vertices to t
        for(int i = 0; i<n; i++){
            if(i == x) continue;
            network.addEdge(new FlowEdge(i,t, maxWinOfX - win[i]));
        }

        // go through (n-1)*(n-2)/2 match vertices
        int seq = n;
        int maxFlow = 0;
        for(int i=0; i<n; i++){
            if(i == x) continue;
            for(int j=i+1; j<n;j++){
                if(j == x) continue;
                network.addEdge(new FlowEdge(seq, i, Double.POSITIVE_INFINITY));
                network.addEdge(new FlowEdge(seq, j, Double.POSITIVE_INFINITY));
                network.addEdge(new FlowEdge(s, seq, g[i][j]));
                maxFlow += g[i][j];
                seq++;
            }
        }

        FordFulkerson ff = new FordFulkerson(network, s, t);
        if(ff.value() != maxFlow) {
            isEliminated = true;

            // add certs
            for(int i=0; i<n; i++){
                if(i == x) continue;
                if(ff.inCut(i)) cert.add(teams.get(i));
            }
        }

        return isEliminated;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team){
        if(!isEliminated(team)) return null;
        return cert;
    }


    public static void main(String[] args) {
        //BaseballElimination division = new BaseballElimination(args[0]);
        BaseballElimination division = new BaseballElimination("baseball/teams60.txt");
        //BaseballElimination division = new BaseballElimination("baseball/teams7.txt");
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
