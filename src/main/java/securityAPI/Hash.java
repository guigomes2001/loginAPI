package securityAPI;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Hash {
	public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        String senhaOriginal = "minhaSenha123";
        String hash = encoder.encode(senhaOriginal);

        System.out.println("Senha original: " + senhaOriginal);
        System.out.println("Hash gerado: " + hash);

        boolean valida = encoder.matches("minhaSenha123", hash);
        System.out.println("Senha confere? " + valida);
    }
}
