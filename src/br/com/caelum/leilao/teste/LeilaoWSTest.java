package br.com.caelum.leilao.teste;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.leilao.modelo.Leilao;
import br.com.caelum.leilao.modelo.Usuario;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.path.xml.XmlPath;

import static com.jayway.restassured.RestAssured.*;

public class LeilaoWSTest {

	private Usuario usuarioMauricio;
	private Leilao leilaoGeladeira;

	@Before
	public void setUp(){
		usuarioMauricio = new Usuario(1L, "Mauricio Aniche", "mauricio.aniche@caelum.com.br");
		leilaoGeladeira = new Leilao(1L, "Geladeira", 800.00, usuarioMauricio, false);			
	}
	
	@Test
	public void deveRetornarLeilaoPorId(){
		JsonPath jsonPath = given()
				.header("Accept", "application/json")
				.parameter("leilao.id", 1)
				.get("/leiloes/show")
				.andReturn()
				.jsonPath();

		Leilao leilaoEsperado = jsonPath.getObject("leilao", Leilao.class);

		Assert.assertEquals(leilaoGeladeira, leilaoEsperado);
	}
	
	@Test
	public void deveRetornarTotalDeLeiloes(){
		JsonPath jsonPath = given()
				.header("Accept", "application/json")
				.get("/leiloes/total")
				.andReturn()
				.jsonPath();

		int qtd = jsonPath.getInt("int");

		Assert.assertEquals(3, qtd);
	}  
	
	@Test
    public void deveAdicionarEExcluirLeiloes() {
		XmlPath retorno = given()
                    .header("Accept", "application/xml")
                    .contentType("application/xml")
                    .body(leilaoGeladeira)
                .expect()
                    .statusCode(200)
                .when()
                    .post("/leiloes")
                .andReturn()
                    .xmlPath();

        Leilao leilaoResposta = retorno.getObject("leilao", Leilao.class);

        Assert.assertEquals("Geladeira", leilaoResposta.getNome());

        given()
            .contentType("application/xml")
            .body(leilaoResposta)
        .expect()
            .statusCode(200)
        .when()
            .delete("/leiloes/deleta")
        .andReturn().asString();
    }
}
