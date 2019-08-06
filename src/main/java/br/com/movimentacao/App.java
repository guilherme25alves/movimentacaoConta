package br.com.movimentacao;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import br.com.movimentacao.bean.PagamentosBean;
import br.com.movimentacao.bean.RecebimentosBean;

/**
 * @author Guilherme Alves
 * @version 1.0
 * 
 * App : Classe que principal que gera o relatório e executa o método main com toda lógica e retorno de informações
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {	
    	Gson gson = new Gson();
    
    	Type listTypeP = new TypeToken<List<PagamentosBean>>(){}.getType();
    	List<PagamentosBean> pagamentos,pagamentosTratados;
        PagamentosBean pagamento = new PagamentosBean();
       
        Type listTypeR = new TypeToken<List<RecebimentosBean>>(){}.getType();
    	List<RecebimentosBean> recebimentos,recebimentosTratados;
        RecebimentosBean recebimento = new RecebimentosBean();
        
        // Recuperando dados do arquivo de log
        LogMovimentacao lm = new LogMovimentacao();
        JsonArray jsPagamento = lm.retornaArray(2);
        pagamentosTratados = gson.fromJson(jsPagamento, listTypeP);
        JsonArray jsRecebimento = lm.retornaArray(1);
        recebimentosTratados = gson.fromJson(jsRecebimento, listTypeR);

        // Recuperando dados dos pagamentos - API
        pagamentos = gson.fromJson(pagamento.retornaPagamentos(), listTypeP);
        pagamentos.addAll(pagamentosTratados);
        
        // Recuperando dados dos recebimentos - API 
        recebimentos = gson.fromJson(recebimento.retornaRecebimentos(), listTypeR);
        recebimentos.addAll(recebimentosTratados);
        
        // Formatar 2 casas decimais
        DecimalFormat formatador = new DecimalFormat("#.00"); 
     
        // Variaveis para o relatório
        float valor=0;
        float totDivesao=0, totViagem=0, totTransporte=0, totHospedagem=0, totAlimentacao=0, totVestuario=0, totHigiene=0;
        float totalSaldo=0, categoriaMaiorGasto=0, totalGasto=0, totalRecebido=0, valorMesMaiorGasto =0;
        float mesesAnoCorrenteGasto[] = {0,0,0,0,0,0,0,0,0,0,0,0};
        int mesMaiorGasto=0;
        
       // System.out.println(" =========== RELATÓRIO DE MOVIMENTAÇÕES DE CONTA ===========");
        
        for (PagamentosBean pagamentosBean : pagamentos) {
			
        	//Converter String de valores em Double
        	String valorFormatado = pagamentosBean.getValor().replace(',', '.').replace(" ", "");
        	valor = Float.parseFloat(valorFormatado);
        	
        	// Converter String em Data
        	String dataFormatada = pagamentosBean.getData().replace(" ", "").concat("/2019");
        	//System.out.println(dataFormatada);
        	DateTimeFormatter formatData = DateTimeFormatter.ofPattern("d/MMM/yyyy");
        	LocalDate localDate = LocalDate.parse(dataFormatada, formatData);
        	//System.out.println(localDate);
        	
        	// Recuperando apenas o mês para fazer o agrupamento
        	String mesPagamento = localDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        	//System.out.println(mesPagamento);
        	
        	// Somando o total gasto por mês 
        	if(mesPagamento.equals("Maio")) {
        		mesesAnoCorrenteGasto[4]+=valor;
        	}else if(mesPagamento.equals("Julho")) {
        		mesesAnoCorrenteGasto[6]+=valor;
        	}else if(mesPagamento.equals("Junho")) {
        		mesesAnoCorrenteGasto[5]+=valor;
        	}if(mesPagamento.equals("Fevereiro")) {
        		mesesAnoCorrenteGasto[1]+=valor;
        	}if(mesPagamento.equals("Abril")) {
        		mesesAnoCorrenteGasto[3]+=valor;
        	}if(mesPagamento.equals("Março")) {
        		mesesAnoCorrenteGasto[2]+=valor;
        	}
        	
        	if(pagamentosBean.getCategoria().equalsIgnoreCase("viagem")) {
        		totViagem+= valor;
        	} else if(pagamentosBean.getCategoria().equalsIgnoreCase("diversao")) {
        		totDivesao += valor;
        	} else if (pagamentosBean.getCategoria().equalsIgnoreCase("transporte")) {
        		totTransporte += valor; 
        	} else if(pagamentosBean.getCategoria().equalsIgnoreCase("hospedagem")) {
        		totHospedagem += valor;
        	} else if(pagamentosBean.getCategoria().equalsIgnoreCase("alimentacao") || pagamentosBean.getCategoria().equalsIgnoreCase("alimentação") ) {
        		totAlimentacao += valor;
        	} else if(pagamentosBean.getCategoria().equalsIgnoreCase("vestuario")) {
        		totVestuario += valor;
        	} else if(pagamentosBean.getCategoria().equalsIgnoreCase("higiene")) {
        		totHigiene += valor;
        	} 
        	 
        	// Somando o valor total de pagamentos do Cliente
        	//totalGasto += valor; // Retornou   R$-1546,45 : valor errado
        	totalGasto = totViagem + totDivesao + totTransporte + totHospedagem + totAlimentacao + totVestuario +totHigiene; 	
		}
        
        //  cada categoria recebe seu total gasto
        float categoriasGasto[] = {totDivesao, totViagem, totTransporte, totHospedagem, totAlimentacao, totVestuario, totHigiene};
        String categorias[] = {"Diversão","Viagem","Transporte","Hospedagem","Alimentação","Vestuário","Higiene"};
        
        for(RecebimentosBean recebimentosBean : recebimentos) {
        	//Converter String de valores em Double
        	String valorFormatado = recebimentosBean.getValor().replace(',', '.').replace(" ", "");
        	valor = Float.parseFloat(valorFormatado);
        	totalRecebido+=valor;
        }
        
        String categoriaMaior="";
        // Comparar qual categoria gastou mais
        for(int i=0; i<categoriasGasto.length; i++) {
        	if(categoriasGasto[i] < categoriaMaiorGasto) {
        		categoriaMaiorGasto = categoriasGasto[i];
        		categoriaMaior = categorias[i];
        	}
        }
        
        // Comparar qual mês o cliente gastou mais
        for(int i=0; i<mesesAnoCorrenteGasto.length; i++) {
        	if(mesesAnoCorrenteGasto[i] < valorMesMaiorGasto) {
        		valorMesMaiorGasto = mesesAnoCorrenteGasto[i];
        		mesMaiorGasto=i+1;
        	}
        }
        
        // Saldo após todas os pagamentos e recebimentos
        totalSaldo = totalRecebido + totalGasto;
            
         Scanner entrada = new Scanner(System.in);
         int menuItem = 0;
         
         do {
        	 System.out.println("															");
        	 System.out.println(" ================== 	RELATÓRIO DE MOVIMENTAÇÕES DE CONTA 	 ====================");
        	 System.out.println("=====================================================================================");
        	 System.out.println("=Digite um dos números abaixo : =====================================================");
        	 System.out.println("=====================================================================================");
             System.out.println("=[ 1 ] - TOTAL POR CATEGORIA =============== [ 2 ] - TOTAL RECEBIDO==================");
        	 System.out.println("=====================================================================================");
             System.out.println("=[ 3 ] - TOTAL GASTO======================== [ 4 ] - MÊS E VALOR COM MAIOR GASTO ====");
        	 System.out.println("=====================================================================================");
        	 System.out.println("=[ 5 ] - CATEGORIA E VALOR COM MAIOR GASTO== [ 6 ] - SALDO TOTAL ====================");
        	 System.out.println("=====================================================================================");
        	 System.out.println("=[ 7 ] - RELATÓRIO COMPLETO================= [ 9 ] -  SAIR ==========================");
        	 System.out.println("=====================================================================================");
        	 System.out.println("															");
        	 
        	 menuItem = entrada.nextInt();
        	 
        	 switch (menuItem) {
			case 1:
				System.out.println("============= TOTAL GASTO POR CATEGORIA ===================");
		         System.out.println("");
		         System.out.println("Viagem : R$" + formatador.format(totViagem));
		         System.out.println("Diversão : R$"+ formatador.format(totDivesao));
		         System.out.println("Transporte : R$"+ formatador.format(totTransporte));
		         System.out.println("Hospedagem : R$"+ formatador.format(totHospedagem));
		         System.out.println("Alimentação : R$"+ formatador.format(totAlimentacao));
		         System.out.println("Vestuário : R$"+ formatador.format(totVestuario));
		         System.out.println("Higiene : R$ " + formatador.format(totHigiene));
		         System.out.println("");
		         System.out.println("============================================================"); 
		         System.out.println("");
				break;
			case 2:
		         System.out.println("*************************************************************"); 
				 System.out.println("============= TOTAL RECEBIDO PELO CLIENTE===================");
		         System.out.println("");
		         System.out.println("Valor total recebido : R$"+formatador.format(totalRecebido));
		         System.out.println("");
		         System.out.println("*************************************************************"); 
				break;
			case 3:
		         System.out.println("*************************************************************"); 
				System.out.println(" ================= TOTAL GASTO PELO CLIENTE=================");
		         System.out.println("");
		         System.out.println("Valor total : R$"+formatador.format(totalGasto));
		         System.out.println("");
		         System.out.println("*************************************************************"); 
				break;
			case 4:
		         System.out.println("*************************************************************"); 
				 System.out.println("========= SALDO TOTAL DE MOVIMENTAÇÕES DO CLIENTE===========");
		         System.out.println("");
		         System.out.println("Saldo cliente : R$"+formatador.format(totalSaldo));
		         System.out.println("");
		         System.out.println("*************************************************************"); 
				break;
			case 5:
		         System.out.println("*************************************************************"); 
		         System.out.println(" ======= MÊS COM MAIOR GASTO E VALOR DO CLIENTE NO ANO======");
		         System.out.println("");
		         System.out.println("MÊS:"+mesMaiorGasto + " VALOR GASTO: R$"+formatador.format(valorMesMaiorGasto));
		         System.out.println("");
		         System.out.println("*************************************************************"); 
				break;
			case 6:
		         System.out.println("*************************************************************"); 
		         System.out.println("======= CATEGORIA E VALOR COM MAIOR GASTO PELO CLIENTE======");
		         System.out.println("");
		         System.out.println("CATEGORIA:"+categoriaMaior+ " VALOR GASTO: R$"+formatador.format(categoriaMaiorGasto));
		         System.out.println("");
		         System.out.println("*************************************************************"); 
				break;
			case 7:
		         System.out.println("*************************************************************"); 
		         System.out.println("============= TOTAL GASTO POR CATEGORIA ===================");
		         System.out.println("");
		         System.out.println("Viagem : R$" + formatador.format(totViagem));
		         System.out.println("Diversão : R$"+ formatador.format(totDivesao));
		         System.out.println("Transporte : R$"+ formatador.format(totTransporte));
		         System.out.println("Hospedagem : R$"+ formatador.format(totHospedagem));
		         System.out.println("Alimentação : R$"+ formatador.format(totAlimentacao));
		         System.out.println("Vestuário : R$"+ formatador.format(totVestuario));
		         System.out.println("Higiene : R$ " + formatador.format(totHigiene));
		         System.out.println("");
		         System.out.println("============================================================"); 
		         System.out.println("");
		        
		         System.out.println("============= TOTAL RECEBIDO PELO CLIENTE===================");
		         System.out.println("");
		         System.out.println("Valor total recebido : R$"+formatador.format(totalRecebido));
		         System.out.println("");
		         System.out.println("============================================================"); 
		         
		         
		         System.out.println(" ================= TOTAL GASTO PELO CLIENTE=================");
		         System.out.println("");
		         System.out.println("Valor total : R$"+formatador.format(totalGasto));
		         System.out.println("");
		         System.out.println("============================================================"); 
		         
		         System.out.println("========= SALDO TOTAL DE MOVIMENTAÇÕES DO CLIENTE===========");
		         System.out.println("");
		         System.out.println("Saldo cliente : R$"+formatador.format(totalSaldo));
		         System.out.println("");
		         System.out.println("============================================================"); 
		         
		         System.out.println(" ======= MÊS COM MAIOR GASTO E VALOR DO CLIENTE NO ANO======");
		         System.out.println("");
		         System.out.println("MÊS:"+mesMaiorGasto + " VALOR GASTO: R$"+formatador.format(valorMesMaiorGasto));
		         System.out.println("");
		         System.out.println("============================================================");
		         
		         System.out.println("======= CATEGORIA E VALOR COM MAIOR GASTO PELO CLIENTE======");
		         System.out.println("");
		         System.out.println("CATEGORIA:"+categoriaMaior+ " VALOR GASTO: R$"+formatador.format(categoriaMaiorGasto));
		         System.out.println("");
		         System.out.println("============================================================");
		         System.out.println("*************************************************************"); 
				break;
			case 9:
				System.out.println("SAIDA DO PROGRAMA");
				System.exit(0);
				break;
			default:
				System.out.println("VALOR FORA DO ESCOPO DIGITE ENTRE 1 E 7 OU 9 PARA SAIR DO PROGRAMA");
				menuItem = entrada.nextInt();
				break;
			}
        	 
         }while(menuItem != 9);
         	entrada.close();
    }
}

