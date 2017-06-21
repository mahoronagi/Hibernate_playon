package entity;

import javax.persistence.*;

@Entity @Table(name="channel")
public class Channel {
        @Id @Column(name="channel_id") @GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
        @Column(name="channel_cat_id")
	Long cat_id;
	@Column(name="channel_name_en")
	String name_en;
        @Column(name="channel_name_th")
	String name_th;
        @Column(name="channel_name_cn")
	String name_cn;
        @Column(name="channel_img")
	String img;
        @Column(name="channel_ch")
	String ch;
        @Column(name="channel_url_stream")
	String url_stream;
        @Column(name="channel_language")
	String language;
        
        public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
        public Long getCatId() { return cat_id; }
	public void setCatId(Long cat_id) { this.cat_id = cat_id; }
	public String getNameEN() { return name_en; }
	public void setNameEN(String name_en) { this.name_en = name_en; }
        public String getNameTH() { return name_th; }
	public void setNameTH(String name_th) { this.name_th = name_th; }
        public String getNameCN() { return name_cn; }
	public void setNameCN(String name_cn) { this.name_cn = name_cn; }
        public String getImg() { return img; }
	public void setImg(String img) { this.img = img; }
        public String getCH() { return ch; }
	public void setCH(String ch) { this.ch = ch; }
        public String getUrlStream() { return url_stream; }
	public void setUrlStream(String url_stream) { this.url_stream = url_stream; }
        public String getLanguage() { return language; }
	public void setLanguage(String language) { this.language = language; }
        
        public Channel() {}
        
}