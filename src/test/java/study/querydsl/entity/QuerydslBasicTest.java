package study.querydsl.entity;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static study.querydsl.entity.QMember.member;

@Transactional
@SpringBootTest
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;
    JPAQueryFactory queryFactory;

    @BeforeEach // -> 테스트 실행 전 설정
    public void before() {
        queryFactory = new JPAQueryFactory(em);

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);

        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }


    @Test
    void startQueryDsl() {
        QMember m = new QMember("m");

        Member findMember = queryFactory
                .selectFrom(m)
                .where(m.name.eq("member1"))
                .fetchFirst();

        String name = findMember.getName();

        assertThat(name).isEqualTo("member1");
    }

    @Test
    void startQueryDsl2() {
        Member findMember = queryFactory
                .selectFrom(member) // static으로 변환해서 사용 가능함 => 권장!!
                .where(member.name.eq("member1"))
                .fetchFirst();

        assertThat(findMember.getName()).isEqualTo("member1");
    }

    @Test
    void search() {
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.name.eq("member1")
                        .and(member.age.eq(10)))
                .fetchFirst();

        assertThat(findMember.getName()).isEqualTo("member1");
    }

    @Test
    void search2() {
        Member findMember = queryFactory
                .selectFrom(member)
                .where(
                        member.name.eq("member1"),  //* 쉼표로 연결해도 무방함
                        member.age.eq(10)
                )
                .fetchFirst();

        assertThat(findMember.getName()).isEqualTo("member1");
    }

    @Test
    public void resultFetch() {
        //X이제는 사용 안함X
        QueryResults<Member> results = queryFactory
                .selectFrom(member)
                .fetchResults();

        long total = results.getTotal();
        System.out.println("====================");
        System.out.println("total = " + total);
        System.out.println("results.getResults() = " + results.getResults());
        System.out.println("====================");
    }


    @Test
    public void findDtoBySetter() {
        List<MemberDto> result = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.name.as("userName"), //=> 필드명 변수명이 다르면 null로 나옴
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto m : result) {
            System.out.println("memberDto = " + m);
        }
    }

    @Test
    public void findDtoByField() {
        List<MemberDto> result = queryFactory
                .select(Projections.fields(MemberDto.class,   //=> 필드에 값 바로 꽂음 (Getter/Setter 무시)
                        member.name.as("userName"),     //=> 필드명 변수명이 다르면 null로 나옴
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto m : result) {
            System.out.println("memberDto = " + m);
        }
    }

    @Test
    public void findDtoByConstructor() {
        List<MemberDto> result = queryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.name,                         //=> 필드명 변수명이 달라도 순서대로 들어감 (변수 타입으로 들어감)
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto m : result) {
            System.out.println("memberDto = " + m);
        }
    }


}
