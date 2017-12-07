package com.saha.slnarch.common.drive;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriveHelper {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private String APPLICATION_NAME = "FlyPgsWeb";
  private HttpTransport HTTP_TRANSPORT;
  private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
  private FileDataStoreFactory DATA_STORE_FACTORY;
  private final java.io.File DATA_STORE_DIR =
      new java.io.File(System.getProperty("user.home"), ".store/drive_sample");

  private Drive drive;


  public Credential authorize() throws IOException {
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
        new InputStreamReader(DriveHelper.class.getResourceAsStream("/client-secrets.json")));
    if (clientSecrets.getDetails().getClientId().startsWith("Enter")
        || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
      logger.error(
          "Enter Client ID and Secret from https://code.google.com/apis/console/?api=drive "
              + "into drive-cmdline-sample/src/main/resources/client_secrets.json");
      throw new IllegalArgumentException();
    }

    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
        Collections.singleton(DriveScopes.DRIVE_FILE))
        .setDataStoreFactory(DATA_STORE_FACTORY)
        .setAccessType("offline")
        .build();
//    return GoogleCredential
//        .fromStream(new FileInputStream("client-secrets.json"))
//        .createScoped(Collections.singleton(DriveScopes.DRIVE_FILE));

    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver())
        .authorize("me");
  }

  public void createDriver() throws IOException {
    try {
      HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    } catch (GeneralSecurityException e) {
      e.printStackTrace();
    }
    DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
    Credential credential = authorize();
    drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(
        APPLICATION_NAME).build();
  }

  public File uploadFile(java.io.File file) throws IOException {
    File fileMetadata = new File();
    fileMetadata.setName(file.getName());

    FileContent mediaContent = new FileContent("*/*", file);
    Drive.Files.Create insert = drive.files().create(fileMetadata, mediaContent);
    MediaHttpUploader uploader = insert.getMediaHttpUploader();
    uploader.setDirectUploadEnabled(true);
    uploader.setProgressListener(new FileUploadListener());
    return insert.execute();
  }


  public void shareFile(File file, String to) throws IOException {
    BatchRequest batch = drive.batch();
    Permission userPermission = new Permission()
        .setType("user")
        .setRole("writer")
        .setEmailAddress(to);
    drive.permissions().create(file.getId(), userPermission)
        .setSendNotificationEmail(true)
        .setFields("id")
        .queue(batch, callback);
    batch.execute();
  }

  JsonBatchCallback<Permission> callback = new JsonBatchCallback<Permission>() {
    @Override
    public void onFailure(GoogleJsonError e,
        HttpHeaders responseHeaders) {
      logger.error("Shared File Failed", e);
    }

    @Override
    public void onSuccess(Permission permission,
        HttpHeaders responseHeaders) {
      logger.info("Shared File Success  id={}  emails={}", permission.getId(),
          permission.getEmailAddress());
    }
  };

}