package Control;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import Settings.Config;

public class EmailSender {
	private String user;
	  private String password;
	  private String host;
	  private String mittente;
	  private String destinatario;
	  private String oggetto;
	  private String text;
	 
	  /**
	  * Costruttore completo, richiede i parametri
	  * di connessione al server di posta
	  * @param user
	  * @param password
	  * @param host
	  * @param mittente
	  * @param destinatari
	  * @param oggetto
	  * @param allegati
	  */
	  public EmailSender(String user, String password, String host, 
	                     String mittente, String destinatario, 
	                     String oggetto, String allegato,String text){
	 
	    this.user = user;
	    this.password = password;
	    this.host = host;
	    this.mittente = mittente;
	    this.destinatario = destinatario;
	    this.oggetto = oggetto;
	    this.text=text;
	  }
	 
	  // Metodo che si occupa dell'invio effettivo della mail
	  public void inviaEmail() {
	    int port = 25; //porta 25 per non usare SSL
	 
	    Properties props = new Properties();
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.user", mittente);
	    props.put("mail.smtp.host", host);
	    props.put("mail.smtp.port", port);
	 
	    // commentare la riga seguente per non usare SSL 
	    //props.put("mail.smtp.starttls.enable","true");
	    props.put("mail.smtp.socketFactory.port", port);
	 
	    // commentare la riga seguente per non usare SSL 
	   // props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	    props.put("mail.smtp.socketFactory.fallback", "false");
	 
	    Session session = Session.getInstance(props, null);
	    session.setDebug(true);
	 
	    // Creazione delle BodyParts del messaggio
	    MimeBodyPart messageBodyPart1 = new MimeBodyPart();
	 
	    try{
	      // COSTRUZIONE DEL MESSAGGIO
	      Multipart multipart = new MimeMultipart();
	      MimeMessage msg = new MimeMessage(session);
	 
	      // header del messaggio
	      msg.setSubject(oggetto);
	      msg.setSentDate(new Date());
	      msg.setFrom(new InternetAddress(mittente));
	 
	      // destinatario
	      msg.addRecipient(Message.RecipientType.TO,
	      new InternetAddress(destinatario));
	      if(Config.getTest()==0){
	    	  msg.addRecipient(Message.RecipientType.CC,
	    	  new InternetAddress(Config.getRespItMail()));
	      }

	      msg.addRecipient(Message.RecipientType.CC,
	    	      new InternetAddress(Config.getRespMail()));
	      
	      // corpo del messaggio
	      messageBodyPart1.setText(text);
	      multipart.addBodyPart(messageBodyPart1);
	 
	      // allegato al messaggio
	      //DataSource source = new FileDataSource(allegato);
	      //messageBodyPart2.setDataHandler(new DataHandler(source));
	      //messageBodyPart2.setFileName(allegato);
	      //multipart.addBodyPart(messageBodyPart2);
	 
	      // inserimento delle parti nel messaggio
	      msg.setContent(multipart);
	 
	      Transport transport = session.getTransport("smtp"); //("smtp") per non usare SSL
	      transport.connect(host, user, password);
	      transport.sendMessage(msg, msg.getAllRecipients());
	      transport.close();
	 
	      System.out.println("Invio dell'email Terminato");
	 
	    }catch(AddressException ae) {
	      ae.printStackTrace();
	    }catch(NoSuchProviderException nspe){
	      nspe.printStackTrace();
	    }catch(MessagingException me){
	      me.printStackTrace();
	    }
	  }
	  
	  public void inviaEmail(String allegato) {
		    int port = 25; //porta 25 per non usare SSL
		 
		    Properties props = new Properties();
		    props.put("mail.smtp.auth", "true");
		    props.put("mail.smtp.user", mittente);
		    props.put("mail.smtp.host", host);
		    props.put("mail.smtp.port", port);
		 
		    // commentare la riga seguente per non usare SSL 
		    //props.put("mail.smtp.starttls.enable","true");
		    props.put("mail.smtp.socketFactory.port", port);
		 
		    // commentare la riga seguente per non usare SSL 
		   // props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		    props.put("mail.smtp.socketFactory.fallback", "false");
		 
		    Session session = Session.getInstance(props, null);
		    session.setDebug(true);
		 
		    // Creazione delle BodyParts del messaggio
		    MimeBodyPart messageBodyPart1 = new MimeBodyPart();
		    MimeBodyPart messageBodyPart2 = new MimeBodyPart();
		 
		    try{
		      // COSTRUZIONE DEL MESSAGGIO
		      Multipart multipart = new MimeMultipart();
		      MimeMessage msg = new MimeMessage(session);
		 
		      // header del messaggio
		      msg.setSubject(oggetto);
		      msg.setSentDate(new Date());
		      msg.setFrom(new InternetAddress(mittente));
		 
		      // destinatario
		      msg.addRecipient(Message.RecipientType.TO,
		      new InternetAddress(destinatario));


		      msg.addRecipient(Message.RecipientType.CC,
		    	      new InternetAddress(Config.getRespMail()));
		      
		      // corpo del messaggio
		      messageBodyPart1.setText(text);
		      multipart.addBodyPart(messageBodyPart1);
		 
		      // allegato al messaggio
		      DataSource source = new FileDataSource(allegato);
		      messageBodyPart2.setDataHandler(new DataHandler(source));
		      messageBodyPart2.setFileName(allegato);
		      multipart.addBodyPart(messageBodyPart2);
		 
		      // inserimento delle parti nel messaggio
		      msg.setContent(multipart);
		 
		      Transport transport = session.getTransport("smtp"); //("smtp") per non usare SSL
		      transport.connect(host, user, password);
		      transport.sendMessage(msg, msg.getAllRecipients());
		      transport.close();
		 
		      System.out.println("Invio dell'email Terminato");
		 
		    }catch(AddressException ae) {
		      ae.printStackTrace();
		    }catch(NoSuchProviderException nspe){
		      nspe.printStackTrace();
		    }catch(MessagingException me){
		      me.printStackTrace();
		    }
		  }

	  
}
