# spring-tutorial-21st
CEOS back-end 21st spring tutorial project

# 1주차

## POJO

: 특정 프레임워크나 라이브러리에 의존하지 않는 순수 자바 코드

---

## Spring

: 자바 기반 객체지향적으로 서버 설계를 편리하게 도와주는 프레임워크. 주로 빈을 통해 객체를 관리하는 컨테이너 역할

- **객체지향 5원칙**
    - **S**: Single Responsibility ⇒ 책임분리(AOP, 관점지향 프로그래밍)
    - **O**: Open closed principle ⇒ 인터페이스에 의존하고 구현체에 의존하지 않는다(PSA, 서비스 추상화)
    - **L**: 리스코프 치환법칙 ⇒ 상속할 때 주의해라. (자식은 부모의 기능을 온전히 수행 가능해야함)

      **ex1)** 잘못된 상속: Rectangle로 Square 인스턴스를 받아도 밑변과 높이를 서로 다르게 설정이 불가능함  
      Rectangle ← Square

      // 코드  
      Rectange rec = new Square();  
      rec.setHeight(10);  
      rec.setWidth(15); // !!!!!!!!!!!!오류 발생. width, height 모두 15로 설정됨.

      **ex2)** 잘한 상속:  
      List ← ArrayList  
      ← LinkedList

    - **I**: Inversion of Control ⇒ 스프링 컨테이너가 객체 관리
    - **D**: Dependency Injection ⇒ 관리 중인 객체를 삽입

---

**Q. 스프링이 POJO 프레임워크라고 함은 무슨 의미?**

⇒ 개발자의 선택에 따라 스프링에 의존할지 안할지 결정 가능 (POJO 객체를 쓸지 말지 선택 가능)

/** POJO 코드
* 스프링 없어도 돌아감
  */

@Service  
public class MemberService {  
private MemberRepository memberRepository;  
//...  
}

//NOT POJO  
@Controller  
public class MemberController {  
@GetMapping("/members")  
public String getMembers() {}  
}

---

## AOP (Aspect Oriented Programming, 관점 지향 프로그래밍)

: 비즈니스 로직과 관리 로직 관심사를 분리

**ex)** Service 계층에서 (비즈니스 로직, 트랜잭션 관리 코드)를 분리

---

## PSA (Portable Service Abstraction)

: 특정 기술에 종속적이지 않은 설계 = 인터페이스에 의존하고 구현체에 의존 X

**ex)** 데이터 접근 기술 중 JPA, JDBC 로 변경하여도 코드의 변경은 최소화된다.  
`PlatformTransactionManager`
- JpaTransactionManager (JPA)
- DataSourceTransactionManager (JDBC)

---

## Spring Boot

- 내장 톰캣
- 의존성 관리
- 설정 자동화
- 모니터링 (성능 측정, 메트릭 확인)

---

## 1. 스프링 빈

- **의미:** 스프링 컨테이너에서 생성과 소멸을 관리하는 **싱글톤 객체**  
  싱글톤 객체: 동일한 인스턴스로 같은 메모리 점유

- **생명주기:**  
  객체 생성 → 의존성 주입 → 초기화 → 사용 중 → 소멸

**Q. @PostConstruct가 적용되는 시점은?**  
⇒ 초기화 단계의 **마지막**에 호출된다.  
객체가 생성(메모리 힙에 할당), 의존성이 주입되고 필요한 설정들도 끝난 초기화의 마지막 단계.  
(이때 개발자가 원하는 추가 설정이 가능)

---

## 2. 스프링 빈 등록 방법

- `@Bean`을 사용한 직접 등록
- `@Component` 사용 (`@Service`, `@Repository`, `@Controller`)
- `@ComponentScan(basePackages = " ")`
  : 지정 패키지(없다면 현재 패키지) 하위의 @Component를 찾으면 자동 빈 등록

---

## 3. 어노테이션

- **기능**
    - 메타 데이터  
      ex) `@Override`
    - 특정 기능을 가짐  
      ex) `@Component`, `@PostConstruct`

// 코드  
@Target(ElementType.???) // TYPE, METHOD, FIELD  
@Retention(RetentionPolicy.???) // RUNTIME, CLASS, SOURCE  
public @interface CustomAnnotation {}

---

## 4. 스프링 Context 구조

- **BeanDefinitionRegistry**와 **BeanFactory** 상속관계

`DefaultListableBeanFactory` (클래스, 구현체)  
→ *BeanFactory 인터페이스*  
→ *BeanDefinitionRegistry 인터페이스*

`AnnotationConfigApplicationContext` (자식)  
→ `GenericApplicationContext` (부모)  
→ *BeanFactory 인터페이스*  
→ *BeanDefinitionRegistry 인터페이스*

// 코드  
public class GenericApplicationContext implements BeanFactory, BeanDefinitionRegistry {  
private DefaultListableBeanFactory beanFactory;

    public Object getBean() {  
        beanFactory.getBean(); // 위임  
    }  
}

---

## 5. 테스트 코드

### 1. **단위 테스트**

: 외부 환경과는 분리된 순수한 코드 로직을 대상으로 검증

// 코드  
@ExtendWith(MockitoExtension.class)  
class MemberServiceTest {  
@Mock  
private MemberRepository memberRepository;

    @InjectMocks  
    private MemberService memberService;  
    
    @Test  
    void findById() {  
        // given  
        Member member = new Member(...);  
        when(memberRepository.findById(1L))  
            .thenReturn(Optional.of(new Member("olaf")));  

        // when  
        Member findMember = memberService.findById(1L);  

        // then  
        Assertions.assertThat(findMember.getName()).isEqualTo("olaf");  
    }  
}

---

### 2. **통합 테스트**

: API 요청, DB 연결과 같은 외부 환경을 포함하여 시스템의 전체적인 동작을 검증

// 코드  
@SpringBootTest // 스프링 환경에서 통합테스트  
@AutoConfigureMockMvc // API 호출 테스트  
class ApiTest {  
}
