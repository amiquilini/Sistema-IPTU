/**
 * AP1_2021_1_Q2
 * Created on 4/22/2021
 *
 * @author: AmandaMiquilini
 **/

import java.time.*;
import java.time.format.*;

public class AP1_2021_1_Q2 {
	public static void main(String[] args) {
    Imovel i1 = new Imovel(100, "1/1/1980", "centro");
    System.out.println("IPTU do imóvel " + i1.getCodigo() + ": R$ " + i1.getValorIPTU());
    Imovel i2 = new Apto(100, "1/1/1980", "periferia", 2, "fundos");
    Imovel i3 = new Loja(100, "1/1/1980", "centro", false);
    Imoveis propriedades = new Imoveis(1000);
    propriedades.adicionaImovel(i1);
    propriedades.adicionaImovel(i2);
    propriedades.adicionaImovel(i3);
    System.out.println("Total de IPTU a ser arrecadado: R$ " + propriedades.calculaIPTUTotal());
	}
}

class Imovel {
    float aliquota, valorVenal, fator_idade;
    String l;
    int t, codigo, idade;
    static int prox_imovel = 1;

    LocalDate d;
    Period p;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

    //Construtor
    Imovel(int tamanho, String data_construcao, String localizacao){
        t = tamanho;
        d = LocalDate.parse(data_construcao, formatter); //converte a data fornecida como string em um objeto da classe LocalDate
        l = localizacao;
        aliquota = 0.1F; //alíquota base do Imovel
        codigo = prox_imovel++; //atribui um código a cada imóvel criado
    }
    //Métodos
    public int getCodigo(){
        return this.codigo;
    }
    public float getValorIPTU(){
        LocalDate hoje = LocalDate.now(); //armazena a data atual para o cálculo dinâmico da idade
        idade = p.between(this.d, hoje).getYears(); //calcula a idade (em anos) do imóvel

        if(idade < 10){ //fator_idade para ajustar o valor venal de acordo com a idade do imóvel
            fator_idade = 1;
        } else if (idade > 10 && idade < 30){
            fator_idade = 0.8F;
        } else {
            fator_idade = 0.6F;
        }

        if(this.l.equals("centro")){ //valor venal dependente da localização do imóvel
            valorVenal = 1000*this.t;
        } else {
            valorVenal = 500*this.t;
        }

        float iptu = valorVenal * fator_idade * this.aliquota;

        return iptu;
    }
}

class Apto extends Imovel {
    int nq;
    String pos;

    //Construtor
    Apto(int tamanho, String data_construcao, String localizacao, int num_quartos, String posicao) {
        super(tamanho, data_construcao, localizacao);
        nq = num_quartos;
        pos = posicao;
        if (this.nq <= 2 && this.pos.equals("fundos")){aliquota = 0.05F;} //se o apartamento tiver até 2 quartos e for de fundos, o valor da aliquota muda
    }
}

class Loja extends Imovel {
    boolean s;

    //Construtor
    Loja(int tamanho, String data_construcao, String localizacao, boolean shopping) {
        super(tamanho, data_construcao, localizacao);
        s = shopping;
        if (!s){aliquota = 0.08F;} //se a loja não estiver em um shopping, o valor da aliquota muda
    }
}

class Imoveis {
    int index;
    static int prox_index = 0;
    float iptu_total = 0F;
    Imovel[] arr_imoveis;

    //Construtor
    Imoveis(int max_imoveis){
        arr_imoveis = new Imovel[max_imoveis]; //cria o vetor com o número máximo de imoveis
    }

    //Métodos
    public void adicionaImovel(Imovel imovel){
        index = prox_index++; //guarda o número de imóveis adicionados no vetor
        this.arr_imoveis[index] = imovel; //adiciona o imóvel no vetor
    }
    public float calculaIPTUTotal(){
        for(int i = 0; i < index+1; i++){
            iptu_total = iptu_total + arr_imoveis[i].getValorIPTU(); //soma os iptus de todos os imóveis do vetor
        }
        return iptu_total;
    }
}
