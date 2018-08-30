/**
 * created on 2018年4月17日 下午3:25:57
 */
package com.utstar.uapollo.custom.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.FileSystemResource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;

/**
 * @author UTSC0167
 * @date 2018年4月17日
 *
 */
public final class UapolloUtil {

    private static final Logger log = LoggerFactory.getLogger(UapolloUtil.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private UapolloUtil() {

    }

    public static final long getUnixTime() {

        return Instant.now().getEpochSecond();
    }

    public static final boolean isDefaultConfigFile(String applicationName, String configFileName) {

        final Pattern pattern = Pattern.compile("^" + applicationName + "\\.(properties|yaml|yml)$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(configFileName);
        return matcher.find();
    }

    public static final boolean isYamlFile(String fileName) {

        final Pattern pattern = Pattern.compile("\\.(yaml|yml)$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fileName);
        return matcher.find();
    }

    public static final boolean isPropertiesFile(String fileName) {

        final Pattern pattern = Pattern.compile("\\.(properties)$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fileName);
        return matcher.find();
    }

    public static final Optional<String> loadYamlFile(File yamlFile) {

        log.info("loadYamlFile begin for yamlFile: {}", yamlFile);
        Optional<String> existingYamlContent = Optional.empty();
        FileSystemResource resource = new FileSystemResource(yamlFile);
        try {
            YamlPropertySourceLoader yamlLoader = new YamlPropertySourceLoader();
            PropertySource<?> propertySource = yamlLoader.load("YAML", resource, null);
            if (propertySource == null) {
                log.info("loadYamlFile for file: {} the propertySource is null, return null!",
                        yamlFile);
                return existingYamlContent;
            }

            Map<String, String> yamlContentMap = ((MapPropertySource) propertySource).getSource()
                    .entrySet().stream()
                    .collect(Collectors.toMap(entry -> String.valueOf(entry.getKey()),
                            entry -> String.valueOf(entry.getValue()), (a, b) -> a, TreeMap::new));
            existingYamlContent = objectToJson(yamlContentMap);
        } catch (IOException e) {
            log.info("loadYamlFile for file: {} generate exception:", yamlFile, e);
        }

        if (existingYamlContent.isPresent()) {
            log.info("loadYamlFile for yamlFile: {} content:\n{}", yamlFile,
                    existingYamlContent.get());
        } else {
            log.info("loadYamlFile for yamlFile: {} content is null", yamlFile);
        }
        return existingYamlContent;
    }

    public static final Optional<String> loadPropertiesFile(File propertiesFile) {

        log.info("loadPropertiesFile begin for propertiesFile: {}", propertiesFile);
        Optional<String> existingPropertiesContent = Optional.empty();
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(propertiesFile)) {
            properties.load(inputStream);
            Map<String, String> propertiesContentMap = properties.entrySet().stream()
                    .collect(Collectors.toMap(entry -> String.valueOf(entry.getKey()),
                            entry -> String.valueOf(entry.getValue()), (a, b) -> a, TreeMap::new));
            existingPropertiesContent = objectToJson(propertiesContentMap);
        } catch (IOException e) {
            log.info("loadPropertiesFile for file: {} generate exception:", propertiesFile, e);
        }

        if (existingPropertiesContent.isPresent()) {
            log.info("loadPropertiesFile for propertiesFile: {} content:\n{}", propertiesFile,
                    existingPropertiesContent.get());
        } else {
            log.info("loadPropertiesFile for propertiesFile: {} content is null", propertiesFile);
        }
        return existingPropertiesContent;
    }

    public static final Optional<String> getLocalHostIp() {

        return getInetAddress().map(inetAddress -> inetAddress.getHostAddress());
    }

    public static final Optional<String> getLocalHostName() {

        return getInetAddress().map(inetAddress -> inetAddress.getHostName());
    }

    public static final Optional<String> objectToJson(Object object) {

        try {
            return Optional.ofNullable(OBJECT_MAPPER.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            log.info("objectToJson convert object: {} to json generate exception:",
                    object.getClass().getName(), e);
            return Optional.empty();
        }
    }

    public static final Optional<String> objectToPrettyJson(Object object) {

        try {
            return Optional.ofNullable(
                    OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object));
        } catch (JsonProcessingException e) {
            log.info("objectToPrettyJson convert object: {} to json generate exception:",
                    object.getClass().getName(), e);
            return Optional.empty();
        }
    }

    public static final Optional<Object> jsonToObject(String json, TypeReference<?> typeReference) {

        try {
            return Optional.ofNullable(OBJECT_MAPPER.readValue(json, typeReference));
        } catch (IOException e) {
            log.info("objectToPrettyJson convert json: {} to objType: {} generate exception:", json,
                    typeReference.getClass().getName(), e);
            return Optional.empty();
        }
    }

    public static final String getSha256(String input) {

        return Hashing.sha256().hashBytes(input.getBytes()).toString();
    }

    private static final Optional<InetAddress> getInetAddress() {

        Optional<InetAddress> existingInetAddress = Optional.empty();
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            existingInetAddress = Optional.ofNullable(inetAddress);
        } catch (UnknownHostException e) {
            log.info("getInetAddress generate exception:", e);
        }
        return existingInetAddress;
    }
}
