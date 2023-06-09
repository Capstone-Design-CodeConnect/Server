package CodeConnect.CodeConnect.domain.chat;

import CodeConnect.CodeConnect.utils.TimeUtils;
import CodeConnect.CodeConnect.domain.post.Recruitment;
import CodeConnect.CodeConnect.domain.todo.Todo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Chat_Room")
@Getter
@Setter
@NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId; // 방 고유 id

    private String title; // 방 제목

    private int currentCount; // 참여인원 수

    private String hostNickname; // 방장

    private String currentDateTime; // 방 생성 시간

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "room_id"))
    @JsonIgnore
    private List<String> currentParticipantMemberList;

    @OneToOne(mappedBy = "chatRoom")
    @JsonIgnore
    private Recruitment recruitment; // 게시글 정보

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Chat> chatList = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Todo> todo;

    public ChatRoom(Recruitment recruitment) {
        this.title = recruitment.getTitle();
        this.hostNickname = recruitment.getNickname();
        this.currentDateTime = TimeUtils.changeDateTimeFormat(LocalDateTime.now());
        this.currentParticipantMemberList = new ArrayList<>(recruitment.getCurrentParticipantMemberList());
        this.currentCount = currentParticipantMemberList.size();
    }

    // 연관관계 메소드
    public void setChatList(Chat chat) {
        this.chatList.add(chat);
        chat.setChatRoom(this);
    }
}
