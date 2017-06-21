package entity;

import javax.persistence.*;

@Entity @Table(name="channel_cat")
public class ChannelType {
        @Id @Column(name="channel_cat_id") @GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	@Column(name="channel_cat_name")
	String name;
        
        public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
        
}
