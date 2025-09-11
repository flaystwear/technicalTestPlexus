package com.plexus.integration;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.web.client.TestRestTemplate;
// import org.springframework.boot.test.web.server.LocalServerPort;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.context.TestPropertySource;
// import org.springframework.transaction.annotation.Transactional;

// import com.plexus.domain.model.in.AssetFileUploadRequest;

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @ActiveProfiles("test")
// @Transactional
// @TestPropertySource(properties = {
//     "spring.task.execution.enabled=false"
// })
// class AssetManagementIntegrationTest {

//     @LocalServerPort
//     private int port;

//     @Autowired
//     private TestRestTemplate restTemplate;

//     @Test
//     void completeWorkflow_ShouldWorkEndToEnd() throws Exception {
//         // Step 1: Upload an asset
//         AssetFileUploadRequest uploadRequest = AssetFileUploadRequest.builder()
//                 .filename("integration-test.pdf")
//                 .contentType("application/pdf")
//                 .encodedFile("JVBERi0xLjQK")
//                 .build();

//         HttpHeaders headers = new HttpHeaders();
//         headers.setContentType(MediaType.APPLICATION_JSON);
//         HttpEntity<AssetFileUploadRequest> request = new HttpEntity<>(uploadRequest, headers);
        
//         ResponseEntity<String> uploadResponse = restTemplate.postForEntity(
//                 "http://localhost:" + port + "/api/mgmt/1/assets/actions/upload",
//                 request,
//                 String.class
//         );
        
//         // Verify upload response
//         assert uploadResponse.getStatusCode() == HttpStatus.ACCEPTED;
//         assert uploadResponse.getHeaders().getContentType().toString().contains("application/json");
//         assert uploadResponse.getBody().contains("id");

//         // Step 2: Search for the uploaded asset
//         String searchUrl = "http://localhost:" + port + "/api/mgmt/1/assets/?filename=integration-test.*&sortDirection=ASC";
//         ResponseEntity<String> searchResponse = restTemplate.getForEntity(searchUrl, String.class);
        
//         // Verify search response
//         assert searchResponse.getStatusCode() == HttpStatus.OK;
//         assert searchResponse.getHeaders().getContentType().toString().contains("application/json");
//         assert searchResponse.getBody().contains("integration-test.pdf");
//         assert searchResponse.getBody().contains("application/pdf");
//     }

//     @Test
//     void searchWithMultipleFilters_ShouldReturnFilteredResults() throws Exception {
//         // Search with multiple filters
//         String searchUrl = "http://localhost:" + port + "/api/mgmt/1/assets/?uploadDateStart=2025-01-01T00:00:00Z&uploadDateEnd=2025-12-31T23:59:59Z&filename=document.*&filetype=text/csv&sortDirection=DESC";
//         ResponseEntity<String> response = restTemplate.getForEntity(searchUrl, String.class);
        
//         // Verify response
//         assert response.getStatusCode() == HttpStatus.OK;
//         assert response.getHeaders().getContentType().toString().contains("application/json");
//     }

//     @Test
//     void searchWithoutFilters_ShouldReturnBadRequest() throws Exception {
//         String searchUrl = "http://localhost:" + port + "/api/mgmt/1/assets/?sortDirection=ASC";
//         ResponseEntity<String> response = restTemplate.getForEntity(searchUrl, String.class);
        
//         // Verify response
//         assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
//     }

//     @Test
//     void uploadWithInvalidData_ShouldReturnBadRequest() throws Exception {
//         AssetFileUploadRequest invalidRequest = AssetFileUploadRequest.builder()
//                 .filename("")
//                 .contentType("")
//                 .encodedFile("")
//                 .build();

//         HttpHeaders headers = new HttpHeaders();
//         headers.setContentType(MediaType.APPLICATION_JSON);
//         HttpEntity<AssetFileUploadRequest> request = new HttpEntity<>(invalidRequest, headers);
        
//         ResponseEntity<String> response = restTemplate.postForEntity(
//                 "http://localhost:" + port + "/api/mgmt/1/assets/actions/upload",
//                 request,
//                 String.class
//         );
        
//         // Verify response
//         assert response.getStatusCode() == HttpStatus.BAD_REQUEST; // Now validates and returns BadRequest
//     }

//     @Test
//     void searchWithInvalidDateFormat_ShouldReturnBadRequest() throws Exception {
//         String searchUrl = "http://localhost:" + port + "/api/mgmt/1/assets/?uploadDateStart=invalid-date&sortDirection=ASC";
//         ResponseEntity<String> response = restTemplate.getForEntity(searchUrl, String.class);
        
//         // Verify response
//         assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
//     }
// }
