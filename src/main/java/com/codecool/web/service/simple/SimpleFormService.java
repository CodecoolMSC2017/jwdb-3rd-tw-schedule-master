package com.codecool.web.service.simple;

import com.codecool.web.service.FormService;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SimpleFormService implements FormService {

    public SimpleFormService() {
    }

    @Override
    public String convertInputStream(ServletInputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((inputStream)));
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    @Override
    public Map<String, String> getParamsFromInputStream(String inputStream) {
        Map<String, String> params = new HashMap<>();
        int length = inputStream.split("&").length;
        System.out.println(length);
        for (int i = 0; i < length; i++) {
            params.put(inputStream.split("&")[i].split("=")[0], inputStream.split("&")[i].split("=")[1] );
        }
        return params;
    }
}
