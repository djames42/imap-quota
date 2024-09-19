package com.djames42;

import com.djames42.models.MyQuota;
import com.djames42.services.GetProperties;
import com.djames42.services.IMAP;

public class Main {
    public static void main(String[] args) {
        String propFile = System.getProperty("propFile");
        boolean output=false;
        if ( System.getProperty("output") != null && System.getProperty("output").toLowerCase().equals("true") ) output=true;
        GetProperties properties = new GetProperties(propFile);
        if (output == true)
            System.out.printf("Prop File: %s\n\tServer: %s - Username: %s - Threshold: %4.2f\n", propFile, properties.getProperties("imap_server"), properties.getProperties("imap_username"), Float.parseFloat(properties.getProperties("imap_quota")) );
        IMAP imap = new IMAP(properties.getProperties("imap_server"), properties.getProperties("imap_username"), properties.getProperties("imap_password"), properties.getProperties("warning_to_email"));
        MyQuota myQuota = imap.IMAPQuota();
        if ( myQuota.getRatio() > Float.valueOf(properties.getProperties("imap_quota"))) imap.IMAPSendWarning(myQuota);
        if (output == true)
            System.out.printf("Speakeasy Quota: %,d of %,d (%.2f%%)\n", myQuota.getUsage(), myQuota.getLimit(), myQuota.getRatio());
    }
}
