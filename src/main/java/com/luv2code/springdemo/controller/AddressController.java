package com.luv2code.springdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luv2code.springdemo.entity.Address;
import com.luv2code.springdemo.service.AddressService;

@Controller
@RequestMapping("/address")
public class AddressController {

	// need to inject our address service
	@Autowired
	private AddressService addressService;
	
	@GetMapping("/list")
	public String listAddress(Model theModel) {
		
		// get customers from the service
		List<Address> theAddress = addressService.getAddress();
				
		// add the customers to the model
		theModel.addAttribute("address", theAddress);
		
		return "list-address";
	}
	
	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model theModel) {
		
		// create model attribute to bind form data
		Address theAddress = new Address();
		
		theModel.addAttribute("address", theAddress);
		
		return "address-form";
	}
	
	@PostMapping("/saveAddress")
	public String saveAddress(@ModelAttribute("address") Address theAddress) {
		
		// save the address using our service
		addressService.saveAddress(theAddress);	
		
		return "redirect:list";
		//return "redirect:address/list";
	}

	
	@GetMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("addressId") int theId,
									Model theModel) {
		
		// get the customer from our service
		Address theAddress = addressService.getAddress(theId);	
		
		// set customer as a model attribute to pre-populate the form
		theModel.addAttribute("address", theAddress);
		
		// send over to our form		
		return "address-form";
	}
	
	@GetMapping("/delete")
	public String deleteAddress(@RequestParam("addressId") int theId) {
		
		// delete the customer
		addressService.deleteAddress(theId);
		
		return "redirect:/address/list";
	}
}










