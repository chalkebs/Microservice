package com.fundoo.utility;

import java.io.UnsupportedEncodingException;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;

@Service
public class TokenGenerator
{
	public final String TOKEN_SECRET = "sd5745FAHFW";

	public String createToken(Long id) 
	{
		try 
		{
			// to set algorithm
			Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
			
			// payload
			String token = JWT.create().withClaim("userId", id).sign(algorithm);
			
			return token;
			
		} 
		catch (JWTCreationException exception) 
		{
			exception.printStackTrace();
			
		} 
		catch (IllegalArgumentException e) 
		{
			e.printStackTrace();
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	public Long decodeToken(String token) 
	{
		Long userid;
		
		// for verification algorithm
		Verification verification = null;
		
		try 
		{
			verification = JWT.require(Algorithm.HMAC256(TOKEN_SECRET));
		} 
		catch (IllegalArgumentException | UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
		
		JWTVerifier jwtverifier = verification.build();
		
		// to decode token
		DecodedJWT decodedjwt = jwtverifier.verify(token);

		Claim claim = decodedjwt.getClaim("userId");
		userid = claim.asLong();
		
		return userid;
	}

}
