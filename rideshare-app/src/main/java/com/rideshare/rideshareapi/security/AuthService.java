package com.rideshare.rideshareapi.security;

import com.rideshare.rideshareapi.account.Account;
import com.rideshare.rideshareapi.account.AccountRepository;
import com.rideshare.rideshareapi.account.exception.InvalidAccountException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public String refreshToken(String refreshToken){
        Long id=jwtService.getAccountIdFromToken(refreshToken);
        Account account=accountRepository.findById(id).orElseThrow( ()-> new InvalidAccountException("Account not found with Id:"+id));
        return jwtService.generateAccessToken(account);
    }
}
