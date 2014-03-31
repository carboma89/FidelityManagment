package Services;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import jxl.write.WriteException;

import org.apache.log4j.Logger;

import Control.Check;
import Control.EmailSender;
import Control.ErrLog;
import Control.Pdv;
import Settings.Config;


public class Service {

	public Service(){
		
		
	}
	/**
	 * Crea invia il report settimanale agli indirizzi mail presisenti nel config file
	 * @throws IOException 
	 * @throws WriteException 
	 * @throws ParseException 
	 */
	public static void sendWeeklyReport(Check checking,ArrayList<Pdv> pdv) {
		Logger log = Logger.getLogger("myLog");
		GregorianCalendar a = new GregorianCalendar();
		int giornoA = a.get(Calendar.DAY_OF_MONTH);
		int meseA = a.get(Calendar.MONTH);
		int annoA=a.get(Calendar.YEAR);//i mesi vengono calcolati da 0
		meseA++;
		int giornoSet = a.get(Calendar.DAY_OF_WEEK);
		if(giornoSet==7){
			log.info("Invio report settimanale");
			log.debug("Calcolo primo giorno della settimana");
			GregorianCalendar da = new GregorianCalendar();
			da.add(Calendar.DATE, -7);
			int giornoDa = da.get(Calendar.DAY_OF_MONTH);
			int meseDa = da.get(Calendar.MONTH);
			int annoDa=da.get(Calendar.YEAR);
			meseDa++;//i mesi vengono calcolati da 0
			String dataDa =""+annoDa+"-"+meseDa+"-"+giornoDa;
			String dataA =""+annoA+"-"+meseA+"-"+giornoA;
			log.debug("Data inizio: "+dataDa+", fino alla data: "+dataA+" ");
			ExcelCreator excel= new ExcelCreator(checking,pdv);
			String path;
			try {
				path = excel.createExelReport(dataDa, dataA);
		
			if(path !=null){
				EmailSender email = new EmailSender(
						"eborges@e-moderna.com",
						"45878442", 
						"smtp-out.kpnqwest.it",
						"eborges@e-moderna.com", 
						"adicaprio@e-moderna.com", 
						"Avviso invio dati scanner", 
						"",msg
						);
				email.inviaEmail(path);
			}else
				log.info("Non e' stato possibile creare il file exel");
			} catch (WriteException | IOException | ParseException e) {
				log.error("Errore creazione file exel resoconto giornaliero", e);
			}

		}
	}
	
	/**
	 * Archiviazione dei errori con stato maggiore uguale ad archiveIndex
	 * @param E
	 * @param T
	 * @param P
	 * @param con
	 * @return
	 */
	public static ArrayList<ErrLog> ToArchivie(ArrayList<ErrLog> E){
		ArrayList<ErrLog> toArchiviate = new ArrayList<ErrLog>();
		Logger log = Logger.getLogger("myLog");
		for(ErrLog e: E){
			if(e.getStato()>=Config.getArchiveIndex()){
				toArchiviate.add(e);	
				log.info("Archiviato errore: "+e.getPdv().getRagioneSociale()+"; in data: "+e.getData()+"; di tipo:  "+e.getType()+"; in stato: "+e.getStato());
			}
		}
		return toArchiviate;
	}
	
	
	private static String msg ="Buongiono,\nIn allegato il resoconto settimanale\n Saluti\nCarlos Borges";
}
