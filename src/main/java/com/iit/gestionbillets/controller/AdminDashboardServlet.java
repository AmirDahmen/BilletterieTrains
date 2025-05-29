package com.iit.gestionbillets.controller;



import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

import java.util.List;

import java.util.Map;

import java.util.ArrayList;

import java.time.YearMonth;

import java.time.format.DateTimeFormatter;

import com.iit.gestionbillets.dao.Interface.IUserDAO;

import com.iit.gestionbillets.dao.Interface.IBilletDAO;

import com.iit.gestionbillets.dao.Interface.IGareDAO;

import com.iit.gestionbillets.dao.impl.UserDAOImpl;

import com.iit.gestionbillets.dao.impl.BilletDAOImpl;

import com.iit.gestionbillets.dao.impl.GareDAOImpl;

import com.iit.gestionbillets.model.Role;

import com.iit.gestionbillets.model.User;

import com.iit.gestionbillets.model.Billet;



import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;

import jakarta.servlet.http.HttpServlet;

import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.http.HttpSession;



import java.io.IOException;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;



public class AdminDashboardServlet extends HttpServlet {

 private static final long serialVersionUID = 1L;

 

 private IUserDAO userDAO;

 private IBilletDAO billetDAO;

 private IGareDAO gareDAO;



 @Override

 public void init() throws ServletException {

 this.userDAO = new UserDAOImpl();

 this.billetDAO = new BilletDAOImpl();

 this.gareDAO = new GareDAOImpl();

 }



 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

 HttpSession session = request.getSession(false);




 if (session != null && session.getAttribute("user") != null) {

 User user = (User) session.getAttribute("user");

 if (user.getRole() == Role.ADMIN) {

 try {


 DashboardStats stats = getDashboardStatistics();

 

 // Set statistics as request attributes

 request.setAttribute("activeUsers", stats.getActiveUsers());

 request.setAttribute("reservationsToday", stats.getReservationsToday());

 request.setAttribute("totalBillets", stats.getTotalBillets());

 request.setAttribute("totalGares", stats.getTotalGares());

 request.setAttribute("adminCount", stats.getAdminCount());

 


 String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

 request.setAttribute("currentTime", currentTime);




 request.setAttribute("userDistributionData", getUserDistributionChartData());

 request.setAttribute("reservationsPerMonthData", getReservationsPerMonthChartData());

 


 request.getRequestDispatcher("/WEB-INF/jsp/admin/dashboardAdmin.jsp").forward(request, response);

 

 } catch (Exception e) {

 e.printStackTrace();

 request.setAttribute("errorMessage", "Erreur lors du chargement des statistiques du dashboard.");

 request.getRequestDispatcher("/WEB-INF/jsp/admin/common/admin_error.jsp").forward(request, response);

 }

 } else {


 response.sendRedirect(request.getContextPath() + "/client/dashboard");

 }

 } else {


 response.sendRedirect(request.getContextPath() + "/auth?action=showLogin");

 }

 }

 

 private DashboardStats getDashboardStatistics() {

 DashboardStats stats = new DashboardStats();

 

 try {


 long activeUsers = userDAO.findAll().stream()

 .filter(user -> !user.isBanned())

 .count();

 stats.setActiveUsers((int) activeUsers);

 


 int reservationsToday = billetDAO.countReservationsToday();

 stats.setReservationsToday(reservationsToday);

 


 int totalBillets = billetDAO.findAll().size();

 stats.setTotalBillets(totalBillets);

 


 int totalGares = gareDAO.findAll().size();

 stats.setTotalGares(totalGares);

 


 long adminCount = userDAO.findAll().stream()

 .filter(user -> user.getRole() == Role.ADMIN)

 .count();

 stats.setAdminCount((int) adminCount);

 

 } catch (Exception e) {

 e.printStackTrace();

 //  default values en cas de  error

 stats.setActiveUsers(0);

 stats.setReservationsToday(0);

 stats.setTotalBillets(0);

 stats.setTotalGares(0);

 stats.setAdminCount(0);

 }

 

 return stats;

 }



 private String getUserDistributionChartData() {

 try {

 List<User> allUsers = userDAO.findAll();



 int activeUsers = (int) allUsers.stream().filter(user -> !user.isBanned()).count();

 int bannedUsers = (int) allUsers.stream().filter(User::isBanned).count();

 int adminUsers = (int) allUsers.stream().filter(user -> user.getRole() == Role.ADMIN).count();

 int clientUsers = (int) allUsers.stream().filter(user -> user.getRole() == Role.CLIENT).count();



 Map<String, Object> chartData = new HashMap<>();



 // User Status Distribution

 Map<String, Object> statusData = new HashMap<>();

 statusData.put("labels", new String[]{"Utilisateurs Actifs", "Utilisateurs Bannis"});

 statusData.put("data", new int[]{activeUsers, bannedUsers});

 statusData.put("backgroundColor", new String[]{"#28a745", "#dc3545"});



 // User Role Distribution

 Map<String, Object> roleData = new HashMap<>();

 roleData.put("labels", new String[]{"Clients", "Administrateurs"});

 roleData.put("data", new int[]{clientUsers, adminUsers});

 roleData.put("backgroundColor", new String[]{"#17a2b8", "#ffc107"});



 chartData.put("status", statusData);

 chartData.put("roles", roleData);



 ObjectMapper mapper = new ObjectMapper();

 return mapper.writeValueAsString(chartData);

 } catch (Exception e) {

 e.printStackTrace();

 return "{}";

 }

 }



 private String getReservationsPerMonthChartData() {

 try {

 List<Billet> allBillets = billetDAO.findAll();

 Map<String, Integer> monthlyReservations = new HashMap<>();



 //  last 6 months

 LocalDateTime now = LocalDateTime.now();

 List<String> months = new ArrayList<>();

 List<Integer> counts = new ArrayList<>();



 for (int i = 5; i >= 0; i--) {

 YearMonth yearMonth = YearMonth.from(now.minusMonths(i));

 String monthKey = yearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));

 String monthLabel = yearMonth.format(DateTimeFormatter.ofPattern("MMM yyyy"));

 months.add(monthLabel);



 int count = (int) allBillets.stream()

 .filter(billet -> {

 YearMonth billetMonth = YearMonth.from(billet.getDateReservation());

 return billetMonth.equals(yearMonth);

 })

 .count();

 counts.add(count);

 }



 Map<String, Object> chartData = new HashMap<>();

 chartData.put("labels", months);

 chartData.put("data", counts);



 ObjectMapper mapper = new ObjectMapper();

 return mapper.writeValueAsString(chartData);

 } catch (Exception e) {

 e.printStackTrace();

 return "{}";

 }

 }



 

 

 // classe interne pour nos stats

 private static class DashboardStats {

 private int activeUsers;

 private int reservationsToday;

 private int totalBillets;

 private int totalGares;

 private int adminCount;

 


 public int getActiveUsers() { return activeUsers; }

 public void setActiveUsers(int activeUsers) { this.activeUsers = activeUsers; }

 

 public int getReservationsToday() { return reservationsToday; }

 public void setReservationsToday(int reservationsToday) { this.reservationsToday = reservationsToday; }

 

 public int getTotalBillets() { return totalBillets; }

 public void setTotalBillets(int totalBillets) { this.totalBillets = totalBillets; }

 

 public int getTotalGares() { return totalGares; }

 public void setTotalGares(int totalGares) { this.totalGares = totalGares; }

 

 public int getAdminCount() { return adminCount; }

 public void setAdminCount(int adminCount) { this.adminCount = adminCount; }

 }

}