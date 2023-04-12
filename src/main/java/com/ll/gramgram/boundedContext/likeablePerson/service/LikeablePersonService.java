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
        InstaMember userInstaMember = member.getInstaMember();
        List<LikeablePerson> likes = userInstaMember.getLikes();

        //
        InstaMember toInstaMember = instaMemberService.findByUsernameOrCreate(username).getData();
        LikeablePerson existedLikeable = findByFromAndToInstaMemberId(member.getInstaMember().getId(), toInstaMember.getId());
        //list에서 아이디 확인하는 방식으로 수정할 것.
        if(existedLikeable != null){
            if(existedLikeable.getAttractiveTypeCode()== attractiveTypeCode)
                return RsData.of("F-3", "이미 추가된 호감상대입니다.");
            else
                return updateAttractiveType(existedLikeable, attractiveTypeCode);
        }
        //list 크기로 비교하도록 수정할 것.
        //if(countByFromInstaMemberId(member.getInstaMember().getId())>10){
        if(likes != null && likes.size()>=2){
            return RsData.of("F-4", "호감상대가 11명을 초과합니다.");
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

    private RsData<LikeablePerson> updateAttractiveType(LikeablePerson likeablePerson, int attractiveTypeCode) {
        String beforeAttractiveType = likeablePerson.getAttractiveTypeDisplayName();
        likeablePerson.setAttractiveTypeCode(attractiveTypeCode);
        LikeablePerson newLikeablePerson = likeablePersonRepository.save(likeablePerson);
        return RsData.of("S-2",
                String.format("%s 에 대한 호감사유를 %s에서 %s으로 변경합니다."
                        , newLikeablePerson.getToInstaMemberUsername()
                        , beforeAttractiveType
                        , newLikeablePerson.getAttractiveTypeDisplayName()));
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


    @Transactional
    public Long countByFromInstaMemberId(Long fromInstaMemberId){
        return likeablePersonRepository.countByFromInstaMemberId(fromInstaMemberId);
    }
}

