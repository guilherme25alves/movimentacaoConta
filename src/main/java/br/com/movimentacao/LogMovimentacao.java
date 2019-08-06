package br.com.movimentacao;

import java.io.BufferedReader;
import java.io.FileReader;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * @author Guilherme Alves 
 * @version 1.0
 *  
 *  Classe responsável por processar os dados do arquivo de Log
 *  Tem como retorno um objeto JsonArray, dependendo de qual for o parâmetro
 *  Parâmetro é um inteiro, sendo 1 para retorno de pagamentos e 2 para o retorno de recebimentos 
 * */
public class LogMovimentacao {

	public JsonArray retornaArray(int situacao) throws Exception {
		
		BufferedReader br = new BufferedReader(new FileReader("resources/movimentacao.log"));
		String data, descricao, valor, valorAlterado, categoria, categoriaAlterada;
		String linha, linhaDesc, linhaValor;
		
		JsonArray arrayPagamento = new JsonArray();
		JsonArray arrayRecebimento = new JsonArray();
		JsonArray retorno = new JsonArray();
		
		while((linha = br.readLine()) != null) {
			data = linha.substring(0,linha.indexOf(" ")).trim().toLowerCase();
			data = data.replaceAll("-", "/");
			
			if(data.contains("feb")) {
				data = data.replace("feb", "fev");
			}else if(data.contains("may")) {
				data = data.replace("may", "mai");
			}else if(data.contains("apr")) {
				data = data.replace("apr", "abr");
			}
			//System.out.println("Valor da data : " + data);
			
			linhaDesc = linha.substring(linha.indexOf("  "), linha.length()).trim();
			descricao = linhaDesc.substring(0,linhaDesc.indexOf("  ")).trim();
			//System.out.println("Valor da descrição : "+ descricao);
			
			linhaValor = linhaDesc.substring(linhaDesc.indexOf(" "),linhaDesc.length()).trim();
			valor = linhaValor.substring(0,linhaValor.indexOf(" ")).trim();
			valor = linhaValor;
		    valorAlterado = valor.replaceAll("[^-0-9,]","").replace(",", ".").trim();
			//System.out.println("Valor : "+valorAlterado);
	
			categoria = linha.substring((linha.lastIndexOf(" ")+1), linha.length());
			categoriaAlterada = categoria.replaceAll("[^A-Za-z]", "");
			//System.out.println("Valor da categoria : "+ categoriaAlterada);
			
			if(Double.parseDouble(valorAlterado) < 0) {
				
				JsonObject jsonObjetoPagamento = new JsonObject();
				jsonObjetoPagamento.addProperty("descricao", descricao);
				jsonObjetoPagamento.addProperty("moeda", "R$");
				jsonObjetoPagamento.addProperty("valor", valorAlterado);
				jsonObjetoPagamento.addProperty("categoria", categoriaAlterada);
				jsonObjetoPagamento.addProperty("data", data);
				arrayPagamento.add(jsonObjetoPagamento);
				//System.out.println("Elemento JSON criado | Pagamento");
				
			}else {

				JsonObject jsonObjectRecebimento = new JsonObject();
				jsonObjectRecebimento.addProperty("descricao", descricao);
				jsonObjectRecebimento.addProperty("moeda", "R$"); 
				jsonObjectRecebimento.addProperty("valor", valorAlterado);
				jsonObjectRecebimento.addProperty("data", data);
				arrayRecebimento.add(jsonObjectRecebimento);
				//System.out.println("Elemento JSON criado | Recebimento");
			}
			//System.out.println(" ---------------------------------------");
			
			
			// Situação : 1 - Recebimentos | 2 - Pagamentos
			if (situacao == 1) {
				retorno = arrayRecebimento;
			}else if(situacao == 2) {
				retorno = arrayPagamento;
			}				
		}
		
		br.close();
		return retorno;
	}
	
	public static void main(String[] args) throws Exception {
		
		LogMovimentacao lm = new LogMovimentacao();
		lm.retornaArray(1);	
	} 
	
}
