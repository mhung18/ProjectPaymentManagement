public class ConvenientCard implements Payment {
	private String type;
	private IDCard idcard;
	private double soDuTaiKhoan;
    
	public ConvenientCard(IDCard idcard) throws CannotCreateCard{
		this.idcard = idcard;
		String date[] = idcard.getNgayThangNamSinh().split("/");
		int year = Integer.parseInt(date[2]);
		int age = 2023 - year;
		if (age < 12){
			throw new CannotCreateCard("Not enough age");
		}
		else if(age <= 18){
			this.type = "Student";
		}
		else{
			this.type = "Adult";
		}
		this.soDuTaiKhoan = 100;
	}
	public boolean pay(double amount){
		if (this.soDuTaiKhoan >= amount){
			if(this.getType().equals("Adult")){
				this.soDuTaiKhoan -= amount + amount * 0.01;
			}
			if(this.getType().equals("Student")){
				this.soDuTaiKhoan -= amount;
			}
			return true;
		}
		return false;
	}
    public double checkBalance(){
		return this.soDuTaiKhoan;
	}
	public void topUp(double soTienCanNap){
		this.soDuTaiKhoan += soTienCanNap;
	}
	public String getType() {
		return this.type;
	}
	
	public String toString(){
		return idcard.toString() + "," + this.type + "," + this.soDuTaiKhoan;
	}
}
