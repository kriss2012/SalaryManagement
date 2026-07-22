package com.one.project.MyController;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.one.project.DAO.EmployeeRepo;
import com.one.project.Employee.Employee;

@Controller
public class MyController {
	@Autowired
	EmployeeRepo repo;

	public List<Employee> getEmployeeData(){
		List<Employee> data=repo.findAll();	
		return data;
	}

	@GetMapping("/index")
	public String listEmployee(Model model) 
	{
		model.addAttribute("employees", getEmployeeData());
	
		 Employee employee=new Employee();
			model.addAttribute("employees1", employee);		
		return "employee";
	}

	@PostMapping("/index1")
	public String AddEmployee( @ModelAttribute("employees1") Employee employee) {
		repo.save(employee);
	return "redirect:/index";
		
	}
	public Employee getEmployeeId(int id) {
		return repo.findById(id).get();
	}
	
	public Employee updateEmployee(Employee employee) {
		return repo.save(employee);
	}
	 @GetMapping("/employee/edit/{id}")
	public String updateEmployeeForm(@PathVariable int id,Model model) {
		model.addAttribute("employees2", getEmployeeId(id));
		return "update";
	}
	 
	 @PostMapping("/employees/{id}")
		public String update(@PathVariable int id, @ModelAttribute("employees2") Employee employee ,Model model) {
		 Employee emp=getEmployeeId(id);
		 emp.setName(employee.getName());
		 emp.setDesignation(employee.getDesignation());
		 emp.setSalary(employee.getSalary());
		 
		    updateEmployee(emp);
			return "redirect:/index";	
	 }
	 @GetMapping("/employee/{id}")
		public String deleteEmployee(@PathVariable int id,Model model) {
			repo.deleteById(id);
			return "redirect:/index";
		}
	 
	 @GetMapping("/emp")
		public String updateform() {
			return "employee";
		}
	
	
	
	
	////////////////////////////////////////////////////////////////////////////////////
	
//	 @GetMapping("/add")
//	public String saveData(Model model) {
//		 Employee employee=new Employee();
//		model.addAttribute("employees1", employee);
//		return "add";
//	}
//	
//	
//	@PostMapping("/index1")
//	public String AddEmployee( @ModelAttribute("employees1") Employee employee ) {
//		repo.save(employee);
//		return "redirect:/index";
//		
//	}
	
	
	
}
