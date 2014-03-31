package Control;
import java.util.ArrayList;


public class Tecnico {

	public Tecnico(){
		
	}
	public Tecnico(String ragioneSociale,String numTel,String responsabile,String mail,int id){
		this.ragioneSociale=ragioneSociale;
		this.numTel= numTel;
		this.responsabile=responsabile;
		this.mail = mail;
		this.id=id;
		
	}
	
	public String getRagioneSociale(){
		return this.ragioneSociale;
	}
	
	public String getnumTel(){
		return this.numTel;
	}
	
	public String getResponsabile(){
		return this.responsabile;
	}
	
	public String getMail(){
		return this.mail;
	}
	
	public int getId(){
		return id;
	}
	public ArrayList<Integer> getPDV(){
		return this.PDV;
	}
	
	public void setPDV(Integer i){
		PDV.add(i);
	}
	
	public void setId(int i){
		this.id=i;
	}
	
	private String ragioneSociale;
	private String numTel;
	private String mail;
	private String responsabile;
	private int id;
	private ArrayList <Integer> PDV = new ArrayList<Integer>();
}
