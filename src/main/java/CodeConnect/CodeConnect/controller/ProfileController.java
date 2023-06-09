package CodeConnect.CodeConnect.controller;

import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.member.UpdateMemberRequestDto;
import CodeConnect.CodeConnect.dto.member.UpdatedMemberResponseDto;
import CodeConnect.CodeConnect.service.MemberService;
import CodeConnect.CodeConnect.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final MemberService memberService;

    @GetMapping("/profile/userinfo/{nickname}")
    public ResponseDto<Object> getUserInfo(@AuthenticationPrincipal String email, @PathVariable String nickname){
        return profileService.showUserInfo(email, nickname);
    }
    @GetMapping("/profile/myinfo")
    public ResponseDto<Object> getUserInfo2(@AuthenticationPrincipal String email){
        return profileService.showUserInfo2(email);
    }

    @GetMapping("/profile/{nickname}")
    public ResponseDto<?> joinRecruitment(@AuthenticationPrincipal String email, @PathVariable String nickname){
        return profileService.showJoinRecruitment(email,nickname);
    }

    @GetMapping("/profile/userRecruitment/{nickname}")
    public ResponseDto<Object> getUserRecruitment(@AuthenticationPrincipal String email,@PathVariable String nickname){
        return profileService.showUserRecruitment(email,nickname);
    }
//
    @GetMapping("/profile/userQna/{nickname}")
    public ResponseDto<Object> getUserQna(@AuthenticationPrincipal String email,@PathVariable String nickname){
        return profileService.showUserQna(email,nickname);
    }

    @PutMapping("/profile/update")
    public ResponseDto<UpdatedMemberResponseDto> update(@RequestBody UpdateMemberRequestDto updateDto, @AuthenticationPrincipal String email) {
        return profileService.updateProfile(updateDto, email);
    }

    @DeleteMapping("/profile/delete")
    public ResponseDto<?> delete(@AuthenticationPrincipal String email) {
        return memberService.deleteMember(email);
    }


}
