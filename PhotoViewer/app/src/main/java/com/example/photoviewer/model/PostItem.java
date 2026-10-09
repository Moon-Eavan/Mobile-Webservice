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

    /**
     * image URL이 실제로 유효한지 확인한다.
     * org.json은 JSON null 값을 optString()으로 읽으면 문자열 "null"을 반환하므로
     * null/빈 문자열뿐 아니라 "null" 문자열도 "이미지 없음"으로 취급해야 한다.
     */
    public boolean hasImage() {
        return isValid(imageUrl);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /**
     * 목록/상세 화면에 보여줄 날짜 하나를 고른다.
     * published_date가 있으면 그것을, 없으면(null) created_date를 사용한다.
     */
    public String getDisplayDate() {
        if (isValid(publishedDate)) {
            return publishedDate;
        }
        return createdDate;
    }

    /**
     * 정렬에 사용할 날짜 문자열.
     * Django가 내려주는 날짜는 "2026-10-09T02:49:20.498404Z"처럼
     * 고정 자릿수의 UTC ISO-8601 형식이라 날짜 파싱 없이 문자열 비교만으로도
     * 시간 순서와 동일하게 정렬된다. null/빈 문자열/"null"/형식이 이상한 값은
     * 모두 빈 문자열로 통일해 크래시 없이 안전하게 처리한다(정렬 시 맨 끝으로 몰림).
     */
    public String getSortableDate() {
        String date = getDisplayDate();
        return isValid(date) ? date : "";
    }

    private static boolean isValid(String value) {
        return value != null && !value.isEmpty() && !"null".equals(value);
    }
}
