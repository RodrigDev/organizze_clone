package com.example.organizze.helper;

import java.text.SimpleDateFormat;

public class DateCustom {

    public static String dataAtual(){

        long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(data);
        return dataString;

    }

    public static String mesAnoDataEscolhida(String data){
        //23/01/2018
        String retornoData[] = data.split("/");
        String dia = retornoData[0];//Dia 23
        String mes = retornoData[1];//Mês 01
        String ano = retornoData[2];//Ano 2018

        String mesAno = mes + ano;
        return mesAno;

    }

}
