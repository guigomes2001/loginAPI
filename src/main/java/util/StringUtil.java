package util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * @author @gui_gomes_18
 */

public class StringUtil {

	public static String applySha256(String input) {
		try {
			MessageDigest msgDigest = MessageDigest.getInstance("SHA-256");

			byte[] hash = msgDigest.digest(input.getBytes("UTF-8"));
			StringBuilder hexString = new StringBuilder();

			for (byte elem : hash) {
				String hex = Integer.toHexString(0xff & elem);
				if (hex.length() == 1) {
					hexString.append('0');
					hexString.append(hex);
				}
			}
			return hexString.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static String getStringDaChave(Key key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}

	public static String getDataTimestampFormatada(Long milis) {
		return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(milis));
	}

	public static String retirarCaracteresEspeciais(String input) {
		if (input == null) {
			return null;
		}
		return input.replaceAll("[^a-zA-Z0-9]", "");
	}

	public static String removerEspacosEmBranco(String input) {
		if (input == null) {
			return null;
		}
		return input.replaceAll("\\s+", "");
	}

	public static String formatCNPJ(String cnpj) {
		cnpj = cnpj.replaceAll("\\D", "");

		cnpj = String.format("%14s", cnpj).replace(' ', '0');

		String formattedCNPJ = cnpj.replaceFirst("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
		return formattedCNPJ;
	}

	public static String formatCNPJ(long cnpjNumber) {
		String cnpj = Long.toString(cnpjNumber);

		return formatCNPJ(cnpj);
	}

	public static String formatCPF(String cpf) {
		cpf = cpf.replaceAll("\\D", "");
		cpf = String.format("%11s", cpf).replace(' ', '0');

		String formattedCPF = cpf.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
		return formattedCPF;
	}

	public static String formatCPF(long cpfNumber) {
		String cpf = Long.toString(cpfNumber);

		return formatCPF(cpf);
	}

	public static String removerCaracteresEspeciais(String value) {
		return value.replaceAll("[\\p{M}a-zA-Z&&[^a-zA-Z0-9. ]]", "").replaceAll("[^a-zA-Z0-9. ]", "");
	}

	public static String corrigirEncoding(String value) {
		if (value == null) {
			return null;
		}

		// Tenta converter de ISO-8859-1 para UTF-8
		String utf8String = new String(value.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

		// Se ainda houver caracteres inválidos, força para UTF-8
		if (!isUTF8(utf8String)) {
			utf8String = new String(value.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
		}

		return utf8String;
	}

	public static boolean isUTF8(String value) {
		try {
			byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
			return value.equals(new String(bytes, StandardCharsets.UTF_8));
		} catch (Exception e) {
			return false;
		}
	}
}
