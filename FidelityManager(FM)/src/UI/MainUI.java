package UI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class MainUI extends JFrame {

	private JFrame FramePrincipale = null;  //  @jve:decl-index=0:visual-constraint="220,21"
	private JPanel PannelloPrincipale = null;
	private JPanel PannelloCentralel = null;
	private JPanel ElencoClienti = null;
	private JPanel ElencoFornitori = null;
	private CardLayout card;
	

	/**
	 * This method initializes FramePrincipale	
	 * 	
	 * @return javax.swing.JFrame	
	 */
	public MainUI() {
		getFramePrincipale();
	}
	
	private JFrame getFramePrincipale() {
		if (FramePrincipale == null) {
			FramePrincipale = new JFrame();
			FramePrincipale.setSize(new Dimension(700, 700));
			FramePrincipale.setTitle("FM - Versione 1.0");
			centerFrame ( FramePrincipale );
			FramePrincipale.setContentPane(getPannelloPrincipale());
			FramePrincipale.setVisible(true);
			FramePrincipale.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		return FramePrincipale;
	}
	
	public static void centerFrame(JFrame frame) {
        Dimension frameSize = frame.getSize();
        frame.setSize(frameSize);
        frame.setLocation(
            (Toolkit.getDefaultToolkit().getScreenSize().width
                - frameSize.width) / 2,
            (Toolkit.getDefaultToolkit().getScreenSize().height
                - frameSize.height) / 2);
    }


	/**
	 * This method initializes PannelloPrincipale	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPannelloPrincipale() {
		if (PannelloPrincipale == null) {
			PannelloPrincipale = new JPanel();
			PannelloPrincipale.setLayout(new BorderLayout());
			PannelloPrincipale.add ( getJMenuBar (), BorderLayout.NORTH );
			PannelloPrincipale.add(getPannelloCentralel(), BorderLayout.CENTER);
		}
		return PannelloPrincipale;
	}
	
	public JMenuBar getJMenuBar () {
		JMenuBar menu = new JMenuBar ();
		JMenu men0 = new JMenu ( "GestioneInterna" );
		JMenuItem item0 = new JMenuItem ("CatalogoAzienda");
		item0.addActionListener ( new ActionListener () {
		public void actionPerformed(ActionEvent arg0) {
			((CardLayout)(PannelloCentralel.getLayout())).show(PannelloCentralel, "Catalogo");
			//a.getJFrame().setVisible(true);
		}

	});
		men0.add(item0);
		JMenu men1 = new JMenu ( "Fornitore" );
		JMenuItem item1 = new JMenuItem ("Inserimento");
		JMenuItem item4 = new JMenuItem ("Seleziona");
		item4.addActionListener ( new ActionListener () {

			public void actionPerformed(ActionEvent arg0) {
				((CardLayout)(PannelloCentralel.getLayout())).show(PannelloCentralel, "SelezionaFornitore");
				//((SelezionaFornitore)ElencoFornitori).aggiorna_tabella();
				//((a.getJFrame().setVisible(false);

			}

		});
		item1.addActionListener ( new ActionListener () {

			public void actionPerformed(ActionEvent arg0) {
				((CardLayout)(PannelloCentralel.getLayout())).show(PannelloCentralel, "InserisciFornitore");
				//a.getJFrame().setVisible(false);

			}
			});
		
		men1.add(item1);
		
		men1.add(new JSeparator ());
		men1.add(item4);
		
		JMenu men2 = new JMenu ( "Cliente" );
		JMenuItem item6 = new JMenuItem ("Inserimento");
		JMenuItem item9 = new JMenuItem ("Seleziona");
		item9.addActionListener ( new ActionListener () {

			public void actionPerformed(ActionEvent arg0) {
				((CardLayout)(PannelloCentralel.getLayout())).show(PannelloCentralel, "SelezionaCliente");
				//((SelezionaCliente)ElencoClienti).aggiorna_tabella();
				//a.getJFrame().setVisible(false);

			}
			});
		item6.addActionListener ( new ActionListener () {

			public void actionPerformed(ActionEvent arg0) {
				((CardLayout)(PannelloCentralel.getLayout())).show(PannelloCentralel, "InserisciCliente");
				//a.getJFrame().setVisible(false);

			}
			});
		
		men2.add(item6);
		men2.add(new JSeparator ());
		men2.add(item9);
		JMenu men3 = new JMenu ( "?" );
		JMenuItem item11 = new JMenuItem ("About GPC");
		item11.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				//new Info();
			}
		});
		men3.add(item11);
		JMenu men4 = new JMenu ( "Magazzino" );
		JMenuItem item12=new JMenuItem("Visualizza Giacenza");
		item12.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JOptionPane.showMessageDialog(null,"Non ci e stato fornito dal gruppo 4");
			}
		});
		JMenuItem item13=new JMenuItem("Alloca Merce");
		item13.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JOptionPane.showMessageDialog(null,"Non ci e stato fornito dal gruppo 4");
			}
		});
		men4.add(item12);
		men4.add(item13);
		JMenu men5 = new JMenu ( "File" );
		JMenuItem item14=new JMenuItem("Esci");
		item14.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				int risp=JOptionPane.showConfirmDialog(null,"Si vuole Uscire?", "GPC v1.0 - uscita",JOptionPane.YES_NO_OPTION);
				if(risp==0)
					System.exit(0);
			}
		});
		
		men5.add(item14);
		menu.add(men5);
		menu.add(men0);
		menu.add(men1);
		menu.add(men2);
		menu.add(men4);
		menu.add(men3);
		return menu;
	}
	
	/**
	 * This method initializes PannelloCentralel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPannelloCentralel() {
		if (PannelloCentralel == null) {
			PannelloCentralel = new JPanel();
			card = new CardLayout ();
			ElencoClienti = getSelezionaCliente();
			ElencoFornitori = getSelezionaFornitore ();
			PannelloCentralel.setLayout(card);
			PannelloCentralel.add(getVuoto(), "Vuoto");
			PannelloCentralel.add ( ElencoClienti, "SelezionaCliente" );
			PannelloCentralel.add ( ElencoFornitori, "SelezionaFornitore" );
			PannelloCentralel.add ( getInserisciCliente (), "InserisciCliente" );
			PannelloCentralel.add ( getInserisciFornitore (), "InserisciFornitore" );
			PannelloCentralel.add ( getCatalogo (), "Catalogo" );
		}
		return PannelloCentralel;
	}
	
	private JPanel getSelezionaCliente () {
		return new SelezionaCliente ();
	}
	
	private JPanel getSelezionaFornitore () {
		return new SelezionaFornitore ();
	}
	
	private JDesktopPane getInserisciCliente () {
		return new anagrafica ().getJDesktopPane();
	}
	private JDesktopPane getInserisciFornitore () {
		return new anagraficaFornitore ().getJDesktopPane();
	}
	private JDesktopPane getCatalogo () {
		return new RicercaPelli().getJDesktopPane();
		
	}
	
	private JPanel getVuoto () {
		return new JPanel ();
	}




}
