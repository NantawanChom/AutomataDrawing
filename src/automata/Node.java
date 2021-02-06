public class Node {
    private String name="";  //∅
    private int nodeRadius=30; //30
    private int X; //Point of circle
    private int Y; // Point of circle
    private int x; //for small circle
    private int y; // for small circle
    private int W; // Width of circle
    private int H; // Hight of circle
    private int w; //for small circle
    private int h; //for small circle
    private int X_Name; //Point of text
    private int Y_Name; // Point of text
    private boolean isStartState;
    private boolean isAcceptState;
    //String text = "";
    Node(int x,int y,int w,int h,boolean isStart,boolean isAccept){ //ตำแหน่ง X,Y
        this.X=x;
        this.Y=y;
        this.W=w;
        this.H=h;
        this.isStartState=isStart;
        this.isAcceptState=isAccept;
    }

    public String getName(){
        return this.name;
    }
    public int getX(){
        return this.X;
    }
    public int getY(){
        return this.Y;
    }
    public int getW(){
        return this.W;
    }
    public int getH(){
        return this.H;
    }
    public int getX_Name(){return this.X_Name;}
    public int getY_Name(){return this.Y_Name;}
    public int getx(){return this.x;}
    public int gety(){return this.y;}
    public int getw(){return this.w;}
    public int geth(){return this.h;}
    public boolean getIsStartState(){
        return isStartState;
    }
    public boolean getIsAcceptState(){
        return this.isAcceptState;
    }
    public double getClosestPointOnCircleX(double x,double y){
        double dx=x-(this.X+30);
        double dy=y-(this.Y+30);
        double scale=Math.sqrt(dx*dx+dy*dy);
        return (this.X+30)+dx*nodeRadius/scale;
    }
    public double getClosestPointOnCircleY(double x,double y){
        double dx=x-(this.X+30);
        double dy=y-(this.Y+30);
        double scale=Math.sqrt(dx*dx+dy*dy);
        return (this.Y+30)+dy*nodeRadius/scale;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setX(int x){
        this.X=x;
    }
    public void setY(int y){
        this.Y=y;
    }
    public void setW(int w){
        this.W=w;
    }
    public void setH(int h){
        this.H=h;
    }
    public void setx(int x){if(getIsAcceptState()){this.x=x;}}
    public void sety(int y){if(getIsAcceptState()){this.y=y;}}
    public void setw(int w){if(getIsAcceptState()){this.w=w;}}
    public void seth(int h){if(getIsAcceptState()){this.h=h;}}
    public void setX_name(int x){ this.X_Name=x; }
    public void setY_name(int y){
        this.Y_Name=y;
    }
    public void setIsStartState(boolean s){
        this.isStartState=s;
    }
    public void setIsAcceptState(boolean a){
        this.isAcceptState=a;
    }


}
