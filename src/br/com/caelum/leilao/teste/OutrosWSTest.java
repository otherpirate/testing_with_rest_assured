package br.com.caelum.leilao.teste;

import static com.jayway.restassured.RestAssured.*;

import org.junit.Test;

public class OutrosWSTest {
	
	@Test
	public void deveGerarCookie(){
		expect()
			.cookie("rest-assured", "funciona")
		.when()
			.get("/cookie/teste");
	}
	
	@Test
	public void deveGerarUmHeader(){
		expect()
			.header("novo-header", "abc")
		.when()
			.get("/cookie/teste");
	}
}