package com.qathrin.bus.payload.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class TripScheduleRequest {

	@NotBlank
	private String tripDate;

	@NotNull
	private int availableSeats;

	private int tripDetail;
}
