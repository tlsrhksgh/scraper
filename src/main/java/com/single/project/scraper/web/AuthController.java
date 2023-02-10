package com.single.project.scraper.web;

import com.single.project.scraper.model.Auth;
import com.single.project.scraper.persist.entity.MemberEntity;
import com.single.project.scraper.security.TokenProvider;
import com.single.project.scraper.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final MemberService memberService;

    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Auth.SignUp request) {
        // 회원가입 API
        MemberEntity result = memberService.register(request);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody Auth.SignIn request) {
        // 로그인 API
        MemberEntity memberEntity = memberService.authenticate(request);
        String token = this.tokenProvider.generateToken(memberEntity.getUsername(), memberEntity.getRoles());

        return ResponseEntity.ok(token);
    }

}
