import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class DataAutomata {
    private ArrayList<Node>node;
    private ArrayList<Link>link;
    private ArrayList<SelfLink1>loop;
    private StartLink start;
    private String state="";
    private String alphabet="";
    private String Initial="";
    private String accept="";
    private ArrayList<String>transition;
    private ArrayList<String>Alphabet;
    private ArrayList<String>Accept;
    private boolean IsDFA=true;
    DataAutomata(ArrayList<Node>node,ArrayList<Link>link,ArrayList<SelfLink1>loop,StartLink start){
        this.node=node;
        this.link=link;
        this.loop=loop;
        this.start=start;
        this.transition=new ArrayList<>();
        this.Alphabet=new ArrayList<>();
        this.Accept=new ArrayList<>();

        setState();
        alphabet();
        setInitial();
        AcceptState();
        setTransition();
    }

    public void setState(){
        for(int i=0;i<node.size();i++){
            if(!node.get(i).getName().equals("")){
                state+=node.get(i).getName()+" ";
            }
        }
    }

    public void alphabet(){
        for(int i=0;i<link.size();i++){
            if(!link.get(i).getText().equals("")){
                alphabet+=link.get(i).getText()+" ";
            }
        }

        for(int j=0;j<loop.size();j++){
            if(!loop.get(j).getText().equals("")){
                alphabet+=loop.get(j).getText()+" ";
            }
        }

        String[] alpha=alphabet.split(" ");
        for(int i=0;i<alpha.length;i++){
            if(!Alphabet.contains(alpha[i])){
                Alphabet.add(alpha[i]);
            }
        }
    }

    public void setInitial(){
        Node temp=start.getNode();
        Initial+=temp.getName();
    }

    public void AcceptState(){
        for(int i=0;i<node.size();i++){
            if(node.get(i).getIsAcceptState()){
                accept+=node.get(i).getName()+" ";
            }
        }

        String[] cut=accept.split(" ");
        for(int i=0;i<cut.length;i++){
            Accept.add(cut[i]);
        }
    }

    public void setTransition(){
        String s="";
        for(int i=0;i<loop.size();i++){
            if(!loop.get(i).getText().equals("")){
                if(!loop.get(i).getText().equals("ε")) {
                    s = loop.get(i).getNode().getName() + "-" + loop.get(i).getText() + "->" + loop.get(i).getNode().getName();
                    transition.add(s);
                }
            }else{
                IsDFA=false;
            }
        }

        for(int j=0;j<link.size();j++){
            if(!link.get(j).getText().equals("")){
                if(!link.get(j).getText().equals("ε")) {
                    s = link.get(j).getNodeA().getName() + "-" + link.get(j).getText() + "->" + link.get(j).getNodeB().getName();
                    transition.add(s);
                }else{
                    IsDFA=false;
                }
            }
        }
    }

    public String getState(){
        return this.state;
    }

    public String getAlphabet(){
        return this.alphabet;
    }
    public String getInitial(){
        return this.Initial;
    }

    public String getAccept(){
        return this.accept;
    }

    public void getTransition(){
        for(int i=0;i<transition.size();i++){
            System.out.println(transition.get(i));
        }
    }

    public HashSet<String> getLanguage(){
        ArrayList<String> lang = new ArrayList<String>();
        ArrayList<String> trans = new ArrayList<String>();
        HashSet<String> hash = new HashSet<String>();

        for (String a : Alphabet) {
            lang.add(a);
        }

        for (String i : transition) {
            String[] cut = i.split("->");
            String[] cut1 = cut[0].trim().split("-");
            trans.add(cut1[0].trim() + " " + cut1[1].trim() + " " + cut[1].trim());
        }

        int temp = 0;
        for (int i = 0; i < 5; i++) {
            int size = lang.size();
            for (int j = temp; j < size; j++) {
                for (String a : Alphabet) {
                    lang.add(lang.get(j) + a);
                }
            }
            temp = size;
        }

       
        for(String a : lang){

            String next=Initial;
            boolean mayAccept = true;
            for(int i=0;i<a.length();i++){
                boolean hasNext = false;
                for(int j=0;j<trans.size();j++){
                    String[] s1 = trans.get(j).split(" ");
                    String l = a.charAt(i) + "";

                    if (s1[0].equals(next) && l.equals(s1[1])) {
                        next = s1[2];
                        hasNext = true;
                        break;
                    }
                }
                if(!hasNext){
                    mayAccept = false;
                    break;
                }
            }
            if(mayAccept&&Accept.contains(next)){
                hash.add(a);
            }
        }
        return hash;


    }








}
