/*
 *  StringUtils
 *  
 *  1.0.0
 *  
 *  © Copyright 2018, Instituto de Gestão Previdenciária do Estado do Pará
 *  http://www.igeprev.pa.gov.br/
 *  Todos os direitos reservados e protegidos pela Lei nº9.610/98.
 */
package br.gov.pa.igeprev.siaag.utils;

import java.util.Base64;

/**
 * Classe Util para manipular string.
 * 
 * @author Aleixo Porpino Filho
 * @version 1.0.0
 * @since 08/02/2017
 */
public class StringUtils {

	/**
	 * Transforma a string passada como parâmetro para caixa baixa se a mesma
	 * não estiver nula.
	 *
	 * @param str
	 * @return String
	 */
	public static String toLowerCase(String str) {
		return str != null ? str.toLowerCase() : null;
	}

	/**
	 * Caso uma string seja vazia, este método irá retornar null. Caso contrário
	 * retornará a própria string.
	 *
	 * @param str
	 * @return String
	 */
	public static String emptyIsNull(String str) {
		return str != null && ! "".equals(str.trim()) ? str.trim() : null;
	}

	
	/**
	 * Transforma a string passada como parâmetro para caixa baixa se a mesma
	 * não estiver nula ou vazia. Caso contrário retorna null.
	 *
	 * @param str
	 * @return String
	 */
	public static String toLowerCaseEmpty(String str) {
		return str != null && str.trim().length() > 0 ? str.toLowerCase() : null;
	}

	/**
	 * Transforma a string passada como parâmetro para caixa baixa se a mesma
	 * não estiver nula ou vazia e remove os espaços em brancos das
	 * extremidades. Caso contrário retorna null.
	 *
	 * @param str
	 * @return String
	 */
	public static String toLowerCaseTrimEmpty(String str) {
		return str != null && str.trim().length() > 0 ? str.toLowerCase().trim() : null;
	}
	
	
	/**
	 * Verifica se a string passada como parâmetro é nula ou vazia.
	 *
	 * @param str
	 * @return boolean
	 */
	public static boolean isEmpty(String str) {
		return str == null || "".equals(str.trim());
	}
	
	/**
	 * Remove os caracteres especiais que geralmetne veem em mascaras de campos como
	 * telefone, CEP, CPF, entre outros.
	 * 
	 * @param str
	 * @return String
	 */
	public static String removeSpecialCharacter(String str) {
		return str != null && str.trim().length() > 0 ? 
				str.replaceAll("\\(", "")
				   .replaceAll("\\)", "")
				   .replaceAll("\\.", "")
				   .replaceAll("\\/", "")
				   .replaceAll("\\_", "")
				   .replaceAll("_", "")
				   .replaceAll("-", "")
				   .replaceAll("[- /.]", "")
				   .replaceAll("[-()]", "") : null;
	}
	
	/**
	 * Esse método retorna null caso a string seja igual a "null".
	 * 
	 * @param str
	 * @return String
	 */
	public static String convertNull(String str) {
		return str != null && ! str.equalsIgnoreCase("null") ? str : null;
	}
	
	/**
	 * Convert uma representação array de bytes para String base 64.
	 * 
	 * @param b byte[]
	 * @return String
	 */
	public static String byteToString(byte[] b) {
		String str = Base64.getEncoder().encodeToString(b);
		return str;
	}
	
	/**
	 * Convert uma representação base 64 String para arry de bytes.
	 * 
	 * @param String str
	 * @return byte[]
	 */
	public static byte[] stringToByte(String str) {
		byte[] b =  Base64.getDecoder().decode(str);
		return b;
	}
	
	/**
	 * Preenche com zeros a esquerda um determinado número caso a quantidade
	 * seja maior que o número. 
	 * <p>
	 * Por exemplo:
	 * <p>
	 * 1- Caso o número seja 240607 e a quantidade seja 5 o retorno será 240607.
	 * <p>
	 * 1- Caso o número seja 240607 e a quantidade seja 10 o retorno será 0000240607.
	 * 
	 * @param numero
	 * @param quantidade
	 * @return String
	 */
	public static String preencheZeros(long numero, int quantidade) {
		String temp = String.valueOf(numero);
		String retorno = "";
		
		if (quantidade < temp.length())
			return temp;
		else {
			for (int i = 0; i < (quantidade - temp.length()); i++)
				retorno = "0" + retorno;
			return retorno + temp;
		}
	}
}
