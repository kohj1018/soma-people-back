package kr.somapeople.somapeopleback.web.posts.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MainPagePostsResponseDto {
    private List<PostsResponseDto> qnaPostList;
    private List<PostsResponseDto> freePostList;
    private List<PostsResponseDto> applicantPostList;

    public MainPagePostsResponseDto(List<PostsResponseDto> qnaPostList, List<PostsResponseDto> freePostList, List<PostsResponseDto> applicantPostList) {
        this.qnaPostList = qnaPostList;
        this.freePostList = freePostList;
        this.applicantPostList = applicantPostList;
    }
}
