package inhatc.cse.spring.spring_resume_project.resume.repository;

import inhatc.cse.spring.spring_resume_project.resume.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long>, QuerydslPredicateExecutor<Resume>, ResumeRepositoryCustom {


    @Query("select i from Resume i where i.contents like %:contents%")
    List<Resume> findContents(@Param("contents") String contents);

    @Query(value = "select * from Resume i where i.contents like %:contents%",nativeQuery = true)
    List<Resume> findContentsNative(@Param("contents") String contents);



}
