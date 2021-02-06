public class Link {
    private Node nodeA;
    private Node nodeB;
    private String text;
    private int x_text;
    private int y_text;
    private Double lineAngleAdjust;
    private Double parallelPart;
    private Double perpendicularPart;
    private  int snapToPadding=6;
    private float ctrlX;
    private float ctrlY;

    Link(Node a,Node b){
        this.nodeA=a;
        this.nodeB=b;
        this.text="ε";
        this.lineAngleAdjust=0.0;
        this.parallelPart=0.5;
        this.perpendicularPart=0.0;
    }
    public void setText(String text){
        this.text=text;
    }
    public String getText(){
        return text;
    }
    public Node getNodeA(){
        return nodeA;
    }
    public Node getNodeB(){
        return nodeB;
    }
    public void setPointText(int x,int y){
        this.x_text=x;
        this.y_text=y;
    }
    public float getCtrlX(){
        return this.ctrlX;
    }
    public float getCtrlY(){
        return this.ctrlY;
    }
    public void setCtrlX(float x){
        this.ctrlX=x;
    }
    public void setCtrlY(float y){
        this.ctrlY=y;
    }
    public int getPointTextX(){
        return this.x_text;
    }
    public int getPointTextY(){
        return this.y_text;
    }
    public double getAnchorPointX(){
        double dx = (this.nodeB.getX()+30) - (this.nodeA.getX()+30);
        double dy = (this.nodeB.getY()+30) - (this.nodeA.getY()+30);
        double scale = Math.sqrt(dx * dx + dy * dy);
        return (this.nodeA.getX()+30) + dx * this.parallelPart - dy * this.perpendicularPart / scale;
    }

    public double getAnchorPointY(){
        double dx = (this.nodeB.getX()+30) - (this.nodeA.getX()+30);
        double dy = (this.nodeB.getY()+30) - (this.nodeA.getY()+30);
        double scale = Math.sqrt(dx * dx + dy * dy);
        return (this.nodeA.getY()+30) + dy * this.parallelPart + dx * this.perpendicularPart / scale;
    }
    public void setAnchorPointX(int x,int y){
        double dx = (this.nodeB.getX()+30) - (this.nodeA.getX()+30);
        double dy = (this.nodeB.getY()+30)- (this.nodeA.getY()+30);
        double scale = Math.sqrt(dx * dx + dy * dy);
        this.parallelPart = (dx * (x - (this.nodeA.getX()+30)) + dy * (y - (this.nodeA.getY()+30))) / (scale * scale);
        this.perpendicularPart = (dx * (y - (this.nodeA.getY()+30)) - dy * (x - (this.nodeA.getX()+30))) / scale;
        // snap to a straight line

        if(this.parallelPart > 0 && this.parallelPart < 1 && Math.abs(this.perpendicularPart) < snapToPadding) {
            if(this.perpendicularPart < 0){
                this.lineAngleAdjust= this.perpendicularPart * Math.PI;
            }
            this.perpendicularPart = 0.0;
        }
    }

    public double getEndPointsAndCircleStartX(){
        if(this.perpendicularPart == 0) {
            double midX = ((this.nodeA.getX()+30) + (this.nodeB.getX()+30)) / 2;
            double midY = ((this.nodeA.getY()+30) + (this.nodeB.getY()+30)) / 2;
            double startX = this.nodeA.getClosestPointOnCircleX(midX, midY);
            return startX;
        }
        return 0.0; //??
    }

    public double getEndPointsAndCircleStartY(){
        if(this.perpendicularPart == 0) {
            double midX = ((this.nodeA.getX()+30) + (this.nodeB.getX()+30)) / 2;
            double midY = ((this.nodeA.getY()+30) + (this.nodeB.getY()+30)) / 2;
            double startY = this.nodeA.getClosestPointOnCircleY(midX, midY);
            return startY;
        }
        return 0.0; //??
    }

    public double getEndPointsAndCircleEndX(){
        if(this.perpendicularPart == 0) {
            double midX = ((this.nodeA.getX()+30) + (this.nodeB.getX()+30)) / 2;
            double midY = ((this.nodeA.getY()+30) + (this.nodeB.getY()+30)) / 2;
            double endX = this.nodeB.getClosestPointOnCircleX(midX, midY);
            return endX;
        }
        return 0.0 ; //?
    }

    public double getEndPointsAndCircleEndY(){
        if(this.perpendicularPart == 0) {
            double midX = ((this.nodeA.getX()+30) + (this.nodeB.getX()+30)) / 2;
            double midY = ((this.nodeA.getY()+30) + (this.nodeB.getY()+30)) / 2;
            double endY = this.nodeB.getClosestPointOnCircleY(midX, midY);
            return endY;
        }
        return 0.0 ; //?
    }
}