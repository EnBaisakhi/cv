import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class server2 {

    // Clearing file.txt and set.txt file
    static{
        try{
            FileOutputStream f = new FileOutputStream("file.txt");
            byte[] b = new byte[0];
            f.write(b);
            f.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        try{
            FileOutputStream f = new FileOutputStream("set.txt");
            byte[] b = new byte[0];
            f.write(b);
            f.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    // ans stores all the word generated
    private String ans="";

    // textSet store all the word in set lexicographically
    private Set<String> textSet = new TreeSet<>() ;

    // function to save the set to file set.txt
    private static void saveSetToFile(Set<String> lexicographicalSet, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(lexicographicalSet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // function to load the set from file set.txt
    private static Set<String> loadSetFromFile(String fileName) {
        Set<String> loadedSet = new TreeSet<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            loadedSet = (Set<String>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return loadedSet;
    }

    // function to return set which store all combination of word made up of given keyword
    private static Set<String> generateCombinations(Set<Character> charSet, int minLength, int maxLength) {
        Set<String> combinations = new TreeSet<>();

        generateCombinationsHelper(charSet, minLength, maxLength, "", combinations);

        return combinations;
    }

    // function to generate combination
    private static void generateCombinationsHelper(Set<Character> charSet, int minLength, int maxLength, String current, Set<String> combinations) {
        if (current.length() >= minLength && current.length() <= maxLength) {
            combinations.add(current);
        }

        if (current.length() < maxLength) {
            for (char c : charSet) {
                generateCombinationsHelper(charSet, minLength, maxLength, current + c, combinations);
            }
        }
    }

    // handle client request
    public void handleClient(Socket socket) {
        
        try {

            // get the value from client
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            String receivedKeyword = dataInputStream.readUTF(); 
            int receivedMax = dataInputStream.readInt();
            int receivedMin = dataInputStream.readInt();

            // key Set store all unique key(character) in lexicograpically
            Set<Character> key = new TreeSet<Character>();
            for(int i=0;i<receivedKeyword.length();i++){
                key.add(receivedKeyword.charAt(i));
            }

            // load set from set.txt in textSet
            textSet = loadSetFromFile("set.txt");

            // store all the word created created in textSet lexicographically
            textSet = generateCombinations(key, receivedMin, receivedMax);

            // save the result in text.txt
            ans="";
            for(String s:textSet){
                ans = ans+s+"\n";
            }
            try (PrintWriter writer = new PrintWriter(new FileWriter("file.txt"))) {
                writer.println(ans);
            } catch (IOException e) {
                e.printStackTrace();
            }
             
            // save the set in set.txt
            saveSetToFile(textSet, "set.txt");

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // interface Creation
    public void showInterface(){

        // Title
        JFrame frame = new JFrame ( "CN Project-Group 10");
        frame.setLayout (new BoxLayout (frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Heading
        JLabel title = new JLabel("Project No. 8(Server)");
        title.setFont (new Font( "Arial", Font.BOLD,25)); 
        title. setBorder (new EmptyBorder ( 10, 0, 10, 0));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        frame.add(title);

        // Sub-Heading
        JLabel subTitle = new JLabel("Click Show to see all the word Generated");
        subTitle.setFont (new Font( "Arial", Font.BOLD,15)); 
        subTitle.setForeground(Color.gray);
        subTitle.setBorder (new EmptyBorder ( 10, 0, 10, 0));
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        frame.add(subTitle);

        // Box where the content get displayed
        JPanel panel0 = new JPanel();
        JTextArea result = new JTextArea();
        result.setLineWrap(true);
        result.setWrapStyleWord(true); 
        result.setEditable(false);
        result.setRows(20);
        result.setSize(450,900);
        panel0.add(result);
        JScrollPane scrollPane = new JScrollPane(panel0);
        scrollPane.setBorder(null);
        frame.getContentPane().add(scrollPane);

        // Show Button
        JPanel panel = new JPanel();
        JButton submit = new JButton("Show");
        panel.add(submit);
        frame.getContentPane().add(panel);

        
        frame.setVisible(true);

        // Show Button event
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                result.setText(ans);
            }
        });
    }

    public static void main(String[] args) {

        // Create an instance of the server
        server2 server = new server2(); 

        // Show Interface
        server.showInterface();

        // Server
        try {
            // Server listen to the port 1234
            ServerSocket serverSocket = new ServerSocket(1234);

            while (true) {
                // Accept Client Request
                Socket socket = serverSocket.accept();
                // Handle Client Request
                server.handleClient(socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
