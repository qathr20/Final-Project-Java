package com.qathrin.bus.controller;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.validation.Valid;

import com.qathrin.bus.controller.request.UserSignupRequest;
import com.qathrin.bus.dto.model.user.UserDto;
import com.qathrin.bus.dto.response.Response;
import com.qathrin.bus.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.qathrin.bus.model.Agency;
import com.qathrin.bus.model.ERole;
import com.qathrin.bus.model.Role;
import com.qathrin.bus.model.User;
import com.qathrin.bus.payload.request.SignupCustomRequest;
import com.qathrin.bus.payload.request.UserCustomRequest;
import com.qathrin.bus.payload.request.UserPasswordRequest;
import com.qathrin.bus.payload.response.MessageResponse;
import com.qathrin.bus.repository.AgencyRepository;
import com.qathrin.bus.repository.RoleRepository;
import com.qathrin.bus.repository.UserRepository;
import com.qathrin.bus.security.jwt.JwtUtils;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@CrossOrigin(origins = "*", maxAge = 3600, methods = { RequestMethod.PUT, RequestMethod.POST, RequestMethod.GET })
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	AgencyRepository agencyRepository;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupCustomRequest signupCustomRequest) {
		if (userRepository.existsByUsername(signupCustomRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signupCustomRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signupCustomRequest.getFirstName(), signupCustomRequest.getLastName(),
				signupCustomRequest.getMobileNumber(), signupCustomRequest.getUsername(),
				signupCustomRequest.getEmail(), encoder.encode(signupCustomRequest.getPassword()));

		Set<String> strRoles = signupCustomRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "ROLE_ADMIN":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);
					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		Agency agency = new Agency(signupCustomRequest.getCode(), signupCustomRequest.getDetails(),
				signupCustomRequest.getName(), user);
		agencyRepository.save(agency);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PutMapping("/{id}")
	@ApiOperation(value = "", authorizations = { @Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<?> updateUser(@PathVariable(value = "id") Long id,
			@Valid @RequestBody UserCustomRequest userCustomRequest) {
		User user = userRepository.findById(id).get();
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		user.setFirstName(userCustomRequest.getFirstName());
		user.setLastName(userCustomRequest.getLastName());
		user.setMobileNumber(userCustomRequest.getMobileNumber());
		user.setUsername(userCustomRequest.getUsername());

		User updatedUser = userRepository.save(user);

		return ResponseEntity.ok(updatedUser);
	}

	@PutMapping("/password/{id}")
	@ApiOperation(value = "", authorizations = { @Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<?> updatePassword(@PathVariable(value = "id") Long id,
			@Valid @RequestBody UserPasswordRequest userPasswordRequest) {
		User user = userRepository.findById(id).get();
		if (user == null) {
			return ResponseEntity.notFound().build();
		}

		user.setPassword(encoder.encode(userPasswordRequest.getPassword()));

		User updatedUser = userRepository.save(user);

		return ResponseEntity.ok(updatedUser);
	}

	@CrossOrigin
	@GetMapping(value = "/")
	@ApiOperation(value = "", authorizations = { @Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public List<User> list(){
		return userService.ListAllUser();
	}

	@CrossOrigin
	@DeleteMapping("users/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id){
		userService.deleteUser(id);
		return new ResponseEntity<>("Success Deleting Data With ID"+id, HttpStatus.OK);
	}

	@CrossOrigin
	@GetMapping("/users/{id}")
	public ResponseEntity<User> users(@PathVariable Long id) {
		try {
			User user = userService.getUser(id);
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
	}

//	@PostMapping("/signup")
//	public Response signup(@RequestBody @Valid UserSignupRequest userSignupRequest) {
//		return Response.ok().setPayload(registerUser(userSignupRequest, false));
//	}
//	private UserDto registerUser(UserSignupRequest userSignupRequest, boolean isAdmin) {
//		UserDto userDto = new UserDto()
//				.setEmail(userSignupRequest.getEmail())
//				.setPassword(userSignupRequest.getPassword())
//				.setFirstName(userSignupRequest.getFirstName())
//				.setLastName(userSignupRequest.getLastName())
//				.setMobileNumber(userSignupRequest.getMobileNumber())
//				.setAdmin(isAdmin);
//		return userService.signup(userDto);
//	}
	
}
