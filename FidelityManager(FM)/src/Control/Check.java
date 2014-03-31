//***************In TEST*****************//

package Control;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import Services.History;
import Settings.Config;
import Settings.DbConnect;
import Settings.XmlMan;


public class Check {

	public Check(ArrayList<Tecnico> Tecnici, ArrayList<Pdv> Pdv,DbConnect connection){
		Check.T=Tecnici;
		Check.P=Pdv;
		Check.con = connection;
		
	}

	/**
	 * Controllo giornaliero inserisce gli errori riscontrati all'interno dell'array passato
	 * @param Erros
	 */
	public void checkDaily(ArrayList<ErrLog> E){
		Logger log = Logger.getLogger("myLog");
		GregorianCalendar gc = new GregorianCalendar();
		gc.add(Calendar.DATE, -1);
		int mese = gc.get(Calendar.MONTH);
		mese++;
		String data = gc.get(Calendar.YEAR)+"-"+mese+"-"+gc.get(Calendar.DAY_OF_MONTH);
		String query = Config.getBasicQuery()+"'"+data+"'";
		ResultSet rs = con.query(query);		
		Double limitSup,limitInf;
		limitSup=Config.getUpperBoundError();
		limitInf= Config.getLowerBoundError();
		String msgDett=null;//inizializazzione variabili
		String dest=null;
		String resp=null;
		ErrLog err;
		log.info("******************Controllo errori del giorno: "+data+"*******************");
		try {
			while(rs.next()){
				err = new ErrLog();
				String totCli = rs.getString(6);
				String totDev = rs.getString(7);
				int pdv = Integer.parseInt(rs.getString(1));
				if((pdv-Config.getBasicIndexPdv())>=0){//controllo per il cliente di prova di nico
					Pdv pdvObj = P.get(pdv-Config.getBasicIndexPdv());
					if(pdvObj.getStato()==Config.getStatusPdvOk()){//controllo se lo stato del pdv e attivo				
						log.info("*************Controllo punto vendita: "+pdvObj.getRagioneSociale()+";id:"+pdvObj.getPDV()+ "***************");
						if((totCli==null)&&(totDev==null))
						{
							msgDett = "\nLa presente per informaLa che il Pdv num. "+pdv+" "+pdvObj.getRagioneSociale()+" non ha inviato il file nella giornata di ieri";
							err.setPDV(pdvObj);
							err.setData(data);
							err.setErr(Config.getErrNotSendFile());//inserisce il nomero di riferimento dell'errore
							err.setStato(0);
							E.add(err);
							log.info("Il pdv non ha inviato i dati");
						
						}else
							if((totCli!=null)&&(totDev!=null)){
								Double tot = Double.parseDouble(totDev)-Double.parseDouble(totCli);
								if((tot<= limitInf)||(tot>=limitSup)){
									log.info("Il pdv ha inviato i dati in maniera errata");
									log.info("totCli= "+totCli+" totDev= "+totDev);
									msgDett ="\nLa presente per informarLa che il Pdv num. "+pdv+" "+pdvObj.getRagioneSociale()+" ha inviato i dati in maniera errata, data la differenza tra riga <totale> inviate e riga totale effettivamente calcolato è "+tot+".\n La preghiamo di contattare Luigi Cusanno di Spark al fine di sopperire all'errore, inoltre ricordiamo che la soglia massima concessa come errore è di +0.5, -0,5 centesimi,  ";
									err.setPDV(pdvObj);
									err.setData(data);
									err.setErr(Config.getErrSendFile());//inserisce il nomero di riferimento dell'errore
									err.setStato(0);
									E.add(err);
								}
						}else
							if((totCli!=null)&&(totDev==null)){
								err = new ErrLog(pdvObj,null,3,0,data);
								log.info("Mancanza riga totale");
								msgDett = "\nLa presente per informaLa che il Pdv num. "+pdv+" "+pdvObj.getRagioneSociale()+" ha inviato i dati senza la riga <totale>,";
								err.setPDV(pdvObj);
								err.setData(data);
								err.setErr(Config.getErrNotTotSend());//inserisce il nomero di riferimento dell'errore
								err.setStato(0);
								E.add(err);
								
									}
						//TODO inserire errore mancanza ultima riga
						if(msgDett!=null){//Se esiste il messaggio quindi c'e errore invio la mail
							if(pdvObj.getRespEst()==0){
								dest = pdvObj.getMail();
								if(Config.getTest()==1){//se e' a 1 test con invio a carboma89
									dest="eborges@e-moderna.com";
								}
								resp=pdvObj.getResponsabile();
							}else
							{
								Tecnico t = T.get(pdvObj.getRespEst()-1);
								err.setTecnico(t);
								dest = t.getMail();
								if(Config.getTest()==1){//se e' a 1 test con invio a carboma89
									dest = "carboma89@gmail.com";
								}
								resp=t.getResponsabile();
							}
							String msg=msg1+resp+msgDett+msg2;//Invio email	
							EmailSender email = new EmailSender(
									"eborges@e-moderna.com",
									"45878442", 
									"smtp-out.kpnqwest.it",
									Config.getRespMail(), 
									dest, 
									"Avviso invio dati scanner", 
									"",msg
									);
							if(Config.getTestMail()==0){//se è 1 non invia la mail perche' in fase di test
							email.inviaEmail();
							err.setStato(1);//setto lo stato ad inviato
							}
						}
					if(msgDett==null)
						log.info("Pdv OK");
					msgDett=null;
					}			
				}
			}
			
		} catch (SQLException e) {
			log.fatal("Errore lettura SQL: "+e);
		}
	}

