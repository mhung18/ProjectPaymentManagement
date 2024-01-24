public class BankAccount implements Payment, Transfer{
    private int soTaiKhoan;
    private double tiLeLaiSuat;
    private double soDuTaiKhoan;

    public BankAccount(int soTaiKhoan, double tiLeLaiSuat){
        this.soTaiKhoan = soTaiKhoan;
        this.tiLeLaiSuat = tiLeLaiSuat;
        this.soDuTaiKhoan = 50;
    }

    public boolean pay(double amount){
        if (amount <= soDuTaiKhoan - 50){
            this.soDuTaiKhoan -= amount;
            return true;
        }
        return false;
    }

    public int getSoTaiKhoan(){
        return this.soTaiKhoan;
    }
    public double checkBalance(){
        return this.soDuTaiKhoan;
    }

    public boolean transfer (double amount, Transfer to){
        double soTienChuyen = amount + amount * transferFee;
        if(soTienChuyen <= this.checkBalance() - 50){
            if(to instanceof BankAccount){
                this.soDuTaiKhoan -= soTienChuyen;
                BankAccount tempBA = (BankAccount) to;
                tempBA.topUp(amount);
            }
            if(to instanceof EWallet){
                this.soDuTaiKhoan -= soTienChuyen;
				EWallet tempEW = (EWallet) to;
                tempEW.topUp(amount);
			}
            return true;
        }
        return false;
    }
    public void topUp(double amount){
        this.soDuTaiKhoan += amount;
    }
    public String toString(){
        return this.soTaiKhoan + "," + this.tiLeLaiSuat + "," + this.soDuTaiKhoan;
    }
}
