package Control;

public class ErrLog {

	public ErrLog(){
	}
	
	public ErrLog(Pdv p, Tecnico t, int type, int stato,String data){
		this.p=p;
		this.t=t;
		this.type=type;
		this.stato=stato;
		this.data = data;
	}
	
	public Pdv getPdv(){
		return this.p;
		
	}
	
	public Tecnico getTecnico(){
		return this.t;
	}
	
	public int getType(){
		return this.type;
	}
	
	public int getStato(){
		return this.stato;
	}
	
	public String getData(){
		return this.data;
	}
	
	
	public void setPDV(Pdv p){
		this.p=p;
	}
	
	public void setTecnico(Tecnico t){
		this.t=t;
	}
	
	public void setStato(int i){
		this.stato=i;
	}
	
	public void setData(String d){
		this.data=d;
	}
	
	public void setErr(int i){
		this.type=i;
	}
	
	public String toString(){
		return "Pdv :"+this.p.getRagioneSociale()+" Tecnico riferimento "+t.getRagioneSociale()+" tipo: "+type+" Stato: "+stato+" Data"+data+";";
	}
	
	
	private Pdv p;
	private Tecnico t;
	private int type;
	private int stato;
	private String data;
}
