package ch.bfh.project1.pwnd.utils;

import ch.bfh.project1.pwnd.db.AppInfoHandler;
import ch.bfh.project1.pwnd.enums.AppInfoKey;
import ch.bfh.project1.pwnd.jobs.CronJob;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HIBPClient {
    static final private String HIBP_API_URL_ACCOUNT = "https://haveibeenpwned.com/api/v3/breachedaccount/";
    static final private String HIBP_API_URL_TYPES = "https://haveibeenpwned.com/api/v3/dataclasses";
    static final private String HIBP_KEY_HEADER = "hibp-api-key";
    private static final Logger logger = LogManager.getLogger(HIBPClient.class);


    public static HttpResponse<String> callAPIForAccount(String email) {
        try (HttpClient client = HttpClient.newHttpClient()){
            URI uri = URI.create(HIBP_API_URL_ACCOUNT + email + "?truncateResponse=false");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header(HIBP_KEY_HEADER, AppInfoHandler.getAppInfoValueString(AppInfoKey.API_KEY.name))
                    .header("User-Agent", "Java API Client") // User-Agent header is required by HIBP API
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check the response code and print the response body
            if (response.statusCode() == 200) {
                return response;
            } else if (response.statusCode() == 404) {
                logger.info("No breaches found for Account: {}", email);
            } else {
                logger.warn("Error calling HIBP API: {}", response.statusCode());
            }
            return null;
        } catch (IOException | InterruptedException e) {
            logger.error("Failed to call API endpoint: {} ", email,  e);
            throw new RuntimeException(e);
        }
    }

    public static HttpResponse<String> callAPIForTypes() {
        try(HttpClient client = HttpClient.newHttpClient()){
            URI uri = URI.create(HIBP_API_URL_TYPES);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header(HIBP_KEY_HEADER, AppInfoHandler.getAppInfoValueString(AppInfoKey.API_KEY.name))
                    .header("User-Agent", "Java API Client")
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check the response code and print the response body
            if (response.statusCode() == 200) {
                return response;
            } else {
                logger.warn("Failed with status code: {}", response.statusCode());
            }
            return null;
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


}
