package Control;



public class Pdv {
	
	public Pdv(String ragioneSociale,String numTel,String responsabile,String mail,int PDV,int stato,int respEst){
		this.ragioneSociale=ragioneSociale;
		this.numTel= numTel;
		this.responsabile=responsabile;
		this.mail = mail;
		this.PDV = PDV;
		this.stato = stato;
		this.respEst=respEst;
		
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
	
	public int getStato(){
		return this.stato;
	}
	
	public int getPDV(){
		return this.PDV;
	}
	
	public int getRespEst(){
		return respEst;
	}
	

	
	private String ragioneSociale;
	private String numTel;
	private String mail;
	private String responsabile;
	private int PDV ;
	private int stato;
	private int respEst;
}
