package entity;
import javax.persistence.*;

@Entity @Table(name="matches")
public class Schedules {
        @Id  @Column(name="match_id") @GeneratedValue(strategy=GenerationType.IDENTITY)
        int match_id;
        @Column(name="sport_id")
        int sport_id;
        @Column(name="sport_name_en")
        String sport_name_en;
        @Column(name="sport_name_th")
        String sport_name_th;
        @Column(name="sport_name_cn")
        String sport_name_cn;
        @Column(name="sport_img")
        String sport_img;
        
        @Column(name="league_home_id")
        int league_home_id;
        @Column(name="team_home_id")
        int team_home_id;
        @Column(name="team_home_name_en")
        String team_home_name_en;
        @Column(name="team_home_name_th")
        String team_home_name_th;
        @Column(name="team_home_name_cn")
        String team_home_name_cn;
        @Column(name="team_home_img")
        String team_home_img;
        
        @Column(name="league_visitor_id")
        int league_visitor_id;
        @Column(name="team_visitor_id")
        int team_visitor_id;
        @Column(name="team_visitor_name_en")
        String team_visitor_name_en;
        @Column(name="team_visitor_name_th")
        String team_visitor_name_th;
        @Column(name="team_visitor_name_cn")
        String team_visitor_name_cn;
        @Column(name="team_visitor_img")
        String team_visitor_img;
        
        @Column(name="league_id")
        int league_id;
        @Column(name="league_name_en")
        String league_name_en;
        @Column(name="league_name_th")
        String league_name_th;
        @Column(name="league_name_cn")
        String league_name_cn;
        @Column(name="league_img")
        String league_img;
        
        @Column(name="channel_id")
        String channel_id;
        @Column(name="language")
        String language;
        @Column(name="match_time")
        String match_time;
        @Column(name="time_created")
        String time_created;
        @Column(name="created_by")
        String created_by;
        
        public int getId() { return match_id; }
	public void setId(int match_id) { this.match_id = match_id; }
        public int getSportId() { return sport_id; }
	public void setSportId(int sport_id) { this.sport_id = sport_id; }
	public String getSportNameEN() { return sport_name_en; }
	public void setSportNameEN(String sport_name_en) { this.sport_name_en = sport_name_en; }
        public String getSportNameTH() { return sport_name_th; }
	public void setSportNameTH(String sport_name_th) { this.sport_name_th = sport_name_th; }
        public String getSportNameCN() { return sport_name_cn; }
	public void setSportNameCN(String sport_name_cn) { this.sport_name_cn = sport_name_cn; }
        public String getSportImg() { return sport_img; }
	public void setSportImg(String sport_img) { this.sport_img = sport_img; }
        
        public int getLeagueHomeId() { return league_home_id; }
	public void setLeagueHomeId(int league_home_id) { this.league_home_id = league_home_id; }
        public int getTeamHomeId() { return team_home_id; }
	public void setTeamHomeId(int team_home_id) { this.team_home_id = team_home_id; }
	public String getTeamHomeNameEN() { return team_home_name_en; }
	public void setTeamHomeNameEN(String team_home_name_en) { this.team_home_name_en = team_home_name_en; }
        public String getTeamHomeNameTH() { return team_home_name_th; }
	public void setTeamHomeNameTH(String team_home_name_th) { this.team_home_name_th = team_home_name_th; }
        public String getTeamHomeNameCN() { return team_home_name_cn; }
	public void setTeamHomeNameCN(String team_home_name_cn) { this.team_home_name_cn = team_home_name_cn; }
        public String getTeamHomeImg() { return team_home_img; }
	public void setTeamHomeImg(String team_home_img) { this.team_home_img = team_home_img; }
        
        public int getLeagueVisitorId() { return league_visitor_id; }
	public void setLeagueVisitorId(int league_visitor_id) { this.league_visitor_id = league_visitor_id; }
        public int getTeamVisitorId() { return team_visitor_id; }
	public void setTeamVisitorId(int team_visitor_id) { this.team_visitor_id = team_visitor_id; }
	public String getTeamVisitorNameEN() { return team_visitor_name_en; }
	public void setTeamVisitorNameEN(String team_visitor_name_en) { this.team_visitor_name_en = team_visitor_name_en; }
        public String getTeamVisitorNameTH() { return team_visitor_name_th; }
	public void setTeamVisitorNameTH(String team_visitor_name_th) { this.team_visitor_name_th = team_visitor_name_th; }
        public String getTeamVisitorNameCN() { return team_visitor_name_cn; }
	public void setTeamVisitorNameCN(String team_visitor_name_cn) { this.team_visitor_name_cn = team_visitor_name_cn; }
        public String getTeamVisitorImg() { return team_visitor_img; }
	public void setTeamVisitorImg(String team_visitor_img) { this.team_visitor_img = team_visitor_img; }
        
        public int getLeagueId() { return league_id; }
	public void setLeagueId(int league_id) { this.league_id = league_id; }
	public String getLeagueNameEN() { return league_name_en; }
	public void setLeagueNameEN(String league_name_en) { this.league_name_en = league_name_en; }
        public String getLeagueNameTH() { return league_name_th; }
	public void setLeagueNameTH(String league_name_th) { this.league_name_th = league_name_th; }
        public String getLeagueNameCN() { return league_name_cn; }
	public void setLeagueNameCN(String league_name_cn) { this.league_name_cn = league_name_cn; }
        public String getLeagueImg() { return league_img; }
	public void setLeagueImg(String league_img) { this.league_img = league_img; }
        
        public String getChannelId() { return channel_id; }
	public void setChannelId(String channel_id) { this.channel_id = channel_id; }
        public String getLanguage() { return language; }
	public void setLanguage(String language) { this.language = language; }
        public String getMatchTime() { return match_time; }
	public void setMatchTime(String match_time) { this.match_time = match_time; }
        public String getCreateBy() { return created_by; }
	public void setCreateBy(String created_by) { this.created_by = created_by; }
        public String getTimeCreate() { 
            String[] datevalue = time_created.split(" ");
            return datevalue[0]; 
        }
        public void setTimeCreate(String time_created) { this.time_created = time_created; }
        
        public Schedules(){}
        
}
