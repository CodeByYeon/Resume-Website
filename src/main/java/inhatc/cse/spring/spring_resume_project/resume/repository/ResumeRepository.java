package inhatc.cse.spring.spring_resume_project.resume.repository;

import inhatc.cse.spring.spring_resume_project.resume.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long>, QuerydslPredicateExecutor<Resume> {

    List<Resume> findByResumeTitle(String title);
    List<Resume> findByResumeTitleLike(String title);
    List<Resume> findByNickname(String nickname);

    @Query("select i from Resume i where i.contents like %:contents%")
    List<Resume> findResumeContents(@Param("contents") String contents);

    @Query(value = "select * from Resume i where i.contents like %:contents%",nativeQuery = true)
    List<Resume> findResumeContentsNative(@Param("contents") String contents);



}
