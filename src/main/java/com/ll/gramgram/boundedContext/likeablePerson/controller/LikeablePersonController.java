package com.ll.gramgram.boundedContext.likeablePerson.controller;

import com.ll.gramgram.base.rq.Rq;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.service.LikeablePersonService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/likeablePerson")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class LikeablePersonController {
    private final Rq rq;
    private final LikeablePersonService likeablePersonService;

    @GetMapping("/add")
    public String showAdd() {
        return "usr/likeablePerson/add";
    }

    @AllArgsConstructor
    @Getter
    public static class AddForm {
        private final String username;
        private final int attractiveTypeCode;
    }

    @PostMapping("/add")
    public String add(@Valid AddForm addForm) {
        RsData<LikeablePerson> createRsData = likeablePersonService.setLikeRsDate(
                rq.getMember(),
                addForm.getUsername(),
                addForm.getAttractiveTypeCode());
        if (createRsData.isFail()) {
            return rq.historyBack(createRsData);
        } else if (createRsData.getData()!= null && createRsData.getData() instanceof  LikeablePerson) {
            likeablePersonService.updateAttractiveType(createRsData.getData(), addForm.getAttractiveTypeCode());
        }
        return rq.redirectWithMsg("/likeablePerson/list", createRsData);
    }

    @GetMapping("/list")
    public String showList() {
        InstaMember instaMember = rq.getMember().getInstaMember();
        if(instaMember== null)
            return "redirect:/likeablePerson/add";
        return "usr/likeablePerson/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        InstaMember instaMember = rq.getMember().getInstaMember();
        RsData deleteRsData = likeablePersonService.setDeleteRsData(id, instaMember);
        if(deleteRsData.isSuccess()){
            likeablePersonService.delete(id);
            return rq.redirectWithMsg("/likeablePerson/list", deleteRsData.getMsg());
        }
        else
            return rq.historyBack(deleteRsData);
    }
}
