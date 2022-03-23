package com.example.project1.data;

// 어댑터를 만들기전 어댑터 안에 들어갈 아이템의 데이터를 담아둘 클래스 정의

public class personArrData {
    // 변수 설정
    String name;
    String url;

    public personArrData(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}