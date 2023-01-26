package kr.mos1981.mosweb.service;

import kr.mos1981.mosweb.entity.GalleryArticle;
import kr.mos1981.mosweb.entity.Member;
import kr.mos1981.mosweb.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AssignmentService assignmentService;
    private final GalleryArticleService galleryArticleService;
    private final NoticeArticleService noticeArticleService;
    private final PastTestArticleService pastTestArticleService;
    private final UsageArticleService usageArticleService;


    /*
     Permission Level
     모스 명단에 없거나 학번과 이름이 일치하지 않는 경우 게스트 레벨(2)
     모스 명단에 있으나 관리자는 아닌 경우 회원 레벨(1)
     모스 명단에 있고 관리자인 경우 관리자 레벨(0)
     */
    public int getPermissionLevel(String studentId, String name){
        Optional<Member> memberOptional = memberRepository.findById(studentId);
        if(memberOptional.isEmpty()) return 2;
        if(!memberOptional.get().getName().equals(name)) return 2;
        if(!memberOptional.get().getAdministrator()) return 1;
        return 0;
    }

    public boolean hasPermission(String category, Long id, String studentId, String name){
        switch(category){
            case "notice" -> {
                return noticeArticleService.findById(id).getCreateBy().equalsIgnoreCase(name+"("+studentId+")");
            }
            case "gallery" -> {
                return galleryArticleService.findById(id).getCreateBy().equalsIgnoreCase(name+"("+studentId+")");
            }
            case "usage" -> {
                return usageArticleService.findById(id).getCreateBy().equalsIgnoreCase(name+"("+studentId+")");
            }
            case "pasttest" -> {
                return pastTestArticleService.findById(id).getCreateBy().equalsIgnoreCase(name+"("+studentId+")");
            }
        }
        return false;
    }

    public Member findById(String studentId){
        Optional<Member> memberOptional = memberRepository.findById(studentId);
        if(memberOptional.isEmpty()) return null;
        return memberOptional.get();
    }

    public Long getGeneration(String studentId){
        Optional<Member> memberOptional = memberRepository.findById(studentId);
        if(memberOptional.isEmpty()) return null;
        return memberOptional.get().getGeneration();
    }

    public String getProfilePhoto(String studentId){
        return "https://intra.wku.ac.kr/SWupis/ViewPicture?sNo="+studentId;
    }
}
