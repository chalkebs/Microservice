package com.fundoo.user.controller;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest 
{

	@MockBean
	private UserController userController;
	
	@Test
	public void testRegister() 
	{
		
	}

	@Test
	public void testOnLogin() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testEmailValidation() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testForgotPassword() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testResetPassword() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testUploadFile() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetProfile() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testClearRedis() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testObject() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetClass() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testHashCode() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testEquals() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testClone() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testToString() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testNotify() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testNotifyAll() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testWaitLong() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testWaitLongInt()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testWait() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testFinalize() 
	{
		fail("Not yet implemented");
	}

}
