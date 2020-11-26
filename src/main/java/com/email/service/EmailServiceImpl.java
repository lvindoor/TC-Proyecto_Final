package com.email.service;

import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.email.model.Mail;
 
@Service("mailService") // Declaracion de Servicio
public class EmailServiceImpl implements EmailService {
 
	/* Inyeccion de Dependencias */
    @Autowired
    JavaMailSender mailSender;
 
    /* Implementacion del metodo */
    public void sendEmail(Mail mail) {
        MimeMessage mimeMessage = mailSender.createMimeMessage(); // Modelo de mensaje tipo Mail
 
        try {
 
        	/* Rellenamos el Objeto para el Mail */
       
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
 
            mimeMessageHelper.setSubject(mail.getMailSubject());
            mimeMessageHelper.setFrom(new InternetAddress(mail.getMailFrom(), "Your Package"));
            mimeMessageHelper.setTo(mail.getMailTo());
            mimeMessageHelper.setText(mail.getMailContent());
          
            /* Agregamos imagenes de paquetes prescritas */
            int imagenRandom = (int)(Math.random()*10+1); // numeros random de 0 a 10 
            mimeMessageHelper.addAttachment( "Paquete-recogido.png", new File("photos/" + imagenRandom + ".jpg"));

            mailSender.send(mimeMessageHelper.getMimeMessage());
 
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
 
}
