package com.blog.Payload;

import lombok.Data;

import java.util.List;

@Data
public class PostResponse {

    private List<PostDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElemnts;
    private int totalPages;
    private boolean isLast;
}
