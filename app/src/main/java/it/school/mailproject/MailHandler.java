package it.school.mailproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

public class MailHandler implements Parcelable {
    final String username, password, host, port;
    protected Store store;

    public MailHandler(String username, String password, String host, String port)
            throws MessagingException {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    public Store connect() throws MessagingException {
        if (store == null) {
            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");
            Session session = Session.getInstance(props);
            store = session.getStore();
            store.connect(host, username, password);
            return store;
        } return store;
    }

    public Folder getInbox() throws MessagingException {
        return connect().getFolder("INBOX");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] { username, password, host, port });
    }

    public static final Parcelable.Creator<MailHandler> CREATOR = new Parcelable.Creator<MailHandler>() {

        @Override
        public MailHandler createFromParcel(Parcel source) {
            String[] data = new String[4];
            source.readStringArray(data);
            try {
                return new MailHandler(data[0], data[1], data[2], data[3]);
            } catch (MessagingException e) {
                return null;
            }
        }

        @Override
        public MailHandler[] newArray(int size) {
            return new MailHandler[size];
        }
    };
}