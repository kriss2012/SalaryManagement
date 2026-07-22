package com.one.project.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.one.project.Employee.Employee;

public interface EmployeeRepo extends JpaRepository<Employee,Integer> {

}
