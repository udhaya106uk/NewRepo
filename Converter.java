package com.onward.hrservice.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.core.GenericTypeResolver;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

@Component
public class Converter extends AbstractGenericHttpMessageConverter<Object> {
	
	@Inject
	private ObjectMapper objectMapper;

	@SuppressWarnings("deprecation")
	public Converter() {
		super(MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_UTF8);
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return true;
	}

	protected JavaType getJavaType(Type type, @Nullable Class<?> contextClass) {
		TypeFactory typeFactory = this.objectMapper.getTypeFactory();
		return typeFactory.constructType(GenericTypeResolver.resolveType(type, contextClass));
	}

	@Override
	protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) {
		return null;
	}

	private String getRequestBody(InputStream inputStream) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		if (inputStream != null) {
			try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) != -1) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			}
		}
		return stringBuilder.toString();
	}

	private String decode(InputStream inputStream) {
		String decodedOutput = "";
		try {
			String requestBody = getRequestBody(inputStream);
			logger.debug("converted::requestBody::" + requestBody);

			JSONParser parse = new JSONParser();
			JSONObject jobj = (JSONObject) parse.parse(requestBody);
			String payload = (String) jobj.get("data");
			logger.debug("converted::payload::" + payload);

			decodedOutput = new String(Base64.getDecoder().decode(payload));
			logger.debug("converted::decodedOutput::" + decodedOutput);
		} catch (Exception e) {
			logger.error(e);
		}
		return decodedOutput;
	}

	@Override
	public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
			throws IOException {
		JavaType javaType = getJavaType(type, contextClass);
		String decrypt = decode(inputMessage.getBody());
		if(decrypt.isEmpty()) {
			return objectMapper.readValue("{}", javaType);
		}
		return objectMapper.readValue(decrypt, javaType);
	}

	@Override
	protected void writeInternal(Object t, Type type, HttpOutputMessage outputMessage)
			throws IOException {
		String json = objectMapper.writeValueAsString(t);
		String encoded = new String(Base64.getEncoder().encode(json.getBytes()));
		Map<String, Object> result = new HashMap<>();
		result.put("data", encoded);

		outputMessage.getBody().write(objectMapper.writeValueAsBytes(result));
	}
}
