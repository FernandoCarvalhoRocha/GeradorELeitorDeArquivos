package entities;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import db.DB;

public class validacao {

	private int qtdLinhas;
	
	public validacao() {
		
	}
	public validacao(int qtdLinhas) {
		this.qtdLinhas = qtdLinhas;
	}
	public int getQtdLinhas() {
		return qtdLinhas;
	}

	public void validaLinhas() {
		String path = "C:\\Data\\Arquivo.txt";
		File arquivoLeitura = new File(path);
			try {
				LineNumberReader linhaLeitura = new LineNumberReader(new FileReader(path));
				linhaLeitura.skip(arquivoLeitura.length());
				int qtdLinha = linhaLeitura.getLineNumber();
				qtdLinhas = qtdLinha -1;
				
				
							
				linhaLeitura.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	
	}
	
	public void validaTabelas() {
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		
		
		try {
			conn = DB.getConnection();
			
			st = conn.createStatement();
			rs = st.executeQuery("select * from rg_arquivo");

			
			while (rs.next()) {
				
				if (rs.getInt("id_registro") == 0) {
					System.out.println("Coluna id_registro com valor nulo ");
					System.exit(0);
				}else if (rs.getDate("data_hora_geracao") == null) {
					System.out.println("Coluna data_hora_geracao com valor nulo ");
					System.exit(0);
				}else if ( rs.getInt("qtd_linhas") == 0  ) {
					System.out.println("Coluna qtd_linhas com valor nulo ");
					System.exit(0);
				}else if (rs.getString("situacao") == null || rs.getString("situacao") == "") {
					System.out.println("Coluna situacao com valor nulo ");
					System.exit(0);
				}else if (rs.getString("usuario") == null || rs.getString("usuario") == "") {
					System.out.println("Coluna usuario com valor nulo ");
					System.exit(0);
				}else if (rs.getString(6) == null || rs.getString("conteudo").isBlank()) {
					System.out.println("Coluna conteudo com valor nulo ");
					System.exit(0);
				}
		
				}
			} catch(SQLException e) {
				e.printStackTrace();
			} finally {
				
			}
	}
	
	public void validaConteudo() {
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		
		
		try {
			conn = DB.getConnection();
			
			st = conn.createStatement();
			rs = st.executeQuery("select * from rg_arquivo ");
			
			
			
			while (rs.next()) {
				if (rs.getString("conteudo").contains("1234567891012345678910")) {
					
				}else {
					System.out.println("Sequência do conteúdo inválida. ");
					System.exit(0);
				}
				}
		
				
			} catch(SQLException e) {
				e.printStackTrace();
			} finally {
				
			}
	}
	
	
	
	
	
	
}


