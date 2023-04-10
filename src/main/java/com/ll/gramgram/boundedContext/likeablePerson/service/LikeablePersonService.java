package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.rq.Rq;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeablePersonService {
    private final LikeablePersonRepository likeablePersonRepository;
    private final InstaMemberService instaMemberService;
    private final Rq rq;

    @Transactional
    public RsData<LikeablePerson> like(Member member, String username, int attractiveTypeCode) {
        if ( member.hasConnectedInstaMember() == false ) {
            return RsData.of("F-2", "먼저 본인의 인스타그램 아이디를 입력해야 합니다.");
        }

        if (member.getInstaMember().getUsername().equals(username)) {
            return RsData.of("F-1", "본인을 호감상대로 등록할 수 없습니다.");
        }

        InstaMember toInstaMember = instaMemberService.findByUsernameOrCreate(username).getData();
        LikeablePerson exitedLikeable = findByFromAndToInstaMemberId(member.getInstaMember().getId(), toInstaMember.getId());
        if(exitedLikeable != null){
            return RsData.of("F-3", "이미 추가된 호감상대입니다.");
        }

        LikeablePerson likeablePerson = LikeablePerson
                .builder()
                .fromInstaMember(member.getInstaMember()) // 호감을 표시하는 사람의 인스타 멤버
                .fromInstaMemberUsername(member.getInstaMember().getUsername()) // 중요하지 않음
                .toInstaMember(toInstaMember) // 호감을 받는 사람의 인스타 멤버
                .toInstaMemberUsername(toInstaMember.getUsername()) // 중요하지 않음
                .attractiveTypeCode(attractiveTypeCode) // 1=외모, 2=능력, 3=성격
                .build();

        likeablePersonRepository.save(likeablePerson); // 저장

        return RsData.of("S-1", "입력하신 인스타유저(%s)를 호감상대로 등록되었습니다.".formatted(username), likeablePerson);
    }

    public List<LikeablePerson> findByFromInstaMemberId(Long fromInstaMemberId) {
        return likeablePersonRepository.findByFromInstaMemberId(fromInstaMemberId);
    }

    @Transactional
    public void delete(Long id) {
        LikeablePerson likeablePerson = likeablePersonRepository.findById(id).orElse(null);
        likeablePersonRepository.delete(likeablePerson);
    }

    public RsData setDeleteRsData(Long id, InstaMember instaMember ){
        LikeablePerson likeablePerson = likeablePersonRepository.findById(id).orElse(null);
        if(likeablePerson == null)
            return RsData.of("F-1", "호감 표현이 존재하지 않아 삭제 실패했습니다.");
        else if(instaMember == null ||
                instaMember.getId() != likeablePerson.getFromInstaMember().getId())
            return RsData.of("F-2", "올바르지 않은 인스타그램 아이디로 인해 삭제 실패했습니다.");
        else
            return RsData.of("S-1",
                String.format("%s님에 대한 호감 표현을 삭제했습니다.", likeablePerson.getToInstaMemberUsername()));
    }

    public LikeablePerson findByFromAndToInstaMemberId(Long fromId, Long ToId) {
        return likeablePersonRepository.findByFromAndToInstaMember(fromId, ToId).orElse(null);
    }
}

