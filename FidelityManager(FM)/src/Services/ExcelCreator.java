package Services;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import org.apache.log4j.Logger;

import Control.Check;
import Control.Pdv;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelCreator {


	public  ExcelCreator(Check checking,ArrayList<Pdv> pdv){
		this.checking=checking;
		this.pdv=pdv;
	}
	
	/**
	 * Crea il pdf con il repot dalla dataDA alla dataA
	 * @param dataDa
	 * @param dataA
	 * @return path del file pdf creato
	 * @throws IOException 
	 * @throws WriteException 
	 * @throws ParseException 
	 */
	@SuppressWarnings("unused")
	public  String createExelReport(String dataDa, String dataA) throws WriteException, IOException, ParseException{
		this.dataA=dataA;
		this.dataDa=dataDa;
		@SuppressWarnings("resource")
		Scanner lettore = new Scanner(System.in);
		this.setOutputFile("resoconto.xls");
		this.write();
		return inputFile;
		
	}
	
	public void setOutputFile(String inputFile) {

	this.inputFile = inputFile;

	}
	
	public void write() throws IOException, WriteException, ParseException {

		File file = new File(inputFile);

		if(!file.exists())

		file.createNewFile();

		WorkbookSettings wbSettings = new WorkbookSettings();

		wbSettings.setLocale(new Locale("it", "IT"));
		

		WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);



		workbook.createSheet("Resoconto ", 1); // crea un nuovo foglio all’interno del workbook,
		
		WritableSheet foglioExcel = workbook.getSheet(0);

		createLabel(foglioExcel);

		creaContenutoTestuale(foglioExcel);

		workbook.write();

		workbook.close();

	}
	
	private void createLabel(WritableSheet sheet) throws WriteException {

		// Lets create a times font

		WritableFont wfont = new WritableFont(WritableFont.ARIAL, 10);

		// Define the cell format

		arial = new WritableCellFormat(wfont);

		// A capo automatico

		arial.setWrap(true);

		WritableFont times10ptBoldUnderline = new WritableFont(

		WritableFont.TIMES, 10, WritableFont.BOLD, false);

		timesBold = new WritableCellFormat(times10ptBoldUnderline);

		// Lets automatically wrap the cells

		timesBold.setWrap(true);

		CellView cv = new CellView();

		cv.setFormat(arial);

		cv.setFormat(timesBold);

		cv.setAutosize(true);
		
		 

		// Scrittura delle intestazioni


		 

		}
	
	//TODO modificare 
	private void creaContenutoTestuale(WritableSheet sheet) throws WriteException,RowsExceededException, ParseException {

	// Come inserire del testo all’interno delle celle
		Logger log = Logger.getLogger("myLog");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date data1 = format.parse(dataDa);
		Date data2 = format.parse(dataA);
		log.debug("Data1: "+data1);
		log.debug("Data2: "+data2);
		this.creaLayout(sheet);
		int i,j;
		i=2;
		j=3;
		Calendar shiftData1 = Calendar.getInstance();
		shiftData1.setTime(data1);
	
		while(shiftData1.getTime().compareTo(data2)!=0){
			int mese = shiftData1.get(Calendar.MONTH);
			mese++;
			String dataCur = shiftData1.get(Calendar.YEAR)+"-"+mese+"-"+shiftData1.get(Calendar.DAY_OF_MONTH);
			log.debug("Data da uguale "+dataCur);
			ArrayList<Integer> res = new ArrayList<Integer>();
			res = checking.checkDate(dataCur);
			for(int t: res){
				if(t!=-1){//se il puntovendita nn è uin fase di test
					
					this.addCaption(sheet, i, 2, dataCur);
					if(t==0)
						this.addLabel(sheet, i, j, "Invio OK");
					if(t==1)
						this.addLabel(sheet, i, j, "Non Inviato");
					if(t==2)
						this.addLabel(sheet, i, j, "Riga totale errata");
					if(t==3)
						this.addLabel(sheet, i, j, "Errore interno");
					if(t==4)
						this.addLabel(sheet, i, j, "Riga totale non inviata");
				j++;
				}
			}
			i++;
			j=3;
			shiftData1.add(Calendar.DATE, 1);
		}

	}
	
	private void creaLayout(WritableSheet sheet) throws RowsExceededException, WriteException{
		int i=3;
		this.addCaption(sheet, 1, 2, "Ragione Sociale");
		this.addCaption(sheet,5, 0, "Moderna Fidelity");
		this.addCaption(sheet,6, 0, "Resconto giornaliero");
		for(Pdv p:pdv){
			if(p.getStato()==1){
				this.addCaption(sheet, 1, i, p.getRagioneSociale());
				i++;
			}
		}
	}
	
	private void addLabel(WritableSheet sheet, int column, int row, String s)

	throws WriteException, RowsExceededException {

	Label label;

	label = new Label(column, row, s, arial);
	
	sheet.addCell(label);

	}

	

	private void addCaption(WritableSheet sheet, int column, int row, String s)

	throws RowsExceededException, WriteException {

	Label label;

	label = new Label(column, row, s, timesBold);
	
	sheet.addCell(label);

	}

		 
	private Check checking;
	
	private String inputFile;

	private WritableCellFormat timesBold;

	private WritableCellFormat arial;
	
	private String dataA;
	
	private String dataDa;
	
	private ArrayList<Pdv> pdv;
		
}
