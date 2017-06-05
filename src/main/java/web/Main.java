package web;

import java.sql.*;
import java.io.*;
import java.util.*;
import javax.sql.*;
import org.hibernate.*;
import org.hibernate.cfg.*;
import org.springframework.http.*;
import org.springframework.web.multipart.*;
import javax.servlet.http.*;
import org.springframework.ui.*;
import org.springframework.boot.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.*;

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
            .addAnnotatedClass(Leagues.class)
            .addAnnotatedClass(Packages.class)
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

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    String Edit(HttpSession session, Model model, String password, String newpass) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Member member = (Member) session.getAttribute("member");
            if (member.password != password && member.type != "admin") {
                member.setPassword(newpass);
                session.setAttribute("member_password", member);
                Session database = factory.openSession();
                database.update(member);
                database.flush();
                database.close();
                return "redirect:/profile/" + member.id;
            } else if (member.type == "admin") {
                member.setPassword(newpass);
                session.setAttribute("member_password", member);
                Session database = factory.openSession();
                database.update(member);
                database.flush();
                database.close();
                return "redirect:/admin";
            } else {
                return "redirect:/edit";
            }
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
            return "admin";
        }
    }

    @RequestMapping("/newadmin")
    String AddNewMember(HttpSession session, Model model) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("i", session.getAttribute("member"));
            return "newadmin";
        }
    }

    @RequestMapping(value = "/newadmin", method = RequestMethod.POST)
    String register(HttpServletRequest request, String name, String username, String password, String type) {
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
        Session database = factory.openSession();
        Query query = database.createQuery(
                "from Member where member_username = :e ");
        query.setParameter("e", username);
        List list = query.list();
        if (list.size() == 1) {
            member = (Member) list.get(0);
            database.save(member);
            database.flush();
            database.close();
            return "redirect:/newadmin";
        } else {
            return "/admin";
        }
    }

    @RequestMapping("/delete_admin/{id}")
    String view(Model model, @PathVariable long id) {
        Session database = factory.openSession();
        Query query = database.createQuery("delete Member where member_id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
        database.flush();
        database.close();
        return "redirect:/admin";
        //return list;
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

    @RequestMapping(value = "/edit_provider", method = RequestMethod.POST)
    String EditProvider(HttpSession session, Model model, String password, String newpass) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Member member = (Member) session.getAttribute("member");
            if (member.password != password && member.type != "admin") {
                member.setPassword(newpass);
                session.setAttribute("member_password", member);
                Session database = factory.openSession();
                database.update(member);
                database.flush();
                database.close();
                return "redirect:/profile/" + member.id;
            } else if (member.type == "admin") {
                member.setPassword(newpass);
                session.setAttribute("member_password", member);
                Session database = factory.openSession();
                database.update(member);
                database.flush();
                database.close();
                return "redirect:/provider";
            } else {
                return "redirect:/edit_provider";
            }
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
    String NewProvider(String provider_name, String domain, String ip,
            String url, String p_package, String type, int p_package_time) {
        Providers provider = new Providers();
        FormatDate d = new FormatDate();
        provider.setName(provider_name);
        provider.setDomain(domain);
        provider.setPackageId(0);
        provider.setMonth(p_package_time / 30);
        provider.setIpHost(ip);
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
            model.addAttribute("i", session.getAttribute("member"));
            return "package";
        }
    }

    ////////////////////////////////////////////////////////////////////////
    @RequestMapping("/channels")
    String showChannels(HttpSession session, Model model) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Session database = factory.openSession();
            List list = database.createQuery("from Channel").list();
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
            model.addAttribute("i", session.getAttribute("member"));
            return "/add_channel";
        }
    }

    @RequestMapping(value = "/add_channel", method = RequestMethod.POST)
    String addChannel(HttpSession session, Model model, String channel_name_en, String channel_name_th,
            String channel_name_cn, String ch, String url, MultipartFile Image) {
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
            database.close();
            model.addAttribute("channel", channel);
            model.addAttribute("i", session.getAttribute("member"));
            return "edit_channel";
        }
    }

    @RequestMapping(value = "/edit_channel/{id}", method = RequestMethod.POST)
    String editChannel(HttpSession session, Model model, @PathVariable long id, String channel_name_en,
            String channel_name_th, String channel_name_cn, String ch, String url, MultipartFile Image) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            /*String filename = "unknown.jpg";
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
                } catch (Exception e) { }
            }*/
            try {
                Channel channel = new Channel();
                channel.setNameEN(channel_name_en);
                channel.setNameTH(channel_name_th);
                channel.setNameCN(channel_name_cn);
                channel.setCH(ch);
                channel.setUrlStream(url);
                Session database = factory.openSession();
                channel = (Channel) database.get(Channel.class, id);
                database.update(channel);
                database.flush();
                database.close();
            } catch (Exception e) {
                System.out.println(e);
            }
            return "redirect:/channels";
        }
    }

    @RequestMapping("/sports")
    String ShowSports(HttpSession session, Model model) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Session database = factory.openSession();
            List list = database.createQuery("from Sports").list();
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
    String editSport(HttpSession session, Model model, @PathVariable long id) {
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
    String editSport(HttpSession session, Model model, @PathVariable long id, String sport_en,
            String sport_th, String sport_cn, MultipartFile Image) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            /*String filename = "unknown.jpg";
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
                } catch (Exception e) { }
            }*/
            try {
                Sports sports = new Sports();
                sports.setNameEN(sport_en);
                sports.setNameTH(sport_th);
                sports.setNameCN(sport_cn);
                //sports.setImg(filename);
                Session database = factory.openSession();
                sports = (Sports) database.get(Sports.class, id);
                database.update(sports);
                database.flush();
                database.close();
            } catch (Exception e) {
                System.out.println(e);
            }
            return "redirect:/sports";
        }
    }

    
    @RequestMapping("/leagues")
    String ShowLeagues(HttpSession session, Model model) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Session database = factory.openSession();
            List listLG = database.createQuery("from Leagues").list();
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
            List list = database.createQuery("from Sports").list();
            database.close();
            model.addAttribute("sps", list);
            model.addAttribute("i", session.getAttribute("member"));
            return "/new_leagues";
        }
    }

    @RequestMapping(value = "/new_leagues", method = RequestMethod.POST)
    String addLeague(HttpSession session, Model model,long sp_id, String league_en, String league_th,
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
    String editLeagues(HttpSession session, Model model, @PathVariable long id) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Leagues leagues = new Leagues();
            Session database = factory.openSession();
            leagues = (Leagues) database.get(Leagues.class, id);
            List listSP = database.createQuery("from Sports").list();
            database.close();
            model.addAttribute("leagues", leagues);
            model.addAttribute("sports", listSP);
            model.addAttribute("i", session.getAttribute("member"));
            return "edit_leagues";
        }
    }

    @RequestMapping(value = "/edit_leagues/{id}", method = RequestMethod.POST)
    String editLeagues(HttpSession session, Model model, @PathVariable long id, long sp_id, 
            String league_en, String league_th, String league_cn, MultipartFile Image) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            /*String filename = "unknown.jpg";
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
                } catch (Exception e) { }
            }*/
            try {
                Leagues leagues = new Leagues();
                leagues.setSportId(sp_id);
                leagues.setNameEN(league_en);
                leagues.setNameTH(league_th);
                leagues.setNameCN(league_cn);
                //sports.setImg(filename);
                Session database = factory.openSession();
                leagues = (Leagues) database.get(Leagues.class, id);
                database.update(leagues);
                database.flush();
                database.close();
            } catch (Exception e) {
                System.out.println(e);
            }
            return "redirect:/leagues";
        }
    }

    @RequestMapping("/teams")
    String ShowTeams(HttpSession session, Model model) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Session database = factory.openSession();
            List list = database.createQuery("from Teams").list();
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
            List list = database.createQuery("from Leagues").list();
            database.close();
            model.addAttribute("lgs", list);
            model.addAttribute("i", session.getAttribute("member"));
            return "/new_team";
        }
    }

    @RequestMapping(value = "/new_team", method = RequestMethod.POST)
    String addTeam(HttpSession session, Model model,long sp_id, String league_en, String league_th,
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
            return "redirect:/teams";
        }
    }

    @RequestMapping("/edit_team/{id}")
    String editTeam(HttpSession session, Model model, @PathVariable long id) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Teams team = new Teams();
            Session database = factory.openSession();
            team = (Teams) database.get(Teams.class, id);
            List listLg = database.createQuery("from Leagues").list();
            database.close();
            model.addAttribute("team", team);
            model.addAttribute("lgs", listLg);
            model.addAttribute("i", session.getAttribute("member"));
            return "edit_team";
        }
    }

    @RequestMapping(value = "/edit_team/{id}", method = RequestMethod.POST)
    String editTeam(HttpSession session, Model model, @PathVariable long id, long lg_id, 
            String team_en, String team_th, String team_cn, MultipartFile Image) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            /*String filename = "unknown.jpg";
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
                } catch (Exception e) { }
            }*/
            try {
                Teams team = new Teams();
                team.setLeague(lg_id);
                team.setNameEN(team_en);
                team.setNameTH(team_th);
                team.setNameCN(team_cn);
                //sports.setImg(filename);
                Session database = factory.openSession();
                team = (Teams) database.get(Teams.class, id);
                database.update(team);
                database.flush();
                database.close();
            } catch (Exception e) {
                System.out.println(e);
            }
            return "redirect:/teams";
        }
    }

    @RequestMapping("/schedule")
    String ShowSchedule(HttpSession session, Model model) {
        if (session.getAttribute("member") == null) {
            return "redirect:/login";
        } else {
            Session database = factory.openSession();
            List list = database.createQuery("from Schedules").list();
            //List list = database.createSQLQuery("select * from match").list();
            //System.out.println(list);
            database.close();
            model.addAttribute("schedule", list);
            model.addAttribute("i", session.getAttribute("member"));
            return "/schedule";
        }
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
        List list = new ArrayList();
        Session session = factory.openSession();
        Query query = session.createQuery("from Member");
        list = query.list();
        session.close();
        return list;
    }

}
