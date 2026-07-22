package com.one.project.MyController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.one.project.DAO.EmployeeRepo;
import com.one.project.DAO.ChatMessageRepo;
import com.one.project.DAO.QuestRepo;
import com.one.project.Employee.Employee;
import com.one.project.Employee.ChatMessage;
import com.one.project.Employee.Quest;

@Controller
public class MyController {
	@Autowired
	EmployeeRepo repo;

	@Autowired
	ChatMessageRepo chatMessageRepo;

	@Autowired
	QuestRepo questRepo;

	public List<Employee> getEmployeeData() {
		return repo.findAll();
	}

	// ==========================================
	// SAAS LANDING & REGISTRATION
	// ==========================================

	// GET: SaaS Public Landing Page
	@GetMapping("/")
	public String showLandingPage(HttpSession session, Model model) {
		// Pass count of members and active quests for social proof
		model.addAttribute("totalHeroes", repo.count());
		model.addAttribute("totalQuests", questRepo.count());
		return "landing";
	}

	// Redirect '/index' (admin list) to root Landing Page or keep as admin section
	@GetMapping("/index")
	public String listEmployee(HttpSession session, Model model) {
		// Protect '/index' so only logged-in users can view the directory list
		Employee loggedIn = (Employee) session.getAttribute("loggedInEmployee");
		if (loggedIn == null) {
			return "redirect:/login";
		}
		
		model.addAttribute("employees", getEmployeeData());
		Employee employee = new Employee();
		employee.setAvatar("naruto");
		employee.setPowerLevel(1000);
		employee.setMagicElement("Fire");
		employee.setPassword("password");
		model.addAttribute("employees1", employee);
		
		return "employee";
	}

