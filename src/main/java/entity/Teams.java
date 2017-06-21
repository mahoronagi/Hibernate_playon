package entity;

import javax.persistence.*;

@Entity @Table(name="team")
public class Teams {
        @Id @Column(name="team_id") @GeneratedValue(strategy=GenerationType.IDENTITY)
	int id;
	@Column(name="team_name_en")
	String name_en;
        @Column(name="team_name_th")
	String name_th;
        @Column(name="team_name_cn")
	String name_cn;
        @Column(name="team_img")
	String img;
        @Column(name="league_id")
	int league_id;
        
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
        public int getLeague() { return league_id; }
	public void setLeague(int league_id) { this.league_id = league_id; }
        
        public Teams() {}
}
