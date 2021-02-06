public class SelfLink1 {
    private Node node;
    private int X;
    private int Y;
    private int W;
    private int H;
    private int startAngle;
    private int arcAngle;
    private String text="ε";
    private int text_x;
    private int text_y;

    public double radianCB;
    public int xCB;
    public int yCB;
    public int rCB;

    SelfLink1(Node node,int W,int H,int startAngle,int arcAngle){
        this.node=node;
        this.X=node.getX();
        this.W=W;
        this.H=H;
        this.startAngle=startAngle;
        this.arcAngle=arcAngle;
    }

    public void setText(String s){
        this.text=s;
    }

    public String getText(){
        return this.text;
    }
    public void setPosition(int x,int y){
        this.text_x=x;
        this.text_y=y;
    }

    public int get_textX(){
        return this.text_x;
    }
    public int get_textY(){
        return this.text_y;
    }

    public Node getNode(){
        return this.node;
    }

    public void setX(int x){
      this.X=x;
    }

    public void setY(int y){
        this.Y=y;
    }

    public int getX(){
        return this.node.getX();
    }
    public int getY(){
        return this.node.getY();//-60;
    }
    public int getXl(){
        return this.X;
    }
    public int getYl(){
        return this.Y;
    }
    public int getW(){
        return this.W;
    }
    public int getH(){
        return this.H;
    }
    public int getStartAngle(){
        return this.startAngle;
    }
    public int getArcAngle(){
        return this.arcAngle;
    }

}
