package Test;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import Settings.Config;
import UI.UILoader;
/**
 * 
 */

/**
 * @author carlosborges
 *
 */
public class BootTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		DOMConfigurator.configure("myLog.xml");
		Logger log= Logger.getLogger("myLog");
		log.info("*******************************Inizio Esecuzione********************************");
		Config config = new Config();
		config.LoadConfig();
		if(Config.getBI()==1)
			new MainBITest();
		else
			try {
				new UILoader();
			} catch (Exception e) {
				log.error("Non e' possibile caricare i dati",e);
			}
		log.info("*******************************Fine Esecuzione********************************");
	}
	

}
