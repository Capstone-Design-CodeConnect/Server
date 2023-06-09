package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.utils.TimeUtils;
import CodeConnect.CodeConnect.domain.member.Member;
import CodeConnect.CodeConnect.domain.post.Comment;
import CodeConnect.CodeConnect.domain.post.Qna;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.post.comment.CommentRequestDto;
import CodeConnect.CodeConnect.repository.CommentRepository;
import CodeConnect.CodeConnect.repository.MemberRepository;
import CodeConnect.CodeConnect.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor // final이 붙은 애들을 자동으로 주입해줌
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    private final QnaRepository qnaRepository;

    @Transactional
    public ResponseDto<Comment> createComment(Long qnaId, CommentRequestDto requestDto, String email) {

        Member findMember = memberRepository.findByEmail(email);
        String nickname = findMember.getNickname();

        Optional<Qna> optionalQna = qnaRepository.findById(qnaId);
        if (optionalQna.isEmpty()) {
            return ResponseDto.setFail("존재하지 않는 Qna입니다.");
        }
        Qna qna = optionalQna.get();

        Comment comment = new Comment(nickname, email);

        comment.setMember(findMember);
        comment.setComment(requestDto.getComment());
        comment.setNickname(nickname);
        comment.setCommentId(requestDto.getCommentId());
        comment.setProfileImagePath(comment.getMember().getProfileImagePath());
        comment.setQna(qna);


        if (comment.getCommentId() != null) {
            comment.setCommentId(null);
        }

        qna.setCommentCount(qna.getCommentCount() + 1);

        Comment savedComment = commentRepository.save(comment);

        return ResponseDto.setSuccess("댓글 쓰기 성공", savedComment);
    }
    //특정 Qna 들어왔을때 댓글 조회
    @Transactional
    public ResponseDto<List<CommentRequestDto>> findComment(Long qnaId, String email) {
        // 해당 회원 검증
        Optional<Member> findMember = memberRepository.findById(email);
        if (findMember.isEmpty()) {
            return ResponseDto.setFail("존재하지 않는 회원입니다.");
        }
        // 해당 Qna 검증
        Optional<Qna> findQna = qnaRepository.findById(qnaId);
        if (findQna.isEmpty()) {
            return ResponseDto.setFail("존재하지 않는 Qna입니다.");
        }

        //특정한 findQna에 달린 댓글을 commentList에 할당
        // Optional내부에 있는 Comment객체를 가져옴
        List<Comment> commentList = commentRepository.findAllByQnaOrderByCurrentDateTimeDesc(findQna.get());
        List<CommentRequestDto> commentResponseDtoList = commentList.stream()
                .map(CommentRequestDto::new)
                .collect(Collectors.toList());

        return ResponseDto.setSuccess("해당 qna 댓글 조회 성공", commentResponseDtoList);
    }

    //댓글 삭제
    @Transactional
    public ResponseDto<String> deleteComment(Long commentId, String email){
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NoSuchElementException("댓글이 존재하지 않습니다"));
        if (!validateMember(email, comment))
            return ResponseDto.setFail("접근 권한이 없습니다");

        Qna qna = qnaRepository.findById(comment.getQna().getQnaId()).orElseThrow(() -> new NoSuchElementException("Qna가 존재하지 않습니다"));
        qna.setCommentCount(qna.getCommentCount()-1);

        commentRepository.delete(comment);
        return ResponseDto.setSuccess("댓글 삭제 성공", null);
    }

    //댓글 수정
    public ResponseDto<Comment> updateComment(Long commentId, String email, String comments){
        Member findMember = memberRepository.findByEmail(email);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NoSuchElementException("댓글이 존재하지 않습니다"));
        if (!validateMember(email, comment))
            return ResponseDto.setFail("접근 권한이 없습니다");

        comment.setMember(findMember);
        comment.setProfileImagePath(comment.getMember().getProfileImagePath());
        comment.setComment(comments);

        return ResponseDto.setSuccess("댓글 수정 성공", comment);
    }

    private boolean validateMember(String email, Comment comment) {
        // 회원
        Member findMember = memberRepository.findByEmail(email);
        String memberEmail = comment.getMember().getEmail();

        if(findMember.getEmail().equals(memberEmail)){
            return true;
        }
        return false;
    }
}