package br.com.movimentacao.bean;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;

/**
 * @author Guilherme Alves 
 * @version 1.0
 *  
 *  Classe modelo para movimentações de recebimentos do cliente
 * 
 * */
public class RecebimentosBean{

	 	private String data;
	    private String descricao;
	    private String moeda;
	    private String valor; 

	    public RecebimentosBean(){}

	    public RecebimentosBean(String data, String descricao,String moeda, String valor){
	        this.data = data;
	        this.descricao = descricao;
	        this.moeda = moeda;
	        this.valor = valor;
	    }

	    /**
	     * @param data the data to set
	     */
	    public void setData(String data) {
	        this.data = data;
	    }

	    /**
	     * @return the data
	     */
	    public String getData() {
	        return data;
	    }

	    /**
	     * @param descricao the descricao to set
	     */
	    public void setDescricao(String descricao) {
	        this.descricao = descricao;
	    }

	    /**
	     * @return the descricao
	     */
	    public String getDescricao() {
	        return descricao;
	    }

	    /**
	     * @param moeda the moeda to set
	     */
	    public void setMoeda(String moeda) {
	        this.moeda = moeda;
	    }

	    /**
	     * @return the moeda
	     */
	    public String getMoeda() {
	        return moeda;
	    }

	    /**
	     * @param valor the valor to set
	     */
	    public void setValor(String valor) {
	        this.valor = valor;
	    }

	    /**
	     * @return the valor
	     */
	    public String getValor() {
	        return valor;
	    }

	    public String retornaRecebimentos() throws Exception{
	    	String objeto = "";
	    	String output;
	    	
	        URL url = new URL("https://my-json-server.typicode.com/cairano/backend-test/recebimentos");
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Accept", "application/json");
	        if(conn.getResponseCode() != 200){
	            throw new RuntimeException("Failed : HTTP Error code" + conn.getResponseCode());
	        }
	        InputStreamReader in = new InputStreamReader(conn.getInputStream());
	        BufferedReader br = new BufferedReader(in);
	        
	        while((output = br.readLine()) != null){
	        	objeto += output;
	        }
	        conn.disconnect();
	    	return objeto;
	    }
	    
	    public void postRecebimentos(JsonObject jsonObjetoRecebimento) throws Exception{
			
	    	URL url = new URL("https://my-json-server.typicode.com/cairano/backend-test/recebimentos");
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type", "application/json; utf-8");
	        conn.setRequestProperty("Accept", "application/json");
	        conn.setDoOutput(true);
	        OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
	        osw.write(jsonObjetoRecebimento.toString());
			System.out.println("Objeto adicionado a API");
			
			/*	URL url = new URL("https://my-json-server.typicode.com/cairano/backend-test/recebimentos");
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		        conn.setRequestMethod("POST");
		        conn.setRequestProperty("Content-Type", "application/json; utf-8");
		        conn.setRequestProperty("Accept", "application/json");
		        conn.setDoOutput(true);
		        //OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
		        //osw.write(jsonObjetoPagamento.toString());
		        //osw.flush();
		        //osw.close();
				PrintStream ps = new PrintStream(conn.getOutputStream());
				ps.println(jsonObjetoPagamento);
		        conn.connect();
		        String resp = new Scanner(conn.getInputStream()).next();
		        System.out.println(" ");
		        System.out.println("Resposta API : " + resp);
		        System.out.println("Objeto adicionado a API");
				conn.disconnect(); */
	    
	    }

	    
	
}
