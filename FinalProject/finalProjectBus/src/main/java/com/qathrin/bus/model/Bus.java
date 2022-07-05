package com.qathrin.bus.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

@Entity
@Table(name="bus")
public class Bus {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String code;

	@Max(value = 25)
	private int capacity;

	@Size(max = 20)
	private String make;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "agency_id")
	private Agency agency;
	
	public Bus() {
    }
	
	public Bus(String code, int capacity, String make, Agency agency) {
        this.code = code;
        this.capacity = capacity;
        this.make = make;
        this.agency = agency;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public Agency getAgency() {
		return agency;
	}

	public void setAgency(Agency agency) {
		this.agency = agency;
	}
}
