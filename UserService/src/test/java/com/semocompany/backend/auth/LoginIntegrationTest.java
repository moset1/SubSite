package com.semocompany.backend.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.semocompany.backend.domain.user.entity.UserEntity;
import com.semocompany.backend.domain.user.entity.UserRoleType;
import com.semocompany.backend.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // 스프링 컨테이너를 포함한 통합 테스트
@AutoConfigureMockMvc // MockMvc 자동 설정 및 주입
@Transactional // 각 테스트 후 DB 롤백
public class LoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // API 테스트를 위한 가짜 HTTP 요청 객체

    @Autowired
    private ObjectMapper objectMapper; // 객체를 JSON으로 변환

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 각 테스트가 실행되기 전에 다양한 종류의 테스트 사용자를 DB에 미리 저장합니다.
     */
    @BeforeEach
    void setUp() {
        userRepository.deleteAllInBatch(); // 데이터 초기화

        // 1. 정상 사용자
        UserEntity normalUser = UserEntity.builder()
                .username("user1")
                .password(passwordEncoder.encode("password123"))
                .nickname("normalUser")
                .isSocial(false)
                .isLock(false)
                .roleType(UserRoleType.USER)
                .build();

        // 2. 잠긴 사용자
        UserEntity lockedUser = UserEntity.builder()
                .username("lockedUser")
                .password(passwordEncoder.encode("password123"))
                .nickname("lockedUser")
                .isSocial(false)
                .isLock(true)
                .roleType(UserRoleType.USER)
                .build();

        // 3. 소셜 로그인 전용 사용자
        UserEntity socialUser = UserEntity.builder()
                .username("NAVER_social123")
                .password("") // 소셜 유저는 비밀번호가 없음
                .nickname("socialUser")
                .isSocial(true)
                .isLock(false)
                .roleType(UserRoleType.USER)
                .build();

        userRepository.save(normalUser);
        userRepository.save(lockedUser);
        userRepository.save(socialUser);
    }

    private ResultActions performLogin(String username, String password) throws Exception {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", username);
        loginRequest.put("password", password);

        String requestBody = objectMapper.writeValueAsString(loginRequest);

        return mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() throws Exception {
        // when: 정상 사용자로 로그인 시도
        ResultActions resultActions = performLogin("user1", "password123");

        // then: 200 OK 응답과 함께 토큰들이 반환되어야 함
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void login_fail_wrong_password() throws Exception {
        // when: 잘못된 비밀번호로 로그인 시도
        ResultActions resultActions = performLogin("user1", "wrongpassword");

        // then: 401 Unauthorized 응답이 와야 함
        resultActions.andExpect(status().isUnauthorized()).andDo(print());
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 사용자")
    void login_fail_user_not_found() throws Exception {
        // when: DB에 없는 사용자로 로그인 시도
        ResultActions resultActions = performLogin("nonexistentuser", "password123");

        // then: 401 Unauthorized 응답이 와야 함
        resultActions.andExpect(status().isUnauthorized()).andDo(print());
    }

    @Test
    @DisplayName("로그인 실패 - 잠긴 계정")
    void login_fail_locked_account() throws Exception {
        // when: isLock=true 인 사용자로 로그인 시도
        ResultActions resultActions = performLogin("lockedUser", "password123");

        // then: 401 Unauthorized 응답이 와야 함
        resultActions.andExpect(status().isUnauthorized()).andDo(print());
    }

    @Test
    @DisplayName("로그인 실패 - 소셜 계정으로 일반 로그인 시도")
    void login_fail_social_account() throws Exception {
        // when: isSocial=true 인 사용자로 로그인 시도
        ResultActions resultActions = performLogin("NAVER_social123", "password123");

        // then: 401 Unauthorized 응답이 와야 함
        resultActions.andExpect(status().isUnauthorized()).andDo(print());
    }
}