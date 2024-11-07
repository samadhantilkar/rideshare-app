package com.rideshare.rideshareapi.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    Account findAccountByUsername(String username);

    Optional<Account> findFirstByUsername(String username);

    Account  findAccountByPassword(String password);
}
