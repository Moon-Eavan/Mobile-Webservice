package com.example.photoviewer.model;

import android.graphics.Bitmap;

/**
 * GET /api_root/Post/ 응답 1건을 나타내는 모델.
 *
 * 실제 Django 응답(PostSerializer)에는 id 필드가 없으므로
 * 여기서도 서버 게시물 id를 임의로 만들어 넣지 않는다.
 * (author/title/text/created_date/published_date/image 필드만 그대로 보관)
 */
public class PostItem {
    private final String author;
    private final String title;
    private final String text;
    private final String createdDate;
    private final String publishedDate;
    private final String imageUrl;
    private Bitmap bitmap;

    public PostItem(String author, String title, String text, String createdDate,
                     String publishedDate, String imageUrl) {
        this.author = author;
        this.title = title;
        this.text = text;
        this.createdDate = createdDate;
        this.publishedDate = publishedDate;
        this.imageUrl = imageUrl;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
