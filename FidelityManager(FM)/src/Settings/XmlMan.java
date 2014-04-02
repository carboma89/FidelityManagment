package Settings;
/**
 * Classe per la gestione (caricamento eliminazioe e store) da file XML
 * @Author: Carlos Borges 12/02/2014
 */
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;   
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document; 
import org.w3c.dom.Element; 
import org.w3c.dom.Node; 
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Services.History;
import Control.ErrLog;
import Control.Pdv;
import Control.Tecnico;
	
public class XmlMan {
	
	public XmlMan(){
		
	}
	
	/**
	 * Carica, nell'ArrayList di Tecnici, i dati presi da Tecnici.xml
	 * @param ArrayList<Tecnico> T 
	 * @Author: Carlos Borges
	 */
	public static void loadXmlTecnici (ArrayList<Tecnico> T){
		Logger log = Logger.getLogger("myLog");
		try { 
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();  
			DocumentBuilder builder = documentFactory.newDocumentBuilder(); 
			Document document = builder.parse(new File(Config.getTecFile()));  //Apertura documento XML nome del file preso dal config
			if(document==null){
				log.fatal("\nErrore nel caricamento del file di gestione dei tecnici:"+Config.getTecFile()+"\n");
			}
			NodeList persone = document.getElementsByTagName("tecnico");   
			log.debug("******************Caricamento tecnici**********************");
			log.debug("Numero tecnici da caricare :"+persone.getLength());
			for(int i=0; i<persone.getLength(); i++) { //lettura e carucamenti dei nodi del file xml
				Node nodo = persone.item(i);   
				if(nodo.getNodeType() == Node.ELEMENT_NODE) { 
					Element persona = (Element)nodo;//Lettura dei value dei tag   
					String ragioneSo = persona.getElementsByTagName("ragioneSociale").item(0).getFirstChild().getNodeValue(); 
					String telefono = persona.getElementsByTagName("numerTel").item(0).getFirstChild().getNodeValue(); 
					String responsabile = persona.getElementsByTagName("responsabile").item(0).getFirstChild().getNodeValue();
					String mail = persona.getElementsByTagName("mail").item(0).getFirstChild().getNodeValue();
					String id = persona.getElementsByTagName("id").item(0).getFirstChild().getNodeValue();
					Tecnico t = new Tecnico(ragioneSo,telefono,responsabile,mail,Integer.parseInt(id));
					T.add(i,t);
					} 
				}
			} 
		catch(Exception e) {
			log.fatal("\nErrore nella funzione loadXmleTecnici classe XmlMan:\n "+e+"\n");
		} 
	}
	
