package kr.mos1981.mosweb.service;

import kr.mos1981.mosweb.entity.Member;
import kr.mos1981.mosweb.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    private MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }


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