	// GET: Registration form
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		Employee employee = new Employee();
		// Starting hero defaults
		employee.setAvatar("naruto");
		employee.setPowerLevel(500);
		employee.setDesignation("Guild Novice");
		employee.setMagicElement("None");
		employee.setSpecialAbility("Basic Strike");
		model.addAttribute("employee", employee);
		return "register";
	}

	// POST: Register process
	@PostMapping("/register")
	public String registerUser(@ModelAttribute("employee") Employee employee) {
		if (employee.getPassword() == null || employee.getPassword().isEmpty()) {
			employee.setPassword("password");
		}
		if (employee.getAvatar() == null || employee.getAvatar().isEmpty()) {
			employee.setAvatar("naruto");
		}
		// Save new employee/hero
		repo.save(employee);
		return "redirect:/login?registered=true";
	}

	// GET: Login Page
	@GetMapping("/login")
	public String showLoginPage(@RequestParam(value = "registered", required = false) Boolean registered,
								HttpSession session, 
								Model model) {
		if (session.getAttribute("loggedInEmployee") != null) {
			return "redirect:/profile";
		}
		if (Boolean.TRUE.equals(registered)) {
			model.addAttribute("registeredSuccess", "Hero successfully enrolled in the guild! Please log in.");
		}
		return "login";
	}

	// POST: Login Validation
	@PostMapping("/login")
	public String processLogin(@RequestParam("id") int id, 
							   @RequestParam("password") String password, 
							   HttpSession session, 
							   Model model) {
		Optional<Employee> empOpt = repo.findById(id);
		if (empOpt.isPresent()) {
			Employee emp = empOpt.get();
			if (password != null && password.equals(emp.getPassword())) {
				session.setAttribute("loggedInEmployee", emp);
				return "redirect:/profile";
			}
		}
		model.addAttribute("error", "Invalid ID or Passcode! Please verify and retry.");
		return "login";
	}

	// GET: Employee Profile Dashboard (Private View)
	@GetMapping("/profile")
	public String showProfile(HttpSession session, Model model) {
		Employee loggedIn = (Employee) session.getAttribute("loggedInEmployee");
		if (loggedIn == null) {
			return "redirect:/login";
		}
		
		Employee current = repo.findById(loggedIn.getId()).orElse(null);
		if (current == null) {
			session.removeAttribute("loggedInEmployee");
			return "redirect:/login";
		}
		
		// Find active quests of this logged-in user
		List<Quest> activeQuests = questRepo.findAll().stream()
				.filter(q -> q.hasParticipant(current.getId()) && !"COMPLETED".equals(q.getStatus()))
				.collect(Collectors.toList());

		model.addAttribute("employee", current);
		model.addAttribute("activeQuests", activeQuests);
		return "profile";
	}

	// GET: Logout Action
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("loggedInEmployee");
		session.invalidate();
		return "redirect:/login";
	}

	// ==========================================
	// GUILD SOCIAL HALL, CHAT & QUESTS (MULTIPLAYER)
	// ==========================================

	// GET: Guild Social Hall Dashboard
	@GetMapping("/guild-hall")
	public String showGuildHall(HttpSession session, Model model) {
		Employee loggedIn = (Employee) session.getAttribute("loggedInEmployee");
		if (loggedIn == null) {
			return "redirect:/login";
		}

		// Ensure default quests exist
		if (questRepo.count() == 0) {
			questRepo.save(new Quest("Slay the Slime King", "A low-tier cooperative raid. Clear out the giant slimes from the southern forest.", 1500, 100, "AVAILABLE", ""));
			questRepo.save(new Quest("Infiltrate the Volcano Chamber", "Find and secure the legendary Fire Magic Scroll before the demon clan.", 5000, 5000, "AVAILABLE", ""));
			questRepo.save(new Quest("Raid the Void Dragon Temple", "Cooperative SSS-Rank battle. Bring your best elements and abilities.", 25000, 20000, "AVAILABLE", ""));
		}

		// Ensure default welcome chat exists
		if (chatMessageRepo.count() == 0) {
			chatMessageRepo.save(new ChatMessage("System Bot", "gojo", "Welcome to the Adventurers Guild Chat Lobby! Team up, select cooperative Quests, and raise your power levels.", LocalDateTime.now()));
		}

		// Load lobby elements
		model.addAttribute("currentUser", repo.findById(loggedIn.getId()).orElse(loggedIn));
		model.addAttribute("roster", repo.findAll());
		model.addAttribute("quests", questRepo.findAll());
		model.addAttribute("chatMessages", chatMessageRepo.findAll());

		return "guild-hall";
	}

	// POST: Send Chat Message
	@PostMapping("/chat/send")
	public String sendChatMessage(@RequestParam("messageText") String messageText, HttpSession session) {
		Employee loggedIn = (Employee) session.getAttribute("loggedInEmployee");
		if (loggedIn != null && messageText != null && !messageText.trim().isEmpty()) {
			Employee sender = repo.findById(loggedIn.getId()).orElse(loggedIn);
			ChatMessage chat = new ChatMessage(
					sender.getName(),
					sender.getAvatar(),
					messageText.trim(),
					LocalDateTime.now()
			);
			chatMessageRepo.save(chat);
		}
		return "redirect:/guild-hall";
	}

	// POST: Join Quest
	@PostMapping("/quest/join/{id}")
	public String joinQuest(@PathVariable("id") Long questId, HttpSession session) {
		Employee loggedIn = (Employee) session.getAttribute("loggedInEmployee");
		if (loggedIn != null) {
			Quest quest = questRepo.findById(questId).orElse(null);
			if (quest != null && "AVAILABLE".equals(quest.getStatus())) {
				// Verify if power level requirements are met
				Employee hero = repo.findById(loggedIn.getId()).orElse(loggedIn);
				if (hero.getPowerLevel() >= quest.getRequiredPower()) {
					quest.addParticipant(hero.getId());
					quest.setStatus("ACTIVE");
					questRepo.save(quest);
				}
			}
		}
		return "redirect:/guild-hall";
	}

	// POST: Complete Quest (Multiplayer reward claims)
	@PostMapping("/quest/complete/{id}")
	public String completeQuest(@PathVariable("id") Long questId, HttpSession session) {
		Employee loggedIn = (Employee) session.getAttribute("loggedInEmployee");
		if (loggedIn != null) {
			Quest quest = questRepo.findById(questId).orElse(null);
			if (quest != null && "ACTIVE".equals(quest.getStatus())) {
				String participantIds = quest.getParticipants();
				if (participantIds != null && !participantIds.isEmpty()) {
					String[] ids = participantIds.split(",");
					for (String idStr : ids) {
						try {
							int empId = Integer.parseInt(idStr.trim());
							Optional<Employee> heroOpt = repo.findById(empId);
							if (heroOpt.isPresent()) {
								Employee hero = heroOpt.get();
								// Increase combat stats/power level
								hero.setPowerLevel(hero.getPowerLevel() + quest.getPowerReward());
								repo.save(hero);
							}
						} catch (NumberFormatException e) {
							// skip malformed ids
						}
					}
				}
				quest.setStatus("COMPLETED");
				questRepo.save(quest);

				// Log system quest clear message in chat
				chatMessageRepo.save(new ChatMessage(
						"Guild Bulletin",
						"gojo",
						"Cooperative Quest [" + quest.getTitle() + "] has been COMPLETED! All participants gained +" + quest.getPowerReward() + " Combat Power!",
						LocalDateTime.now()
				));
			}
		}
		return "redirect:/guild-hall";
	}

	// ==========================================
	// CRUD OVERRIDES FOR DIRECTORY AND EDITING
	// ==========================================

	// Add Employee Action
	@PostMapping("/index1")
	public String AddEmployee(@ModelAttribute("employees1") Employee employee) {
		if (employee.getPassword() == null || employee.getPassword().isEmpty()) {
			employee.setPassword("password");
		}
		if (employee.getAvatar() == null || employee.getAvatar().isEmpty()) {
			employee.setAvatar("naruto");
		}
		repo.save(employee);
		return "redirect:/index";
	}

	// Edit Employee Form
	@GetMapping("/employee/edit/{id}")
	public String updateEmployeeForm(@PathVariable int id, Model model, HttpSession session) {
		Employee loggedIn = (Employee) session.getAttribute("loggedInEmployee");
		if (loggedIn == null) {
			return "redirect:/login";
		}
		// Security safeguard: can only edit own stats or need to be logged in
		Employee emp = repo.findById(id).orElse(null);
		if (emp == null) {
			return "redirect:/index";
		}
		model.addAttribute("employees2", emp);
		return "update";
	}
	 
	// Process Update Action
	@PostMapping("/employees/{id}")
	public String update(@PathVariable int id, @ModelAttribute("employees2") Employee employee, Model model) {
		Employee emp = repo.findById(id).orElse(null);
		if (emp != null) {
			emp.setName(employee.getName());
			emp.setDesignation(employee.getDesignation());
			emp.setSalary(employee.getSalary());
			emp.setPassword(employee.getPassword());
			emp.setAnimeAlias(employee.getAnimeAlias());
			emp.setAvatar(employee.getAvatar());
			emp.setPowerLevel(employee.getPowerLevel());
			emp.setMagicElement(employee.getMagicElement());
			emp.setSpecialAbility(employee.getSpecialAbility());
			repo.save(emp);
		}
		return "redirect:/profile";	
	}

	// Delete Employee Action
	@GetMapping("/employee/{id}")
	public String deleteEmployee(@PathVariable int id, Model model) {
		repo.deleteById(id);
		return "redirect:/index";
	}
}
