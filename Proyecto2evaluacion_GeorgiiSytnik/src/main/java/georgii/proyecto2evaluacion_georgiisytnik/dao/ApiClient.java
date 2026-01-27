/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package georgii.proyecto2evaluacion_georgiisytnik.dao;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

public class ApiClient {
    private final String baseUrl = "https://slime-rancher.vercel.app/api/";

    public String get(String path) throws Exception {
        System.out.println("CONNECTING TO URL: " + baseUrl + path);
        URL url = new URI(baseUrl + path).toURL();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        System.out.println("CONNECTION CODE: " + responseCode);

        if (responseCode != 200) {
            throw new Exception("HTTP error: " + responseCode);
        }

        StringBuilder sb = new StringBuilder();
        try (Scanner sc = new Scanner(url.openStream())) {
            while (sc.hasNext()) {
                sb.append(sc.nextLine());
            }
        }
        return sb.toString();
    }
}