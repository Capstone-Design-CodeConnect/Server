package CodeConnect.CodeConnect.domain.post;

import CodeConnect.CodeConnect.utils.TimeUtils;
import CodeConnect.CodeConnect.domain.member.Member;
import CodeConnect.CodeConnect.dto.post.qna.QnaRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Qna")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Qna extends Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qnaId;

    /**
     * 작성자 정보에 대한 매핑 정보를 통해 작성자(Member) 엔티티를 참조할 수 있다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email") // 조인 컬럼명과 조인할 대상 필드명 설정
    @JsonIgnore
    private Member member;

    @Column(name = "comment_count")
    private Integer commentCount = 0; // 댓글 개수

    @Column(name = "image_path")
    private String imagePath; // 이미지 파일 경로

    private String profileImagePath; //회원 면상

    private int likeCount; // 게시글 좋아요 수

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "qna_id"))
    @JsonIgnore
    private List<String> likesEmail = new ArrayList<>();

    // 연관관계 메소드
    public void setMember(Member member) {
        this.member = member;
        member.getQnas().add(this);
    }

     /**
     * 하나의 게시글이 여러개의 댓글과 관계를 가지므로 1:N 관계를 사용.
     */
    @OneToMany(mappedBy = "qna", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("currentDateTime ASC") //qna 오름차순
    @JsonIgnore
    private final List<Comment> comments = new ArrayList<>();


    public Qna(QnaRequestDto dto,String nickname, String title, String content){
        super.title = title;
        super.nickname = nickname;
        super.content = content;
        this.commentCount = dto.getCommentCount();
        super.setCurrentDateTime(TimeUtils.changeDateTimeFormat(LocalDateTime.now()));
    }



}
