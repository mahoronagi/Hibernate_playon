package web;

import tool.DateCounterMatch;
import java.sql.*;
import java.io.*;
import java.util.*;
import java.text.*;
import javax.sql.*;
import org.hibernate.*;
import org.hibernate.cfg.*;
import javax.servlet.http.*;
import org.springframework.ui.*;
import org.springframework.boot.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.multipart.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.scheduling.annotation.Scheduled;

import entity.*;
import tool.*;

@Controller
public class Main {

    @Value("${my.upload.path}")
    String uploadPath;
    SessionFactory factory = new Configuration()
            .addPackage("entity")
            .addAnnotatedClass(Member.class)
            .addAnnotatedClass(Channel.class)
            .addAnnotatedClass(ChannelType.class)
            .addAnnotatedClass(Leagues.class)
            .addAnnotatedClass(PackagesCH.class)
            .addAnnotatedClass(Teams.class)
            .addAnnotatedClass(Providers.class)
            .addAnnotatedClass(Sports.class)
            .addAnnotatedClass(Schedules.class)
            .buildSessionFactory();

    @RequestMapping("/logout")
    String logout(HttpSession session) {
        session.removeAttribute("member");
        session.invalidate();
        return "logout";
    }

    @RequestMapping("/")
    String showHome(HttpSession session, Model model) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("i", session.getAttribute("member"));
            return "index";
        }
    }

    @RequestMapping("/index")
    String showHomeIndex(HttpSession session, Model model) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("i", session.getAttribute("member"));
            return "index";
        }
    }

    @RequestMapping("/login")
    String showLogin() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    String login(HttpSession session, String username, String password) {
        Session database = factory.openSession();
        org.hibernate.Query query = database.createQuery(
                "from Member where member_username = :e and member_password = :p");
        query.setParameter("e", username);
        query.setParameter("p", password);
        List list = query.list();
        if (list.size() == 1) {
            Member member = (Member) list.get(0);
            session.setAttribute("member", member);
            database.close();
            return "redirect:/index";
        } else {
            return "login";
        }
    }

    @RequestMapping("/profile")
    String Profile(HttpSession session, Model model) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("user", session.getAttribute("member"));
            model.addAttribute("i", session.getAttribute("member"));
            return "profile";
        }
    }

    @RequestMapping("/view/{id}")
    String view(HttpSession session, Model model, @PathVariable long id) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Member member = new Member();
            Session database = factory.openSession();
            member = (Member) database.get(Member.class, id);
            database.close();
            model.addAttribute("user", member);
            model.addAttribute("i", session.getAttribute("member"));
            return "/view";
        }
    }

    @RequestMapping("/edit")
    String Edit(HttpSession session, Model model) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("i", session.getAttribute("member"));
            return "edit";
        }
    }

    @RequestMapping("/edit/{id}")
    String Edit(HttpSession session, Model model, @PathVariable long id) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("i", session.getAttribute("member"));
            return "edit";
        }
    }
    
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    String Edit(HttpSession session, Model model, String password, String newpass, @PathVariable long id) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Session database = factory.openSession();
            Query query = database.createQuery("update Member set member_password = :pass where member_id = :id");
            query.setParameter("id", id);
            query.setParameter("pass", newpass);
            query.executeUpdate();
            database.flush();
            database.close();  
            model.addAttribute("i", session.getAttribute("member"));
            return "redirect:/admin";
        }      
    }
    
    @RequestMapping("/edit_profile")
    String EditProfile(HttpSession session, Model model) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("i", session.getAttribute("member"));
            return "edit";
        }
    }

    @RequestMapping(value = "/edit_profile", method = RequestMethod.POST)
    String EditProfile(HttpSession session, Model model, String password, String newpass) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Member member = (Member) session.getAttribute("member");
            if (member.password != password) {
                member.setPassword(newpass);
                session.setAttribute("member_password", member);
                Session database = factory.openSession();
                database.update(member);
                database.flush();
                database.close();
                return "redirect:/profile";
            } 
            return "edit_profile";
        }      
    }

    @RequestMapping("/admin")
    String showAdmin(HttpSession session, Model model) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Session database = factory.openSession();
            List list = database.createQuery("from Member").list();
            database.close();
            model.addAttribute("members", list);
            model.addAttribute("i", session.getAttribute("member"));
            return "/admin";
        }
    }

    @RequestMapping("/newadmin")
    String AddNewMember(HttpSession session, Model model) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("i", session.getAttribute("member"));
            return "/newadmin";
        }
    }

    @RequestMapping(value = "/newadmin", method = RequestMethod.POST)
    String register(HttpSession session, Model model, HttpServletRequest request, String name, String username, String password, 
                    String type, int Admin, int Provider, int Schedule) {
        
        Member member = new Member();
        FormatDate d = new FormatDate();
        long n = 0;
        member.setName(name);
        member.setUserName(username);
        member.setPassword(password);
        member.setIpClient(request.getRemoteAddr());
        member.setTimeLogin(d.CurrentDate());
        member.setType(type);
        member.setSessionId(request.getSession().getId());
        member.setLastActivity(n);
        member.setRoleAdmin(Admin);
        member.setRoleProvider(Provider);
        member.setRoleSchedule(Schedule);
                
        Session database = factory.openSession();
        database.save(member);
        database.flush();
        database.close();
        model.addAttribute("i", session.getAttribute("member"));
        return "redirect:/admin";
        
    }

    @RequestMapping("/provider")
    String showProvider(HttpSession session, Model model) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Session database = factory.openSession();
            List list = database.createQuery("from Providers").list();
            database.close();
            model.addAttribute("provider", list);
            model.addAttribute("i", session.getAttribute("member"));
            return "/provider";
        }
    }

    @RequestMapping("/view_provider/{id}")
    String viewProvider(HttpSession session, Model model, @PathVariable long id) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Providers providers = new Providers();
            Session database = factory.openSession();
            providers = (Providers) database.get(Providers.class, id);
            database.close();
            model.addAttribute("provider", providers);
            model.addAttribute("i", session.getAttribute("member"));
            return "/view_provider";
        }
    }

    @RequestMapping("/edit_provider/{id}")
    String EditProvider(HttpSession session, Model model, @PathVariable long id) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Providers providers = new Providers();
            Session database = factory.openSession();
            providers = (Providers) database.get(Providers.class, id);
            database.close();
            model.addAttribute("provider", providers);
            model.addAttribute("i", session.getAttribute("member"));
            return "edit_provider";
        }
    }

    @RequestMapping(value = "/edit_provider/{id}", method = RequestMethod.POST)
    String EditProvider(HttpSession session, Model model,@PathVariable long id, String provider_name, String domainpv, String iphost,
                        String p_package, String type, int p_package_time) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            FormatDate d = new FormatDate();
            Session database = factory.openSession();
            Query query = database.createQuery("update Providers set "
                    + " customer_name = :n ,"
                    + " customer_domain = :d ,"
                    + " customer_month = :m ,"
                    + " customer_type = :t ,"
                    + " customer_create = :cur ,"
                    + " customer_expire = :exp ,"
                    + " customer_ip_host = :i "
                    + " where customer_id = :id ");
            query.setParameter("id", id);
            query.setParameter("n", provider_name);
            query.setParameter("d", domainpv);
            query.setParameter("m", p_package_time / 30);
            query.setParameter("t", type);
            query.setParameter("i", iphost);
            query.setParameter("cur", d.CurrentDate());
            query.setParameter("exp", d.SumDate(p_package_time));
            query.executeUpdate();
            database.flush();
            database.close();  
            return "redirect:/provider";
        }
    }

    @RequestMapping("/newprovider")
    String NewProvider(HttpSession session, Model model) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("i", session.getAttribute("member"));
            return "newprovider";
        }
    }

    @RequestMapping(value = "/newprovider", method = RequestMethod.POST)
    String NewProvider(String provider_name, String domain, String iphost,String p_package, String type, int p_package_time) {
        Providers provider = new Providers();
        FormatDate d = new FormatDate();
        provider.setName(provider_name);
        provider.setDomain(domain);
        provider.setPackageId(0);
        provider.setMonth(p_package_time / 30);
        provider.setIpHost(iphost);
        provider.setTimeCreate(d.CurrentDate());
        provider.setTimeExpire(d.SumDate(p_package_time));
        provider.setType(type);
        Session database = factory.openSession();
        database.save(provider);
        database.flush();
        database.close();

        return "redirect:/provider";
    }

    /////////////////////////////////////////////////////////////////////////
    @RequestMapping("/package")
    String ShowPackage(HttpSession session, Model model) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Session database = factory.openSession();
            List list = database.createQuery("from PackagesCH").list();
            database.close();
            model.addAttribute("packs", list);
            model.addAttribute("i", session.getAttribute("member"));
            return "package";
        }
    }

    @RequestMapping("/add_package")
    String addPackage(HttpSession session, Model model) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Session database = factory.openSession();
            List list = database.createQuery("from Channel").list();
            database.close();
            model.addAttribute("channel", list);
            model.addAttribute("i", session.getAttribute("member"));
            return "add_package";
        }
    }
    
    @RequestMapping(value = "/add_package", method = RequestMethod.POST)
    String addPackage(HttpSession session, Model model,String package_name, String pg_ch) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            PackagesCH packageCH = new PackagesCH();
            packageCH.setNameCH(package_name);
            packageCH.setPcCH(pg_ch);
            Session database = factory.openSession();
            database.save(packageCH);
            database.flush();
            database.close();
            return "redirect:/package";
        }
    }
    
    @RequestMapping("/edit_package/{id}")
    String editPackage(HttpSession session, Model model, @PathVariable long id) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            PackagesCH pack = new PackagesCH();
            Session database = factory.openSession();
            pack = (PackagesCH) database.get(PackagesCH.class, id);
            List list = database.createQuery("from Channel").list();
            database.close();
            model.addAttribute("channel", list);
            model.addAttribute("pack", pack);
            model.addAttribute("i", session.getAttribute("member"));
            return "edit_package";
        }
    }

    @RequestMapping(value = "/edit_package/{id}", method = RequestMethod.POST)
    String editPackage(HttpSession session, Model model, @PathVariable long id, String package_name, String pg_ch) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            //PackagesCH pack = new PackagesCH();
            Session database = factory.openSession();
            //pack = (PackagesCH) database.get(PackagesCH.class, id);
            Query query = database.createQuery("update PackagesCH set "
                        + " api_packet_name = :pn ,"
                        + " api_packet_channel_id = :pc "
                        + " where api_packet_id = :id ");
            query.setParameter("id", id);
            query.setParameter("pn", package_name);
            query.setParameter("pc", pg_ch);
            query.executeUpdate();
            database.flush();
            database.close();  
            return "redirect:/package";
        }
    }
        
    ////////////////////////////////////////////////////////////////////////
    @RequestMapping("/channels")
    String showChannels(HttpSession session, Model model) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Session database = factory.openSession();
            List list = database.createQuery("from Channel  order by channel_name_en").list();
            database.close();
            model.addAttribute("channel", list);
            model.addAttribute("i", session.getAttribute("member"));
            return "/channels";
        }
    }

    @RequestMapping("/add_channel")
    String addChannel(HttpSession session, Model model) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Session database = factory.openSession();
            List list = database.createQuery("from ChannelType  order by channel_name_en").list();
            database.close();
            model.addAttribute("cat", list);
            model.addAttribute("i", session.getAttribute("member"));
            return "/add_channel";
        }
    }

    @RequestMapping(value = "/add_channel", method = RequestMethod.POST)
    String addChannel(HttpSession session, Model model, String channel_name_en, String channel_name_th,
            String channel_name_cn, String ch, String url, MultipartFile Image, String ch_language, long ch_type) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            String filename = "unknown.jpg";
            if (!Image.isEmpty()) {
                String[] tokens = Image.getOriginalFilename().split("\\.");
                String fileType = tokens[tokens.length - 1];
                filename = UUID.randomUUID() + "." + fileType;
                try {
                    byte[] bytes = Image.getBytes();
                    BufferedOutputStream stream = new BufferedOutputStream(
                            new FileOutputStream(uploadPath + "/" + filename)
                    );
                    stream.write(bytes);
                    stream.close();
                } catch (Exception e) {
                }
            }
            Channel channel = new Channel();
            channel.setNameEN(channel_name_en);
            channel.setNameTH(channel_name_th);
            channel.setNameCN(channel_name_cn);
            channel.setCH(ch);
            channel.setUrlStream(url);
            channel.setCatId(ch_type);
            channel.setLanguage(ch_language);
            channel.setImg(filename);
            Session database = factory.openSession();
            database.save(channel);
            database.flush();
            database.close();
            return "redirect:/channels";
        }
    }

    @RequestMapping("/edit_channel/{id}")
    String editChannel(HttpSession session, Model model, @PathVariable long id) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Channel channel = new Channel();
            Session database = factory.openSession();
            channel = (Channel) database.get(Channel.class, id);
            List list = database.createQuery("from ChannelType  order by channel_cat_name").list();
            database.close();
            model.addAttribute("cat", list);
            model.addAttribute("channel", channel);
            model.addAttribute("i", session.getAttribute("member"));
            return "edit_channel";
        }
    }

    @RequestMapping(value = "/edit_channel/{id}", method = RequestMethod.POST)
    String editChannel(HttpSession session, Model model, @PathVariable long id, String channel_name_en, String channel_name_th, 
                        String channel_name_cn, String ch, String url, MultipartFile Image, String ch_language, long ch_type) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            String filename = "unknown.jpg";
            String str = "";
            Channel channel = new Channel();
            Session database = factory.openSession();
            channel = (Channel) database.get(Channel.class, id);
            if (!Image.getOriginalFilename().equals(channel.getImg()) && !Image.isEmpty()) {
                String[] tokens = Image.getOriginalFilename().split("\\.");
                String fileType = tokens[tokens.length - 1];
                filename = UUID.randomUUID() + "." + fileType;
                str = " channel_img = :img ,";
                try {
                    byte[] bytes = Image.getBytes();
                    BufferedOutputStream stream = new BufferedOutputStream(
                            new FileOutputStream(uploadPath + "/" + filename)
                    );
                    stream.write(bytes);
                    stream.close();
                } catch (Exception e) { System.out.println(e); }
            }
            Query query = database.createQuery("update Channel set "
                        + " channel_cat_id = :ci ,"
                        + " channel_language = :lg ,"
                        + " channel_name_en = :en ,"
                        + " channel_name_th = :th ,"
                        + " channel_name_cn = :cn ,"
                        + str
                        + " channel_ch = :ch ,"
                        + " channel_url_stream = :url "
                        + " where channel_id = :id ");
            query.setParameter("id", id);
            query.setParameter("ci", ch_type);
            query.setParameter("lg", ch_language);
            query.setParameter("en", channel_name_en);
            query.setParameter("th", channel_name_th);
            query.setParameter("cn", channel_name_cn);
            if(!str.equals("")){ query.setParameter("img", filename); }
            query.setParameter("ch", ch);
            query.setParameter("url", url);
            query.executeUpdate();
            database.flush();
            database.close();  
            model.addAttribute("i", session.getAttribute("member"));
            return "redirect:/channels";
        }
    }

    @RequestMapping("/sports")
    String ShowSports(HttpSession session, Model model) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Session database = factory.openSession();
            List list = database.createQuery("from Sports ").list();
            database.close();
            model.addAttribute("sports", list);
            model.addAttribute("i", session.getAttribute("member"));
            return "/sports";
        }
    }

    @RequestMapping("/new_sport")
    String addSport(HttpSession session, Model model) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("i", session.getAttribute("member"));
            return "/new_sport";
        }
    }

    @RequestMapping(value = "/new_sport", method = RequestMethod.POST)
    String addSport(HttpSession session, Model model, String sport_en, String sport_th,
            String sport_cn, MultipartFile Image) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            String filename = "unknown.jpg";
            if (!Image.isEmpty()) {
                String[] tokens = Image.getOriginalFilename().split("\\.");
                String fileType = tokens[tokens.length - 1];
                filename = UUID.randomUUID() + "." + fileType;
                try {
                    byte[] bytes = Image.getBytes();
                    BufferedOutputStream stream = new BufferedOutputStream(
                            new FileOutputStream(uploadPath + "/" + filename)
                    );
                    stream.write(bytes);
                    stream.close();
                } catch (Exception e) {
                }
            }
            Sports sports = new Sports();
            sports.setNameEN(sport_en);
            sports.setNameTH(sport_th);
            sports.setNameCN(sport_cn);
            sports.setImg(filename);
            Session database = factory.openSession();
            database.save(sports);
            database.flush();
            database.close();
            return "redirect:/sports";
        }
    }

    @RequestMapping("/edit_sport/{id}")
    String editSport(HttpSession session, Model model, @PathVariable int id) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Sports sports = new Sports();
            Session database = factory.openSession();
            sports = (Sports) database.get(Sports.class, id);
            database.close();
            model.addAttribute("sports", sports);
            model.addAttribute("i", session.getAttribute("member"));
            return "edit_sport";
        }
    }

    @RequestMapping(value = "/edit_sport/{id}", method = RequestMethod.POST)
    String editSport(HttpSession session, Model model, @PathVariable int id, String sport_en,
            String sport_th, String sport_cn, MultipartFile Image) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            String filename = "unknown.jpg";
            String str = "";
            Sports sports = new Sports();
            Session database = factory.openSession();
            sports = (Sports) database.get(Sports.class, id);
            if (!Image.getOriginalFilename().equals(sports.getImg()) && !Image.isEmpty()) {
                String[] tokens = Image.getOriginalFilename().split("\\.");
                String fileType = tokens[tokens.length - 1];
                filename = UUID.randomUUID() + "." + fileType;
                str = " sport_img = :img ,";
                try {
                    byte[] bytes = Image.getBytes();
                    BufferedOutputStream stream = new BufferedOutputStream(
                            new FileOutputStream(uploadPath + "/" + filename)
                    );
                    stream.write(bytes);
                    stream.close();
                } catch (Exception e) { System.out.println(e); }
            }
            Query query = database.createQuery("update Sports set "
                        + str
                        + " sport_name_en = :en ,"
                        + " sport_name_th = :th ,"
                        + " sport_name_cn = :cn "
                        + " where sport_id = :id ");
            query.setParameter("id", id);
            query.setParameter("en", sport_en);
            query.setParameter("th", sport_th);
            query.setParameter("cn", sport_cn);
            if(!str.equals("")){ query.setParameter("img", filename); }
            query.executeUpdate();
            database.flush();
            database.close();  
            return "redirect:/sports";
        }
    }

    
    @RequestMapping("/leagues")
    String ShowLeagues(HttpSession session, Model model) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Session database = factory.openSession();
            List listLG = database.createQuery("from Leagues order by sport_id , league_name_en").list();
            database.close();
            model.addAttribute("lg", listLG);
            model.addAttribute("i", session.getAttribute("member"));
            return "/leagues";
        }
    }

    @RequestMapping("/new_leagues")
    String addLeague(HttpSession session, Model model) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Session database = factory.openSession();
            List list = database.createQuery("from Sports ").list();
            database.close();
            model.addAttribute("sps", list);
            model.addAttribute("i", session.getAttribute("member"));
            return "/new_leagues";
        }
    }

    @RequestMapping(value = "/new_leagues", method = RequestMethod.POST)
    String addLeague(HttpSession session, Model model,int sp_id, String league_en, String league_th,
            String league_cn, MultipartFile Image) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            String filename = "unknown.jpg";
            if (!Image.isEmpty()) {
                String[] tokens = Image.getOriginalFilename().split("\\.");
                String fileType = tokens[tokens.length - 1];
                filename = UUID.randomUUID() + "." + fileType;
                try {
                    byte[] bytes = Image.getBytes();
                    BufferedOutputStream stream = new BufferedOutputStream(
                            new FileOutputStream(uploadPath + "/" + filename)
                    );
                    stream.write(bytes);
                    stream.close();
                } catch (Exception e) {
                }
            }
            Leagues leagues = new Leagues();
            leagues.setSportId(sp_id);
            leagues.setNameEN(league_en);
            leagues.setNameTH(league_th);
            leagues.setNameCN(league_cn);
            leagues.setImg(filename);
            Session database = factory.openSession();
            database.save(leagues);
            database.flush();
            database.close();
            return "redirect:/leagues";
        }
    }

    @RequestMapping("/edit_leagues/{id}")
    String editLeagues(HttpSession session, Model model, @PathVariable int id) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Leagues leagues = new Leagues();
            Session database = factory.openSession();
            leagues = (Leagues) database.get(Leagues.class, id);
            List listSP = database.createQuery("from Sports orger by sport_name_en").list();
            database.close();
            model.addAttribute("leagues", leagues);
            model.addAttribute("sports", listSP);
            model.addAttribute("i", session.getAttribute("member"));
            return "edit_leagues";
        }
    }

    @RequestMapping(value = "/edit_leagues/{id}", method = RequestMethod.POST)
    String editLeagues(HttpSession session, Model model, @PathVariable int id, long sp_id, 
            String league_en, String league_th, String league_cn, MultipartFile Image) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            String filename = "unknown.jpg";
            String str = "";
            Leagues leagues = new Leagues();
            Session database = factory.openSession();
            leagues = (Leagues) database.get(Leagues.class, id);
            if (!Image.getOriginalFilename().equals(leagues.getImg()) && !Image.isEmpty()) {
                String[] tokens = Image.getOriginalFilename().split("\\.");
                String fileType = tokens[tokens.length - 1];
                filename = UUID.randomUUID() + "." + fileType;
                str = " league_img = :img ,";
                try {
                    byte[] bytes = Image.getBytes();
                    BufferedOutputStream stream = new BufferedOutputStream(
                            new FileOutputStream(uploadPath + "/" + filename)
                    );
                    stream.write(bytes);
                    stream.close();
                } catch (Exception e) { System.out.println(e); }
            }
            Query query = database.createQuery("update Leagues set "
                        + str
                        + " sport_id = :si ,"
                        + " league_name_en = :en ,"
                        + " league_name_th = :th ,"
                        + " league_name_cn = :cn "
                        + " where league_id = :id ");
            query.setParameter("id", id);
            query.setParameter("si", sp_id);
            query.setParameter("en", league_en);
            query.setParameter("th", league_th);
            query.setParameter("cn", league_cn);
            if(!str.equals("")){ query.setParameter("img", filename); }
            query.executeUpdate();
            database.flush();
            database.close(); 
            return "redirect:/leagues";
        }
    }

    @RequestMapping("/teams")
    String ShowTeams(HttpSession session, Model model) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Session database = factory.openSession();
            List list = database.createQuery("from Teams order by league_id , team_name_en ").list();
            database.close();
            model.addAttribute("teams", list);
            model.addAttribute("i", session.getAttribute("member"));
            return "/teams";
        }
    }
    
    @RequestMapping("/teams/{id}")
    String ShowTeams(HttpSession session, Model model, @PathVariable String id) throws ParseException {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            NumberFormat format = NumberFormat.getInstance(Locale.US);    
            Number number = format.parse(id);
            Session database = factory.openSession();
            Teams team = new Teams();
            team = (Teams) database.get(Teams.class, number.intValue());
            List list = database.createQuery("from Teams where league_id = "+number.toString()+" order by team_name_en ").list();
            database.close();
            model.addAttribute("teams", list);
            model.addAttribute("i", session.getAttribute("member"));
            return "/teams";
        }
    }

    @RequestMapping("/new_team")
    String addTeam(HttpSession session, Model model) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Session database = factory.openSession();
            List list = database.createQuery("from Leagues order by league_name_en").list();
            database.close();
            model.addAttribute("lgs", list);
            model.addAttribute("i", session.getAttribute("member"));
            return "/new_team";
        }
    }

    @RequestMapping(value = "/new_team", method = RequestMethod.POST)
    String addTeam(HttpSession session, Model model,int lg_id, String team_en, String team_th,
            String team_cn, MultipartFile Image) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            String filename = "unknown.jpg";
            if (!Image.isEmpty()) {
                String[] tokens = Image.getOriginalFilename().split("\\.");
                String fileType = tokens[tokens.length - 1];
                filename = UUID.randomUUID() + "." + fileType;
                try {
                    byte[] bytes = Image.getBytes();
                    BufferedOutputStream stream = new BufferedOutputStream(
                            new FileOutputStream(uploadPath + "/" + filename)
                    );
                    stream.write(bytes);
                    stream.close();
                } catch (Exception e) {
                }
            }
            Teams team = new Teams();
            team.setLeague(lg_id);
            team.setNameEN(team_en);
            team.setNameTH(team_th);
            team.setNameCN(team_cn);
            team.setImg(filename);
            Session database = factory.openSession();
            database.save(team);
            database.flush();
            database.close();
            return "redirect:/teams";
        }
    }

    @RequestMapping("/edit_team/{id}")
    String editTeam(HttpSession session, Model model, @PathVariable String id) throws ParseException {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            NumberFormat format = NumberFormat.getInstance(Locale.US);    
            Number number = format.parse(id);
            Teams team = new Teams();
            Session database = factory.openSession();
            team = (Teams) database.get(Teams.class, number.intValue());
            List listLg = database.createQuery("from Leagues  order by league_name_en").list();
            database.close();
            model.addAttribute("team", team);
            model.addAttribute("lgs", listLg);
            model.addAttribute("i", session.getAttribute("member"));
            return "edit_team";
        }
    }

    @RequestMapping(value = "/edit_team/{id}", method = RequestMethod.POST)
    String editTeam(HttpSession session, Model model, @PathVariable String id, int lg_id, 
            String team_en, String team_th, String team_cn, MultipartFile Image) throws ParseException {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            NumberFormat format = NumberFormat.getInstance(Locale.US);    
            Number number = format.parse(id);
            String filename = "unknown.jpg";
            String str = "";
            Teams teams = new Teams();
            Session database = factory.openSession();
            teams = (Teams) database.get(Teams.class, number.intValue());
            if (!Image.getOriginalFilename().equals(teams.getImg()) && !Image.isEmpty()) {
                String[] tokens = Image.getOriginalFilename().split("\\.");
                String fileType = tokens[tokens.length - 1];
                filename = UUID.randomUUID() + "." + fileType;
                str = " team_img = :img ,";
                try {
                    byte[] bytes = Image.getBytes();
                    BufferedOutputStream stream = new BufferedOutputStream(
                            new FileOutputStream(uploadPath + "/" + filename)
                    );
                    stream.write(bytes);
                    stream.close();
                } catch (Exception e) { System.out.println(e); }
            }
            Query query = database.createQuery("update Teams set "
                        + str
                        + " league_id = :si ,"
                        + " team_name_en = :en ,"
                        + " team_name_th = :th ,"
                        + " team_name_cn = :cn "
                        + " where team_id = :id ");
            query.setParameter("id", number.intValue());
            query.setParameter("si", lg_id);
            query.setParameter("en", team_en);
            query.setParameter("th", team_th);
            query.setParameter("cn", team_cn);
            if(!str.equals("")){ query.setParameter("img", filename); }
            query.executeUpdate();
            database.flush();
            database.close(); 
            return "redirect:/teams";
        }
    }

    //////////////////////////////////////////////////////////////////
    @RequestMapping("/schedule")
    String ShowSchedule(HttpSession session, Model model) throws ParseException {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            FormatDate cDate = new FormatDate();
            ArrayList<DateCounterMatch> week = new ArrayList<DateCounterMatch>();
            Session database = factory.openSession();
            String start = cDate.DateO(cDate.SumDate(-3));
            String end   = cDate.DateO(cDate.SumDate(5));
            //String start = "2016-03-1";
            //String end = "2016-03-9";
            String t = "";
            for (int i = 0; i <= 7; i++) {
                DateCounterMatch dd = new DateCounterMatch();
                t = cDate.SumDate(start, i);
                dd.setDateMatch(t);
                Query querya = database.createSQLQuery("select  count(*)as total  from matches where match_time LIKE :t");
                querya.setParameter("t", t + "%");
                List listM = querya.list();
                for (Object o : listM) {
                    dd.setCounter(o.toString());
                    week.add(dd);
                }
            }

            Query query = database.createQuery(" from Schedules where match_time between :start and :end  order by match_time ");
            query.setParameter("start", start);
            query.setParameter("end", end);
            List list = query.list();
            database.close();
            model.addAttribute("schedule", list);
            model.addAttribute("week", week);
            model.addAttribute("i", session.getAttribute("member"));
            return "/schedule";
        }
    }
    
    @RequestMapping("/matches")
    String ShowScheduleMatch(@RequestParam("update_date") String n_week, @RequestParam("date") String datem,
                             @RequestParam("update_type") String update,   Model model) throws ParseException, SQLException {
        FormatDate cDate = new FormatDate();
        ArrayList<DateCounterMatch> week = new ArrayList<DateCounterMatch>();
        String start;
        String end;
        Session database = factory.openSession();
        if(update.equals("next")){
            start = cDate.SumDate(datem,1);
            end   = cDate.SumDate(datem,9);
            //System.out.println("next" + start + "//"+ end);
        }else{
            start = cDate.SumDate(datem,-8);
            end= cDate.SumDate(datem,0);
            //System.out.println("prve= " + start + "//"+ end  );
        }
        //start = cDate.DateO(cDate.SumDate(-4));
        //end   = cDate.DateO(cDate.SumDate(4));
        //String start = "2016-03-1";
        //String end = "2016-03-8";
        String t = "";
        for (int i = 0; i <= 7; i++) {
            DateCounterMatch dd = new DateCounterMatch();
            t = cDate.SumDate(start, i);
            dd.setDateMatch(t);
            Query querya = database.createSQLQuery("select  count(*)as total  from matches where match_time LIKE :t");
            querya.setParameter("t", t + "%");
            List listM = querya.list();
            for (Object o : listM) {
                dd.setCounter(o.toString());
                week.add(dd);
            }
        }
        
        Query query = database.createQuery(" from Schedules where match_time between :start and :end  order by match_time ");
        query.setParameter("start", start);
        query.setParameter("end", end);
        List list = query.list();
        database.close();
        model.addAttribute("schedule", list);
        model.addAttribute("week", week);
        return "/matches";
    }
    
    @RequestMapping(value="/get_league_by_sport_id") 
    String getLeaguById(@RequestParam("sport_id") int id, Model model){
        Session database = factory.openSession();
        Query query = database.createQuery(" from Leagues where sport_id = :id ");
        query.setParameter("id", id);
        List list = query.list();
        database.close();
        model.addAttribute("leagues", list);
        return "/get_league_by_sport_id";
    }
    
    @RequestMapping(value="/get_team_by_league_id") 
    String getTeamsById(@RequestParam("league_id") int id, Model model){
        Session database = factory.openSession();
        Query query = database.createQuery(" from Teams where league_id = :id ");
        query.setParameter("id", id);
        List list = query.list();
        database.close();
        model.addAttribute("teams", list);
        return "/get_team_by_league_id";
    }
    
    @RequestMapping("/new_schedule")
    String addSchedule(HttpSession session, Model model) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Session database = factory.openSession();
            List list_a = database.createQuery("from Sports order by sport_name_en").list();
            List list_b = database.createQuery("from Leagues order by league_name_en").list();
            List list_c = database.createQuery("from Teams order by league_id , team_name_en ").list();
            List list_d = database.createQuery("from Channel order by channel_name_en").list();
            database.close();
            model.addAttribute("sp", list_a);
            model.addAttribute("lg", list_b);
            model.addAttribute("tm", list_c);
            model.addAttribute("ch", list_d);
            model.addAttribute("i", session.getAttribute("member"));
            return "/new_schedule";
        }
    }

    @RequestMapping(value = "/new_schedule", method = RequestMethod.POST)
    String addSchedule(HttpSession session, Model model, int sport_id , int league_id, int league_home_id, 
                        String team_home_id, int league_visitor_id, String team_visitor_id, 
                        @RequestParam("channel_id[]") List<String> channel_id , 
                        @RequestParam("channel_language[]") List<String> channel_language,
                        String match_time, String usercreate) throws ParseException {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        }else {
            NumberFormat format = NumberFormat.getInstance(Locale.US);    
            Number numberHometeam = format.parse(team_home_id);
            Number numberVisterteam = format.parse(team_visitor_id);
            String listIdCH = "";
            String listIdLG = "";
            Collections.sort(channel_id);
            for(int i=0; i < channel_id.size(); i++){
                if(i==0) listIdCH += channel_id.get(i);
                if(i>0 && !channel_id.get(i).equals(channel_id.get(i-1)) ) listIdCH += channel_id.get(i);
            }
            
            Collections.sort(channel_language);
            for(int i=0; i < channel_language.size(); i++){
                listIdLG += channel_language.get(i);
            }
            
            Session database = factory.openSession();
            Schedules sc = new Schedules();
            
            Sports sports = new Sports();
            sports = (Sports) database.get(Sports.class, sport_id);
            sc.setSportId(sport_id);
            sc.setSportNameEN(sports.getNameEN());
            sc.setSportNameTH(sports.getNameTH());
            sc.setSportNameCN(sports.getNameCN());
            sc.setSportImg(sports.getImg());
            
            Teams leagueH = new Teams();
            leagueH = (Teams) database.get(Teams.class, numberHometeam.intValue());
            sc.setLeagueHomeId(league_home_id);
            sc.setTeamHomeId(numberHometeam.intValue());
            sc.setTeamHomeNameEN(leagueH.getNameEN());
            sc.setTeamHomeNameTH(leagueH.getNameTH());
            sc.setTeamHomeNameCN(leagueH.getNameCN());
            sc.setTeamHomeImg(leagueH.getImg());
            
            Teams leagueV = new Teams();
            leagueV = (Teams) database.get(Teams.class, numberVisterteam.intValue());
            sc.setLeagueVisitorId(league_visitor_id);
            sc.setTeamVisitorId(numberVisterteam.intValue());
            sc.setTeamVisitorNameEN(leagueV.getNameEN());
            sc.setTeamVisitorNameTH(leagueV.getNameTH());
            sc.setTeamVisitorNameCN(leagueV.getNameCN());
            sc.setTeamVisitorImg(leagueV.getImg());
            
            Leagues league = new Leagues();
            league = (Leagues) database.get(Leagues.class, league_id);
            sc.setLeagueId(league_id);
            sc.setLeagueNameEN(league.getNameEN());
            sc.setLeagueNameTH(league.getNameTH());
            sc.setLeagueNameCN(league.getNameCN());
            sc.setLeagueImg(league.getImg());
            
            
            FormatDate d = new FormatDate();
            sc.setChannelId(listIdCH);
            sc.setLanguage(listIdLG);
            sc.setMatchTime(d.formatDate(match_time));
            sc.setCreateBy(usercreate);
            sc.setTimeCreate(d.CurrentDate());
            
            
            database.save(sc);
            database.flush();
            database.close();
            //return "#";
            return "redirect:/schedule";
        }
    }

    @RequestMapping("/edit_schedule/{id}")
    String editSchedule(HttpSession session, Model model, @PathVariable String id) throws ParseException {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            NumberFormat format = NumberFormat.getInstance(Locale.US);    
            Number number = format.parse(id);
            Schedules schedules = new Schedules();
            Session database = factory.openSession();
            schedules = (Schedules) database.get(Schedules.class, number.intValue());
            List list_a = database.createQuery("from Sports order by sport_name_en").list();
            
            Query query_b = database.createQuery("from Leagues where sport_id = :spid");
            query_b.setParameter("spid", schedules.getSportId());
            List list_b = query_b.list();
            
            Query query_h = database.createQuery("from Teams where league_id = :hid");
            query_h.setParameter("hid", schedules.getLeagueHomeId());
            List list_h = query_h.list();
            
            Query query_v = database.createQuery("from Teams where league_id = :vid");
            query_v.setParameter("vid", schedules.getLeagueHomeId());
            List list_v = query_v.list();
            
            List list_d = database.createQuery("from Channel").list();
            database.close();
            model.addAttribute("sp", list_a);
            model.addAttribute("lg", list_b);
            model.addAttribute("tmH", list_h);
            model.addAttribute("tmV", list_v);
            model.addAttribute("ch", list_d);
            model.addAttribute("schedules", schedules);
            model.addAttribute("i", session.getAttribute("member"));
            return "edit_schedule";
        }
    }

    @RequestMapping(value = "/edit_schedule/{id}", method = RequestMethod.POST)
    String editSchedule(HttpSession session, Model model, int sport_id , int league_id, int league_home_id, 
                        String team_home_id, int league_visitor_id, String team_visitor_id, 
                        @RequestParam("channel_id[]") List<String> channel_id , 
                        @RequestParam("channel_language[]") List<String> channel_language,
                        String match_time, String usercreate, @PathVariable String id) throws ParseException {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            //System.out.println(sport_id+"/"+league_id+"/"+league_home_id+"/"+team_home_id+"/"+league_visitor_id+"/"+team_visitor_id+"/"+
            //        channel_id+"/"+channel_language +"/"+match_time+"/"+usercreate+"/"+id);
            NumberFormat format = NumberFormat.getInstance(Locale.US);    
            Number numberHometeam = format.parse(team_home_id);
            Number numberVisterteam = format.parse(team_visitor_id);
            Number number = format.parse(id);
            String listIdCH = "";
            String listIdLG = "";
            String Mat = "";
            Collections.sort(channel_id);
            for(int i=0; i < channel_id.size(); i++){
                if(i==0) listIdCH += channel_id.get(i);
                if(i>0 && !channel_id.get(i).equals(channel_id.get(i-1)) ) listIdCH += channel_id.get(i);
            }
            
            Collections.sort(channel_language);
            for(int i=0; i < channel_language.size(); i++){
                listIdLG += channel_language.get(i);
            }
            if(!match_time.equals("")){
                Mat =  " match_time = :mt ,";
            }
            Session database = factory.openSession();
//            Schedules schedules = new Schedules();
//            schedules = (Schedules) database.get(Schedules.class, number.intValue());
            
            Query query = database.createQuery("update Schedules set "
                    + " sport_id = :spid,"
                    + " sport_name_en = :spen,"
                    + " sport_name_th = :spth,"
                    + " sport_name_cn = :spcn,"
                    + " sport_img = :spim,"
                    
                    + " league_home_id = :lhid,"
                    + " team_home_id = :thid,"
                    + " team_home_name_en = :thnen,"
                    + " team_home_name_th = :thnth,"
                    + " team_home_name_cn = :thncn,"
                    + " team_home_img = :thim,"
                    
                    + " league_visitor_id = :lvid,"
                    + " team_visitor_id = :tvid,"
                    + " team_visitor_name_en = :tvnen,"
                    + " team_visitor_name_th = :tvnth,"
                    + " team_visitor_name_cn = :tvncn,"
                    + " team_visitor_img = :tvim,"
                    
                    + " league_id = :lgig,"
                    + " league_name_en = :lgen,"
                    + " league_name_th = :lgth,"
                    + " league_name_cn = :lgcn,"
                    + " league_img = :lgim,"
                    + " channel_id = :chid,"
                    + " language = :lg,"
                    
                    + Mat
                    
                    + " time_created = :cret ,"
                    + " created_by = :by "
                    + " where match_id = :id ");
            
            Sports sports = new Sports();
            sports = (Sports) database.get(Sports.class, sport_id);
            query.setParameter("id", number.intValue());
            query.setParameter("spid", sport_id);
            query.setParameter("spen", sports.getNameEN());
            query.setParameter("spth", sports.getNameTH());
            query.setParameter("spcn", sports.getNameCN());
            query.setParameter("spim", sports.getImg());
            
            Teams leagueH = new Teams();
            leagueH = (Teams) database.get(Teams.class, numberHometeam.intValue());
            query.setParameter("lhid", league_home_id);
            query.setParameter("thid", numberHometeam.intValue());
            query.setParameter("thnen", leagueH.getNameEN());
            query.setParameter("thnth", leagueH.getNameTH());
            query.setParameter("thncn", leagueH.getNameCN());
            query.setParameter("thim", leagueH.getImg());
            
            Teams leagueV = new Teams();
            leagueV = (Teams) database.get(Teams.class, numberVisterteam.intValue());
            query.setParameter("lvid", league_visitor_id);
            query.setParameter("tvid", numberVisterteam.intValue());
            query.setParameter("tvnen", leagueV.getNameEN());
            query.setParameter("tvnth", leagueV.getNameTH());
            query.setParameter("tvncn", leagueV.getNameCN());
            query.setParameter("tvim", leagueV.getImg());
            
            Leagues league = new Leagues();
            league = (Leagues) database.get(Leagues.class, league_id);
            query.setParameter("lgig", league_id);
            query.setParameter("lgen", league.getNameEN());
            query.setParameter("lgth", league.getNameTH());
            query.setParameter("lgcn", league.getNameCN());
            query.setParameter("lgim", league.getImg());
            query.setParameter("chid", listIdCH);
            query.setParameter("lg", listIdLG);
            
            FormatDate d = new FormatDate();
            if(!match_time.equals("")){
            query.setParameter("mt", match_time);
            }
            query.setParameter("cret", d.CurrentDate());
            query.setParameter("by", usercreate);
            
            //System.out.println(query.getQueryString());
            query.executeUpdate();
            database.flush();
            database.close(); 
            return "redirect:/schedule";
        }
    }

    //////////////////////////////////////////////////////////////////

    @RequestMapping("/delete_admin/{id}")
    String deleteUser(Model model, @PathVariable long id) {
        Session database = factory.openSession();
        Query query = database.createQuery("delete Member where member_id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
        database.flush();
        database.close();
        return "redirect:/admin";
    }
    
    @RequestMapping("/delete_provider/{id}")
    String deleteProvider(Model model, @PathVariable long id) {
        Session database = factory.openSession();
        Query query = database.createQuery("delete Providers where customer_id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
        database.flush();
        database.close();
        return "redirect:/provider";
    }

    @RequestMapping("/delete_channel/{id}")
    String deleteChannel(Model model, @PathVariable long id) {
        Session database = factory.openSession();
        Query query = database.createQuery("delete Channel where channel_id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
        database.flush();
        database.close();
        return "redirect:/channels";
    }
    
    @RequestMapping("/delete_sport/{id}")
    String deleteSport(Model model, @PathVariable int id) {
        Session database = factory.openSession();
        Query query = database.createQuery("delete Sports where sport_id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
        database.flush();
        database.close();
        return "redirect:/sports";
    }
    
    @RequestMapping("/delete_team/{id}")
    String deleteTeam(Model model, @PathVariable String id) throws ParseException {
        NumberFormat format = NumberFormat.getInstance(Locale.US);    
        Number number = format.parse(id);
        Session database = factory.openSession();
        Query query = database.createQuery("delete Teams where team_id = :id");
        query.setParameter("id", number.intValue());
        query.executeUpdate();
        database.flush();
        database.close();
        return "redirect:/teams";
    }
    
    @RequestMapping("/delete_leagues/{id}")
    String deleteLeages(Model model, @PathVariable int id) {
        Session database = factory.openSession();
        Query query = database.createQuery("delete Leagues where league_id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
        database.flush();
        database.close();
        return "redirect:/leagues";
    }

    @RequestMapping("/delete_package/{id}")
    String deletePackage(Model model, @PathVariable String id) throws ParseException {
        NumberFormat format = NumberFormat.getInstance(Locale.US);    
        Number number = format.parse(id);
        Session database = factory.openSession();
        Query query = database.createQuery("delete PackagesCH where api_packet_id = :id");
        query.setParameter("id", number.intValue());
        query.executeUpdate();
        database.flush();
        database.close();
        return "redirect:/package";
    }
    
    @RequestMapping("/delete_schedule/{id}")
    String deleteSchedules(Model model, @PathVariable String id) throws ParseException {
        NumberFormat format = NumberFormat.getInstance(Locale.US);    
        Number number = format.parse(id);
        Session database = factory.openSession();
        Query query = database.createQuery("delete Schedules where match_id = :id");
        query.setParameter("id", number.intValue());
        query.executeUpdate();
        database.flush();
        database.close();
        return "redirect:/schedule";
    }
    
    String encode(String s) {
        String result = "";
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.
                    getInstance("SHA-256");
            byte[] hash = digest.digest(s.getBytes("UTF-8"));
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    result += '0';
                }
                result += hex;
            }
        } catch (Exception e) {
        }
        return result;
    }

    @RequestMapping("/get-user-list") @ResponseBody
    List getUserList() {
        FormatDate cDate = new FormatDate();
        //String start = cDate.DateO(cDate.SumDate(-4));
        //String end   = cDate.DateO(cDate.SumDate(4));
        String start = "2016-02-26";
        String end   = "2016-02-28";
        List list = new ArrayList();
        Session database = factory.openSession();
        Query query = database.createQuery(" from Schedules where match_time between :start and :end  order by match_time ");
        query.setParameter("start", start);
        query.setParameter("end",end);
        list = query.list();
        database.close();
        return list;
    }
    
    @RequestMapping("/newhtml") 
    String ShowHTML(HttpSession session, Model model) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            long id = 11;
            PackagesCH pack = new PackagesCH();
            Session database = factory.openSession();
            pack = (PackagesCH) database.get(PackagesCH.class, id);
            List list = database.createQuery("from Channel").list();
            database.close();
            model.addAttribute("channel", list);
            model.addAttribute("pack", pack);
            model.addAttribute("i", session.getAttribute("member"));
            return "/newhtml";
        }     
    }
}
