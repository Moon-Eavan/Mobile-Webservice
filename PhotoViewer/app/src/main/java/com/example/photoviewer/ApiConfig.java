package com.example.photoviewer;

public final class ApiConfig {

    private ApiConfig() {
    }

    // Android Emulator에서 PC localhost(Django runserver)에 접근하기 위한 주소
    public static final String BASE_URL = "http://10.0.2.2:8000";
}
