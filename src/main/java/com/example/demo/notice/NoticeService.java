package com.example.demo.notice;


import com.example.demo.DataNotFoundException;
import com.example.demo.user.dto.UserDTO;
import com.example.demo.user.entity.User;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final ModelMapper modelMapper;

    
    private Specification<Notice> search(String kw) {
        return new Specification<Notice>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Notice> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Notice, User> u1 = q.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목 
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용 
                        cb.like(u1.get("username"), "%" + kw + "%"));    // 질문 작성자
            }
        };
    }

    private NoticeDto of(Notice notice) {
        return modelMapper.map(notice, NoticeDto.class);
    }
    
    private Notice of(NoticeDto noticeDto) {
        return modelMapper.map(noticeDto, Notice.class);
    }

    public Page<NoticeDto> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Notice> spec = search(kw);
        Page<Notice> noticeList = this.noticeRepository.findAll(spec, pageable);
        Page<NoticeDto> noticeDtoList = noticeList.map(q -> of(q));
        return noticeDtoList;
    }

    public Page<NoticeDto> getListMain(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 5, Sort.by(sorts));
        Specification<Notice> spec = search(kw);
        Page<Notice> noticeList = this.noticeRepository.findAll(spec, pageable);
        Page<NoticeDto> noticeDtoList = noticeList.map(q -> of(q));
        return noticeDtoList;
    }
    
    public NoticeDto getNotice(Integer id) {
        Optional<Notice> notice = this.noticeRepository.findById(id);
        if (notice.isPresent()) {
            return of(notice.get());
        } else {
            throw new DataNotFoundException("notice not found");
        }
    }
    
    public NoticeDto create(String subject, String content, UserDTO userDTO) {
        NoticeDto noticeDto = new NoticeDto();
        noticeDto.setSubject(subject);
        noticeDto.setContent(content);
        noticeDto.setCreateDate(LocalDateTime.now());
        noticeDto.setAuthor(userDTO);
        Notice notice = of(noticeDto);
        this.noticeRepository.save(notice);
        return noticeDto;
    }
    
    public NoticeDto modify(NoticeDto noticeDto, String subject, String content) {
        noticeDto.setSubject(subject);
        noticeDto.setContent(content);
        noticeDto.setModifyDate(LocalDateTime.now());
        Notice question = of(noticeDto);
        this.noticeRepository.save(question);
        return noticeDto;
    }
    
    public void delete(NoticeDto noticeDto) {
        this.noticeRepository.delete(of(noticeDto));
    }

    public long getQuestionCount() {
        return noticeRepository.count();
    }
}