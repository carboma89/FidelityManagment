package Boot;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import BI.MainBI;
import Settings.Config;
import UI.UILoader;

public class Main {

	public static void main(String[] args) {
		DOMConfigurator.configure("myLog.xml");
		Logger log= Logger.getLogger("myLog");
		log.info("*******************************Inizio Esecuzione********************************");
		Config config = new Config();
		config.LoadConfig();
		if(Config.getBI()==1)
			new MainBI();
		else
			try {
				new UILoader();
			} catch (Exception e) {
				log.error("Non e' possibile caricare i dati",e);
			}
		log.info("*******************************Fine Esecuzione********************************");
	}

}
