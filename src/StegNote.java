import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

class StegNote extends JPanel implements ActionListener{
    private static JFrame frame;
    private static JFileChooser fc;
    private static boolean debugLayout = false;

    static private File carrier;
    private JLabel filePathLabel;
    private JLabel thumbnail;
    private JTextArea payload;

    /*
     * StegNote object creation
     */
    public StegNote(){
        generateInterface();
    }

    /*
     * Places all the JComponents into the container
     */
    private void generateInterface(){
        //root panel
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //file chooser
        fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(new FileNameExtensionFilter("BMP file", "bmp", "BMP"));
        
        //head
        JPanel head = new JPanel();
        head.setLayout(new BoxLayout(head, BoxLayout.X_AXIS));
        head.setAlignmentX(CENTER_ALIGNMENT);
        this.add(head);

        //title
        head.add(loadImage("title.png"));

        //file open/save panel
        JPanel filePanel = new JPanel();
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.Y_AXIS));
        head.add(filePanel);


        //open panel
        JPanel openPanel = new JPanel();
        openPanel.setLayout(new BoxLayout(openPanel, BoxLayout.X_AXIS));
        openPanel.setAlignmentX(LEFT_ALIGNMENT);
        filePanel.add(openPanel);

        //open button
        JButton open = new JButton("Open");
        open.setActionCommand("open");
        open.addActionListener(this);
        openPanel.add(open);
        
        //file path label
        filePathLabel = new JLabel("Filename");
        openPanel.add(filePathLabel);

        //save button
        JButton save = new JButton("Save");
        save.setActionCommand("save");
        save.addActionListener(this);
        save.setAlignmentX(LEFT_ALIGNMENT);
        filePanel.add(save);

        //payload
        payload = new JTextArea("Open a BMP file to begin.");
        payload.setAlignmentX(CENTER_ALIGNMENT);
        payload.setEditable(false);
        this.add(new JScrollPane(payload));
        
        //tail
        JPanel tail = new JPanel();
        tail.setLayout(new BoxLayout(tail, BoxLayout.X_AXIS));
        this.add(tail);

        //settings
        JPanel settings = new JPanel();
        settings.setBorder(BorderFactory.createTitledBorder("Settings"));
        tail.add(settings);

        //file info
        JPanel fileInfo = new JPanel();
        fileInfo.setBorder(BorderFactory.createTitledBorder("File Info"));
        fileInfo.setLayout(new BoxLayout(fileInfo, BoxLayout.Y_AXIS));
        tail.add(fileInfo);

        //file info labels
        JLabel size = new JLabel("Size: x");
        fileInfo.add(size);
        JLabel spaceRemaining = new JLabel("Data Remaining: x");
        fileInfo.add(spaceRemaining);
        JLabel encodingDensity = new JLabel("Encoding Density: x");
        fileInfo.add(encodingDensity);

        //thumbnail
        thumbnail = loadImage("preview.png");
        tail.add(thumbnail);

        //draws borders for every panel allowing for debugging of the layout
        if(debugLayout){
            head.setBorder(BorderFactory.createLineBorder(Color.black));
            openPanel.setBorder(BorderFactory.createLineBorder(Color.black));
            this.setBorder(BorderFactory.createLineBorder(Color.black));
            tail.setBorder(BorderFactory.createLineBorder(Color.black));
            settings.setBorder(BorderFactory.createLineBorder(Color.black));
            filePanel.setBorder(BorderFactory.createLineBorder(Color.black));
        }
    }

    /*
     * Sets up the swing interface which then manages the program
     */
    public static void main(String[] args){
        setupSwing();

        //display
        frame.pack();
        frame.setVisible(true);
    }

    /*
     * Setup swing frame and containers
     */
    private static void setupSwing(){
        frame = new JFrame("Steganographers Notepad");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new StegNote());
    }

    /*
     * Returns JLabel containing ImageIcon of specified image from jar package.
     */
    private JLabel loadImage(String name){
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("images/" + name);
        ImageIcon imageIcon = null;
        try{
            imageIcon = new ImageIcon(ImageIO.read(is));
        } catch(IOException e){
            imageIcon = new ImageIcon();
        }
        return new JLabel(imageIcon);
    }
    
    /*
     * Respond to button press
     */
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().equals("open")){
            if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
                carrier = fc.getSelectedFile();
                filePathLabel.setText(carrier.getName());

                //update thumbnail
                BufferedImage image = null;
                try{
                    image = ImageIO.read(carrier);
                } catch(IOException f){
                    try{
                        image = ImageIO.read(new File("images/error.png"));
                    } catch(IOException g){}
                }
                thumbnail.setIcon(new ImageIcon(image.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));

                payload.setText(Steganography.decode(carrier));
                payload.setEditable(true);
            }
        }
        else if(e.getActionCommand().equals("save")){
            Steganography.encode(carrier, payload.getText());
        }
        
    }
}
