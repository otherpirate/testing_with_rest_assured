package br.com.caelum.leilao.teste;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.leilao.modelo.Usuario;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.path.xml.XmlPath;

import static com.jayway.restassured.RestAssured.*;

public class UsuarioWSTest {
	
	private Usuario usuarioGuilherme;
	private Usuario usuarioMauricio;

	@Before
	public void setUp(){
		usuarioMauricio = new Usuario(1L, "Mauricio Aniche", "mauricio.aniche@caelum.com.br");
		usuarioGuilherme = new Usuario(2L, "Guilherme Silveira", "guilherme.silveira@caelum.com.br");			
	}
	
	@Test
	public void deveRetornarListaDeUsuarios(){
		XmlPath xmlPath = given()
				.header("Accept", "application/xml")
				.get("/usuarios")
				.andReturn()
				.xmlPath();

		List<Usuario> usuarios = xmlPath.getList("list.usuario", Usuario.class);

		Assert.assertEquals(usuarioMauricio, usuarios.get(0));
		Assert.assertEquals(usuarioGuilherme, usuarios.get(1));
	}
	
	@Test
	public void deveRetornarUsuarioPorId(){
		JsonPath jsonPath = given()
				.header("Accept", "application/json")
				.parameter("usuario.id", 1)
				.get("/usuarios/show")
				.andReturn()
				.jsonPath();

		Usuario usuario = jsonPath.getObject("usuario", Usuario.class);

		Assert.assertEquals(usuarioMauricio, usuario);
	}
	
	@Test
	public void deveAdicionarEExcluirUmUsuario(){
		Usuario usuarioJoao = new Usuario("João", "joao@email.com.br"); 
		
		XmlPath xmlPath = given()
				.header("Accept", "application/xml")
				.contentType("application/xml")
				.body(usuarioJoao)
			.expect()
				.statusCode(200)
			.when()
				.post("/usuarios")
			.andReturn()
				.xmlPath();

		Usuario usuarioResposta = xmlPath.getObject("usuario", Usuario.class);

		Assert.assertEquals(usuarioJoao.getNome(), usuarioResposta.getNome());
		Assert.assertEquals(usuarioJoao.getEmail(), usuarioResposta.getEmail());
		
		given()
			.contentType("application/xml").body(usuarioResposta)
			.expect().statusCode(200)
			.when().delete("/usuarios/deleta").andReturn().asString();
	}	
}
