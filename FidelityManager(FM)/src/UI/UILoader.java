package UI;
import javax.swing.ImageIcon;
import javax.swing.JWindow;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JLabel;

import java.awt.Rectangle;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JDesktopPane;
import javax.swing.JProgressBar;

import org.apache.log4j.Logger;

import Control.ErrLog;
import Control.Pdv;
import Control.Tecnico;
import Settings.Config;
import Settings.DbConnect;
import Settings.XmlMan;


public class UILoader {

	public UILoader() throws Exception{
		getJWindow();
	}
	private JWindow jWindow = null;  
	private JDesktopPane jDesktopPane = null;
	private JLabel jLabel = null;
	private JProgressBar jProgressBar = null;
	/**
	 * This method initializes jWindow	
	 * 	
	 * @return javax.swing.JWindow	
	 * @throws InterruptedException 
	 */
	public JWindow getJWindow() throws InterruptedException {
		if (jWindow == null) {
			
			Toolkit t = Toolkit.getDefaultToolkit();
			Dimension screenSize = t.getScreenSize();
			int width = screenSize.width;
			int height = screenSize.height;

			jWindow = new JWindow();
			jWindow.setSize(new Dimension(460, 305));
			jWindow.setContentPane(getJDesktopPane());  // Generated
			jWindow.setLocation(height /2 , width / 6);
			jWindow.setVisible(true);
			Logger log= Logger.getLogger("myLog");
			log.info("*******************************Caricamento dati********************************");
			DbConnect con = new DbConnect();
			log.info("Inizio connessione al database");
			log.debug("Configurazione server conn "+Config.getServerConn()+" UserName "+Config.getdbUserName()+" Password "+Config.getdbPasswd());
			if((con.dbConnect(Config.getServerConn(), Config.getdbUserName(),Config.getdbPasswd()))==false){
				log.fatal("Non e' porribile collegarsi al DB. Controllare la connessione e rieseguire");
				System.exit(0);
			}
			for (double i = 0.00; i < 100; i++){
				Thread.sleep(30);
				jProgressBar.setValue(jProgressBar.getValue()+1);
				
			}
			
			jWindow.setVisible(false);
			
			//new Autenticazione();
			new MainUI();
		}
		return jWindow;
	}
	/**
	 * This method initializes jDesktopPane	
	 * 	
	 * @return javax.swing.JDesktopPane	
	 * @throws InterruptedException 
	 */
	private JDesktopPane getJDesktopPane() throws InterruptedException {
		if (jDesktopPane == null) {
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(-2, -5, 473, 293));
			jLabel.setText("JLabel");
			jLabel.setIcon(new ImageIcon (Toolkit.getDefaultToolkit().getImage("GPC.JPG")));
			jDesktopPane = new JDesktopPane();
			jDesktopPane.add(jLabel, null);
			jDesktopPane.add(getJProgressBar(), null);
			
		}
		return jDesktopPane;
	}
	/**
	 * This method initializes jProgressBar	
	 * 	
	 * @return javax.swing.JProgressBar	
	 * @throws InterruptedException 
	 */
	private JProgressBar getJProgressBar() throws InterruptedException {
		if (jProgressBar == null) {
			jProgressBar = new JProgressBar();
			jProgressBar.setBounds(new Rectangle(-1, 289, 465, 15));
			jProgressBar.setStringPainted(true);

		}
		return jProgressBar;
	}
}
