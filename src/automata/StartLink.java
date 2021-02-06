public class StartLink {
    private Node node;
    private double deltaX;
    private double deltaY;
    private String text;
    private double snapToPadding = 6; //6
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    StartLink(Node node,int mouseX,int mouseY){
        this.node=node;
        this.deltaX=0;
        this.deltaY=0;
        this.text="";
        setAnchorPoint(mouseX,mouseY);
    }
    public void setAnchorPoint(int x,int y){

        this.deltaX=x-(this.node.getX()+30);  //currentLink = new StartLink(targetNode, originalClick);
        this.deltaY=y-(this.node.getY()+30);

        if(Math.abs(this.deltaX)<snapToPadding){
            this.deltaX=0;
        }
        if(Math.abs(this.deltaY)<snapToPadding){
            this.deltaY=0;
        }
    }
    public Node getNode(){
        return node;
    }
    public double getEndPointsStartX(){
        return this.startX=(this.node.getX()+30)+this.deltaX;
    }
    public double getEndPointsStartY(){
        return this.startY=(this.node.getY()+30)+this.deltaY;
    }
    public double getEndPointsEndX(){
        return this.endX=this.node.getClosestPointOnCircleX(startX,startY);
    }
    public double getEndPointsEndY(){
        return this.endY=this.node.getClosestPointOnCircleY(startX,startY);
    }



}
