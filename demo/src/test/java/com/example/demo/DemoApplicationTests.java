package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.model.Brand;
import com.example.demo.model.Category;
import com.example.demo.model.dto.UserDTO;
import com.example.demo.repository.BrandRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.PersonRepository;
import com.example.demo.repository.TypeRepository;
import com.example.demo.repository.UserRepository;

@SpringBootTest
class DemoApplicationTests {

	// @Autowired 
	// private BrandRepository brandRepository;
	
	//cara 2
	private BrandRepository brandRepository;
	private CategoryRepository categoryRepository;
	private UserRepository userRepository;

	@Autowired
	public DemoApplicationTests(BrandRepository brandRepository, 
	CategoryRepository categoryRepository, TypeRepository typeRepository, 
	UserRepository userRepository) {
		this.brandRepository = brandRepository;
		this.categoryRepository = categoryRepository;
		this.userRepository = userRepository;
	}

	@Test
	void brandGetTest() {
		//Arrange
		Integer expectBrand = 7;

		//Act
		List<Brand> actualBrand = brandRepository.findAll();

		//Assert
		assertEquals(expectBrand, actualBrand.size());
	}

	@Test
	void brandAvailableTest() {
		//Arrange
		Boolean expectBrand = true;

		// Act
        List<Brand> resultBrand = brandRepository.findAll();
        Boolean actualBrand  = resultBrand .stream().anyMatch(v -> v.getId().equals(1));
        
        // Assert
        assertEquals(expectBrand , actualBrand );
	}

	@Test
	void categoryGetTest() {
		//Arrange
		Integer expectCategory = 3;

		//Act
		List<Category> actualCategory = categoryRepository.findAll();

		//Assert
		assertEquals(expectCategory, actualCategory.size());
	}

	@Test
	void brandCount(){
		//Arrange
		long expected = 7;

		//Act
		long actual = brandRepository.count();

		//Assert
		assertEquals(expected, actual);		
	}

	// @Test
	// void typeView(){
	// 	//Arrange
	// 	String expected = "vario";

	// 	//Act
	// 	TypeDTO dtoResult = typeRepository.getUsingDTO();
	// 	String typeActual = dtoResult.getName();

	// 	//Assert
	// 	assertEquals(expected, typeActual);		
	// }

	@Test
	void GetUserIdDTO() {
		// Arrange
		UserDTO userDTO = userRepository.getUsingDTO("janedoe@gmail.com");

		// Act
		// Integer actualRoleName = userDTO.getId();

		userDTO.toString();

		// Assert
		//assertEquals(new UserDTO(1, "jane", "doe", "master", "$2a$12$mpFUgIZnIql9miT6YKhsZuMbGV/PsOKRrtKiA7TIYEPHumGkgojYe"), userDTO);
	}

}
