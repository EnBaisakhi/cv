
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class client2 {

    public static void main(String[] args) {

        // Interface Work

        // Title
        JFrame frame = new JFrame ( "CN Project-Group 10");
        frame.setLayout (new BoxLayout (frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setSize(500,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Heading
        JLabel title = new JLabel("Project No. 8(Client)");
        title.setFont (new Font( "Arial", Font.BOLD,25)); 
        title. setBorder (new EmptyBorder ( 10, 0, 10, 0));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        frame.add(title);

        // Sub-Heading
        JLabel subTitle = new JLabel("Generating a dictionary for some given keywords");
        subTitle.setFont (new Font( "Arial", Font.BOLD,15)); 
        subTitle.setForeground(Color.gray);
        subTitle.setBorder (new EmptyBorder ( 10, 0, 10, 0));
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        frame.add(subTitle);
        
        // Keyword
        JPanel panel = new JPanel();
        JTextField keywordsField = new JTextField(15);
        panel.add(new JLabel("Enter Keyword: "));
        panel.add(keywordsField);
        frame.getContentPane().add(panel);
        
        // Min Max Input
        JPanel panel2 = new JPanel();
        JTextField MaxField = new JTextField(5);
        JTextField MinField = new JTextField(5);
        panel2.add(new JLabel("Length: "));
        panel2.add(new JLabel("Min: "));
        panel2.add(MinField);
        panel2.add(new JLabel("Max: "));
        panel2.add(MaxField);
        frame.getContentPane().add(panel2);


        // Submit Button
        JPanel panel3 = new JPanel();
        JButton submit = new JButton( "Submit");
        panel3.add(submit);
        frame.getContentPane().add(panel3);
        
        // Submit Button Event
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Socket socket = new Socket ( "localhost",  1234);
                    
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream()) ;
                    String keyword = new StringBuilder(keywordsField.getText()).toString();

                    dataOutputStream.writeUTF(keyword);
                    dataOutputStream.writeInt(Integer.parseInt(MaxField.getText()));
                    dataOutputStream.writeInt(Integer.parseInt(MinField.getText()));

                    dataOutputStream.close();
                    socket.close();
                }catch(IOException error){
                    error.printStackTrace();
                }
            }
        });  

        // Frame Visibility
        frame.setVisible(true);
    }
}