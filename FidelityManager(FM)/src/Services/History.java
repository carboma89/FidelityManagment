package Services;

public class History {

	public History(int pdv,int type){
		this.pdv=pdv;
		this.type=type;
	}
	
	public void setPdv(int pdv){
		this.pdv=pdv;
	}
	
	public void settype(int type){
		this.type=type;
	}
	
	public int getPdv(){
		return this.pdv;
	}
	
	public int getType(){
		return this.type;
	}
	
	private int pdv;
	private int type;
}
