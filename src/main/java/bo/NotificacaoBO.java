package bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoBO {

	@Autowired
	private JavaMailSender mailSender;

	public void enviarEmail(String emailDestinatario, String assunto, String mensagem) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(emailDestinatario);
        mail.setSubject(assunto);
        mail.setText(mensagem);
        mailSender.send(mail);
	}
}
