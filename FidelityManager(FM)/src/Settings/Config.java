package Settings;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Config {

	
	public Config(){
		
	}
	
	public static String getServerConn(){
		return serverConn;
	}
	
	public static String getdbUserName(){
		return dbUserName;
	}
	
	public static String getdbPasswd(){
		return dbPasswd;
	}
	
	public static String getBasicQuery(){
		return basicQuery;
	}
	
	public static int getBasicIndexPdv(){
		return basicIndexPdv;
	}
	
	public static int getBasicIndexTec(){
		return basicIndexTec;
	}
	
	public static int getArchiveIndex(){
		return archiveIndex;
	}
	
	public static String getTecFile(){
		return tecFile;
	}
	
	public static String getPdvFile(){
		return pdvFile;
	}
	
	public static String getErrFile(){
		return errFile;
	}
	
	public static int getStatusPdvTest(){
		return statusPdvTest;
	}
	public static int getStatusPdvOk(){
		return statusPdvOk;
	}
	public static double getUpperBoundError(){
		return upperBoundError;
	}
	public static double getLowerBoundError(){
		return lowerBoundError;
	}
	public static int getErrNotSendFile(){
		return errNotSendFile;
	}
	public static int getErrSendFile(){
		return errSendFile;
	}
	public static int getErrNotTotSend(){
		return errNotTotSend;
	}
	public static int getTest(){
		return test;
	}
	public static int getTestMail(){
		return testMail;
	}
	public static int getBI(){
		return BI;
	}
	public static String getRespMail(){
		return respMail;
	}
	public static String getRespItMail(){
		return respItMail;
	}
	public  void LoadConfig(){
		Logger log = Logger.getLogger("myLog");
		try {
			final InputStream cfg = new FileInputStream("config.cfg");
			configFile.load(cfg);
			serverConn = this.configFile.getProperty("serverConn");
			dbUserName = this.configFile.getProperty("dbUserName");
			dbPasswd = this.configFile.getProperty("dbPasswd");
			basicQuery = this.configFile.getProperty("basicQuery");
			basicIndexPdv =Integer.parseInt( this.configFile.getProperty("basicIndexPdv"));
			basicIndexTec = Integer.parseInt(this.configFile.getProperty("basicIndexTec"));
			archiveIndex = Integer.parseInt(this.configFile.getProperty("archiveIndex"));
			tecFile = this.configFile.getProperty("tecFile");
			pdvFile = this.configFile.getProperty("pdvFile");
			errFile = this.configFile.getProperty("errFile");
			statusPdvTest = Integer.parseInt(this.configFile.getProperty("statusPdvTest"));
			statusPdvOk = Integer.parseInt(this.configFile.getProperty("statusPdvOk"));
			upperBoundError = Double.parseDouble(this.configFile.getProperty("upperBoundError"));
			lowerBoundError = Double.parseDouble(this.configFile.getProperty("lowerBoundError"));
			errNotSendFile = Integer.parseInt(this.configFile.getProperty("errNotSendFile"));
			errNotTotSend = Integer.parseInt(this.configFile.getProperty("errNotTotSend"));
			errSendFile = Integer.parseInt(this.configFile.getProperty("errSendFile"));
			test = Integer.parseInt(this.configFile.getProperty("test"));
			testMail = Integer.parseInt(this.configFile.getProperty("testMail"));
			BI = Integer.parseInt(this.configFile.getProperty("BI"));
			respMail = this.configFile.getProperty("respMail");
			respItMail = this.configFile.getProperty("respMail");
		} catch (IOException e) {
			log.error("Errore nel caricamento del file di log: "+e.getLocalizedMessage());
			System.exit(0);//uscita dal programma

		}
	}
	
	private Properties configFile = new java.util.Properties();
	private static String serverConn;//Stringa per la connessione al server per il metodo dbConnect
	private static String dbUserName;//Stringa username per la connessione al server
	private static String dbPasswd;//Stringa password per la connessione al server
	private static String basicQuery;//stringa di base per la query per la procedra di controllo
	private static int basicIndexPdv;//indice di base del primo Pdv attivo ( per l'accesso diretto agli array)
	private static int basicIndexTec;//indice di base del primo Tecnico attivo ( per l'accesso diretto agli array)
	private static int archiveIndex ;//indice il quale raggiunto l'errore potra essere archiviato
	private static String tecFile;//posizionamento e nome del file xml dei tecnici
	private static String pdvFile;//posizionamento e nome del file xml dei pdv
	private static String errFile;//posizionamento e nome del file xml degli errori
	private static int statusPdvTest;//id che deve avere il pdv per essere in test
	private static int statusPdvOk;//id che deve avere il pdv per essere oj
	private static double upperBoundError;//limite superiore di errore
	private static double lowerBoundError;//limite inferiore di errore
	private static int errNotSendFile;
	private static int errSendFile;
	public static  int errNotTotSend;
	private static int test;//se e a 1 inva tutte le mail solo ed esclusivamente a il responsabile
	private static int testMail;//se a 1 non invia le mail
	private static int BI;//batch input se e a 1 il prog gira in batch
	private static String respMail;
	private static String respItMail;
}
