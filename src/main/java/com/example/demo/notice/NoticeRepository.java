package com.example.demo.notice;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    Notice findBySubject(String subject);
    Notice findBySubjectAndContent(String subject, String content);
    List<Notice> findBySubjectLike(String subject);
    Page<Notice> findAll(Pageable pageable);
    Page<Notice> findAll(Specification<Notice> spec, Pageable pageable);

    @Query("select "
            + "distinct q "
            + "from Notice q "
            + "left outer join User u1 on q.author=u1 "
            + "where "
            + "   q.subject like %:kw% "
            + "   or q.content like %:kw% "
            + "   or u1.username like %:kw% ")
    Page<Notice> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
}