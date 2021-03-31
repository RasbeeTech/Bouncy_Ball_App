import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class BounceBallAppV2 extends JApplet {

    public BounceBallAppV2 (){
        add(new BallControl());
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Create a frame
        JFrame frame = new JFrame("Bouncy Ball App");
        // Create an instance of the applet
        BounceBallAppV2 applet = new BounceBallAppV2();
        // Add the applet to the frame
        frame.add(applet);
        // Display the frame
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null); // Center the frame   
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
class BallControl extends JPanel{
    
    private JScrollBar jsbDelay;
    private JButton jbtResume,jbtSuspend,jbtAdd,jbtSubtract;
    private BallPanel ballPanel = new BallPanel();
    
    public BallControl(){
        jsbDelay = new JScrollBar(JScrollBar.HORIZONTAL);
        ballPanel.setDelay(jsbDelay.getMaximum());
        ballPanel.setBorder(new javax.swing.border.LineBorder(Color.BLUE));
        
        // Group buttons in a panel
        JPanel buttonpanel = new JPanel();
        buttonpanel.add(jbtSuspend = new JButton("Suspend"));
        buttonpanel.add(jbtResume = new JButton("Resume"));
        buttonpanel.add(jbtAdd = new JButton("Add"));
        buttonpanel.add(jbtSubtract = new JButton("Subtract"));
        
        //Add ball, scroll bar and buttons to the panel 
        setLayout(new BorderLayout());
        add(jsbDelay, BorderLayout.NORTH);
        add(ballPanel, BorderLayout.CENTER);
        add(buttonpanel, BorderLayout.SOUTH);
        
        // Register listeners
        
       
        class AdjustListener implements AdjustmentListener
        {
            public void adjustmentValueChanged(AdjustmentEvent ae)
            {
               ballPanel.setDelay(jsbDelay.getMaximum() - ae.getValue());
            }
        }
        AdjustmentListener adjustmentListener = new AdjustListener();
        jsbDelay.addAdjustmentListener(adjustmentListener);
        jbtSuspend.addActionListener(new ActionListener()
            {
               public void actionPerformed(ActionEvent e)
               {
                  ballPanel.suspend();
               }
            }
        
        );
        jbtResume.addActionListener(new ActionListener()
            {
               public void actionPerformed(ActionEvent e)
               {
                  ballPanel.resume();
               }
            }
        
        );
        jbtAdd.addActionListener(new ActionListener()
            {
               public void actionPerformed(ActionEvent e)
               {
                  ballPanel.add();
               }
            }
        
        );
        jbtSubtract.addActionListener(new ActionListener()
            {
               public void actionPerformed(ActionEvent e)
               {
                  ballPanel.subtract();
               }
            }
        
        );

    }

}

class BallPanel extends JPanel {
    
    private List<Object> balls = new ArrayList();
    private int delay = 10;
    private Timer timer = new Timer(delay, new TimerListener());
    
    public BallPanel(){
        this.add();
        this.resume();
    }
    
    private class TimerListener implements ActionListener {
        @Override /** Handle the action event */
        public void actionPerformed(ActionEvent e) {
            repaint();
        }
    }
    
    public void setDelay(int delay){
        this.delay = delay;
        timer.setDelay(delay);
    }
    
    public void suspend() {timer.stop();}

    public void resume(){timer.start();}

    public void add(){balls.add(new SingleBall());}

    public void subtract(){
        if (balls.size() > 0)
            balls.remove(balls.size() - 1);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        for (int i = 0; i < balls.size(); i++) {
            //0=x, 1=y, 2=radius  //update ball location and send back an array to draw the ball
            int[] drawB = ((SingleBall)balls.get(i)).drawball(getWidth(),getHeight());

            //draw & set the color of the ball
            g.setColor(((SingleBall)balls.get(i)).color);
            g.fillOval(drawB[0] - drawB[2],drawB[1] - drawB[2], drawB[2] * 2 , drawB[2] * 2);
        }
    }
}


class SingleBall {
    
    public int x=0, y=0, dx=0, dy=0, radius=0;
    public Color color;

    public SingleBall() {
        //Setting random numbers
        x=(int)(Math.random()*100 +50);
        y=(int)(Math.random()*100 +50);
        while (dx ==0 || dy ==0){
            dx = (int)(Math.random()*11 -5);
            dy = (int)(Math.random()*11 -5);
        }
        radius= (int)(Math.random()*10 + 5);
        color = new Color( (int)(Math.random()*255 +1),
                (int)(Math.random()*255 +1),
                (int)(Math.random()*255 +1));
    }
    
    public int[] drawball(int width,int height){
        //check boundaries & return to the box if out of bound
        if (x < radius ) {dx = Math.abs(dx);}
        else if (x > width - radius) {dx = -Math.abs(dx);}
        if (y < radius ) {dy = Math.abs(dy);}
        else if (y > height - radius) {dy = -Math.abs(dy);}
        //if (x < radius || x > width - radius) {dx *= -1;}
        //if (y < radius || y > height - radius) {dy *= -1;}
        
        //adjust ball positions
        x += dx;
        y += dy;
        
        return new int[] {x, y, radius};
    }
}
