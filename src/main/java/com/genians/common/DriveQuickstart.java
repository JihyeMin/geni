package com.genians.common;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

/* class to demonstrate use of Drive files list API */
public class DriveQuickstart {
  /**
   * Application name.
   */
  private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
  /**
   * Global instance of the JSON factory.
   */
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
  /**
   * Directory to store authorization tokens for this application.
   */
  private static final String TOKENS_DIRECTORY_PATH = "tokens";

  /**
   * Global instance of the scopes required by this quickstart.
   * If modifying these scopes, delete your previously saved tokens/ folder.
   */
  private static final List<String> SCOPES =
      Collections.singletonList(DriveScopes.DRIVE_FILE);
  private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

  /**
   * Creates an authorized Credential object.
   *
   * @param HTTP_TRANSPORT The network HTTP Transport.
   * @return An authorized Credential object.
   * @throws IOException If the credentials.json file cannot be found.
   */
  private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
      throws IOException {
    // Load client secrets.
    InputStream in = DriveQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
    if (in == null) {
      throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
    }
    GoogleClientSecrets clientSecrets =
        GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    // Build flow and trigger user authorization request.
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
        .setAccessType("offline")
        .build();
    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
    Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    //returns an authorized Credential object.
    return credential;
  }

public static void uploadFolderToDrive(String id, String localFolderPath) throws IOException, GeneralSecurityException {
    
    id = "1Zww4BJ0f1MQhN4dPyGY_IJV4W1EyPEo0"; // 공유드라이브의 ID
            // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
    
            String folderName = "tokens"; // 폴더의 이름
    
            File folderMetadata = new File();
            folderMetadata.setName(folderName); // 폴더의 이름
            
            folderMetadata.setParents(Collections.singletonList(id));
            
            folderMetadata.setMimeType("application/vnd.google-apps.folder"); // MIME 타입으로 폴더임을 명시
            
            File folder = service.files().create(folderMetadata)
                    .setFields("id")
                    .execute();
            
            String folderId = folder.getId();
            
            java.io.File[] files = new java.io.File(localFolderPath).listFiles();
            
            for (java.io.File file : files) {
                if (file.isFile()) {
                    File fileMetaData = new File();
                    fileMetaData.setName(file.getName());
                     fileMetaData.setParents(Collections.singletonList(folderId)); // 폴더의 ID를 설정
            
                    FileContent fileContent = new FileContent("application/octet-stream", file);
            
                    File uploadedFile = service.files().create(fileMetaData, fileContent).execute();
            
                    if (uploadedFile != null) {
                        System.out.println("파일 업로드 성공!");
                        System.out.printf("파일 이름: %s, 파일 ID: %s\n", uploadedFile.getName(), uploadedFile.getId());
                    } else {
                        System.out.println("파일 업로드 실패.");
                    }
                }
            }
        }
}