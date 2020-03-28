package testes;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;

import db.DB;

public class testeArquivo {
	
	public SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS ");
	public int linhas = 10;
	public String path = "C:\\Data\\Arquivo.txt";

	@Test
	public void testGeraArquivo() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(path));
			bw.write("Header: ");
			bw.newLine();
			for (int i = 1; i<=5; i++) {
				bw.write("12345678910");		
				bw.newLine();
				
			}
			bw.write("Trailer: ");
			bw.close();
			
		BufferedReader br = new BufferedReader(new FileReader ("C:\\Data\\Arquivo.txt"));
		String line = br.readLine();
        assertEquals("Header: ", line);
        line = br.readLine();
        for (int i = 1; i<=5; i++) {
	        assertEquals("12345678910", line);
	        line = br.readLine();
	        }
        assertEquals("Trailer: ", line);
        
        br.close();
	}

	@Test(expected = ParseException.class)
	public void testGravaArquivoEstaging() throws SQLException, ParseException{	
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Statement st = null;
		conn = DB.getConnection(); 
		st = conn.createStatement();
		ps = conn.prepareStatement(
					"INSERT INTO rg_arquivo" 
					+ "(data_hora_geracao, qtd_linhas, situacao, usuario, conteudo) "
					+ "values"
					+ "(?, ?, ?, ?, ?)");
			
			ps.setTimestamp(1, new Timestamp(sdf1.parse("28/03/2020 16:20:24:498").getTime()));
			ps.setInt(2, 5);
			ps.setString(3, "CA");
			ps.setString(4, System.getProperty("user.name"));
			ps.setString(5, "12345678910");
		
			ps.executeUpdate();
			
			rs = st.executeQuery("select * from rg_arquivo "); 
			
			assertEquals("28/03/2020 16:20:24:498", rs.getTimestamp(1));
			assertEquals(5, rs.getInt(2));
			assertEquals("CA", rs.getString(3));
			assertEquals(System.getProperty("user.name"), rs.getString(4));
			assertEquals("12345678910", rs.getString(5));
		
}
				
	@Test(expected = ParseException.class)
	public void testGravaArquivoJaValidado() throws SQLException, ParseException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		PreparedStatement ps = null;
			conn = DB.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery("select * from rg_arquivo "); 
			int idArquivo = 0;
			int qtdlinhasArquivo = 0;		
			while (rs.next()) {
				idArquivo = rs.getInt(1); 
				qtdlinhasArquivo = rs.getInt(3);
				}						
			ps = conn.prepareStatement(
					"INSERT INTO arquivo"
					+"(id_arquivo, data_hora_carga, qtd_linhas, situacao, usuario) "
					+ "values"
					+ "(?, ?, ?, ?, ?)");
			
			ps.setInt(1, idArquivo);
			ps.setTimestamp(2, new Timestamp(sdf1.parse("28/03/2020 16:20:24:498").getTime()));
			ps.setInt(3, qtdlinhasArquivo);
			ps.setString(4, "VA");
			ps.setString(5, System.getProperty("user.name"));
			
			ps.executeUpdate();
			
			rs1 = st.executeQuery("select * from arquivo "); 
			
			assertEquals(rs.getInt(1), rs1.getInt(1));
			assertEquals("28/03/2020 16:20:24:498", rs1.getTimestamp(2));
			assertEquals(rs.getInt(3), rs1.getInt(3));
			assertEquals("VA", rs1.getString(4));
			assertEquals(System.getProperty("user.name"), rs1.getString(5));
			
	}

}
