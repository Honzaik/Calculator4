package eu.honzaik.finalCalc;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.net.URL;

import javax.swing.SwingConstants;

public class Ui extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtTypeHere;
	private JTextField txtResult;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ui frame = new Ui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public Ui() {
		try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } 
	    catch (Exception e) {
	      e.printStackTrace();
	    }
		
		setTitle("Calculator by Honzaik");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Calculator by Honzaik");
		lblNewLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblNewLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				if (Desktop.isDesktopSupported()) {
				      try {
				    	  URL url = new URL("http://twitter.com");
				    	  Desktop.getDesktop().browse(url.toURI());
				      }catch (Exception ex){
				    	  ex.printStackTrace();
				      }
				}
			}
		});
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(Color.PINK);
		lblNewLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 29));
		lblNewLabel.setBounds(146, 11, 301, 35);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("version 4");
		lblNewLabel_1.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(443, 26, 73, 14);
		contentPane.add(lblNewLabel_1);
		
		txtTypeHere = new JTextField();
		lblNewLabel.setLabelFor(txtTypeHere);
		txtTypeHere.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				if(txtTypeHere.getText().equals("Type here...")) txtTypeHere.setText("");
			}
		});
		txtTypeHere.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					txtResult.setText(formatString(txtTypeHere.getText()));
				}else{
					if(txtTypeHere.getText().equals("Type here...")) txtTypeHere.setText("");
				}
			}
		});
		txtTypeHere.setText("Type here...");
		txtTypeHere.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		txtTypeHere.setBounds(96, 51, 402, 35);
		contentPane.add(txtTypeHere);
		txtTypeHere.setColumns(10);
		
		JButton btnNewButton = new JButton("Calculate");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtResult.setText(formatString(txtTypeHere.getText()));
			}
		});
		btnNewButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 22));
		btnNewButton.setBounds(146, 86, 300, 39);
		contentPane.add(btnNewButton);
		
		txtResult = new JTextField();
		txtResult.setEditable(false);
		txtResult.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		txtResult.setColumns(10);
		txtResult.setBounds(96, 125, 402, 35);
		contentPane.add(txtResult);
	}
	
	public static String formatString(String input){
		input = input.replaceAll(",", ".");
		String newInput;
		try{
			newInput = input;
			boolean one = false;
			boolean two = false;
			int startIndex = 0;
			int endIndex = 0;
			for(int i = 0; i < input.length(); i++){
				if(!one || !two){
					if(input.charAt(i) == '('){
						startIndex = i;
						one = true;
					}
					if(input.charAt(i) == ')'){
						endIndex = i;
						two = true;
					}
				}
			};
			if(startIndex >= 0 && endIndex > 0){
				newInput = input.replace(input.substring(startIndex, endIndex + 1), calculateString(input.substring(startIndex + 1, endIndex)));
			}
			if(newInput.indexOf("(") >= 0){
				newInput = formatString(newInput);
			}else newInput = calculateString(newInput);
		}catch (Exception e){
			e.printStackTrace();
			newInput = "Wrong format!";
		}
		return newInput;
	}
	
	public static String calculateString(String input){
		input = input.replace("++", "+");
		input = input.replace("--", "+");
		input = input.replace("+-", "-");
		String newInput = input;
		boolean containsHigherValue = input.indexOf("*") > 0 || input.indexOf("/") > 0;
		boolean znamenkoFound = false;
		for(int i = 0; i < input.length(); i++){
			if(!znamenkoFound){
				double cislo1 = 0;
				double cislo2 = 0;
				if(input.charAt(i) == '*' || input.charAt(i) == '/'){
					znamenkoFound = true;
					int indexZnamenka = i;
					int startIndex = 0;
					int endIndex = 0;
					if(indexZnamenka > 1){
						startIndex = indexZnamenka - 2;
						endIndex = indexZnamenka + 2 ;
					}else{
						startIndex = indexZnamenka - 1;
						endIndex = indexZnamenka + 1;
					}
					while(startIndex != 0 && input.charAt(startIndex) != '+' && input.charAt(startIndex) != '-' && input.charAt(startIndex) != '*' && input.charAt(startIndex) != '/'){
						startIndex--;
					}
					while(endIndex != input.length() && input.charAt(endIndex) != '+' && input.charAt(endIndex) != '-' && input.charAt(endIndex) != '*' && input.charAt(endIndex) != '/'){
						endIndex++;
					}
					if(startIndex != 0) startIndex++;
					cislo1 = Double.parseDouble(input.substring(startIndex, indexZnamenka));
					cislo2 = Double.parseDouble(input.substring(indexZnamenka + 1, endIndex));
					double result = 0;
					if(input.charAt(i) == '*') result = cislo1*cislo2;
					if(input.charAt(i) == '/') result = cislo1/cislo2;
					String toReplace = input.substring(startIndex, endIndex).replace("+", "\\+");
					toReplace = toReplace.replace("*", "\\*");
					newInput = input.replaceFirst(toReplace, result + "");
					newInput = newInput.replace("\\+", "+");
					newInput = newInput.replace("\\*", "*");		
				}else if((input.charAt(i) == '+' || input.charAt(i) == '-') && !containsHigherValue && i != 0){
					znamenkoFound = true;
					int indexZnamenka = i;
					int startIndex = 0;
					int endIndex = 0;
					if(indexZnamenka > 1){
						startIndex = indexZnamenka - 2;
						endIndex = indexZnamenka + 2 ;
					}else{
						startIndex = indexZnamenka - 1;
						endIndex = indexZnamenka + 1;
					}
					while(startIndex != 0 && input.charAt(startIndex) != '+' && input.charAt(startIndex) != '-'){
						startIndex--;
					}
					while(endIndex != input.length() && input.charAt(endIndex) != '+' && input.charAt(endIndex) != '-'){
						endIndex++;
					}
					cislo1 = Double.parseDouble(input.substring(startIndex, indexZnamenka));
					cislo2 = Double.parseDouble(input.substring(indexZnamenka + 1, endIndex));
					double result = 0;
					if(input.charAt(i) == '+') result = cislo1+cislo2;
					if(input.charAt(i) == '-') result = cislo1-cislo2;
					String toReplace = input.substring(startIndex, endIndex).replace("+", "\\+");
					toReplace = toReplace.replace("*", "\\*");
					newInput = input.replaceFirst(toReplace, result + "");
				}
			}
		}
		if(newInput.indexOf("*") > 0 || newInput.indexOf("/") > 0 || newInput.indexOf("+") > 0 || newInput.indexOf("-") > 0)newInput = calculateString(newInput);
		return newInput;
	}
}
