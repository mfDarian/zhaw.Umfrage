package FieldGUI;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.EventQueue;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.GridBagLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextPane;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JTextField;

public class Fframe extends JFrame {

	private JPanel contentPane;
	private JTextField txtAnzahlDerAntworten;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Fframe frame = new Fframe();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Fframe() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 301);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenui = new JMenu("Interviews");
		menuBar.add(mnNewMenui);
		
		JMenuItem mntmNewMenuItemi = new JMenuItem("Lade Interview");
		mnNewMenui.add(mntmNewMenuItemi);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Neues Interview");
		mnNewMenui.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItemi_1 = new JMenuItem("Speichere Interview");
		mnNewMenui.add(mntmNewMenuItemi_1);
		
		JMenu mnNewMenuii = new JMenu("Kampagnen");
		menuBar.add(mnNewMenuii);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("neue Kampagne eröffnen");
		mnNewMenuii.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItemii = new JMenuItem("Lade Kampagne");
		mnNewMenuii.add(mntmNewMenuItemii);
		
		JMenuItem mntmNewMenuItemII2 = new JMenuItem("speichere Kampagne");
		mnNewMenuii.add(mntmNewMenuItemII2);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 420, 77);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JTextPane txtpnfragedas = new JTextPane();
		txtpnfragedas.setText("\"\\\"Frage 1 /Das ist die erste Frage dieses Surveys. Ich möchte auch herausfinden, ob mit dieser Component Zeilenumbrüche möglich sind. Das scheint der Fall zu sein, und ich bin zufrieden;-)\\\"\")");
		txtpnfragedas.setBounds(0, 0, 434, 77);
		panel.add(txtpnfragedas);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 77, 420, 84);
		contentPane.add(panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JRadioButton rdbtnNewRadioButton_2 = new JRadioButton("Antwort 1");
		panel_1.add(rdbtnNewRadioButton_2);
		
		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Antwort 2");
		panel_1.add(rdbtnNewRadioButton_1);
		
		JRadioButton rdbtnNewRadioButton_3 = new JRadioButton("Antwort 3");
		panel_1.add(rdbtnNewRadioButton_3);
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("Antwort 4");
		panel_1.add(rdbtnNewRadioButton);										
		//15.01.Frage Swen:braucht es eine "Intelligenz, welche definiert wieviele Antwort-Buttons je Frage im GUI angezeigt werden sollen?. Soll diese Intelligenz im GUI abgelegt werden oder anderswo (wo?)
		
		//15.01. Bemerkung Swen: Möglicher Mecano für Action Listener: Pro Bearbeitungsschritt der jeweiligen Frage:
		//Pro Klick des Buttons Action Listeners werden die ausgewählten Frage-Buttons jeweils individuell als boolean "true" gestellt (siehe Kommentar bei Interview, Variante B)Ko  oder deren Antwort ID ermittelt (siehe Kommentar bei Interview, Variante A). in die Interview-Sammlung übergeben / per Klicken des submit-Buttons ( eine eigene Methode sollSubmitten();)
		//geschieht eine Abschrift der Boolean-Bestände einer Frage in eine Antworte-Sammlung der Interview-Klasse.
		// Diese Methode sollSubmitten() überprüft auch die Maximal zulässige Anzahl antworten, und "motzt" wenn diese überschritten wird. / Schaue ob in den Frage-Objekten irgendwo eine Instanzvariable für die Maximal zulässige Anzahl von Antworten existiert..
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(0, 160, 432, 69);
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		
		JButton btnEingabe = new JButton("Eingabe + Weiter");
		btnEingabe.setBounds(0, 44, 155, 25);
		panel_2.add(btnEingabe);
		
		txtAnzahlDerAntworten = new JTextField();
		txtAnzahlDerAntworten.setText("Anzahl der Antworten welche Maximal eingegeben werden können:");
		txtAnzahlDerAntworten.setBounds(0, 0, 402, 22);
		panel_2.add(txtAnzahlDerAntworten);
		txtAnzahlDerAntworten.setColumns(10);
		
		textField = new JTextField();
		textField.setBounds(403, 0, 29, 22);
		panel_2.add(textField);
		textField.setColumns(10);
		
	
		
		
		
	}
}
