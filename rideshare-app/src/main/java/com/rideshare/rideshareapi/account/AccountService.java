package com.rideshare.rideshareapi.account;

import com.rideshare.rideshareapi.account.exception.InvalidAccountException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findByUsername(username).orElse(null);
    }

    public Account getAccountById(Long accountId) {

        return accountRepository.findById(accountId).orElseThrow( ()-> new InvalidAccountException("Account not found with Id:"+accountId));
    }
}
