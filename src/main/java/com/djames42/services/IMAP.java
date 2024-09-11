package com.djames42.services;

import java.util.Properties;
import javax.mail.Quota;
import javax.mail.Session;
import javax.mail.Store;

import com.djames42.models.MyQuota;
import com.sun.mail.imap.IMAPStore;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class IMAP {
    String imap_server;
    String imap_username;
    String imap_password;
    String warning_to_email;
    Properties properties;
    Session emailSession;
    Store store;
    IMAPStore imapStore;

    public IMAP(String imap_server, String imap_username, String imap_password, String warning_to_email) {
        try {
            this.imap_server = imap_server;
            this.imap_username = imap_username;
            this.imap_password = imap_password;
            this.warning_to_email = warning_to_email;

            this.properties = new Properties();
            this.properties.put("mail.store.protocol", "imaps");
            this.properties.put("mail.imaps.port", "993");
            this.properties.put("mail.imaps.starttls.enable", "true");
            this.emailSession = Session.getDefaultInstance(properties);
            this.store = this.emailSession.getStore("imaps");
            this.store.connect(this.imap_server, this.imap_username, this.imap_password);
            this.imapStore = (IMAPStore) this.store;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MyQuota IMAPQuota() {
        Quota[] quotas = new Quota[2];
        MyQuota myQuota;
        try {
            quotas = this.imapStore.getQuota("INBOX");
        } catch (Exception e) {
            e.printStackTrace();
        }
        myQuota = new MyQuota((int) quotas[0].resources[0].usage, (int) quotas[0].resources[0].limit);
        return myQuota;
    }

    public void IMAPSendWarning(MyQuota myQuota) {
        String to = this.warning_to_email;
        String from = this.imap_username;
        final String username = this.imap_username;
        final String password = this.imap_password;
        String host = this.imap_server;

        boolean sslEnable = true;
        boolean startTlsEnable = false;

        Properties smtp_properties = new Properties();
        smtp_properties.put("mail.smtp.host", host);
        smtp_properties.put("mail.smtp.port", 465);
        smtp_properties.put("mail.smtps.host", host);
        smtp_properties.put("mail.smtps.port", 465);
        smtp_properties.put("mail.smtp.user", username);
        smtp_properties.put("mail.smtps.user", username);
        smtp_properties.put("mail.smtp.sendpartial", "true");
        smtp_properties.put("mail.smtps.sendpartial", "true");
        smtp_properties.put("mail.smtp.auth", "true");

        if (startTlsEnable) {
            smtp_properties.put("mail.smtp.starttls.enable", "true");
            smtp_properties.put("mail.smtps.starttls.enable", "true");
            smtp_properties.put("mail.smtp.starttls.enable.auth", "true");
            smtp_properties.put("mail.smtp.ssl.trust", host);
            smtp_properties.put("mail.smtps.ssl.trust", host);
            smtp_properties.put("mail.protocol.ssl.trust", host);
        }
        if (sslEnable) {
            smtp_properties.put("mail.smtp.ssl.enable", "true");
            smtp_properties.put("mail.smtp.ssl.trust", host);
            smtp_properties.put("mail.smtps.ssl.enable", "true");
            smtp_properties.put("mail.smtps.ssl.trust", host);
            smtp_properties.put("mail.protocol.ssl.trust", host);
        }

        Session session = Session.getInstance(smtp_properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("WARNING!!! Speakeasy Email Quota!");
            message.setText(String.format("WARNING! Speakeasy Mail Quota at %,d of %,d (%6.2f%%)", myQuota.getUsage(), myQuota.getLimit(), myQuota.getRatio()));
            message.setHeader("X-Priority", "1");
            Transport.send(message);
        } catch (AddressException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
