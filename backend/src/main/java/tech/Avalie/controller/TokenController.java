package tech.Avalie.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.Avalie.controller.dto.auth.login.request.LoginRequest;
import tech.Avalie.controller.dto.auth.refresh.request.TokenRefreshRequest;
import tech.Avalie.controller.dto.auth.login.response.LoginResponse;
import tech.Avalie.controller.dto.auth.refresh.response.RefreshResponseDto;
import tech.Avalie.repository.DirectorRepository;
import tech.Avalie.repository.ProfessorRepository;
import tech.Avalie.repository.StudentRepository;


import java.time.Duration;
import java.time.Instant;

@RestController
public class TokenController {
    private JwtEncoder jwtEncoder;
    private JwtDecoder jwtDecoder;
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final DirectorRepository directorRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public TokenController(DirectorRepository directorRepository,ProfessorRepository professorRepository ,JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, StudentRepository studentRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.directorRepository = directorRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws Exception{
        try {
            var student = studentRepository.findByEmail(loginRequest.email());
            if (!student.isEmpty() && student.get().isLoginIsCorrect(loginRequest.password(), bCryptPasswordEncoder)) {
                var accessToken = generateAccesToken(student.get().getAccess_level(),student.get().getId());
                var refreshToken = generateRefreshToken(student.get().getAccess_level(),student.get().getId());
                Jwt decodedJwt = jwtDecoder.decode(accessToken);
                return ResponseEntity.ok(new LoginResponse(student.get().getId(),accessToken,student.get().getAccess_level(), refreshToken));
        }
            var professor = professorRepository.findByEmail(loginRequest.email());
            if(!professor.isEmpty() && professor.get().isLoginIsCorrect(loginRequest.password(),bCryptPasswordEncoder)){
                var accessToken = generateAccesToken(professor.get().getAccess_level(),professor.get().getId());
                var refreshToken = generateRefreshToken(professor.get().getAccess_level(),professor.get().getId());
                Jwt decodedJwt = jwtDecoder.decode(accessToken);
                return ResponseEntity.ok(new LoginResponse(professor.get().getId(),accessToken,professor.get().getAccess_level(), refreshToken));
        }
            var director = directorRepository.findByEmail(loginRequest.email());
            if(!director.isEmpty() && director.get().isLoginIsCorrect(loginRequest.password(),bCryptPasswordEncoder)){
                var accessToken = generateAccesToken(director.get().getAccess_level(),director.get().getId());
                var refreshToken = generateRefreshToken(director.get().getAccess_level(),director.get().getId());
                Jwt decodedJwt = jwtDecoder.decode(accessToken);
                return ResponseEntity.ok(new LoginResponse(director.get().getId(),accessToken,director.get().getAccess_level(), refreshToken));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponseDto> refreshToken(@RequestBody TokenRefreshRequest tokenRefreshRequest){
        try{
            String refreshToken = tokenRefreshRequest.refreshToken();
            Jwt decodedJwt = jwtDecoder.decode(refreshToken);

            if(decodedJwt.getExpiresAt().isBefore(Instant.now())){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
            var newAccessToken = generateAccesToken(Integer.valueOf(decodedJwt.getSubject()),(String)decodedJwt.getClaims().get("user_id"));
            return ResponseEntity.ok(new RefreshResponseDto((String)decodedJwt.getClaims().get("user_id"),newAccessToken,Integer.valueOf(decodedJwt.getSubject()), refreshToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public String generateAccesToken(int access_level, String id)throws Exception{
        if(access_level < 0){throw new Exception("Invalid access level");}
        var acessTokenExpiresIn = 2000L;
        var claims = JwtClaimsSet.builder()
                .issuer("control-backend")
                .subject(String.valueOf(access_level))
                .claim("user_id",id)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(acessTokenExpiresIn))
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateRefreshToken(int access_level, String id)throws Exception{
        if(access_level < 0){throw new Exception("Invalid access level");}
        var refreshTokenExpiresIn = Duration.ofDays(15).getSeconds();
        var refreshTokenClaims  = JwtClaimsSet.builder()
                .issuer("control-backend")
                .subject(String.valueOf(access_level))
                .claim("user_id",id)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(refreshTokenExpiresIn))
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(refreshTokenClaims)).getTokenValue();

    }
}