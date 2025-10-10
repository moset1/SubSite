package com.semocompany.backend.api;

import com.semocompany.backend.domain.user.dto.UserRequestDTO;
import com.semocompany.backend.domain.user.dto.UserResponseDTO;
import com.semocompany.backend.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Tag(name = "User API", description = "사용자 정보 관련 API")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "아이디 중복 확인", description = "입력한 아이디의 사용 가능 여부를 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class),
                            examples = {
                                    @ExampleObject(name = "사용 가능", summary = "사용 가능한 아이디", value = "false"),
                                    @ExampleObject(name = "중복 (사용 불가)", summary = "이미 존재하는 아이디", value = "true")
                            })),
            @ApiResponse(responseCode = "400", description = "요청 데이터 유효성 검증 실패", content = @Content)
    })
    @PostMapping(value = "/user/exist", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> existUserApi(
            @Validated(UserRequestDTO.existGroup.class) @RequestBody UserRequestDTO dto
    ) {
        return ResponseEntity.ok(userService.existUser(dto));
    }

    @Operation(summary = "자체 회원가입", description = "새로운 사용자를 시스템에 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{\"userEntityId\": 1}"))),
            @ApiResponse(responseCode = "400", description = "요청 데이터 유효성 검증 실패", content = @Content),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 아이디일 경우 (서비스 로직에서 예외 처리 시)", content = @Content)
    })
    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, UUID>> joinApi (
            @Validated(UserRequestDTO.addGroup.class) @RequestBody UserRequestDTO dto
    ) {
        UUID id = userService.addUser(dto);
        Map<String, UUID> responseBody = Collections.singletonMap("userEntityId", id);
        return ResponseEntity.status(201).body(responseBody);
    }



    @Operation(summary= "내 정보 조회", description = "인증된 사용자의 정보를 조회합니다. (JWT 토큰 필요)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정보 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class),
                            examples = @ExampleObject(value = "{\"username\":\"testuser\",\"isSocial\":false,\"nickname\":\"테스트유저\",\"email\":\"test@example.com\"}"))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자 (토큰 없음, 만료 등)", content = @Content),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content)
    })
    @GetMapping(value = "/user")
    public UserResponseDTO userMeApi() {
        return userService.readUser();
    }

    @Operation(summary = "내 정보 수정", description = "현재 로그인된 사용자의 정보를 수정합니다. (JWT 토큰 필요)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공 (수정된 유저의 ID 반환)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1"))),
            @ApiResponse(responseCode = "400", description = "요청 데이터 유효성 검증 실패", content = @Content),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "403", description = "소셜 로그인 유저는 정보 수정 불가 (AccessDeniedException 발생)"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @PutMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UUID> updateUserApi(
            @Validated(UserRequestDTO.updateGroup.class) @RequestBody UserRequestDTO dto
    ) throws AccessDeniedException {
        return ResponseEntity.status(200).body(userService.updateUser(dto));
    }

    @Operation(summary = "회원 탈퇴", description = "현재 로그인된 사용자의 계정을 삭제합니다. (JWT 토큰 필요)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "탈퇴 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class),
                            examples = @ExampleObject(value = "true"))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content)
    })
    @DeleteMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> deleteUserApi(
            @Validated(UserRequestDTO.deleteGroup.class) @RequestBody UserRequestDTO dto
    ) throws AccessDeniedException {

        userService.deleteUser(dto);
        return ResponseEntity.status(200).body(true);
    }
}
