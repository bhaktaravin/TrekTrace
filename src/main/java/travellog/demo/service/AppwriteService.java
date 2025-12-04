package travellog.demo.service;

import java.util.UUID;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;

@Service
public class AppwriteService {
    
    @Value("${appwrite.projectId}")
    private String projectId;
    @Value("${appwrite.apiKey}")
    private String apiKey;
    @Value("${appwrite.endpoint:http://localhost/v1}")
    private String endpoint;

      // Uploads file, returns JSON string response (Appwrite returns file object)
  public String uploadFile(MultipartFile file) throws Exception {
    String url = endpoint + "/storage/files";

    try (CloseableHttpClient client = HttpClients.createDefault()) {
      HttpPost post = new HttpPost(url);
      post.addHeader("X-Appwrite-Project", projectId);
      post.addHeader("X-Appwrite-Key", apiKey);

      MultipartEntityBuilder builder = MultipartEntityBuilder.create();
      // Create unique filename
      String fname = UUID.randomUUID() + "-" + file.getOriginalFilename();
      builder.addBinaryBody("file", file.getInputStream(), org.apache.hc.core5.http.ContentType.DEFAULT_BINARY, fname);
      // optional: set read permissions, write permissions etc by metadata fields. For now use defaults.
      HttpEntity multipart = (HttpEntity) builder.build();
      post.setEntity((org.apache.hc.core5.http.HttpEntity) multipart);

      ClassicHttpResponse response = client.executeOpen(null, post, null);
      int code = response.getCode();
      String body = EntityUtils.toString(response.getEntity());
      if (code >= 200 && code < 300) {
        return body;
      } else {
        throw new RuntimeException("Appwrite upload failed: " + code + " " + body);
      }
    }
  }

}
