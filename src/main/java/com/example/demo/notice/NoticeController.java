package com.example.demo.notice;

import com.example.demo.question.QuestionForm;
import com.example.demo.user.dto.UserDTO;
import com.example.demo.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class NoticeController {
    private final NoticeService noticeService;
    private final UserService userService;
    @PreAuthorize("isAuthenticated()")
    @GetMapping("notice/create")
    public String questionCreate(QuestionForm questionForm, Model model,Principal principal) {
        UserDTO userDto = this.userService.getUserDto(principal.getName());//관리자 확인(등록 버튼 활성화여부)
        model.addAttribute("roleid", userDto.getRole().getRoleid());
        model.addAttribute("source",1);
        return "question_form";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("notice/create")
    public String questionCreate(@Valid QuestionForm questionForm,
                                 BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        UserDTO userDto = this.userService.getUserDto(principal.getName());
        this.noticeService.create(questionForm.getSubject(), questionForm.getContent(), userDto);
        return "redirect:/question/notice";
    }
    @RequestMapping("/notice")
    public String list(Model model, @RequestParam(value="page", defaultValue="1") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw, Principal principal) {
        Page<NoticeDto> paging = this.noticeService.getList(page-1, kw);
        String loginid= principal.getName();
        UserDTO userDto=this.userService.getUserDto(loginid);//관리자 확인(등록 버튼 활성화여부)
        model.addAttribute("roleid", userDto.getRole().getRoleid());
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "list_notice";
    }
    @RequestMapping("notice/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id,Principal principal) {
        UserDTO userDto = this.userService.getUserDto(principal.getName());//관리자 확인(등록 버튼 활성화여부)
        model.addAttribute("roleid", userDto.getRole().getRoleid());
        NoticeDto notice = this.noticeService.getNotice(id);
        model.addAttribute("notice", notice);
        return "notice_detail";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/notice/modify/{id}")
    public String noticeModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal,Model model) {
        NoticeDto noticeDto = this.noticeService.getNotice(id);
        UserDTO userDto = this.userService.getUserDto(principal.getName());//관리자 확인(등록 버튼 활성화여부)
        model.addAttribute("roleid", userDto.getRole().getRoleid());
        model.addAttribute("source", 1);
        if (!noticeDto.getAuthor().getUserid().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(noticeDto.getSubject());
        questionForm.setContent(noticeDto.getContent());
        return "question_form";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/notice/modify/{id}")
    public String noticeModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        NoticeDto noticeDto = this.noticeService.getNotice(id);
        if (!noticeDto.getAuthor().getUserid().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.noticeService.modify(noticeDto, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/notice/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/notice/delete/{id}")
    public String noticeDelete(Principal principal, @PathVariable("id") Integer id,Model model) {
        NoticeDto noticeDto = this.noticeService.getNotice(id);
        UserDTO userDto = this.userService.getUserDto(principal.getName());//관리자 확인(등록 버튼 활성화여부)
        model.addAttribute("roleid", userDto.getRole().getRoleid());
        if (!noticeDto.getAuthor().getUserid().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.noticeService.delete(noticeDto);
        return "redirect:/question/notice";
    }
}