	/**
	 * 	Caricamento dal file/BD i Pdv 
	 * @param ArrayList<P> P, ResultSet rs  
	 * @Author: Carlos Borges
	 */
	public static void loadPdv(ArrayList<Pdv> P, ResultSet rs){
		Logger log = Logger.getLogger("myLog");
		try {	
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document document = docBuilder.parse(Config.getPdvFile());
			if(document==null){
				log.fatal("Errore nel caricamento del file di gestione dei Pdv:"+Config.getPdvFile()+"\n");
			}
			while(rs.next()){//lettura dal rs del db	
				String telefono = rs.getString(5);
				String mail = rs.getString(4);
				String pdv2= rs.getString(1);
				int j = Integer.parseInt(pdv2);
				int i = j-Config.getBasicIndexPdv();
				Node pdv = document.getElementsByTagName("pdv").item(i);
				if(pdv==null){//caricamento nel xml dei nuovi pdv inseriti nel db
					Node elenco = document.getFirstChild();
					Element newPdv = document.createElement("pdv");
					elenco.appendChild(newPdv);
					Attr attr = document.createAttribute("id");	
					attr.setValue(""+pdv2);
					newPdv.setAttributeNode(attr);
					Element ragioneSo = document.createElement("ragioneSociale");
					ragioneSo.appendChild(document.createTextNode("null"));
					newPdv.appendChild(ragioneSo);	
					Element telefonoXml = document.createElement("telefono");
					telefonoXml.appendChild(document.createTextNode(telefono));
					newPdv.appendChild(telefonoXml);
					Element responsabile = document.createElement("responsabile");
					responsabile.appendChild(document.createTextNode("null"));
					newPdv.appendChild(responsabile);	
					Element mailXml = document.createElement("mail");
					mailXml.appendChild(document.createTextNode(mail));
					newPdv.appendChild(mailXml);
					Element stato = document.createElement("stato");
					stato.appendChild(document.createTextNode("0"));
					newPdv.appendChild(stato);
					Element respEst = document.createElement("respEst");
					respEst.appendChild(document.createTextNode("0"));
					newPdv.appendChild(respEst);				
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					DOMSource source = new DOMSource(document);
					StreamResult result = new StreamResult(new File("Pdv.xml"));
					transformer.transform(source, result);		
				}
				else{//nel casi che gia presente nel xml carica il pdv in memoria
				if(pdv.getNodeType() == Node.ELEMENT_NODE) { 
					Element pdvXml = (Element)pdv;	
					String ragioneSo = pdvXml.getElementsByTagName("ragioneSociale").item(0).getFirstChild().getNodeValue();
					String stato = pdvXml.getElementsByTagName("stato").item(0).getFirstChild().getNodeValue();
					String respEst = pdvXml.getElementsByTagName("respEst").item(0).getFirstChild().getNodeValue();
					String responsabile = pdvXml.getElementsByTagName("responsabile").item(0).getFirstChild().getNodeValue();	
					Pdv p = new Pdv(ragioneSo,telefono, responsabile, mail, Integer.parseInt(pdv2),Integer.parseInt(stato),Integer.parseInt(respEst));
					P.add(i, p);
					}
				}	
			}		
	  } catch (SQLException e) {
			log.fatal("Errore lettura res swl: "+e);
	  }catch (ParserConfigurationException pce) {
			log.fatal("Errore parsing xml: "+pce);
	  } catch (IOException ioe) {
			log.fatal("Errore IO: "+ioe);
	  } catch (SAXException sae) {
			log.fatal("Errore chiamata funzione SAX: "+sae);
	  } catch (TransformerException e){
			log.fatal("Errore scrittura xml : "+e);
	  }
	}
	
