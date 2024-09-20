package com.smart.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.smart.dao.UserRepositiory;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
public class HomeController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	 
	@Autowired
	private UserRepositiory userRepository;
	
	@RequestMapping("/")//it will directly open home
	public String home(Model model)
	{
		model.addAttribute("title","Home - hhtt Contact Manager");
		return "home";
	}
	@RequestMapping("/about")// it will take to about page 
	public String about(Model model)
	{
		model.addAttribute("title","About - Smart Contact Manager");
		return "about";
	}
	@RequestMapping("/signup")// it will take it to signup page 
	public String signup(Model model)
	{
		model.addAttribute("title","Register - Smart Contact Manager");
		model.addAttribute("user",new User());
		return "signup";
	}
	
	//handler for registering user
	@PostMapping("/do_register")
	public String registerUser( @Valid  @ModelAttribute("user") User user,BindingResult result1,@RequestParam(value ="agreement",defaultValue="false") boolean agreement,Model model,HttpSession session) 
	{
		try {
			if(!user.isTermsAccepted())
			{
				//throw new Exception("You have not agreed the terms and condition");
				result1.rejectValue("termsAccepted", "error.user", "You must agree to the terms and conditions");
	            model.addAttribute("user", user);
	            return "signup";
			}
			
			// System.out.println("Binding result has errors: " + result1.hasErrors());
			if(result1.hasErrors())
			{
				//System.out.println("Binding result has errors: " + result1.hasErrors());
				//System.out.println("ERROR "+result1.toString());
				System.out.println("Errors: " + result1.getAllErrors());
				model.addAttribute("user",user);
				return "signup";// return early if there are error
			}
			
			//set attribute
			user.setRole("ROLE_USER");//for setting user role  
			user.setEnabled(true);//for enabling user
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			
			
			System.out.println("Agreement "+agreement);
			System.out.println("USER "+user);
			
			// Save user and handle potential exceptions
			User savedUser = this.userRepository.save(user);
			
			model.addAttribute("user", new User());
			session.setAttribute("message",new Message("Sucessfully register !! ","alert-success"));
			return "signup";
			
			
			
		}
		catch (DataIntegrityViolationException e) {
		    // Handle the duplicate email error
		    e.printStackTrace();
		    model.addAttribute("user", user);
		    session.setAttribute("message", new Message("Email is already registered! Please use another email.", "alert-danger"));
		    return "signup";
		}
		
		
		catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message",new Message("Something went wrong!! "+e.getMessage(),"alert-danger"));
			return "signup";
		}
		
		
	}
	
}

