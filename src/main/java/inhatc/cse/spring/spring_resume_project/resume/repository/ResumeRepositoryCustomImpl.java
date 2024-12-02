package inhatc.cse.spring.spring_resume_project.resume.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import inhatc.cse.spring.spring_resume_project.resume.dto.ResumeSearchDto;
import inhatc.cse.spring.spring_resume_project.resume.entity.QResume;
import inhatc.cse.spring.spring_resume_project.resume.entity.Resume;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

public class ResumeRepositoryCustomImpl implements ResumeRepositoryCustom{
    private JPAQueryFactory queryFactory;

    public ResumeRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }


    private BooleanExpression regDtsAfter(String searchDateType){

        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null){
            return null;
        } else if(StringUtils.equals("1d", searchDateType)){
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w", searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m", searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }
        //날짜에 따라 검색할 수 있게 하는 메소드

        return QResume.resume.regeTime.after(dateTime);
    }
    private BooleanExpression searchByLike(String searchBy, String searchQuery){

        if(StringUtils.equals("title", searchBy)){ //제목 검색
            return QResume.resume.title.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("nickname", searchBy)){ //닉네임 검색
            return QResume.resume.nickname.like("%" + searchQuery + "%");
        }

        return null;
    }
    @Override
    public Page<Resume> getResumePage(ResumeSearchDto resumeSearchDto, Pageable pageable) {
        List<Resume> content = queryFactory.selectFrom(QResume.resume)
                .where(regDtsAfter(resumeSearchDto.getSearchDateType()),
                        searchByLike(resumeSearchDto.getSearchBy(),resumeSearchDto.getSearchQuery()))
                .orderBy(QResume.resume.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch(); //조회 대상 리스트 반환
        long total = queryFactory.select(Wildcard.count).from(QResume.resume)
                .where(regDtsAfter(resumeSearchDto.getSearchDateType()),
                        searchByLike(resumeSearchDto.getSearchBy(),resumeSearchDto.getSearchQuery()))
                .fetchOne(); //조회 대상이 1건이면 해당 타입 반환, 1건 이상이면 에러 발생
        return new PageImpl<>(content,pageable,total);
    }

}
