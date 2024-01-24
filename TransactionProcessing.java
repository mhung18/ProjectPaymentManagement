import java.util.*;
import java.io.*;

public class TransactionProcessing {
    private ArrayList<Payment> paymentObjects;
    private IDCardManagement idcm;
    
    public TransactionProcessing(String idCardPath, String paymentPath) {
        idcm = new IDCardManagement(idCardPath);
        readPaymentObject(paymentPath);
    }

    public ArrayList<Payment> getPaymentObject() {
        return this.paymentObjects;
    }

    // Requirement 3
    public boolean readPaymentObject(String path) {
        try{
            FileReader fr = new FileReader(path);
            Scanner sc = new Scanner(fr);
            paymentObjects = new ArrayList<Payment>();
            while(sc.hasNextLine()){
                String data = sc.nextLine();
                String info[] = data.split(",");
                if(info.length == 2){
                    paymentObjects.add(new BankAccount(Integer.parseInt(info[0]),Double.parseDouble(info[1])));
                }
                if(info.length == 1){
                    if(info[0].length() == 6){
                        for (IDCard temp : idcm.getIDCards()){
                            if(temp.getSoDinhDanh() == Integer.parseInt(info[0])){
                                int a = temp.getSoDinhDanh();
                                String b = temp.getHoTen();
                                String c = temp.getGioiTinh();
                                String d = temp.getNgayThangNamSinh();
                                String e = temp.getDiaChi();
                                int f = temp.getSoDienThoai();
                                IDCard idc = new IDCard(a,b,c,d,e,f);
                                try{
                                    paymentObjects.add(new ConvenientCard(idc));
                                } catch (CannotCreateCard ex){
                                    System.out.println(ex);
                                }
                            }
                        }
                        
                    }
                    if(info[0].length() == 7){
                        paymentObjects.add(new EWallet(Integer.parseInt(info[0])));
                    }
                }
            }
            fr.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return true;
    }

    // Requirement 4
    public ArrayList<ConvenientCard> getAdultConvenientCards() {
        ArrayList<ConvenientCard> listAdultConvenientCard = new ArrayList<ConvenientCard>();
        for (Payment temp : this.getPaymentObject()){
            if (temp instanceof ConvenientCard){
                ConvenientCard temp1 = (ConvenientCard) temp;
                if (temp1.getType().equals("Adult")){
                    listAdultConvenientCard.add(temp1);
                }
            }
        }
        return listAdultConvenientCard;
    }

    // Requirement 5
    public ArrayList<IDCard> getCustomersHaveBoth() {
        ArrayList<IDCard> listCustomersHaveBoth = new ArrayList<IDCard>();
        for (IDCard tempIDC : idcm.getIDCards()){
            int countPayment = 0;
            for (Payment tempP : this.getPaymentObject()){
                if (tempP instanceof BankAccount){
                    BankAccount ba = (BankAccount) tempP;
                    if (ba.getSoTaiKhoan() == tempIDC.getSoDinhDanh()){
                        countPayment++;
                    }
                }
                if(tempP instanceof EWallet){
                    EWallet ew = (EWallet) tempP;
                    if(ew.getSoDienThoai() == tempIDC.getSoDienThoai()){
                        countPayment++;
                    }
                }
                if(tempP instanceof ConvenientCard){
                    try{
                        ConvenientCard cc = (ConvenientCard)tempP;
                        ConvenientCard conv = new ConvenientCard(tempIDC);
                        if(cc.toString().equals(conv.toString())){
                            countPayment++;
                        }
                    }catch(CannotCreateCard ccc){
                        System.out.println(ccc);
                    }
                }
                
            }
            if(countPayment == 3){
                listCustomersHaveBoth.add(tempIDC);
            }
        }
        return listCustomersHaveBoth;
    }

    // Requirement 6
    public void processTopUp(String path) {
        try {
            FileReader fr = new FileReader(path);
            Scanner sc = new Scanner(fr);
            while(sc.hasNextLine()){
                String data = sc.nextLine();
                String info[] = data.split(",");

                if(info[0].equals("CC")){
                    for (Payment tempP : this.getPaymentObject()){
                        if (tempP instanceof ConvenientCard){
                            ConvenientCard cc = (ConvenientCard) tempP;
                            if(cc.toString().substring(0,6).equals(info[1])){
                                cc.topUp(Double.parseDouble(info[2]));
                            }
                        }
                    }
                }
                else if(info[0].equals("EW")){
                    for (Payment tempP : this.getPaymentObject()){
                        if (tempP instanceof EWallet){
                            EWallet ew = (EWallet) tempP;
                            if(ew.getSoDienThoai() == Integer.parseInt(info[1])){
                                ew.topUp(Double.parseDouble(info[2]));
                            }
                        }
                    }
                }
                else if(info[0].equals("BA")){
                    for (Payment tempP : this.getPaymentObject()){
                        if (tempP instanceof BankAccount){
                            BankAccount ba = (BankAccount) tempP;
                            if(ba.getSoTaiKhoan() == Integer.parseInt(info[1])){
                                ba.topUp(Double.parseDouble(info[2]));
                            }
                        }
                    }
                }
            }
            fr.close();
        } catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    // Requirement 7
    public ArrayList<Bill> getUnsuccessfulTransactions(String path) {
        ArrayList<Bill> listUnsuccessfulTransactions = new ArrayList<Bill>();
        try {
            FileReader fr = new FileReader(path);
            Scanner sc = new Scanner(fr);
            while(sc.hasNextLine()){
                String data = sc.nextLine();
                String info[] = data.split(",");
                Bill b = new Bill(Integer.parseInt(info[0]),Double.parseDouble(info[1]),info[2]);
                if(info[3].equals("CC")){
                    for(Payment tempP : this.getPaymentObject()){
                        if (tempP instanceof ConvenientCard){ 
                            ConvenientCard cc = (ConvenientCard) tempP;
                            if (cc.toString().substring(0,6).equals(info[4])){
                                boolean bl = cc.pay(Double.parseDouble(info[1]));
                                if(bl == false){                 
                                    listUnsuccessfulTransactions.add(b);
                                }
                            }
                        }
                    }
                }
                else if(info[3].equals("EW")){
                    for(Payment tempP : this.getPaymentObject()){
                        if (tempP instanceof EWallet){
                            EWallet ew = (EWallet) tempP;
                            if(ew.getSoDienThoai() == Integer.parseInt(info[4])){
                                boolean bl = ew.pay(Double.parseDouble(info[1]));
                                if(bl == false){                 
                                    listUnsuccessfulTransactions.add(b);
                                }
                            }
                        }
                    }
                }
                else if(info[3].equals("BA")){
                    for(Payment tempP : this.getPaymentObject()){
                        if (tempP instanceof BankAccount){
                            BankAccount ba = (BankAccount)tempP;
                            if(ba.getSoTaiKhoan() == Integer.parseInt(info[4])){
                                boolean bl = ba.pay(Double.parseDouble(info[1]));
                                if(bl == false){                 
                                    listUnsuccessfulTransactions.add(b);
                                }
                            }
                        }
                    }
                }
            }
            fr.close();
        } catch(IOException ioEx){
            ioEx.printStackTrace();
        }
        return listUnsuccessfulTransactions;
    }

    // Requirement 8
    public ArrayList<BankAccount> getLargestPaymentByBA(String path) {
        ArrayList<BankAccount> listLargestPaymentByBA = new ArrayList<BankAccount>(); 
        ArrayList<BankAccount> listBASuccessfulPayment = new ArrayList<BankAccount>();
        ArrayList<Double> listBalanceOfBASuccessfulPayment = new ArrayList<Double>();
        ArrayList<BankAccount> listBAAfterPaymentSuccessful = new ArrayList<BankAccount>();
        ArrayList<Double> listBalanceOfBAAfterPaymentSuccessful = new ArrayList<Double>();
        
        try {
            
            FileReader fr = new FileReader(path);
            Scanner sc = new Scanner(fr);
            while(sc.hasNextLine()){
                String data = sc.nextLine();
                String info[] = data.split(",");
                Bill b = new Bill(Integer.parseInt(info[0]),Double.parseDouble(info[1]),info[2]);
                if(info[3].equals("BA")){
                    for(Payment tempP : this.getPaymentObject()){
                        if (tempP instanceof BankAccount){
                            BankAccount ba = (BankAccount)tempP;
                            if(ba.getSoTaiKhoan() == Integer.parseInt(info[4])){
                                if(ba.pay(Double.parseDouble(info[1]))){
                                    ba.topUp(Double.parseDouble(info[1]));                 
                                    if(!(listBASuccessfulPayment.contains(ba))){
                                        listBASuccessfulPayment.add(ba);
                                        listBalanceOfBASuccessfulPayment.add(ba.checkBalance());
                                    }
                                }
                            }
                        }
                    }
                }
            }
            fr.close();

            
            FileReader fr1 = new FileReader(path);
            Scanner sc1 = new Scanner(fr1);
            while(sc1.hasNextLine()){
                String data = sc1.nextLine();
                String info[] = data.split(",");
                Bill b = new Bill(Integer.parseInt(info[0]),Double.parseDouble(info[1]),info[2]);
                if(info[3].equals("BA")){
                    for(Payment tempP : this.getPaymentObject()){
                        if (tempP instanceof BankAccount){
                            BankAccount ba = (BankAccount)tempP;
                            if(ba.getSoTaiKhoan() == Integer.parseInt(info[4])){
                                if(ba.pay(Double.parseDouble(info[1]))){
                                    if(!(listBAAfterPaymentSuccessful.contains(ba))){
                                        listBAAfterPaymentSuccessful.add(ba);
                                        listBalanceOfBAAfterPaymentSuccessful.add(ba.checkBalance());
                                    }
                                }
                            }
                        }
                    }
                }
            }
            fr1.close();
            
            Double largest = 0.0;
            for (int i = 0;i < listBASuccessfulPayment.size();i++){
                Double totalPayment = listBalanceOfBASuccessfulPayment.get(i) - listBalanceOfBAAfterPaymentSuccessful.get(i);
                if (totalPayment == largest){
                    listLargestPaymentByBA.add(listBAAfterPaymentSuccessful.get(i));
                }
                else if (totalPayment > largest){
                    listLargestPaymentByBA.clear();
                    listLargestPaymentByBA.add(listBAAfterPaymentSuccessful.get(i));
                    largest = totalPayment;
                }
            }
        } catch(IOException ioEx){
            ioEx.printStackTrace();
        }
        
        
        return listLargestPaymentByBA;
    }

    //Requirement 9
    public void processTransactionWithDiscount(String path) {
        try{
            FileReader fr = new FileReader(path);
            Scanner sc = new Scanner(fr);
            while(sc.hasNextLine()){
                String data = sc.nextLine();
                String info[] = data.split(",");
                if (info[3].equals("CC")){
                    for (Payment tempP : this.getPaymentObject()){
                        if (tempP instanceof ConvenientCard){
                            ConvenientCard cc = (ConvenientCard) tempP;
                            if (cc.toString().substring(0,6).equals(info[4])){
                                cc.pay(Double.parseDouble(info[1]));
                            }   
                        }
                    }
                }
                else if (info[3].equals("BA")){
                    for (Payment tempP : this.getPaymentObject()){
                        if (tempP instanceof BankAccount){
                            BankAccount ba = (BankAccount) tempP;
                            if(ba.getSoTaiKhoan() == Integer.parseInt(info[4])){
                                ba.pay(Double.parseDouble(info[1]));
                            }
                        }
                    }
                }
                else if (info[3].equals("EW")){
                    for (Payment tempP : this.getPaymentObject()){
                        if (tempP instanceof EWallet){
                            EWallet ew = (EWallet) tempP;
                            if(ew.getSoDienThoai() == Integer.parseInt(info[4])){
                                for(Payment tempP1 : this.getPaymentObject()){
                                    if(tempP1 instanceof ConvenientCard){
                                        ConvenientCard cc = (ConvenientCard) tempP1;
                                        String information[] = cc.toString().split(",");
                                        String dayOfBirth = information[3];
                                        String sex  = information[2];
                                        int soDienThoai = Integer.parseInt(information[5]);
                                        if (soDienThoai == ew.getSoDienThoai()){
                                            String date[] = dayOfBirth.split("/");
                                            int tuoi = 2023 - Integer.parseInt(date[2]);
                                            if((sex.equals("Female") && tuoi < 18) || (sex.equals("Male") && tuoi < 20)){
                                                ew.pay(Double.parseDouble(info[1]) * 0.85);
                                            }
                                            else {
                                                ew.pay(Double.parseDouble(info[1]));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException i){
            i.printStackTrace();
        }
    }
}
