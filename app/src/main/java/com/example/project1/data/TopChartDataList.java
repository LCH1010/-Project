package com.example.project1.data;

public class TopChartDataList {
    String collectionName;
    String artworkUrl600;
    int collectionId;

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }

    public void setArtworkUrl600(String artworkUrl600) {
        this.artworkUrl600 = artworkUrl600;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public int getCollectionId() {
        return collectionId;
    }

    public String getArtworkUrl600() {
        return artworkUrl600;
    }

    public TopChartDataList(String collectionName, String artworkUrl600, int collectionId) {
        this.collectionName = collectionName;
        this.artworkUrl600 = artworkUrl600;
        this.collectionId = collectionId;
    }
}