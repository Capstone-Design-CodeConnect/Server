package CodeConnect.CodeConnect.dto.post.qna;

import CodeConnect.CodeConnect.domain.post.Qna;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QnaRequestDto{
    private Long qnaId;
    private String nickname;
    private String title;
    private String content;
    private int commentCount;
    private String currentDateTime;
    private String modifiedDateTime;
    private String base64Image;


    public QnaRequestDto(Qna qna){
        this.qnaId = qna.getQnaId();
        this.nickname = qna.getNickname();
        this.title = qna.getTitle();
        this.content = qna.getContent();
        this.commentCount = qna.getCommentCount();
        this.currentDateTime=qna.getCurrentDateTime();
        this.modifiedDateTime = qna.getModifiedDateTime();
    }




}