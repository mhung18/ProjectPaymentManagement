public class EWallet implements Payment, Transfer {
	private int soDienThoai;
	private double soDuTaiKhoan;

	public EWallet (int sdt){
		this.soDienThoai = sdt;
		this.soDuTaiKhoan = 0;
	}

	public boolean pay(double amount){
		if (amount <= this.soDuTaiKhoan){
			this.soDuTaiKhoan -= amount;
			return true;
		}
		return false;
	}
	
	public int getSoDienThoai(){
		return this.soDienThoai;
	}
    public double checkBalance(){
		return this.soDuTaiKhoan;
	}

	public boolean transfer (double amount, Transfer to){
		double soTienChuyen = amount + amount * Transfer.transferFee;
        if(soTienChuyen <= this.checkBalance()){
			this.soDuTaiKhoan -= amount + amount * Transfer.transferFee;
			if(to instanceof EWallet){
				EWallet tempEW = (EWallet) to;
            	tempEW.topUp(amount);
			}
			if(to instanceof BankAccount){
                BankAccount tempBA = (BankAccount) to;
                tempBA.topUp(amount);
            }
            
			return true;
		}
		return false;
	}

	public void topUp(double soTienCanNap){
		this.soDuTaiKhoan += soTienCanNap;
	}
	
	public String toString(){
		return this.soDienThoai + "," + this.soDuTaiKhoan;
	}
}
