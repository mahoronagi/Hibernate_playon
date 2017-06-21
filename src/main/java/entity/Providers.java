package entity;

import javax.persistence.*;

@Entity @Table(name="customer")
public class Providers {
        @Id  @Column(name="customer_id") @GeneratedValue(strategy=GenerationType.IDENTITY)
        Long id;
        @Column(name="customer_api_packet_id")
        int api_package;
        @Column(name="customer_name")
        String name;
        @Column(name="customer_domain")
        String domain;
        @Column(name="customer_month")
        int month;
        @Column(name="customer_create")//datetime
        String create;
        @Column(name="customer_expire")//datetime
        String expire;
        @Column(name="customer_type")
        String type;
        @Column(name="customer_ip_host")
        String iphost;
        
        
        public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
        
        public int getPackageId() { return api_package; }
	public void setPackageId(int api_package) { this.api_package = api_package; }
        
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
        
        public String getDomain() { return domain; }
	public void setDomain(String domain) { this.domain = domain; }
        
        public int getMonth() { return month; }
	public void setMonth(int month) { this.month = month; }
        
        public String getTimeCreate() { 
            String[] datevalue = create.split(" ");
            return datevalue[0]; 
        }
	public void setTimeCreate(String create) { this.create = create; }
        
        public String getTimeExpire() { 
            String[] datevalue = expire.split(" ");
            return datevalue[0]; 
        }
	public void setTimeExpire(String expire) { this.expire = expire; }
        
        public String getType() { return type; }
	public void setType(String type) { this.type = type; }
        
        public String getIpHost() { return iphost; }
	public void setIpHost(String iphost) { this.iphost = iphost; }
        
        public Providers(){}
}
