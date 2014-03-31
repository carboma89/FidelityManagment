package BI;

import java.io.IOException;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.ArrayList;

import jxl.write.WriteException;

import org.apache.log4j.Logger;

import Control.Check;
import Control.ErrLog;
import Control.Pdv;
import Control.Tecnico;
import Services.ExcelCreator;
import Services.Service;
import Settings.Config;
import Settings.DbConnect;
import Settings.XmlMan;

public class MainBI {

	public MainBI() {
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
			log.fatal("Non è porribile collegarsi al DB. Controllare la connessione e rieseguire");
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
	    log.debug("Inizio controllo vecchi");
	    runtimeCheking.checkOld(EOld);
	    log.debug("fine caricamente Tecnici, inizio check del giorno");
	    runtimeCheking.checkDaily(E);
	    log.debug("fine check del giorno.");
	    log.debug("Inizio salvataggio errori");
	    XmlMan.StoreXmlErr(E);
	    log.debug("Fine salvataggio");
	    Service.sendWeeklyReport(runtimeCheking, P);
	    Service.ToArchivie(EOld);
	    
	    ExcelCreator newExcel = new ExcelCreator(runtimeCheking,P);
	    try {
			newExcel.createExelReport("2014-3-01", "2014-3-24");
		} catch (WriteException | IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
