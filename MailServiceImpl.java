package com.onward.hrservice.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.onward.hrservice.common.Utility;
import com.onward.hrservice.entity.UserMaster;
import com.onward.hrservice.repository.UserMasterRepository;
import com.onward.hrservice.service.MailService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MailServiceImpl implements MailService {
	
	private static final String TEXT_HTML = "text/html; charset=utf-8";

	@Value("${smtp.mail.sender.mail}")
	private String senderMail;

	@Value("${smtp.mail.sender.name}")
	private String senderName;

	@Autowired
	private Session session;
	
	@Autowired
	UserMasterRepository userMasterRepo;

	@Override
	public Map<String, Object> sendForgotPassword(String toRecipient, Integer empNumber) {
		UserMaster userMaster = userMasterRepo.findByEmpCode(empNumber);
		log.info("User Details ::{}", userMaster);
		if (!toRecipient.contains("onwardgroup.com") || userMaster == null) {
			return Utility.fail("Mail ID or User ID is not valid");
		} else {
			try {
				String subject = "HRMS: Password Change Request";
				String otp = generateOTP(6);
				saveOTP(otp, userMaster);
				String body = "<p>Greetings...!!!</p><p style=\"margin-top:0;margin-bottom:12pt;\">Your Verification code : "
						+ otp
						+ "<br aria-hidden=\"true\"></p><p>Use the Verification code to reset your password in HRMS Portal</p><p style=\"margin-top:0;margin-bottom:12pt;\">Thanks & regards.,<br aria-hidden=\"true\">Admin - HRMS</p>";
				send(senderMail, senderName, toRecipient, null, null, subject, body);
			} catch (MessagingException | UnsupportedEncodingException e) {
				log.info("Mail failed to send due to", e);
			}
			return Utility.success("Verification Code Sent Successfully");
		}

	}
	
	public void send(String fromRecipient, String fromName, String toRecipients, String ccRecipients,
			String bccRecipients, String subject, String body) throws MessagingException, UnsupportedEncodingException {
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(fromRecipient, fromName));
		// Set To String.join(",",toRecipients)
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toRecipients));
		log.info("send ::: To Recepients ::{}", toRecipients);
		// Set CC
		if(ccRecipients != null) {
			message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccRecipients));
			log.info("send ::: CC Recepients ::{}", ccRecipients);
		}
		// Set BCC
		if(bccRecipients != null) {
			message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bccRecipients));
			log.info("send ::: BCC Recepients ::{}", bccRecipients);
		}
		// Set Subject
		message.setSubject(subject);
		message.setContent(body, TEXT_HTML);
		log.info("send ::: Body");
		Transport.send(message);
		log.info("Mail sent successfully");
	}
	
	private String generateOTP(int length) {
        StringBuilder otp = new StringBuilder();
        // Generate random digits
        for (int i = 0; i < length; i++) {
            int randomDigit = ThreadLocalRandom.current().nextInt(0, 10);
            otp.append(randomDigit);
        }
        return otp.toString();
    }
	
	private void saveOTP(String otp, UserMaster userMaster) {
		userMaster.setUserPwSecretKey(otp);
		userMasterRepo.save(userMaster);
	}

}
