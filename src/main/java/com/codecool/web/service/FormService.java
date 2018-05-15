package com.codecool.web.service;

import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.util.Map;

public interface FormService {

    String convertInputStream(ServletInputStream inputStream) throws IOException;

    Map<String, String> getParamsFromInputStream(String inputStream);
}
