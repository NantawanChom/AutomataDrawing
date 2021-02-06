import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
public class Draw extends JFrame implements MouseListener, MouseMotionListener, KeyListener {
    Canvas c;
    ArrayList<Node> arr_node=new ArrayList<>();
    ArrayList<Link>arr_link=new ArrayList<>();
    ArrayList<SelfLink1>SelfLinks=new ArrayList<>();
    StartLink[] arr_start=new StartLink[1];
    boolean accept=false; //Make accept state
    boolean alt=false; // make line ?
    boolean del=false; // want to delete ?
    boolean spacebar=false;
    boolean s = false;
    boolean o = false;
    Node start_node=null; // check tail of arrow
    Node target_node=null; // check head of arrow
    int x1=-1;
    int y1=-1;
    int x2=-1;
    int y2=-1;
    int w=1200;
    int h=800;
    Draw() {
        super("My Drawing");

        // create a empty canvas
        c = new Canvas() {
            public void paint(Graphics g) {
            }
        };


        JPanel panel=new JPanel();
        panel.setSize(200,h);
        panel.setBounds(0,0,300,h);
        panel.setBackground(Color.LIGHT_GRAY);
        JLabel label=new JLabel("แสดงสายอักขระที่ยอมรับ");
        JTextArea a1=new JTextArea(15,20);
        JButton b1=new JButton("OK");
        JButton b2=new JButton("Clear Canvas");
        JButton b3=new JButton("Save File");
        JButton b4=new JButton("Open File");
        //JTextArea a1=new JTextArea(20,20);
        //a1.setSize(200,300);
        panel.add(label);
        panel.add(a1);
        panel.add(b1);
        panel.add(b2);
        panel.add(b3);
        panel.add(b4);

        // set background
        c.setBackground(Color.white);

        // add mouse listener
        c.addMouseListener(this);
        c.addMouseMotionListener(this);

        // add keyboard listener

        c.addKeyListener(this);
        c.setSize(1000,h);
        add(panel);
        add(c);

        setSize(w,h);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        show();

        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Graphics2D g = (Graphics2D) c.getGraphics();
                g.setColor(Color.white);
                g.fillRect(0,0,w,h);
                arr_node.clear();
                arr_link.clear();
                SelfLinks.clear();
                arr_start[0]=null;
            }
        });
        
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                Savee();
            } catch (IOException ex) {
                Logger.getLogger(Draw.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        });
         b4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                Open();
            } catch (IOException ex) {
                Logger.getLogger(Draw.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        });
        

       b1.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               DataAutomata d=new DataAutomata(arr_node,arr_link,SelfLinks,arr_start[0]);
               HashSet<String>hash=d.getLanguage();
               int count=0;
               Iterator p=hash.iterator();
               while(p.hasNext()){
                   String t=(String)p.next();
                   if(t.length()<=5) {
                       if (!p.hasNext()) {
                           a1.append(t);
                       } else {
                           if(count==4) {
                               a1.append(t + "," + "\n");
                               count=0;
                           }else{
                               a1.append(t + ",");
                               count++;
                           }
                       }
                   }
               }
           }
       });
    }



   class Save {

        ArrayList<Node> arrN = new ArrayList<>();
        ArrayList<Link> arrL = new ArrayList<>();
        ArrayList<SelfLink1> arrS = new ArrayList<>();
        StartLink[] arrSt = new StartLink[1];

        
        public Save(ArrayList<Node> arrN, ArrayList<Link> arrL, ArrayList<SelfLink1> arrS, StartLink[] arrSt) {
            this.arrN = arrN;
            this.arrL = arrL;
            this.arrS = arrS;
            this.arrSt = arrSt;
        }
        
    }
    
     public void Savee() throws IOException, java.io.IOException{

        Save s = new Save(arr_node,arr_link,SelfLinks,arr_start);
        
        Gson gson = new Gson();
        java.lang.reflect.Type type = new TypeToken<Save>() {
        }.getType();
        String json = gson.toJson(s, type);
        System.out.println(json);
        FileWriter fileWriter = new FileWriter("C:\\Users\\Admin\\Desktop\\Test.txt");
        fileWriter.write(json);
        fileWriter.close();
        java.util.List<Save> fromJson = gson.fromJson(json, type);

        for (Save Save : fromJson) {
            System.out.println();
        }
    }
     public void Open() throws FileNotFoundException{
         
         BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\Users\\Admin\\Desktop\\Test.txt"));
        Gson gson = new Gson();
        Save json = gson.fromJson(bufferedReader, Save.class);
         System.out.println(json);
         arr_node = json.arrN;
	 arr_link = json.arrL;
	 SelfLinks = json.arrS;
	 arr_start = json.arrSt;
         DrawAuomata();
         
         }



    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(KeyEvent.VK_SHIFT==e.getKeyCode()){
            accept=true;  // when press Shift
        }
        if(KeyEvent.VK_ALT==e.getKeyCode()){
            alt=true; // when press Alt
            System.out.println("alt");
        }
        if(KeyEvent.VK_BACK_SPACE==e.getKeyCode()){
            del=true;  //when press Backspace
            System.out.println("DeL");
        }
        if(KeyEvent.VK_SPACE==e.getKeyCode()){
            spacebar=true;
            System.out.println("SpaceBar");
        }
        if(KeyEvent.VK_S == e.getKeyCode()){
            s = true;
            System.out.println("Save");
        }
       /* if(s == true){
            try {
                Savee();
            } catch (IOException ex) {
                Logger.getLogger(Draw.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(KeyEvent.VK_O == e.getKeyCode()){
            o = true;
            System.out.println("Opennnnnnnnnn");
        }*/
      /*  if(o == true){
            try {
                Open();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Draw.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x=e.getX();
        int y=e.getY();
        Node check=null;
        check=selectNode(x,y);
        Link check_link=null;
        SelfLink1 select=null;
        select=selectSelfLink(x,y);
        check_link=selectLink(x,y);
        StartLink check_start=null;
        check_start=selectStartLink(x,y);

        if(select!=null){
            System.out.println("Find");
        }

        if(e.getClickCount()==2 && !e.isConsumed() && !accept && check==null && check_link==null && check_start==null){
            e.consume();
            drawCircle(x,y);
        }
        if(e.getClickCount()==1 && !e.isConsumed() && !accept && ! alt && !del && ! spacebar) {
                e.consume();
                drawSelect(x, y);
               // selectStartLink(x,y);
        }
        if(e.getClickCount()==2 && !e.isConsumed() && accept && check==null && check_link==null && check_start==null){
            e.consume();
            drawCircle(x,y);
        }
        if(e.getClickCount()==1 && !e.isConsumed() && del){
            e.consume();
            drawSelect(x, y);
            deleteSelect(x,y);
        }
        if(e.getClickCount()==1 && !e.isConsumed() && spacebar){
            e.consume();
            drawSelect(x, y);
            editName(x,y);
            drawSelect(x,y);
        }



    }

    @Override
    public void mousePressed(MouseEvent e) {
        x1=e.getX();
        y1=e.getY();

        if(alt){
            start_node=selectNode(x1,y1);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        x2=e.getX();
        y2=e.getY();

        if(alt){
            target_node=selectNode(x2,y2);
            drawArrow(start_node,target_node);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(! alt) {
            int x = e.getX();
            int y = e.getY();
            System.out.println("Drag");
            Node temp = null;
            Link temp1=null;
            StartLink temp2=null;
            SelfLink1 temp3=null;
            temp = selectNode(x, y);
            temp1=selectLink(x,y);
            temp2=selectStartLink(x,y);
            temp3=selectSelfLink(x,y);
            if (temp != null) {
                drawSelect(x, y);
                Move(x, y, temp);
            }else if(temp1!=null){
                drawSelect(x, y);
                moveLink(x,y,temp1);
            }else if(temp2!=null){
                drawSelect(x,y);
                moveStart(x,y);
            }else if(temp3!=null){
                drawSelect(x,y);
                moveSelfLink(temp3,x,y);
            }
            ///change radianCB in SelfLink1
            //
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
    public void editName(int x,int y){
        Node temp_node=null;
        Link temp_link=null;
        SelfLink1 temp_self=null;

        temp_node=selectNode(x,y);
        temp_link=selectLink(x,y);
        temp_self=selectSelfLink(x,y);

        if(temp_node!=null){
            String s=JOptionPane.showInputDialog(null,"input message",
                    "input name of state",JOptionPane.INFORMATION_MESSAGE);
            if(s!=null){
                temp_node.setName(s);
                //node_s.setX_name(x-5);
                //node_s.setY_name(y+1);
            }
            spacebar=false;
        }else if(temp_link!=null){
            String s=JOptionPane.showInputDialog(null,"input message",
                    "input name of Arrow",JOptionPane.INFORMATION_MESSAGE);
            if(s!=null){
                temp_link.setText(s);
            }
            spacebar=false;
        }else if(temp_self!=null){
            String s=JOptionPane.showInputDialog(null,"input message",
                    "input name of Arrow",JOptionPane.INFORMATION_MESSAGE);
            if(s!=null){
                temp_self.setText(s);
            }
            spacebar=false;
        }
    }
    public void drawCircle(int x,int y){
        Graphics2D g = (Graphics2D) c.getGraphics();
        g.setColor(Color.black);
        if(accept==false){
            Shape circle = new Ellipse2D.Double(x-30, y-30, 60, 60);
            g.draw(circle);
            System.out.println(((Ellipse2D.Double) circle).getCenterX()+" "
                    +((Ellipse2D.Double) circle).getCenterY());
            // built object of Node
            Node node_s=new Node(x-30,y-30,60,60,false,false);

            // save node to arr_node
            arr_node.add(node_s);

            String s=JOptionPane.showInputDialog(null,"input message",
                    "input name of state",JOptionPane.INFORMATION_MESSAGE);
            if(s!=null){
                g.drawString(s, x-5, y+1);
                node_s.setName(s);
                node_s.setX_name(x-5);
                node_s.setY_name(y+1);
            }else{
                g.drawString(node_s.getName(), x -5, y+1);
            }
        }else{

            Shape circle = new Ellipse2D.Double(x-30, y-30, 60, 60); // big circle
            Shape circle1 = new Ellipse2D.Double(x-25, y-25, 50, 50); // small circle

            g.draw(circle);
            g.draw(circle1);

            Node node_s=new Node(x-30,y-30,60,60,false,true);
            node_s.setx(x-25); //set small circle
            node_s.sety(y-25);
            node_s.setw(50);
            node_s.seth(50);

            String s=JOptionPane.showInputDialog(null,"input message",
                    "input name of state",JOptionPane.INFORMATION_MESSAGE);
            if(s!=null){
                g.drawString(s, x -5, y+1);
                node_s.setName(s);
                node_s.setX_name(x-5);
                node_s.setY_name(y+1);
            }else{
                g.drawString(node_s.getName(), x -5, y+1);
            }

            // save node to arr_node
            arr_node.add(node_s);
            accept=false;
        }
    }

    public void drawArrow(Node start,Node end){
        Graphics2D line_g = (Graphics2D) c.getGraphics();
        Graphics2D g1 = (Graphics2D) c.getGraphics();
        Graphics2D triangle_g2 = (Graphics2D) c.getGraphics();
        Graphics2D g = (Graphics2D) c.getGraphics();

        g.setColor(Color.white);
        line_g.setColor(Color.black);
        triangle_g2.setColor(Color.black);
        g1.setColor(Color.black);

        if(start==end){  //loop link
            SelfLink1 s=new SelfLink1(start,60,60,0,360);
            g.fillRect(0,0,w,h);
            s.xCB = s.getNode().getX();
            s.yCB = s.getNode().getY();
            s.rCB = (int)((s.getNode().getW()/2)*Math.sqrt(2));
            s.radianCB = -Math.PI/2; // radian start

            double dx = Math.cos(s.radianCB);
            double dy = Math.sin(s.radianCB);

            line_g.drawArc(s.xCB + (int)(s.rCB*dx),s.yCB + (int)((s.rCB)*dy), s.getW(), s.getH(), s.getStartAngle(), s.getArcAngle());

            s.setX(s.xCB + (int)(s.rCB*dx));
            s.setY(s.yCB + (int)((s.rCB)*dy));
            SelfLinks.add(s);
            DrawAuomata();
            //Name of Arrow
            int test1=(s.getXl()+s.getW());
            int test2=(s.getYl()+s.getH());
            String t=JOptionPane.showInputDialog(null,"input message",
                    "input name of Arrow",JOptionPane.INFORMATION_MESSAGE);
            if(t!=null){
                line_g.drawString(t, test1, test2);
                s.setText(t);
                s.setPosition(test1,test2);
            }else{
                line_g.drawString(s.getText(), test1,test2);
                s.setPosition(test1,test2);
            }


            alt=false;
            double endAngle = Math.atan2(s.getYl()-end.getY(),s.getXl() - end.getX())+Math.PI/4;
            int xStuff = (int)(end.getX()+30 + end.getW()/2*Math.cos(endAngle));
            int yStuff = (int)(end.getY()+30 + end.getW()/2*Math.sin(endAngle));

            drawArrowReal(g1,xStuff,yStuff,Math.atan2(end.getY()-s.getYl(),end.getX()-s.getXl())+Math.PI/4);

        }else if(start==null && end != null){
            StartLink startLink=new StartLink(end,x1,y1);
            double startX=startLink.getEndPointsStartX();
            double startY=startLink.getEndPointsStartY();
            double endX=startLink.getEndPointsEndX();
            double endY=startLink.getEndPointsEndY();
            //Shape line = new Line2D.Double(startX, startY, endX, endY);
            //line_g.draw(line);
            line_g.draw(new QuadCurve2D.Float((float) x1,(float) y1,(float) (startX+endX)/2,
                    (float) (startY+endY)/2,(float) endX,(float) endY));

            double endAngle = Math.atan2((y1-30)-end.getY(),(x1-30) - end.getX());
            int xStuff = (int)(end.getX()+30 + end.getW()/2*Math.cos(endAngle));
            int yStuff = (int)(end.getY()+30 + end.getW()/2*Math.sin(endAngle));

            drawArrowReal(g1,xStuff,yStuff,Math.atan2(end.getY()-(y1-30),end.getX()-(x1-30)));
            arr_start[0]=startLink;
            end.setIsStartState(true);
            alt=false;
        }else if(start!=null && end != null){
            Link link = new Link(start, end);
            double startX = link.getEndPointsAndCircleStartX();
            double startY = link.getEndPointsAndCircleStartY();
            double endX = link.getEndPointsAndCircleEndX();
            double endY = link.getEndPointsAndCircleEndY();

            //Shape line = new Line2D.Double(startX, startY, endX, endY);
            //line_g.draw(line);

            line_g.draw(new QuadCurve2D.Float((float) startX,(float) startY,(float) (startX+endX)/2,
                    (float) (startY+endY)/2,(float) endX,(float) endY));
            link.setCtrlX((float) (startX+endX)/2);
            link.setCtrlY((float) (startY+endY)/2);

            //Name of Arrow
            String s=JOptionPane.showInputDialog(null,"input message",
                    "input name of Arrow",JOptionPane.INFORMATION_MESSAGE);

            int midX= (int) ((startX+endX)/2);
            int tail= (int) ((endY+startY)/2)+8;

            if(s!=null){
                line_g.drawString(s, midX, tail);
                link.setText(s);
                link.setPointText(midX,tail);
            }else{
                line_g.drawString(link.getText(), midX, tail);
                link.setPointText(midX,tail);
                line_g.drawString(link.getText(), link.getPointTextX(), link.getPointTextY());
            }

            double endAngle = Math.atan2(start.getY()-end.getY(),start.getX() - end.getX());
            int xStuff = (int)(end.getX()+30 + end.getW()/2*Math.cos(endAngle));
            int yStuff = (int)(end.getY()+30 + end.getW()/2*Math.sin(endAngle));

            drawArrowReal(g1,xStuff,yStuff,Math.atan2(end.getY()-start.getY(),end.getX()-start.getX()));

           arr_link.add(link);
           alt=false;
        }


    }
    public void drawArrowReal(Graphics c,int x,int y,double angle){
        double dx = Math.cos(angle);
        double dy = Math.sin(angle);

        int[] arrX = {x,(int)(x - 8 * dx + 5 * dy),(int)(x - 8 * dx - 5 * dy)};
        int[] arrY = {y,(int)(y - 8 * dy - 5 * dx),(int)(y - 8 * dy + 5 * dx)};
        c.fillPolygon(arrX,arrY,3);
    }

    public Node selectNode(int x,int y){  //select node of state
        Node temp=null;
        for(int i=0;i<arr_node.size();i++){
            Shape circle = new Ellipse2D.Double(arr_node.get(i).getX(), arr_node.get(i).getY(), arr_node.get(i).getW(),
                    arr_node.get(i).getH());
            System.out.println();
            if(circle.contains(x,y)){
                temp=arr_node.get(i);
                break;
            }
        }
        return temp;
    }

    public SelfLink1 selectSelfLink(int x,int y){
        SelfLink1 temp=null;
        for(int i=0;i<SelfLinks.size();i++){
            Shape circle = new Ellipse2D.Double(SelfLinks.get(i).getXl(), SelfLinks.get(i).getYl(), SelfLinks.get(i).getW(),
                    SelfLinks.get(i).getH());
            System.out.println();
            if(circle.contains(x,y)){
                temp=SelfLinks.get(i);
                break;
            }
        }
        return temp;
    }

    public Link selectLink(int x,int y){
        Link temp=null;
        //Graphics2D g = (Graphics2D) c.getGraphics();
        for(int i=0;i<arr_link.size();i++){
            Link t = (Link) arr_link.get(i);

            int midX = (t.getNodeA().getX() + t.getNodeB().getX())/2;
            int midY = (t.getNodeA().getY()+t.getNodeB().getY())/2;
            int r = 30;
            Shape circle = new Ellipse2D.Double(midX,midY,2*r,2*r);
            //g.draw(circle);
            if (circle.contains(x, y)) {
                temp = t;
                System.out.println("Find Link!!");
                break;
            }
        }
        return temp;
    }

    public StartLink selectStartLink(int x,int y){
        StartLink temp=null;
        Graphics2D g = (Graphics2D) c.getGraphics();
        if(arr_start[0]!=null){
            int midX = (int) (arr_start[0].getEndPointsStartX() + arr_start[0].getEndPointsEndX())/2;
            int midY = (int)(arr_start[0].getEndPointsStartY()+arr_start[0].getEndPointsEndY())/2;
            int r = 20;
            Shape circle = new Ellipse2D.Double(midX-30,midY-30,2*r,2*r);
            //g.draw(circle);
            if (circle.contains(x, y)) {
                temp = arr_start[0];
                System.out.println("Find Link!!");
            }
        }
        return temp;
    }

    public void drawSelect(int x,int y){
        Graphics2D g = (Graphics2D) c.getGraphics();
        Graphics2D g1 = (Graphics2D) c.getGraphics();
        Graphics2D g2 = (Graphics2D) c.getGraphics();
        Graphics2D g3 = (Graphics2D) c.getGraphics();
        g.setColor(Color.red);
        g1.setColor(Color.black);
        g2.setColor(Color.red);
        g3.setColor(Color.white);

        Node temp_node=selectNode(x,y);
        Link temp_Link=selectLink(x,y);
        StartLink temp_start=selectStartLink(x,y);
        SelfLink1 temp_self=selectSelfLink(x,y);

        if(temp_node!=null){ //select state
            //g.setColor(Color.white);
            g3.fillRect(0,0,w,h);
            int position=arr_node.indexOf(temp_node);
            if(SelfLinks.size()!=0){
                for(int i=0;i<SelfLinks.size();i++){
                    SelfLink1 s=(SelfLink1)SelfLinks.get(i);
                    //g1.drawArc(t.getX(),t.getY(),t.getW(),t.getH(),t.getStartAngle(),t.getArcAngle());

                    double dx = Math.cos(s.radianCB);
                    double dy = Math.sin(s.radianCB);

                    // line_g.drawArc(s.xCB-s.rCB,s.yCB-s.rCB,s.rCB*2,s.rCB*2,0,360);
                    g1.drawArc(s.xCB + (int)(s.rCB*dx),s.yCB + (int)((s.rCB)*dy), s.getW(), s.getH(), s.getStartAngle(), s.getArcAngle());
                    double endAngle = Math.atan2(s.getYl()-s.getY(),s.getXl() - s.getX())+Math.PI/4;
                    int xStuff = (int)(s.getX()+30 + s.getW()/2*Math.cos(endAngle));
                    int yStuff = (int)(s.getY()+30 + s.getW()/2*Math.sin(endAngle));

                    drawArrowReal(g1,xStuff,yStuff,Math.atan2(s.getY()-s.getYl(),s.getX()-s.getXl())+Math.PI/4);

                    g1.drawString(s.getText(),s.getXl()+s.getW(),s.getYl()+s.getH());
                }
            }
            for(int i=0;i<arr_node.size();i++){
                Node t=(Node)arr_node.get(i);
                if(i==position){
                    if(temp_node.getIsAcceptState()){
                        //Shape circle = new Ellipse2D.Double(temp_node.getX(), temp_node.getY(), temp_node.getW(), temp_node.getH());
                        //Shape circle1 = new Ellipse2D.Double(temp_node.getx(), temp_node.gety(), temp_node.getw(), temp_node.geth());
                        g3.fillArc(temp_node.getX(), temp_node.getY(), temp_node.getW(), temp_node.getH(),0,360);
                        g2.drawArc(temp_node.getX(), temp_node.getY(), temp_node.getW()+1, temp_node.getH()+1,0,360);
                        g2.drawArc(temp_node.getx(), temp_node.gety(), temp_node.getw(), temp_node.geth(),0,360);
                        //g2.draw(circle);
                        //g2.draw(circle1);
                            g.drawString(temp_node.getName(), temp_node.getX_Name(), temp_node.getY_Name());
                    }else{
                        //Shape circle = new Ellipse2D.Double(temp_node.getX(), temp_node.getY(), temp_node.getW(), temp_node.getH());
                        g3.fillArc(temp_node.getX(), temp_node.getY(), temp_node.getW(), temp_node.getH(),0,360);
                        g2.drawArc(temp_node.getX(), temp_node.getY(), temp_node.getW()+1, temp_node.getH()+1,0,360);
                        //g2.draw(circle);
                            g2.drawString(temp_node.getName(), temp_node.getX_Name(), temp_node.getY_Name());

                    }
                }else{
                    if(!t.getIsAcceptState()) {
                        //Shape circle = new Ellipse2D.Double(t.getX(), t.getY(), t.getW(), t.getH());
                        g3.fillArc(t.getX(), t.getY(), t.getW(), t.getH(),0,360);
                        g1.drawArc(t.getX(), t.getY(), t.getW()+1, t.getH()+1,0,360);
                        //g1.draw(circle);
                            g1.drawString(t.getName(), t.getX_Name(), t.getY_Name());
                    }else{
                        //Shape circle = new Ellipse2D.Double(t.getX(), t.getY(), t.getW(), t.getH());
                        //Shape circle1 = new Ellipse2D.Double(t.getx(), t.gety(), t.getw(), t.geth());
                        //g1.draw(circle);
                       // g1.draw(circle1);
                        g3.fillArc(t.getX(), t.getY(), t.getW(), t.getH(),0,360);
                        g1.drawArc(t.getX(), t.getY(), t.getW()+1, t.getH()+1,0,360);
                        g1.drawArc(t.getx(), t.gety(), t.getw(), t.geth(),0,360);
                            g1.drawString(t.getName(), t.getX_Name(), t.getY_Name());

                    }
                }
            }

            if(arr_link.size()!=0){
                // (float)(t.getEndPointsAndCircleStartX()+t.getEndPointsAndCircleEndX())/2
                // (float)(t.getEndPointsAndCircleStartY()+t.getEndPointsAndCircleEndY())/2
                for(int i=0;i<arr_link.size();i++){
                    Link t=(Link)arr_link.get(i);
                    g1.draw(new QuadCurve2D.Float((float) t.getEndPointsAndCircleStartX(),(float) t.getEndPointsAndCircleStartY()
                            ,t.getCtrlX(),
                            t.getCtrlY(), (float) t.getEndPointsAndCircleEndX(),(float) t.getEndPointsAndCircleEndY()));
                        int midX=(int)(t.getEndPointsAndCircleEndX()+t.getEndPointsAndCircleStartX())/2;
                        int tail=(int)((t.getEndPointsAndCircleEndY()+t.getEndPointsAndCircleStartY())/2)+8;
                        g1.drawString(t.getText(), midX, tail);

                    double endAngle = Math.atan2(t.getNodeA().getY()-t.getNodeB().getY(),t.getNodeA().getX() - t.getNodeB().getX());
                    int xStuff = (int)(t.getNodeB().getX()+30 + t.getNodeB().getW()/2*Math.cos(endAngle));
                    int yStuff = (int)(t.getNodeB().getY()+30 + t.getNodeB().getW()/2*Math.sin(endAngle));
                    drawArrowReal(g1,xStuff,yStuff,Math.atan2(t.getNodeB().getY()-t.getNodeA().getY(),t.getNodeB().getX()-t.getNodeA().getX()));
                }
            }
            if(arr_start[0]!=null){
                StartLink t=(StartLink)arr_start[0];
                Shape line = new Line2D.Double(arr_start[0].getEndPointsStartX(), arr_start[0].getEndPointsStartY()
                        , arr_start[0].getEndPointsEndX(), arr_start[0].getEndPointsEndY());
                g1.draw(line);

                double endAngle = Math.atan2((arr_start[0].getEndPointsStartY()-30)-arr_start[0].getNode().getY(),(arr_start[0].getEndPointsStartX()-30) - arr_start[0].getNode().getX());
                int xStuff = (int)(arr_start[0].getNode().getX()+30 + arr_start[0].getNode().getW()/2*Math.cos(endAngle));
                int yStuff = (int)(arr_start[0].getNode().getY()+30 + arr_start[0].getNode().getW()/2*Math.sin(endAngle));

                drawArrowReal(g1,xStuff,yStuff,Math.atan2(arr_start[0].getNode().getY()-(arr_start[0].getEndPointsStartY()-30),arr_start[0].getNode().getX()-(arr_start[0].getEndPointsStartX()-30)));


            }
        }else if(temp_Link!=null){  //Select Link
            g.setColor(Color.white);
            g.fillRect(0,0,w,h);
            int position=arr_link.indexOf(temp_Link);


            if(SelfLinks.size()!=0){
                for(int i=0;i<SelfLinks.size();i++){
                    SelfLink1 s=(SelfLink1)SelfLinks.get(i);
                    //g1.drawArc(t.getX(),t.getY(),t.getW(),t.getH(),t.getStartAngle(),t.getArcAngle());
                    double dx = Math.cos(s.radianCB);
                    double dy = Math.sin(s.radianCB);

                    // line_g.drawArc(s.xCB-s.rCB,s.yCB-s.rCB,s.rCB*2,s.rCB*2,0,360);
                    g1.drawArc(s.xCB + (int)(s.rCB*dx),s.yCB + (int)((s.rCB)*dy), s.getW(), s.getH(), s.getStartAngle(), s.getArcAngle());
                    double endAngle = Math.atan2(s.getYl()-s.getY(),s.getXl() - s.getX())+Math.PI/4;
                    int xStuff = (int)(s.getX()+30 + s.getW()/2*Math.cos(endAngle));
                    int yStuff = (int)(s.getY()+30 + s.getW()/2*Math.sin(endAngle));

                    drawArrowReal(g1,xStuff,yStuff,Math.atan2(s.getY()-s.getYl(),s.getX()-s.getXl())+Math.PI/4);
                    g1.drawString(s.getText(),s.get_textX(),s.get_textY());
                }
            }

            for(int i=0;i<arr_link.size();i++){
                Link t=(Link)arr_link.get(i);
                if(i==position){
                    g2.draw(new QuadCurve2D.Float((float) t.getEndPointsAndCircleStartX(),(float) t.getEndPointsAndCircleStartY()
                            ,t.getCtrlX(), t.getCtrlY(), (float) t.getEndPointsAndCircleEndX(),(float) t.getEndPointsAndCircleEndY()));
                    int midX=(int)(t.getEndPointsAndCircleEndX()+t.getEndPointsAndCircleStartX())/2;
                    int tail=(int)((t.getEndPointsAndCircleEndY()+t.getEndPointsAndCircleStartY())/2)+8;
                    g2.drawString(t.getText(), midX, tail);
                    double endAngle = Math.atan2(t.getNodeA().getY()-t.getNodeB().getY(),t.getNodeA().getX() - t.getNodeB().getX());
                    int xStuff = (int)(t.getNodeB().getX()+30 + t.getNodeB().getW()/2*Math.cos(endAngle));
                    int yStuff = (int)(t.getNodeB().getY()+30 + t.getNodeB().getW()/2*Math.sin(endAngle));

                    drawArrowReal(g2,xStuff,yStuff,Math.atan2(t.getNodeB().getY()-t.getNodeA().getY(),t.getNodeB().getX()-t.getNodeA().getX()));
                }else{
                    g1.draw(new QuadCurve2D.Float((float) t.getEndPointsAndCircleStartX(),(float) t.getEndPointsAndCircleStartY()
                            ,t.getCtrlX(), t.getCtrlY(), (float) t.getEndPointsAndCircleEndX(),(float) t.getEndPointsAndCircleEndY()));
                    int midX=(int)(t.getEndPointsAndCircleEndX()+t.getEndPointsAndCircleStartX())/2;
                    int tail=(int)((t.getEndPointsAndCircleEndY()+t.getEndPointsAndCircleStartY())/2)+8;
                    g1.drawString(t.getText(), midX, tail);

                    double endAngle = Math.atan2(t.getNodeA().getY()-t.getNodeB().getY(),t.getNodeA().getX() - t.getNodeB().getX());
                    int xStuff = (int)(t.getNodeB().getX()+30 + t.getNodeB().getW()/2*Math.cos(endAngle));
                    int yStuff = (int)(t.getNodeB().getY()+30 + t.getNodeB().getW()/2*Math.sin(endAngle));

                    drawArrowReal(g1,xStuff,yStuff,Math.atan2(t.getNodeB().getY()-t.getNodeA().getY(),t.getNodeB().getX()-t.getNodeA().getX()));

                }
            }

            if(arr_node.size()!=0){
                for(int i=0;i<arr_node.size();i++){
                    Node t=(Node)arr_node.get(i);
                    if(!t.getIsAcceptState()) {
                        //Shape circle = new Ellipse2D.Double(t.getX(), t.getY(), t.getW(), t.getH());
                        //g1.draw(circle);
                        g3.fillArc(t.getX(), t.getY(), t.getW(), t.getH(),0,360);
                        g1.drawArc(t.getX(), t.getY(), t.getW()+1, t.getH()+1,0,360);
                            g1.drawString(t.getName(), t.getX_Name(), t.getY_Name());

                    }else{
                        //Shape circle = new Ellipse2D.Double(t.getX(), t.getY(), t.getW(), t.getH());
                        //Shape circle1 = new Ellipse2D.Double(t.getx(), t.gety(), t.getw(), t.geth());
                        //g1.draw(circle);
                        //g1.draw(circle1);
                        g3.fillArc(t.getX(), t.getY(), t.getW(), t.getH(),0,360);
                        g1.drawArc(t.getX(), t.getY(), t.getW()+1, t.getH()+1,0,360);
                        g1.drawArc(t.getx(), t.gety(), t.getw(), t.geth(),0,360);

                            g1.drawString(t.getName(), t.getX_Name(), t.getY_Name());
                    }
                }
            }
            if(arr_start[0]!=null){
                Shape line = new Line2D.Double(arr_start[0].getEndPointsStartX(), arr_start[0].getEndPointsStartY()
                        , arr_start[0].getEndPointsEndX(), arr_start[0].getEndPointsEndY());
                g1.draw(line);

                double endAngle = Math.atan2((arr_start[0].getEndPointsStartY()-30)-arr_start[0].getNode().getY(),(arr_start[0].getEndPointsStartX()-30) - arr_start[0].getNode().getX());
                int xStuff = (int)(arr_start[0].getNode().getX()+30 + arr_start[0].getNode().getW()/2*Math.cos(endAngle));
                int yStuff = (int)(arr_start[0].getNode().getY()+30 + arr_start[0].getNode().getW()/2*Math.sin(endAngle));

                drawArrowReal(g1,xStuff,yStuff,Math.atan2(arr_start[0].getNode().getY()-(arr_start[0].getEndPointsStartY()-30),arr_start[0].getNode().getX()-(arr_start[0].getEndPointsStartX()-30)));
            }

        }else if(temp_start!=null){ //select start link
            g.setColor(Color.white);
            g.fillRect(0,0,w,h);

            Shape line = new Line2D.Double(arr_start[0].getEndPointsStartX(), arr_start[0].getEndPointsStartY()
                    , arr_start[0].getEndPointsEndX(), arr_start[0].getEndPointsEndY());
            g2.draw(line);

            double endAngle1 = Math.atan2((arr_start[0].getEndPointsStartY()-30)-arr_start[0].getNode().getY(),(arr_start[0].getEndPointsStartX()-30) - arr_start[0].getNode().getX());
            int xStuff1 = (int)(arr_start[0].getNode().getX()+30 + arr_start[0].getNode().getW()/2*Math.cos(endAngle1));
            int yStuff1 = (int)(arr_start[0].getNode().getY()+30 + arr_start[0].getNode().getW()/2*Math.sin(endAngle1));

            drawArrowReal(g2,xStuff1,yStuff1,Math.atan2(arr_start[0].getNode().getY()-(arr_start[0].getEndPointsStartY()-30),arr_start[0].getNode().getX()-(arr_start[0].getEndPointsStartX()-30)));

            if(SelfLinks.size()!=0){
                for(int i=0;i<SelfLinks.size();i++){
                    SelfLink1 s=(SelfLink1)SelfLinks.get(i);
                    //g1.drawArc(t.getX(),t.getY(),t.getW(),t.getH(),t.getStartAngle(),t.getArcAngle());
                    double dx = Math.cos(s.radianCB);
                    double dy = Math.sin(s.radianCB);

                    // line_g.drawArc(s.xCB-s.rCB,s.yCB-s.rCB,s.rCB*2,s.rCB*2,0,360);
                    g1.drawArc(s.xCB + (int)(s.rCB*dx),s.yCB + (int)((s.rCB)*dy), s.getW(), s.getH(), s.getStartAngle(), s.getArcAngle());
                    double endAngle = Math.atan2(s.getYl()-s.getY(),s.getXl() - s.getX())+Math.PI/4;
                    int xStuff = (int)(s.getX()+30 + s.getW()/2*Math.cos(endAngle));
                    int yStuff = (int)(s.getY()+30 + s.getW()/2*Math.sin(endAngle));

                    drawArrowReal(g1,xStuff,yStuff,Math.atan2(s.getY()-s.getYl(),s.getX()-s.getXl())+Math.PI/4);
                    g1.drawString(s.getText(),s.getXl()+s.getW(),s.getYl()+s.getH());
                }
            }

            if(arr_node.size()!=0){
                for (int k = 0; k < arr_node.size(); k++) {
                    Node temp = arr_node.get(k);
                    if (!temp.getIsAcceptState()) { //ไม่ใช่ Accept State
                        //Shape circle = new Ellipse2D.Double(temp.getX(), temp.getY(), temp.getW(), temp.getH());
                        //g1.draw(circle);
                        g3.fillArc(temp.getX(), temp.getY(), temp.getW(), temp.getH(),0,360);
                        g1.drawArc(temp.getX(), temp.getY(), temp.getW()+1, temp.getH()+1,0,360);
                        if (temp.getName() != "") {
                            g1.drawString(temp.getName(), temp.getX_Name(), temp.getY_Name());
                        }
                    } else { // เป็น Accept State
                        //Shape circle = new Ellipse2D.Double(temp.getX(), temp.getY(), temp.getW(), temp.getH());
                        //Shape circle1 = new Ellipse2D.Double(temp.getx(), temp.gety(), temp.getw(), temp.geth());
                        //g1.draw(circle);
                        //g1.draw(circle1);
                        g3.fillArc(temp.getX(), temp.getY(), temp.getW(), temp.getH(),0,360);
                        g1.drawArc(temp.getX(), temp.getY(), temp.getW()+1, temp.getH()+1,0,360);
                        g1.drawArc(temp.getx(), temp.gety(), temp.getw(), temp.geth(),0,360);
                        if (temp.getName() != "") {
                            g1.drawString(temp.getName(), temp.getX_Name(), temp.getY_Name());
                        }
                    }
                }
            }

            if(arr_link.size()!=0){
                for(int i=0;i<arr_link.size();i++){
                    // (float)(t.getEndPointsAndCircleStartX()+t.getEndPointsAndCircleEndX())/2
                    // (float)(t.getEndPointsAndCircleStartY()+t.getEndPointsAndCircleEndY())/2
                    Link t=(Link)arr_link.get(i);
                    g1.draw(new QuadCurve2D.Float((float) t.getEndPointsAndCircleStartX(),(float) t.getEndPointsAndCircleStartY()
                            ,t.getCtrlX(),
                            t.getCtrlY(),
                            (float) t.getEndPointsAndCircleEndX(),(float) t.getEndPointsAndCircleEndY()));
                    int midX=(int)(t.getEndPointsAndCircleEndX()+t.getEndPointsAndCircleStartX())/2;
                    int tail=(int)((t.getEndPointsAndCircleEndY()+t.getEndPointsAndCircleStartY())/2)+8;
                    g1.drawString(t.getText(), midX, tail);

                    double endAngle = Math.atan2(t.getNodeA().getY()-t.getNodeB().getY(),t.getNodeA().getX() - t.getNodeB().getX());
                    int xStuff = (int)(t.getNodeB().getX()+30 + t.getNodeB().getW()/2*Math.cos(endAngle));
                    int yStuff = (int)(t.getNodeB().getY()+30 + t.getNodeB().getW()/2*Math.sin(endAngle));
                    drawArrowReal(g1,xStuff,yStuff,Math.atan2(t.getNodeB().getY()-t.getNodeA().getY(),t.getNodeB().getX()-t.getNodeA().getX()));
                }
            }

        }else if(temp_self!=null){ //seslf
            g.setColor(Color.white);
            g.fillRect(0,0,w,h);

            int position=SelfLinks.indexOf(temp_self);
            for(int i=0;i<SelfLinks.size();i++){
                SelfLink1 s=(SelfLink1)SelfLinks.get(i);
                if(i==position){
                    //g2.drawArc(t.getX(),t.getY(),t.getW(),t.getH(),t.getStartAngle(),t.getArcAngle());
                    double dx = Math.cos(s.radianCB);
                    double dy = Math.sin(s.radianCB);

                    // line_g.drawArc(s.xCB-s.rCB,s.yCB-s.rCB,s.rCB*2,s.rCB*2,0,360);
                    g2.drawArc(s.xCB + (int)(s.rCB*dx),s.yCB + (int)((s.rCB)*dy), s.getW(), s.getH(), s.getStartAngle(), s.getArcAngle());
                    double endAngle = Math.atan2(s.getYl()-s.getY(),s.getXl() - s.getX())+Math.PI/4;
                    int xStuff = (int)(s.getX()+30 + s.getW()/2*Math.cos(endAngle));
                    int yStuff = (int)(s.getY()+30 + s.getW()/2*Math.sin(endAngle));

                    drawArrowReal(g2,xStuff,yStuff,Math.atan2(s.getY()-s.getYl(),s.getX()-s.getXl())+Math.PI/4);
                    s.setPosition(s.getXl()+s.getW(),s.getYl()+s.getH());
                    g2.drawString(s.getText(),s.getXl()+s.getW(),s.getYl()+s.getH());
                }else{
                    //g1.drawArc(t.getX(),t.getY(),t.getW(),t.getH(),t.getStartAngle(),t.getArcAngle());
                    double dx = Math.cos(s.radianCB);
                    double dy = Math.sin(s.radianCB);

                    // line_g.drawArc(s.xCB-s.rCB,s.yCB-s.rCB,s.rCB*2,s.rCB*2,0,360);
                    g1.drawArc(s.xCB + (int)(s.rCB*dx),s.yCB + (int)((s.rCB)*dy), s.getW(), s.getH(), s.getStartAngle(), s.getArcAngle());
                    g1.drawString(s.getText(),s.get_textX(),s.get_textY());
                }
            }
            if(arr_node.size()!=0){
                for (int k = 0; k < arr_node.size(); k++) {
                    Node temp = arr_node.get(k);
                    if (!temp.getIsAcceptState()) { //ไม่ใช่ Accept State
                        //Shape circle = new Ellipse2D.Double(temp.getX(), temp.getY(), temp.getW(), temp.getH());
                        //g1.draw(circle);
                        g3.fillArc(temp.getX(), temp.getY(), temp.getW(), temp.getH(),0,360);
                        g1.drawArc(temp.getX(), temp.getY(), temp.getW()+1, temp.getH()+1,0,360);
                        if (temp.getName() != "") {
                            g1.drawString(temp.getName(), temp.getX_Name(), temp.getY_Name());
                        }
                    } else { // เป็น Accept State
                        //Shape circle = new Ellipse2D.Double(temp.getX(), temp.getY(), temp.getW(), temp.getH());
                        // Shape circle1 = new Ellipse2D.Double(temp.getx(), temp.gety(), temp.getw(), temp.geth());
                        // g1.draw(circle);
                        // g1.draw(circle1);
                        g3.fillArc(temp.getX(), temp.getY(), temp.getW(), temp.getH(),0,360);
                        g1.drawArc(temp.getX(), temp.getY(), temp.getW()+1, temp.getH()+1,0,360);
                        g1.drawArc(temp.getx(), temp.gety(), temp.getw(), temp.geth(),0,360);
                        if (temp.getName() != "") {
                            g1.drawString(temp.getName(), temp.getX_Name(), temp.getY_Name());
                        }
                    }
                }
            }

            if(arr_link.size()!=0){
                for(int i=0;i<arr_link.size();i++){
                    // (float)(t.getEndPointsAndCircleStartX()+t.getEndPointsAndCircleEndX())/2
                    // (float)(t.getEndPointsAndCircleStartY()+t.getEndPointsAndCircleEndY())/2
                    Link t=(Link)arr_link.get(i);
                    g1.draw(new QuadCurve2D.Float((float) t.getEndPointsAndCircleStartX(),(float) t.getEndPointsAndCircleStartY()
                            ,t.getCtrlX(),
                            t.getCtrlY(),
                            (float) t.getEndPointsAndCircleEndX(),(float) t.getEndPointsAndCircleEndY()));
                    int midX=(int)(t.getEndPointsAndCircleEndX()+t.getEndPointsAndCircleStartX())/2;
                    int tail=(int)((t.getEndPointsAndCircleEndY()+t.getEndPointsAndCircleStartY())/2)+8;
                    g1.drawString(t.getText(), midX, tail);

                    double endAngle = Math.atan2(t.getNodeA().getY()-t.getNodeB().getY(),t.getNodeA().getX() - t.getNodeB().getX());
                    int xStuff = (int)(t.getNodeB().getX()+30 + t.getNodeB().getW()/2*Math.cos(endAngle));
                    int yStuff = (int)(t.getNodeB().getY()+30 + t.getNodeB().getW()/2*Math.sin(endAngle));
                    drawArrowReal(g1,xStuff,yStuff,Math.atan2(t.getNodeB().getY()-t.getNodeA().getY(),t.getNodeB().getX()-t.getNodeA().getX()));
                }
            }

            if(arr_start[0]!=null){
                StartLink t=(StartLink)arr_start[0];
                Shape line = new Line2D.Double(arr_start[0].getEndPointsStartX(), arr_start[0].getEndPointsStartY()
                        , arr_start[0].getEndPointsEndX(), arr_start[0].getEndPointsEndY());
                g1.draw(line);

                double endAngle = Math.atan2((arr_start[0].getEndPointsStartY()-30)-arr_start[0].getNode().getY(),(arr_start[0].getEndPointsStartX()-30) - arr_start[0].getNode().getX());
                int xStuff = (int)(arr_start[0].getNode().getX()+30 + arr_start[0].getNode().getW()/2*Math.cos(endAngle));
                int yStuff = (int)(arr_start[0].getNode().getY()+30 + arr_start[0].getNode().getW()/2*Math.sin(endAngle));

                drawArrowReal(g1,xStuff,yStuff,Math.atan2(arr_start[0].getNode().getY()-(arr_start[0].getEndPointsStartY()-30),arr_start[0].getNode().getX()-(arr_start[0].getEndPointsStartX()-30)));
            }


        }else{ // Select Space
            g3.fillRect(0,0,w,h);

            if(SelfLinks.size()!=0){
                for(int i=0;i<SelfLinks.size();i++){
                    SelfLink1 s=(SelfLink1)SelfLinks.get(i);
                    //g1.drawArc(t.getX(),t.getY(),t.getW(),t.getH(),t.getStartAngle(),t.getArcAngle());
                    double dx = Math.cos(s.radianCB);
                    double dy = Math.sin(s.radianCB);

                    // line_g.drawArc(s.xCB-s.rCB,s.yCB-s.rCB,s.rCB*2,s.rCB*2,0,360);
                    g1.drawArc(s.xCB + (int)(s.rCB*dx),s.yCB + (int)((s.rCB)*dy), s.getW(), s.getH(), s.getStartAngle(), s.getArcAngle());
                    double endAngle = Math.atan2(s.getYl()-s.getY(),s.getXl() - s.getX())+Math.PI/4;
                    int xStuff = (int)(s.getX()+30 + s.getW()/2*Math.cos(endAngle));
                    int yStuff = (int)(s.getY()+30 + s.getW()/2*Math.sin(endAngle));

                    drawArrowReal(g1,xStuff,yStuff,Math.atan2(s.getY()-s.getYl(),s.getX()-s.getXl())+Math.PI/4);
                    g1.drawString(s.getText(),s.get_textX(),s.get_textY());

                }
            }
            if(arr_node.size()!=0){
                for (int k = 0; k < arr_node.size(); k++) {
                    Node temp = arr_node.get(k);
                    if (!temp.getIsAcceptState()) { //ไม่ใช่ Accept State
                        //Shape circle = new Ellipse2D.Double(temp.getX(), temp.getY(), temp.getW(), temp.getH());
                        //g1.draw(circle);
                        g3.fillArc(temp.getX(), temp.getY(), temp.getW(), temp.getH(),0,360);
                        g1.drawArc(temp.getX(), temp.getY(), temp.getW()+1, temp.getH()+1,0,360);
                        if (temp.getName() != "") {
                            g1.drawString(temp.getName(), temp.getX_Name(), temp.getY_Name());
                        }
                    } else { // เป็น Accept State
                       //Shape circle = new Ellipse2D.Double(temp.getX(), temp.getY(), temp.getW(), temp.getH());
                      // Shape circle1 = new Ellipse2D.Double(temp.getx(), temp.gety(), temp.getw(), temp.geth());
                       // g1.draw(circle);
                       // g1.draw(circle1);
                        g3.fillArc(temp.getX(), temp.getY(), temp.getW(), temp.getH(),0,360);
                        g1.drawArc(temp.getX(), temp.getY(), temp.getW()+1, temp.getH()+1,0,360);
                        g1.drawArc(temp.getx(), temp.gety(), temp.getw(), temp.geth(),0,360);
                        if (temp.getName() != "") {
                            g1.drawString(temp.getName(), temp.getX_Name(), temp.getY_Name());
                        }
                    }
                }
            }
            if(arr_link.size()!=0){
                for(int i=0;i<arr_link.size();i++){
                    // (float)(t.getEndPointsAndCircleStartX()+t.getEndPointsAndCircleEndX())/2
                    // (float)(t.getEndPointsAndCircleStartY()+t.getEndPointsAndCircleEndY())/2
                    Link t=(Link)arr_link.get(i);
                    g1.draw(new QuadCurve2D.Float((float) t.getEndPointsAndCircleStartX(),(float) t.getEndPointsAndCircleStartY()
                            ,t.getCtrlX(),
                            t.getCtrlY(),
                            (float) t.getEndPointsAndCircleEndX(),(float) t.getEndPointsAndCircleEndY()));
                    int midX=(int)(t.getEndPointsAndCircleEndX()+t.getEndPointsAndCircleStartX())/2;
                    int tail=(int)((t.getEndPointsAndCircleEndY()+t.getEndPointsAndCircleStartY())/2)+8;
                    g1.drawString(t.getText(), midX, tail);
                    double endAngle = Math.atan2(t.getNodeA().getY()-t.getNodeB().getY(),t.getNodeA().getX() - t.getNodeB().getX());
                    int xStuff = (int)(t.getNodeB().getX()+30 + t.getNodeB().getW()/2*Math.cos(endAngle));
                    int yStuff = (int)(t.getNodeB().getY()+30 + t.getNodeB().getW()/2*Math.sin(endAngle));
                    drawArrowReal(g1,xStuff,yStuff,Math.atan2(t.getNodeB().getY()-t.getNodeA().getY(),t.getNodeB().getX()-t.getNodeA().getX()));
                }
            }
            if(arr_start[0]!=null){
                StartLink t=(StartLink)arr_start[0];
                Shape line = new Line2D.Double(arr_start[0].getEndPointsStartX(), arr_start[0].getEndPointsStartY()
                        , arr_start[0].getEndPointsEndX(), arr_start[0].getEndPointsEndY());
                g1.draw(line);
                double endAngle = Math.atan2((arr_start[0].getEndPointsStartY()-30)-arr_start[0].getNode().getY(),(arr_start[0].getEndPointsStartX()-30) - arr_start[0].getNode().getX());
                int xStuff = (int)(arr_start[0].getNode().getX()+30 + arr_start[0].getNode().getW()/2*Math.cos(endAngle));
                int yStuff = (int)(arr_start[0].getNode().getY()+30 + arr_start[0].getNode().getW()/2*Math.sin(endAngle));

                drawArrowReal(g1,xStuff,yStuff,Math.atan2(arr_start[0].getNode().getY()-(arr_start[0].getEndPointsStartY()-30),arr_start[0].getNode().getX()-(arr_start[0].getEndPointsStartX()-30)));
            }
        }

    }
    public void Move(int x,int y,Node temp){
        int position=arr_node.indexOf(temp);
        if(!temp.getIsAcceptState()) {
            arr_node.get(position).setX(x - 30);
            arr_node.get(position).setY(y - 30);
            arr_node.get(position).setX_name(x-5);
            arr_node.get(position).setY_name(y+1);
        }else{
            arr_node.get(position).setX(x-30);
            arr_node.get(position).setY(y-30);
            arr_node.get(position).setx(x-25);
            arr_node.get(position).sety(y-25);
            arr_node.get(position).setX_name(x-5);
            arr_node.get(position).setY_name(y+1);
        }
        SelfLink1 s1;
        for(SelfLink1 s:SelfLinks){
            if(s.getNode()==arr_node.get(position)){
                s.xCB = x - 30;
                s.yCB = y - 30;
                break;
            }
        }

    }

    public void moveLink(int x,int y,Link temp){
        int position=arr_link.indexOf(temp);
        arr_link.get(position).setCtrlX(x);
        arr_link.get(position).setCtrlY(y);
    }

    public void moveStart(int x,int y){
        arr_start[0].setAnchorPoint(x,y);
    }

    public void DrawAuomata(){
        Graphics2D g = (Graphics2D) c.getGraphics();
        Graphics2D g1 = (Graphics2D) c.getGraphics();
        g.setColor(Color.white);
        g1.setColor(Color.black);


        if(SelfLinks.size()!=0){
            for(int i=0;i<SelfLinks.size();i++){
                SelfLink1 s=(SelfLink1)SelfLinks.get(i);
                //g1.drawArc(t.getX(),t.getY(),t.getW(),t.getH(),t.getStartAngle(),t.getArcAngle());
                double dx = Math.cos(s.radianCB);
                double dy = Math.sin(s.radianCB);

                // line_g.drawArc(s.xCB-s.rCB,s.yCB-s.rCB,s.rCB*2,s.rCB*2,0,360);
                g1.drawArc(s.xCB + (int)(s.rCB*dx),s.yCB + (int)((s.rCB)*dy), s.getW(), s.getH(), s.getStartAngle(), s.getArcAngle());
                double endAngle = Math.atan2(s.getYl()-s.getY(),s.getXl() - s.getX())+Math.PI/4;
                int xStuff = (int)(s.getX()+30 + s.getW()/2*Math.cos(endAngle));
                int yStuff = (int)(s.getY()+30 + s.getW()/2*Math.sin(endAngle));

                drawArrowReal(g1,xStuff,yStuff,Math.atan2(s.getY()-s.getYl(),s.getX()-s.getXl())+Math.PI/4);
                g1.drawString(s.getText(),s.get_textX(),s.get_textY());

            }
        }

        if(arr_node.size()!=0){
            for(int i=0;i<arr_node.size();i++){
                Node temp_node=(Node)arr_node.get(i);
                if(!temp_node.getIsAcceptState()) {
                    g.fillArc(temp_node.getX(), temp_node.getY(), temp_node.getW(), temp_node.getH(), 0, 360);
                    g1.drawArc(temp_node.getX(), temp_node.getY(), temp_node.getW() + 1, temp_node.getH() + 1, 0, 360);
                    if (temp_node.getName() != "") {
                        g1.drawString(temp_node.getName(), temp_node.getX_Name(), temp_node.getY_Name());
                    }
                }else{
                    g.fillArc(temp_node.getX(), temp_node.getY(), temp_node.getW(), temp_node.getH(),0,360);
                    g1.drawArc(temp_node.getX(), temp_node.getY(), temp_node.getW()+1, temp_node.getH()+1,0,360);
                    g1.drawArc(temp_node.getx(), temp_node.gety(), temp_node.getw(), temp_node.geth(),0,360);
                    if (temp_node.getName() != "") {
                        g1.drawString(temp_node.getName(), temp_node.getX_Name(), temp_node.getY_Name());
                    }
                }
            }
        }
        if(arr_link.size()!=0){
            for(int i=0;i<arr_link.size();i++){
                Link lnk=(Link)arr_link.get(i);
                g1.draw(new QuadCurve2D.Float((float) lnk.getEndPointsAndCircleStartX(),(float) lnk.getEndPointsAndCircleStartY()
                        ,lnk.getCtrlX(),
                        lnk.getCtrlY(),
                        (float) lnk.getEndPointsAndCircleEndX(),(float) lnk.getEndPointsAndCircleEndY()));
                int midX=(int)(lnk.getEndPointsAndCircleEndX()+lnk.getEndPointsAndCircleStartX())/2;
                int tail=(int)((lnk.getEndPointsAndCircleEndY()+lnk.getEndPointsAndCircleStartY())/2)+8;
                g1.drawString(lnk.getText(), midX, tail);

                double endAngle = Math.atan2(lnk.getNodeA().getY()-lnk.getNodeB().getY(),lnk.getNodeA().getX() - lnk.getNodeB().getX());
                int xStuff = (int)(lnk.getNodeB().getX()+30 + lnk.getNodeB().getW()/2*Math.cos(endAngle));
                int yStuff = (int)(lnk.getNodeB().getY()+30 + lnk.getNodeB().getW()/2*Math.sin(endAngle));
                drawArrowReal(g1,xStuff,yStuff,Math.atan2(lnk.getNodeB().getY()-lnk.getNodeA().getY(),lnk.getNodeB().getX()-lnk.getNodeA().getX()));
            }
        }
        if(arr_start[0]!=null){
            Shape line = new Line2D.Double(arr_start[0].getEndPointsStartX(), arr_start[0].getEndPointsStartY()
                    , arr_start[0].getEndPointsEndX(), arr_start[0].getEndPointsEndY());
            g1.draw(line);

            double endAngle = Math.atan2((arr_start[0].getEndPointsStartY()-30)-arr_start[0].getNode().getY(),(arr_start[0].getEndPointsStartX()) - arr_start[0].getNode().getX()-30);
            int xStuff = (int)(arr_start[0].getNode().getX()+30 + arr_start[0].getNode().getW()/2*Math.cos(endAngle));
            int yStuff = (int)(arr_start[0].getNode().getY()+30 + arr_start[0].getNode().getW()/2*Math.sin(endAngle));

            drawArrowReal(g1,xStuff,yStuff,Math.atan2(arr_start[0].getNode().getY()-(arr_start[0].getEndPointsStartY()-30),arr_start[0].getNode().getX()-(arr_start[0].getEndPointsStartX()-30)));
        }

    }

    public void moveSelfLink(SelfLink1 temp,int x,int y){
        SelfLink1 s = temp;
        temp.radianCB = Math.atan2(y-temp.getY(),x-temp.getX());
        //temp.radianCB = Math.atan2(temp.getYl()-y,temp.getXl()-x);
        System.out.println("radian");
        Graphics2D g = (Graphics2D) c.getGraphics();
        g.setColor(Color.black);
        double dx = Math.cos(s.radianCB);
        double dy = Math.sin(s.radianCB);
        s.setX(s.xCB + (int)(s.rCB*dx));
        s.setY(s.yCB + (int)((s.rCB)*dy));
        drawSelect(x,y);
    }

    public void deleteSelect(int x,int y){
        Node select=null;
        Link select_link=null;
        SelfLink1 select_selfLink=null;
        StartLink select_start=null;
        select=selectNode(x,y);
        select_link=selectLink(x,y);
        select_selfLink=selectSelfLink(x,y);
        select_start=selectStartLink(x,y);

        if(select !=null) { //When we find node to delete
            int index=-1;
            if(arr_start[0]!=null) {
                if (arr_start[0].getNode() == select) {
                    arr_start[0] = null;
                }
            }

            for(int k=0;k<arr_link.size();k++){
                Link temp=arr_link.get(k);
                if(temp.getNodeA()==select || temp.getNodeB()==select){
                    index=k;
                }
            }
            if(index!=-1) {
                arr_link.remove(index);  //delete arrow if arrow connect node
                index = -1;
            }

            for(int i=0;i<SelfLinks.size();i++){
                SelfLink1 temp=(SelfLink1)SelfLinks.get(i);
                if(temp.getNode()==select){
                    index=i;
                }
            }

            if(index!=-1){
                SelfLinks.remove(index);
            }

            arr_node.remove(select);

        }else if(select_link!=null){
            arr_link.remove(select_link);
        }else if(select_selfLink!=null){
            SelfLinks.remove(select_selfLink);
        }else if(select_start!=null){
            arr_start[0]=null;
        }


        del=false;
        drawSelect(x,y);
    }

    public static void main(String args[]) {
        Draw c = new Draw();
    }
}