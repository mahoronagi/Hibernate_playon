package entity;

import javax.persistence.*;

@Entity @Table(name="api_packet")
public class PackagesCH {
    
        @Id @Column(name="api_packet_id") @GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	@Column(name="api_packet_name")
	String namech;
        @Column(name="api_packet_channel_id")
        String pc_ch;
        
        public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getNameCH() { return namech; }
	public void setNameCH(String namech) { this.namech = namech; }
        public String getPcCH() { return pc_ch; }
	public void setPcCH(String pc_ch) { this.pc_ch = pc_ch; }
        
        public PackagesCH() {}
}
