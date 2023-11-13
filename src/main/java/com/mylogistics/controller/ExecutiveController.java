package com.mylogistics.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mylogistics.enums.RoleType;
import com.mylogistics.model.Carrier;
import com.mylogistics.model.Customer;
import com.mylogistics.model.Executive;
import com.mylogistics.model.Route;
import com.mylogistics.model.User;
import com.mylogistics.service.CarrierService;
import com.mylogistics.service.CustomerService;
import com.mylogistics.service.ExecutiveService;
import com.mylogistics.service.RouteService;
import com.mylogistics.service.UserService;

@RestController
@RequestMapping("/executive")
public class ExecutiveController {
	@Autowired
	private ExecutiveService executiveService;
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RouteService routeService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private CarrierService carrierService;
	
	@PostMapping("/add")
	public Executive postExecutive(@RequestBody Executive executive) {
		User user = executive.getUser();
		/*I am encrypting the password*/
		String passwordPlain=user.getPassword();
		String encodedPassword=passwordEncoder.encode(passwordPlain);
		user.setPassword(encodedPassword);
		user.setRole(RoleType.EXECUTIVE);
		//Step 1 Save user in db and attach saved user to customer
		user = userService.postUser(user);
		//Step 2: Attach user and save customer
		executive.setUser(user);
		executive = executiveService.postExecutive(executive);
		return executive; 
	}
	@PostMapping("/addRoute")
	public Route postExecutive(@RequestBody Route route) {
		route = routeService.postRoute(route);
		return route; 
	}
	@GetMapping("/getallRoutes")
	public List<Route> getAll(@RequestParam(value="page",required=false,defaultValue="0") Integer page,
			@RequestParam(value="size",required=false,defaultValue="100000") Integer size) {
		Pageable pageable= PageRequest.of(page, size);
		return routeService.getAll(pageable);
	}
	@GetMapping("/getallCustomers")
	public List<Customer> getAllCustomers(@RequestParam(value="page",required=false,defaultValue="0") Integer page,
			@RequestParam(value="size",required=false,defaultValue="100000") Integer size) {
		Pageable pageable= PageRequest.of(page, size);
		return customerService.getAllCustomers(pageable);
	}
	@GetMapping("/getallCarriers")
	public List<Carrier> getAllCarriers(@RequestParam(value="page",required=false,defaultValue="0") Integer page,
			@RequestParam(value="size",required=false,defaultValue="100000") Integer size) {
		Pageable pageable= PageRequest.of(page, size);
		return carrierService.getAllCarriers(pageable);
	}

}