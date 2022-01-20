package com.luv2code.springdemo.entity;

//import java.util.ArrayList;
//import java.util.List;

//import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="address" )
public class Address {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="street")
	private String street;
	
	@Column(name="Num")
	private int num;
	
	@Column(name="city")
	private String city;
	
	@Column(name="province")
	private String province;
	
//	@OneToMany(mappedBy="address",
//			cascade= {	CascadeType.PERSIST,
//						CascadeType.MERGE,
//						CascadeType.DETACH,
//						CascadeType.REFRESH})
//	private List<Customer> customers;
//	

//	
//	public List<Customer> getCustomers() {
//		return customers;
//	}
//
//	public void setCustomers(List<Customer> customers) {
//		this.customers = customers;
//	}

	public Address() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
	
//	public void add(Customer tempCustomer) {
//		if (customers == null) {
//			customers = new ArrayList<>();
//		}
//		
//		customers.add(tempCustomer);
//		
//		tempCustomer.setAddress(this);
//		
//		
//	}

	@Override
	public String toString() {
		return "Address [id=" + id + ", street=" + street + ", num=" + num + ", city=" + city + ", province=" + province
				+ "]";
	}
	
    

		
}





