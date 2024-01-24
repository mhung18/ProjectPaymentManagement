public class IDCard {
      private int soDinhDanh;
      private String hoTen;
      private String gioiTinh;
      private String ngayThangNamSinh; //dd/mm/yyyy
      private String diaChi;
      private int soDienThoai;

      public IDCard (int soDinhDanh, String hoTen, String gioiTinh, String ngayThangNamSinh, String diaChi, int soDienThoai){
            this.soDinhDanh = soDinhDanh;
            this.hoTen = hoTen;
            this.gioiTinh = gioiTinh;
            this.ngayThangNamSinh = ngayThangNamSinh;
            this.diaChi = diaChi;
            this.soDienThoai = soDienThoai;
      }

      public int getSoDinhDanh(){
            return this.soDinhDanh;
      }
      public void setSoDinhDanh(int sdd){
            this.soDinhDanh = sdd;
      }
      
      public String getHoTen(){
            return this.hoTen;
      }
      public void setHoTen(String ht){
            this.hoTen = ht;
      }

      public String getGioiTinh(){
            return this.gioiTinh;
      }
      public void setGioiTinh(String gt){
            this.gioiTinh = gt;
      }

      public String getNgayThangNamSinh(){
            return this.ngayThangNamSinh;
      }
      public void setNgayThangNamSinh(String ntns){
            this.ngayThangNamSinh = ntns;
      }

      public String getDiaChi(){
            return this.diaChi;
      }
      public void setDiaChi(String dc){
            this.diaChi = dc;
      }

      public int getSoDienThoai(){
            return this.soDienThoai;
      }
      public void setSoDienThoai(int sdt){
            this.soDienThoai = sdt;
      }

      public String toString(){
            return this.soDinhDanh + "," + this.hoTen + "," + this.gioiTinh + "," + this.ngayThangNamSinh + "," + this.diaChi + "," + this.soDienThoai;
      }

}
