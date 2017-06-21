package entity;

import javax.persistence.*;

@Entity @Table(name="sport")
public class Sports {
        @Id @Column(name="sport_id") @GeneratedValue(strategy=GenerationType.IDENTITY)
	int id;
	@Column(name="sport_name_en")
	String name_en;
        @Column(name="sport_name_th")
	String name_th;
        @Column(name="sport_name_cn")
	String name_cn;
        @Column(name="sport_img")
	String img;
        
        public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public String getNameEN() { return name_en; }
	public void setNameEN(String name_en) { this.name_en = name_en; }
        public String getNameTH() { return name_th; }
	public void setNameTH(String name_th) { this.name_th = name_th; }
        public String getNameCN() { return name_cn; }
	public void setNameCN(String name_cn) { this.name_cn = name_cn; }
        public String getImg() { return img; }
	public void setImg(String img) { this.img = img; }
        
        public Sports() {}
}
