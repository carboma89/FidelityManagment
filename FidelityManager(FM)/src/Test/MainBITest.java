package Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import Control.Check;
import Control.ErrLog;
import Control.Pdv;
import Control.Tecnico;
import Settings.Config;
import Settings.DbConnect;
import Settings.XmlMan;

public class MainBITest {
	public MainBITest(){
		Logger log= Logger.getLogger("myLog");
		log.info("*******************************Inizio Esecuzione in Batch********************************");
		ArrayList<Pdv> P = new ArrayList<Pdv>();
		ArrayList<Tecnico> T =new ArrayList<Tecnico>();
		ArrayList<ErrLog> E = new ArrayList<ErrLog>();
		ArrayList<ErrLog> EOld = new ArrayList<ErrLog>();
		DbConnect con = new DbConnect();
		log.info("Inizio connessione al database");
		log.debug("Configurazione server conn "+Config.getServerConn()+" UserName "+Config.getdbUserName()+" Password "+Config.getdbPasswd());
		if((con.dbConnect(Config.getServerConn(), Config.getdbUserName(),Config.getdbPasswd()))==false){
			log.fatal("Non e' porribile collegarsi al DB. Controllare la connessione e rieseguire");
			System.exit(0);
		}
	    ResultSet rs;
	    rs = con.query("exec ControlloGiornaliero '2014-01-04'");
	    log.debug("Fine query, inizio caricamento");   
	    XmlMan.loadPdv(P, rs);
	    log.debug("Fine caricamento Pdv,inzio caricamento Tecnici");
	    XmlMan.loadXmlTecnici(T);
	    log.debug("Fine caricamente Tecnici, inizio caricamento errori vecchi");
	    XmlMan.loadXmlErr(EOld, P, T);
	    Check runtimeCheking = new Check(T,P,con);
	    ErrLog controllo;
		try {
			controllo = runtimeCheking.checkPdv(P.get(1), "2014-01-03");
			System.out.print(controllo.toString());
		} catch (NumberFormatException | SQLException e1) {
			log.fatal("Errore controllo pdv",e1);
		}
	    
	    try {
			XmlMan.updateXmlTecnico("ragioneSociale", "computerDis", T.get(1));
			XmlMan.updateXmlPdv("stato", "2", P.get(1));
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			log.fatal("Errore di trasformazione", e);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			log.fatal("Errore di trasformazione", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.fatal("Errore di trasformazione", e);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			log.fatal("Errore di trasformazione", e);
		}
	}
}
