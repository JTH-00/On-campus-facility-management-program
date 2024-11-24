package com.example.demo.question;

import com.example.demo.answer.Answer;
import com.example.demo.answer.AnswerDto;
import com.example.demo.answer.AnswerForm;
import com.example.demo.answer.AnswerService;
import com.example.demo.notice.NoticeDto;
import com.example.demo.notice.NoticeService;
import com.example.demo.user.dto.UserDTO;
import com.example.demo.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Set;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;
    private final NoticeService noticeService;
    private final UserService userService;

    @RequestMapping("/questionmain")
    public String questionmain(Model model, @RequestParam(value="page", defaultValue="1") int page,
                               @RequestParam(value = "kw", defaultValue = "") String kw,Principal principal) {
        Page<QuestionDto> paging = this.questionService.getListMain(page-1, kw);//자유게시판
        Page<NoticeDto> paging2 = this.noticeService.getListMain(page-1, kw);//공지사항
        UserDTO userDto=this.userService.getUserDto(principal.getName());//관리자 확인(등록 버튼 활성화여부)
        model.addAttribute("roleid", userDto.getRole().getRoleid());
        model.addAttribute("paging", paging);
        model.addAttribute("paging2", paging2);
        model.addAttribute("kw", kw);
        return "question_main";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/freeboard/create")
    public String questionCreate(QuestionForm questionForm, Model model,Principal principal) {
        UserDTO userDto=this.userService.getUserDto(principal.getName());//관리자 확인(등록 버튼 활성화여부)
        model.addAttribute("roleid", userDto.getRole().getRoleid());
        model.addAttribute("source",2);
        return "question_form";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/freeboard/create")
    public String questionCreate(@Valid QuestionForm questionForm,
                                 BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        UserDTO userDto = this.userService.getUserDto(principal.getName());
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), userDto);
        return "redirect:/question/freeboard";

    }
    //위에는 게시판 공용 컨트롤러
    @RequestMapping(value = "/freeboard/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm,Principal principal) {
        QuestionDto question = this.questionService.getQuestion(id);
        UserDTO userDto = this.userService.getUserDto(principal.getName());//관리자 확인(등록 버튼 활성화여부)
        model.addAttribute("roleid", userDto.getRole().getRoleid());
        int vote=0; //게시글 추천
        if(question.getVoter().contains(userDto)){
            vote=1; //게시글의 추천자 목록 확인 후 포함하면 1
        }
        int a = question.getAnswerList().size();
        for(int i=0; i<a; i++){
            if(question.getAnswerList().get(i).getVoter().contains(userDto)==true){
                int tlqkf = question.getAnswerList().get(i).getId();
                model.addAttribute("tlqkf"+i,tlqkf); // 추천여부 확인 후에 전송
            }
        }
        System.out.println(question.getAnswerList());
        model.addAttribute("question", question);
        model.addAttribute("vote",vote); // 추천여부 확인 후에 전송
        return "freeboard_detail";
    }

    @RequestMapping("/freeboard")
    public String freeboard(Model model, @RequestParam(value="page", defaultValue="1") int page,
                            @RequestParam(value = "kw", defaultValue = "") String kw,Principal principal) {
        Page<QuestionDto> paging = this.questionService.getList(page-1, kw);//자유게시판
        UserDTO userDto = this.userService.getUserDto(principal.getName());//관리자 확인(등록 버튼 활성화여부)
        model.addAttribute("roleid", userDto.getRole().getRoleid());
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "question_freeboard";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/freeboard/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal,Model model) {
        QuestionDto questionDto = this.questionService.getQuestion(id);
        UserDTO userDto = this.userService.getUserDto(principal.getName());//관리자 확인(등록 버튼 활성화여부)
        model.addAttribute("roleid", userDto.getRole().getRoleid());
        model.addAttribute("source", 2);
        if (!questionDto.getAuthor().getUserid().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(questionDto.getSubject());
        questionForm.setContent(questionDto.getContent());
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/freeboard/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        QuestionDto questionDto = this.questionService.getQuestion(id);
        if (!questionDto.getAuthor().getUserid().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(questionDto, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/freeboard/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/freeboard/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id,Model model) {
        UserDTO userDto = this.userService.getUserDto(principal.getName());//관리자 확인(등록 버튼 활성화여부)
        model.addAttribute("roleid", userDto.getRole().getRoleid());
        QuestionDto questionDto = this.questionService.getQuestion(id);
        if (!questionDto.getAuthor().getUserid().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.questionService.delete(questionDto);
        return "redirect:/question/freeboard";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id,Model model) {
        QuestionDto questionDto = this.questionService.getQuestion(id);
        UserDTO userDto = this.userService.getUserDto(principal.getName());//관리자 확인(등록 버튼 활성화여부)
        model.addAttribute("roleid", userDto.getRole().getRoleid());
        this.questionService.vote(questionDto, userDto);
        return String.format("redirect:/question/freeboard/detail/%s", id);
    }
}