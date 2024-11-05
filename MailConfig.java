package com.onward.hrservice.config;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Session;
import javax.mail.PasswordAuthentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class MailConfig {
	
	private static final String SERVIDOR_SMTP = "smtp.office365.com";
    private static final int PORTA_SERVIDOR_SMTP = 587;
    private static final String CONTA_PADRAO = "gnanasakthi_kannan@onwardgroup.com";
    private static final String SENHA_CONTA_PADRAO = "Sakthi@8395";
    
    @Bean
    public Session session() {
        final Properties config = new Properties();
        config.put("mail.smtp.auth", "true");
        config.put("mail.smtp.starttls.enable", "true");
        config.put("mail.smtp.host", SERVIDOR_SMTP);
        config.put("mail.smtp.port", PORTA_SERVIDOR_SMTP);
        config.put("mail.smtp.ssl.protocols", "TLSv1.2");

        log.info("Mail Session initialized successfully");

        return Session.getInstance(config, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(CONTA_PADRAO, SENHA_CONTA_PADRAO);
            }
        });
    }

}
