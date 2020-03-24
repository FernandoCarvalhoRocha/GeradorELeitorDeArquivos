package application;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import db.DB;
import entities.validacao;

public class Program  {
	
	private static SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS ");
	@SuppressWarnings("unused")
	private static Date dataInicial;
	private static Date atualDate = new Date() ;
	private static String linha1Conteudo = new String();
	private static int linhas;
	private static int qtdLinhas;
	private static String path = "C:\\Data\\Arquivo.txt";
	
	
	
	public static void main(String[] args) {
	
		Scanner sc = new Scanner(System.in);
		
		validacao valida = new validacao();
		System.out.println("Data e hora de execução do programa: " + sdf1.format(atualDate));
		System.out.print("Informe a quantidade de linhas: ");
		linhas = sc.nextInt();
		
		geraArquivo();
		valida.validaLinhas();
		qtdLinhas = valida.getQtdLinhas();
		leituraArquivo();
		gravaArquivoEstaging();
		valida.validaTabelas();
		valida.validaConteudo();
		if (valida.getQtdLinhas() != linhas) {
			System.out.println("Quantidade gravada inválida!");
			System.exit(0);
		}
		
		gravaArquivoJaValidado();
		
		Date finalDate1 = new Date() ;
		
		System.out.println("Data e hora de finalização do programa: " + sdf1.format(finalDate1));
		System.out.println( "Arquivo gerado, validado e gravado com sucesso. " );
		
		sc.close();
		
	}
		public static void geraArquivo() {	
				
				
				
				Date dataHeader = new Date();
				dataInicial = dataHeader;
				try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
					bw.write("Header: " + sdf1.format(dataHeader));
					bw.newLine();
					for (int i = 1; i<=linhas; i++) {
						bw.write("1234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910");		
						bw.newLine();
						
					}
					Date finalDate = new Date();
					bw.write("Trailer: " + sdf1.format(finalDate));
					bw.close();
					
				} catch (IOException e) {
					System.out.println("Error: " + e.getMessage());
					} 
	}
		public static void leituraArquivo() {
			
			int contador = 0;
			String conteudo = new String();
			
			try (BufferedReader br = new BufferedReader(new FileReader (path))) {
				String lines = br.readLine();
				if (lines != null) {
					lines = br.readLine();
				}
				while (lines != null) {
					while(lines != null && contador <= 0){  
			              contador ++;
			              conteudo = lines;
					}
					lines = br.readLine();
					
						
				}
		        
			} catch (IOException e) {
				System.out.println("Erro: " + e.getMessage());
				}
			  if (contador == 1 ) {
	        	  linha1Conteudo = conteudo;
	           } else {
	        	   linha1Conteudo = "null";
	           }
		}
		public static  void gravaArquivoEstaging() {
					
			Connection conn = null;
			PreparedStatement ps = null;
			String leituraHeader = null;
			
			try (BufferedReader br = new BufferedReader(new FileReader (path))) {
				String header = br.readLine();
				String dataHeader = header.substring(8);
				leituraHeader = dataHeader;
						
			}catch(IOException e) {
					e.printStackTrace();
			}
		        
			
			
			try {
				conn = DB.getConnection();
				
				
				ps = conn.prepareStatement(
						"INSERT INTO rg_arquivo" 
						+ "(data_hora_geracao, qtd_linhas, situacao, usuario, conteudo) "
						+ "values"
						+ "(?, ?, ?, ?, ?)");
				
				ps.setTimestamp(1, new java.sql.Timestamp(sdf1.parse(leituraHeader).getTime()));
				ps.setInt(2, qtdLinhas);
				ps.setString(3, "CA");
				ps.setString(4, System.getProperty("user.name"));
				ps.setString(5,  linha1Conteudo);
			
				ps.executeUpdate();
				
			} catch(SQLException e) {
				e.printStackTrace();
			} catch(ParseException e) {
					e.printStackTrace();
			}
			
			
		}
		
		
		public static void gravaArquivoJaValidado() {
			
			Date dataConversao = new Date();
			Connection conn = null;
			Statement st = null;
			ResultSet rs = null;
			PreparedStatement ps = null;
			
			try {
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
				ps.setTimestamp(2, new java.sql.Timestamp(dataConversao.getTime()));
				ps.setInt(3, qtdlinhasArquivo);
				ps.setString(4, "VA");
				ps.setString(5, System.getProperty("user.name"));
				
				ps.executeUpdate();
				
				} catch(SQLException e) {
					e.printStackTrace();
					
				} finally {
					DB.closeStatement(st);
					DB.closeConnection();
				}
		}
		
		
		
}
		

	
		





