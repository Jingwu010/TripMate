package model.settings;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.*;

/**
 * Created by Jingwu Xu on 2019-04-04
 */
public class SettingReader {
    Secretes secretes;

    public Secretes getSecretes() {
        return secretes;
    }

    public SettingReader() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            secretes = mapper.readValue(new File("secrete.yaml"), Secretes.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SettingReader sc = new SettingReader();
        System.out.println(sc.secretes.getGoogle_api_key());
    }
}