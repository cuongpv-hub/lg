package vn.vsd.agro.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import vn.vsd.agro.domain.User;

public class EmailSender {
	
	@Autowired
    private MailSender mailSender;
    
    @Autowired
    private SimpleMailMessage defaultMessage;
    
    private String activeUserSubject;
    private String activeUserBody;
    private String forgetPasswordSubject;
    private String forgetPasswordBody;
    
    public EmailSender(String activeUserSubject, String activeUserBody, 
    		String forgetPasswordSubject, String forgetPasswordBody) {
		this.activeUserSubject = activeUserSubject;
		this.activeUserBody = activeUserBody;
		this.forgetPasswordSubject = forgetPasswordSubject;
		this.forgetPasswordBody = forgetPasswordBody;
	}
    
    public void send(String to, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		defaultMessage.copyTo(message);
		
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        
        mailSender.send(message);
    }
    
    public boolean sendForActiveUser(String activeUrl, User user) {
    	String realActiveUrl = activeUrl 
    			+ (activeUrl.indexOf("?") > 0 ? "&" : "?") 
    			+ "activeCode=" + user.getValidToken();
    	
    	String mailBody = this.activeUserBody.replace("[ActiveUrl]", realActiveUrl);
    	
    	if (JavaMailSender.class.isAssignableFrom(mailSender.getClass()))
    	{
    		try {
	    		JavaMailSender javaMailSender = (JavaMailSender) mailSender;
	    		MimeMessage message = javaMailSender.createMimeMessage();
	    			message.setSubject(this.activeUserSubject);
				
	    		MimeMessageHelper helper = new MimeMessageHelper(message, true);
	            helper.setFrom(defaultMessage.getFrom());
	            helper.setBcc(defaultMessage.getBcc());
	            helper.setCc(defaultMessage.getCc());
	            helper.setTo(user.getEmail());
	            helper.setText(mailBody, true);
	            
	            javaMailSender.send(message);
	            return true;
    		} catch (MessagingException e) {
    			
			} catch (Exception e) {
				
			}
    	}
    	
    	try {
			SimpleMailMessage message = new SimpleMailMessage();
			defaultMessage.copyTo(message);
			
	        message.setTo(user.getEmail());
	        message.setSubject(this.activeUserSubject);
	        message.setText(mailBody);
	        
	        mailSender.send(message);
	        
	        return true;
    	} catch (Exception e) {
			
		}
    	
    	return false;
    }
    
    public boolean sendForForgetPassword(String forgetPasswordUrl, User user) {
    	String realActiveUrl = forgetPasswordUrl
    			+ (forgetPasswordUrl.indexOf("?") > 0 ? "&" : "?")
    			+ "validCode=" + user.getValidToken();
    	
    	String mailBody = this.forgetPasswordBody.replace("[ForgetPasswordUrl]", realActiveUrl);
    	
    	if (JavaMailSender.class.isAssignableFrom(mailSender.getClass()))
    	{
    		try {
	    		JavaMailSender javaMailSender = (JavaMailSender) mailSender;
	    		MimeMessage message = javaMailSender.createMimeMessage();
	    			message.setSubject(this.forgetPasswordSubject);
				
	    		MimeMessageHelper helper = new MimeMessageHelper(message, true);
	            helper.setFrom(defaultMessage.getFrom());
	            helper.setBcc(defaultMessage.getBcc());
	            helper.setCc(defaultMessage.getCc());
	            helper.setTo(user.getEmail());
	            helper.setText(mailBody, true);
	            
	            javaMailSender.send(message);
	            return true;
    		} catch (MessagingException e) {
				//e.printStackTrace();
			} catch (Exception e) {
				//e.printStackTrace();
			}
    	}
    	
    	try {
			SimpleMailMessage message = new SimpleMailMessage();
			defaultMessage.copyTo(message);
			
	        message.setTo(user.getEmail());
	        message.setSubject(this.forgetPasswordSubject);
	        message.setText(mailBody);
	        
	        mailSender.send(message);
	        
	        return true;
    	} catch (Exception e) {
			//e.printStackTrace();
		}
    	
    	return false;
    }
}
