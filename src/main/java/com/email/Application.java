package com.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.email.model.Mail;
import com.email.service.EmailService;
 
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.email"
})
public class Application {

	public static void main(String[] args) {
			 
		Mail mail = new Mail();
	    mail.setMailFrom("your.package.mx@gmail.com");
	    mail.setMailTo("karime.perez5588@alumnos.udg.mx");
	    mail.setMailSubject("Tu Paquete Ha Sido Recibido");
	    mail.setMailContent("\n\n¡¡Prueba Exitosa!!\n\n");
	 
	    ApplicationContext ctx = SpringApplication.run(Application.class);
	    EmailService mailService = (EmailService) ctx.getBean("mailService");
	    mailService.sendEmail(mail);
		
	}
 
}