	/**
	 * Controllo Errori Vecchi passati come parametro
	 * @param Errors
	 */
	public void checkOld(ArrayList<ErrLog> E){
		Logger log = Logger.getLogger("myLog");
		String pdv;
		Double limitSup,limitInf;
		limitSup=Config.getUpperBoundError();
		limitInf=Config.getLowerBoundError();
		String msgDett=null;
		String dest=null;
		String resp=null;
		//TODO fare controllo di non invio ai controlli fatti oggi, cancellazione errori vecchi,aumento lo stato dell'errore
		for(ErrLog e:E){
			log.info("********Controllo vecchio Pdv: "+e.getPdv().getRagioneSociale()+"; data: "+e.getData()+"; type: "+e.getStato()+"; stato: "+e.getStato()+"*********");
			GregorianCalendar gc = new GregorianCalendar();
			gc.add(Calendar.DATE, -1);
			int mese = gc.get(Calendar.MONTH);
			mese++;
			String data = gc.get(Calendar.YEAR)+"-"+mese+"-"+gc.get(Calendar.DAY_OF_MONTH);
			if((e.getData().equals(data))==false){
			String query = Config.getBasicQuery()+"'"+e.getData()+"'";
			ResultSet rs = con.query(query);
			try {
				while(rs.next()){
					pdv=rs.getString(1);
					if(pdv.compareTo(String.valueOf(e.getPdv().getPDV()))==0){//ricerca nella query il pdv del xml
						String totCli = rs.getString(6);
						String totDev = rs.getString(7);
						int pdv2 = Integer.parseInt(rs.getString(1));
						if((pdv2-12)>=0){//controllo per il cliente di prova di nico
							Pdv pdvObj = P.get(pdv2-Config.getBasicIndexPdv());
							if(pdvObj.getStato()==1){//controllo se il pvd e attivo
								if((totCli==null)&&(totDev==null))
								{
									msgDett = "\nLa presente per informaLa non abbiamo ancora ricevuto i dati del Pdv num. "+pdv+" "+pdvObj.getRagioneSociale()+" del giorno "+e.getData()+" ";
									e.setStato(e.getStato()+1);
									XmlMan.upErrStatus(e);
									log.info("Il pdv non ha inviato i dati");
								}else
									if((totCli!=null)&&(totDev!=null)){
										Double tot = Double.parseDouble(totDev)-Double.parseDouble(totCli);
										if((tot<= limitInf)||(tot>=limitSup)){
											log.info("Il pdv ha inviato i dati in maniera errata");
											log.info("totCli= "+totCli+" totDev= "+totDev+"; Differenza di: "+tot+"\n");
											msgDett ="\nLa presente per informarLa non abbiamo ancora ricevuto i dati del Pdv num. "+pdv+" "+pdvObj.getRagioneSociale()+"del giorno "+e.getData()+" in modo corretto, la preghiamo di contattare Luigi Cusanno di Spark al fine di sopperire all'errore di differenza tra riga <totale> inviata e il calcolo efettivo del totale ";
											e.setStato(e.getStato()+1);
											XmlMan.upErrStatus(e);
										}
									}
								}else
									if((totCli==null)&&(totDev!=null)){
											//TODO invio mail luigi cusanno e antonio
									}
								if(msgDett!=null){//Se esiste il messaggio quindi c'e errore invio la mail
									if(pdvObj.getRespEst()==0){
										dest = pdvObj.getMail();
										if(Config.getTest()==1){//se è a 1 test con invio a carboma89
											dest=Config.getRespMail();
										}
										resp=pdvObj.getResponsabile();
									}else
									{
										Tecnico t = T.get((pdvObj.getRespEst()-1));
										if(Config.getTest()==1){//se è a 1 test con invio a carboma89
											dest=Config.getRespMail();
										}
										dest = t.getMail();
										resp=t.getResponsabile();
									}
									String msg=msg1+resp+msgDett+msg2;//Invio email	
									EmailSender email = new EmailSender(
											"eborges@e-moderna.com",
											"45878442", 
											"smtp-out.kpnqwest.it",
											Config.getRespMail(), 
											dest, 
											"Avviso invio dati scanner", 
											"",msg
											);
									if(Config.getTestMail()==0){//se e' a 1 non invia la mail perche' in fase di test senza mail
										email.inviaEmail();
									}
									
								}
								if(msgDett==null){
									XmlMan.deleteXmlErr(e.getData(),e.getPdv());
									log.info("Il pdv ha corretto l'errore");
								}
								msgDett=null;
							}		
						break;
					}
				
				}
			} catch (SQLException e1) {
				log.fatal("Impossibile leggere SQL: "+e1);
			}
		}
		}
	}
	