		/**
		 * Salvataggio nel file xml ErrLog gli errori 
		 * @param E
		 */
		public static void StoreXmlErr(ArrayList<ErrLog> E){
			Logger log = Logger.getLogger("myLog");
			try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder;
				docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(Config.getErrFile());
				if(doc==null){
					log.fatal("\nErrore nell'apertura del file degli errori:"+Config.getErrFile()+"\n");
				}
				for(ErrLog e: E){//caricamento dell'array di ErrorLog nel file XML
					log.debug("Salvataggio dell'errore:"+e.getPdv().getRagioneSociale()+"; del:"+e.getData()+"; Type:"+e.getType());
					Node errors = doc.getFirstChild();
					Element newErr = doc.createElement("error");
					errors.appendChild(newErr);
					Element pdv = doc.createElement("pdv");
					pdv.appendChild(doc.createTextNode(String.valueOf(e.getPdv().getPDV())));
					newErr.appendChild(pdv);
					if(e.getTecnico()!=null){//controllo se il pdv ha un tecnico associato lo carico altrimenti metto a null
						Element tecnico = doc.createElement("tecnico");
						tecnico.appendChild(doc.createTextNode(String.valueOf(e.getPdv().getRespEst())));
						newErr.appendChild(tecnico);
					}
					else{
						Element tecnico = doc.createElement("tecnico");
						tecnico.appendChild(doc.createTextNode("null"));
						newErr.appendChild(tecnico);
					}
					Element stato = doc.createElement("stato");
					stato.appendChild(doc.createTextNode(String.valueOf(e.getStato())));
					newErr.appendChild(stato);	
					Element data = doc.createElement("data");
					data.appendChild(doc.createTextNode(e.getData()));
					newErr.appendChild(data);
					Element type = doc.createElement("type");
					type.appendChild(doc.createTextNode(String.valueOf(e.getType())));
					newErr.appendChild(type);
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer;
					transformer = transformerFactory.newTransformer();
					DOMSource source = new DOMSource(doc);
					StreamResult result = new StreamResult(new File("ErrLog.xml"));
					transformer.transform(source, result);
				}
		}catch (ParserConfigurationException e) {
			log.fatal("Errore di parsing del xml: "+e);
		}catch (IOException ioe) {
			log.fatal("Errore IO "+ioe);
		} catch (SAXException sae) {
			log.fatal("Errore libreria SAX: "+sae);
		}catch (TransformerConfigurationException e1) {
			log.fatal("Errore riscrittura XML:"+e1);
		}catch (TransformerException e2) {
			log.fatal("Errore riscrittura XML:"+e2);
		}
	}

	/**
	 * Cancelazione degli errori nel xml con pdv e data uguali ai parametri
	 * @param data
	 * @param pdv
	 */
	public static void deleteXmlErr(String data, Pdv pdv) {
		Logger log = Logger.getLogger("myLog");
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docFactory.newDocumentBuilder();	
			Document doc = docBuilder.parse(Config.getErrFile());
			if(doc==null){
				log.fatal("\nErrore nell'apertura del file degli errori:"+Config.getErrFile()+"\n");
			}
			NodeList padre = doc.getElementsByTagName("error");
			for(int i=0;i<padre.getLength();i++){//scorro tutti gli errori nel xml
				Element error = (Element) padre.item(i);
				Element pdvXml = (Element) error.getElementsByTagName("pdv").item(0);//carico pdv e data
				String pdv2 = pdvXml.getTextContent();
				Element dataXml = (Element) error.getElementsByTagName("data").item(0);
				String data2 = dataXml.getTextContent();	
					if((pdv2.equals(String.valueOf(pdv.getPDV())))&&(data.equals(data2))){//confronto pdv e data
						error.getParentNode().removeChild(error);//cancello nodo
						log.debug("Cancellato nodo ErrLog :"+data+"; Pdv:"+pdv); 
						doc.normalize();
						TransformerFactory transformerFactory = TransformerFactory.newInstance();
						Transformer transformer;
						transformer = transformerFactory.newTransformer();
						DOMSource source = new DOMSource(doc);
						StreamResult result = new StreamResult(new File("ErrLog.xml"));
						transformer.transform(source, result);
					}
			}
		} catch (ParserConfigurationException e) {
			log.fatal("Errore di parsing del xml: "+e);
		}catch (IOException ioe) {
			log.fatal("Errore IO "+ioe);
		} catch (SAXException sae) {
			log.fatal("Errore libreria SAX: "+sae);
		} catch (TransformerConfigurationException e) {
			log.fatal("Errore riscrittura XML:"+e);
		} catch (TransformerException e) {
			log.fatal("Errore riscrittura XML:"+e);
		}
	}
		
	/**
	 * Caricamento in un array di ErrLog gli err salvati nel xml
	 * @param E
	 * @param P
	 * @param T
	 */
	public static void loadXmlErr(ArrayList<ErrLog> E,ArrayList<Pdv> P,ArrayList<Tecnico> T){
		Logger log = Logger.getLogger("myLog");
		try{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(Config.getErrFile());
		if(doc==null){
			log.fatal("\nErrore nell'apertura del file degli errori:"+Config.getErrFile()+"\n");
		}
		NodeList error = doc.getElementsByTagName("error");
		for(int i=0;i<error.getLength();i++){
			Node errNode = error.item(i);
			if(errNode.getNodeType() == Node.ELEMENT_NODE) { 
				Element pdvXml = (Element)errNode;
				Tecnico t = null;
				String pdv = pdvXml.getElementsByTagName("pdv").item(0).getFirstChild().getNodeValue();
				String data = pdvXml.getElementsByTagName("data").item(0).getFirstChild().getNodeValue();
				String tecnico = pdvXml.getElementsByTagName("tecnico").item(0).getFirstChild().getNodeValue();
				if(tecnico.equals("null")==false){//se presenta il campo tecnico lo carico
					log.debug("\ntecnico :"+tecnico);
					t = T.get((Integer.parseInt(tecnico)-1));
				}
				String stato = pdvXml.getElementsByTagName("stato").item(0).getFirstChild().getNodeValue();
				String type = pdvXml.getElementsByTagName("type").item(0).getFirstChild().getNodeValue();
				ErrLog mewError = new  ErrLog(P.get(Integer.parseInt(pdv)-12),t,Integer.parseInt(type),Integer.parseInt(stato),data);
				E.add(mewError);
				}
			}
		}catch (ParserConfigurationException e) {
			log.fatal("Errore di parsing del xml: "+e);
		}catch (IOException ioe) {
			log.fatal("Errore IO "+ioe);
		} catch (SAXException sae) {
			log.fatal("Errore libreria SAX: "+sae);
		} 
	}
	
	/**
	 * Caricamento degli historici per controllo errori
	 * @param H
	 * @param date
	 * @return
	 */
	public static int loadHistorical(ArrayList<History> H,String date){
		Logger log = Logger.getLogger("myLog");
		String[] parts = date.split("-"); 
		String anno = parts[0];
		String mese = parts[1];
		String giorno = parts[2];
		String folder=null;
		String folderPath;
		String file;
		int g = Integer.parseInt(giorno);
		if(g<=10)//controllo di quale folder fa parte
			folder="fold1";
		if((g>10)&&(g<=20))
			folder="fold2";
		if(g>20)
			folder="fold3";
		folderPath="history/"+anno+"/"+mese+"/"+folder;//creazione della path completa
		File foldFile = new File(folderPath);
		if(foldFile.isDirectory()==false)
			return 0;//se non esiste il file
		file= folderPath+"/"+giorno+".xml";
		try{
			log.debug("Cerco istorico all'interno del file: "+folderPath);
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;	
			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(file);
			NodeList error = doc.getElementsByTagName("error");
			for(int i=0;i<error.getLength();i++){
				Node errNode = error.item(i);
				if(errNode.getNodeType() == Node.ELEMENT_NODE) { 
					Element pdvXml = (Element)errNode;
					String pdv = pdvXml.getElementsByTagName("pdv").item(0).getFirstChild().getNodeValue();
					String type = pdvXml.getElementsByTagName("type").item(0).getFirstChild().getNodeValue();
					History h = new History(Integer.parseInt(pdv),Integer.parseInt(type));
					H.add(h);
					}
				}
			}
			catch (ParserConfigurationException e) {
				log.fatal("Errore di parsing del xml: "+e);
			}catch (IOException ioe) {
				return 0;
			} catch (SAXException sae) {
				log.fatal("Errore libreria SAX: "+sae);
			}
		return 1;//se trova il file 
	}
	
	
	/**
	 * Archiviazione degli istorici sugli errori
	 * @param e
	 */
	public static void archiviate(ErrLog e){
		Logger log = Logger.getLogger("myLog");
		String date = e.getData();
		String[] parts = date.split("-"); 
		String anno = parts[0];
		String mese = parts[1];
		String giorno = parts[2];
		int g = Integer.parseInt(giorno);
		String folder=null;
		String folderPath;
		if(g<=10)
			folder="fold1";
		if((g>10)&&(g<=20))
			folder="fold2";
		if(g>20)
			folder="fold3";
		folderPath="history/"+anno+"/"+mese+folder;
		File foldFile = new File(folderPath);		
		if(foldFile.isDirectory()==false)//se non esiste ancora il folder lo crea
			new File(folderPath).mkdirs();
		XmlMan.deleteXmlErr(e.getData(), e.getPdv());
		try { 
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();  
			DocumentBuilder builder = documentFactory.newDocumentBuilder(); 
			Document doc = builder.parse(new File(folderPath+"/"+giorno+".xml"));  //Apertura documento XML 
			Node errors = doc.getFirstChild();
			Element newErr = doc.createElement("error");
			errors.appendChild(newErr);
			Element pdv = doc.createElement("pdv");
			pdv.appendChild(doc.createTextNode(String.valueOf(e.getPdv().getPDV())));
			newErr.appendChild(pdv);	
			Element type = doc.createElement("type");
			pdv.appendChild(doc.createTextNode(String.valueOf(e.getType())));
			newErr.appendChild(type);	
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(folderPath+"/"+giorno+".xml"));
			transformer.transform(source, result);	
			} 
		catch(Exception e1) { 
			log.fatal("Errore nel salvatagio xml history: "+e1);
		} 
	}
	
	/**
	 * Update degli stati degli errori
	 * @param e
	 */
	public static void upErrStatus(ErrLog e){
		Logger log = Logger.getLogger("myLog");
		log.debug("Aggiornamento errore :"+e.getPdv().getRagioneSociale()+"; Data: "+e.getData()+"; Stato: "+e.getStato());
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(Config.getErrFile());
			if(doc==null){
				log.fatal("\nErrore nell'apertura del file degli errori:"+Config.getErrFile()+"\n");
			}
			NodeList padre = doc.getElementsByTagName("error");
			for(int i=0;i<padre.getLength();i++){
				Element error = (Element) padre.item(i);
				Element pdvXml = (Element) error.getElementsByTagName("pdv").item(0);
				String pdv2 = pdvXml.getTextContent();
				Element dataXml = (Element) error.getElementsByTagName("data").item(0);
				String data2 = dataXml.getTextContent();
				if((pdv2.equals(String.valueOf(e.getPdv().getPDV())))&&(e.getData().equals(data2))){//ricerca dello stato
					error.getElementsByTagName("stato").item(0).setTextContent(String.valueOf(e.getStato()));
					doc.normalize();
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer;	
					transformer = transformerFactory.newTransformer();	
					DOMSource source = new DOMSource(doc);
					StreamResult result = new StreamResult(new File(Config.getErrFile()));
					transformer.transform(source, result);
					break;
					}
				}
			} catch (ParserConfigurationException e1) {
				log.fatal("Errore di parsing del xml: "+e1);
			}catch (IOException ioe) {
				log.fatal("Errore IO "+ioe);
			} catch (SAXException sae) {
				log.fatal("Errore libreria SAX: "+sae);
			} catch (TransformerConfigurationException e2) {
				log.fatal("Errore riscrittura XML:"+e2);
			} catch (TransformerException e3) {
				log.fatal("Errore riscrittura XML:"+e3);
			}
	}
	
	
	/**
	 * Update del tag passato come parametro con il valore passato e sul xml pdv
	 * @param tag
	 * @param value
	 * @param Pdv
	 * @return 1 se l'aggiornamento e' andato a buon fine 0 alrimenti
	 * @throws TransformerException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public static int updateXmlPdv(String tag, String value,Pdv p) throws TransformerException, SAXException, IOException, ParserConfigurationException{
		Logger log = Logger.getLogger("myLog");
		log.debug("Aggiornamento Pdv, tag: "+tag+"con il valore: "+value+"Del pdv "+p.getRagioneSociale());
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(Config.getPdvFile());
		if(doc==null){
			log.fatal("\nErrore nell'apertura del file degli errori:"+Config.getErrFile()+"\n");
			return 0;
		}
		NodeList padre = doc.getElementsByTagName("pdv");
		for(int i=0;i<padre.getLength();i++){
			Element pdv = (Element) padre.item(i);
			Element ragSocTag = (Element) pdv.getElementsByTagName("ragioneSociale").item(0);
			String ragSoc = ragSocTag.getTextContent();
			if(ragSoc.equals(p.getRagioneSociale())){//ricerca della ragione sociale
				pdv.getElementsByTagName(tag).item(0).setTextContent(value);
				doc.normalize();
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer;	
				transformer = transformerFactory.newTransformer();	
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File(Config.getPdvFile()));
				transformer.transform(source, result);
				return 0;
				}
			}
		return 1;
	}
	
	/**
	 * Update del tag passato come parametro con il valore passato e sul xml Tecnico passato
	 * @param tag
	 * @param value
	 * @param Texnico
	 * @return 1 se l'aggiornamento e' andato a buon fine 0 alrimenti
	 * @throws TransformerException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public static int updateXmlTecnico(String tag, String value,Tecnico t) throws TransformerException, SAXException, IOException, ParserConfigurationException{
		Logger log = Logger.getLogger("myLog");
		log.debug("Aggiornamento file Tecnico.xml. Tecnico : "+t.getRagioneSociale()+" tag: "+tag+" con il valore: "+value);
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(Config.getTecFile());
		if(doc==null){
			log.fatal("\nErrore nell'apertura del file degli errori:"+Config.getErrFile()+"\n");
			return 0;
		}
		NodeList padre = doc.getElementsByTagName("tecnico");
		for(int i=0;i<padre.getLength();i++){
			Element tec = (Element) padre.item(i);
			Element IdTag = (Element) tec.getElementsByTagName("id").item(0);
			String IdSoc = IdTag.getTextContent();
			if(IdSoc.equals(String.valueOf(t.getId()))){//ricerca della ragione sociale
				tec.getElementsByTagName(tag).item(0).setTextContent(value);
				doc.normalize();
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer;	
				transformer = transformerFactory.newTransformer();	
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File(Config.getTecFile()));
				transformer.transform(source, result);
				return 0;
				}
			}
		return 1;
	}
	
	
}