	/**
	 * Controllo errori per data ritorna un array di interi dei pdv se 0 lo stato e' ok 1 per non inviato 2 errore invio 3 non ha inviato 
	 * la riga totale 4 non e stata calcolata la riga finale (l'indice dell'array determina ID del PDV)
	 * @param data
	 * @return Array di interi 
	 */
	@SuppressWarnings("unused")
	public ArrayList<Integer> checkDate(String data){
		Logger log2 = Logger.getLogger("myLog");
		ArrayList<Integer> log = new ArrayList<Integer>();
		ArrayList<History> H  = new ArrayList<History>();
		int i = XmlMan.loadHistorical(H, data);//controllo se e' presente  historico nel file xml
		int j=0;
		if(i!=0){
			for(Pdv p: P){//inizializazzione del array con tutti 0
				log.add(0);
			}
			for(History h: H){		
				j=h.getType();
				log.set(h.getPdv()-Config.getBasicIndexPdv(), h.getType());		
					}		
		}else{//se non esiste nel xml controllo nel db
			String query = Config.getBasicQuery()+"'"+data+"'";
			log2.debug("Esecuzione query: "+query);
			ResultSet rs = con.query(query);		
			try {
				while(rs.next()){			
					String totCli = rs.getString(6);
					String totDev = rs.getString(7);				
					int pdv = Integer.parseInt(rs.getString(1));				
					if((pdv-Config.getBasicIndexPdv())>=0){//controllo per il cliente di prova di nico
						Pdv pdvObj = P.get(pdv-Config.getBasicIndexPdv());
						
						log2.debug("\nControllo punto vendita: "+pdvObj.getRagioneSociale()+"\n");
						if(pdvObj.getStato()==1){//controllo solo i punti vendita che nn sono in stato di test
							int chekIndex = this.getCheck(totCli, totDev);
							log.add(pdvObj.getPDV()-Config.getBasicIndexPdv(),chekIndex);
					}
						else log.add(-1);//aggiungo se -1 se il punto vendita è in fase di test 
					}
					
				}
			}catch (SQLException e) {
				log2.fatal("Impossibile leggere SQL: "+e);
			}
		}
		return log;
	}
	
	
	/**
	 * Controllo per punto vendita e per data
	 * @param pdvIn
	 * @param data
	 * @return log
	 * @throws NumberFormatException
	 * @throws SQLException
	 */
	public ErrLog checkPdv(Pdv pdvIn,String data) throws NumberFormatException, SQLException{
		Logger log2 = Logger.getLogger("myLog");
		ErrLog log = null;
		String query = "exec ControlloGiornalieroPerNegozio" +"'"+data+"' , '"+pdvIn.getPDV()+"'";
		log2.debug("Esecuzione query: "+query);
		ResultSet rs = con.query(query);		
		
		while(rs.next()){
			String totCli = rs.getString(6);
			String totDev = rs.getString(7);
			int pdv2 = Integer.parseInt(rs.getString(1));
				if((pdv2-12)>=0){//controllo per il cliente di prova di nico
					int indexCheck = this.getCheck(totCli, totDev);
					if(pdvIn.getRespEst()!=0){
					 log = new ErrLog(pdvIn,T.get(pdvIn.getRespEst()),indexCheck,0,data);
					}else
						 log = new ErrLog(pdvIn,null,indexCheck,0,data);
				}
		}
		return log;
	}
		
	/**
	 * Metodo privato per il controllo sui totali
	 * @param totComunicato
	 * @param totCalcolato
	 * @return 0 senza errore; 1 file non inviato; 2 inviato con errore; 3 errore interno; 4 mancanza ultima riga
	 */
	private int getCheck(String totComunicato, String totCalcolato){
		Logger log2 = Logger.getLogger("myLog");
		if((totComunicato==null)&&(totCalcolato==null)){
			log2.info("Il pdv non ha inviato i dati");
			return 1;
		}
		if((totComunicato!=null)&&(totCalcolato!=null)){
			Double tot = Double.parseDouble(totCalcolato)-Double.parseDouble(totComunicato);
			if((tot<= Config.getLowerBoundError())||(tot>=Config.getUpperBoundError())){
				log2.debug("Il pdv ha inviato i dati in maniera errata\n");
				log2.debug("totCli= "+totComunicato+" totDev= "+totCalcolato+"; Differenza di: "+tot+"\n");
				return 2;
			}
			return 0;
		}
		if((totComunicato==null)&&(totCalcolato!=null)){
			log2.debug("Errore interno");	
				return 3;
		}
		if((totComunicato!=null)&&(totCalcolato==null)){	
			log2.debug("Manca invio ultima riga");
			return 4;
		}
		return 0;
	}
	private static DbConnect con; 
	private static ArrayList<Pdv> P;
	private static ArrayList<Tecnico> T;
	private static String msg1="Buongiorno Sig.";
	private static String msg2=", al fine di mantenere l'integrita dei dati le chiedo di RINVIARE i dati risultati in errore.\n Saluti Borges Carlos";
}



